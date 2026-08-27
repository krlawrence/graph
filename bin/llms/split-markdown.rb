#!/usr/bin/env ruby
# frozen_string_literal: true

# split-markdown.rb
# -----------------
# Produces the agent-friendly Markdown mirror of the book, following the same
# publishing pattern Apache TinkerPop uses for its llms.txt docs.
#
# Because the book is authored as a single Practical-Gremlin.adoc that
# `include::`s nine Section-*.adoc chapter files -- and because downdoc does not
# resolve include:: directives -- each chapter file is converted individually
# with downdoc (https://gitlab.com/antora/downdoc), then split into
# agent-sized pages under target/md/.
#
# Splitting is summary/size driven (mirrors TinkerPop's MarkdownSplitter):
#   * every chapter (== ) becomes a page;
#   * a chapter whose converted Markdown exceeds the size budget is additionally
#     split at its === sub-sections;
#   * a `// llms-summary: <text>` or `// llms-split` marker above a heading forces
#     a split there (llms-summary also supplies a curated description that is
#     embedded as a hidden <!-- llms-summary: --> comment for the index generator);
#   * `// llms-allow-oversize` above a heading exempts its page from the budget
#     split and the size lint.
#
# Cross-references are rewritten so links resolve across the split pages, and a
# size lint fails the run under --strict when a page exceeds the budget.
#
# Usage: ruby bin/llms/split-markdown.rb [--strict] [--out DIR] [--budget N] [--site-url URL]

require 'fileutils'
require 'tmpdir'
require 'optparse'
require 'pathname'
require 'json'

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
REPO_ROOT   = File.expand_path('../..', __dir__)
BOOK_DIR    = File.join(REPO_ROOT, 'book')
MASTER_ADOC = File.join(BOOK_DIR, 'Practical-Gremlin.adoc')
DOWNDOC     = ENV['DOWNDOC'] || 'downdoc'

options = { strict: false, out: File.join(REPO_ROOT, 'target', 'md'), budget: 50_000, site_url: nil }
OptionParser.new do |o|
  o.banner = 'Usage: split-markdown.rb [options]'
  o.on('--strict', 'Exit non-zero if any page exceeds the size budget') { options[:strict] = true }
  o.on('--out DIR', 'Output directory for Markdown pages (default target/md)') { |v| options[:out] = File.expand_path(v) }
  o.on('--budget N', Integer, 'Per-page size budget in bytes (default 50000)') { |v| options[:budget] = v }
  o.on('--site-url URL', 'Absolute site root (e.g. https://krlawrence.github.io/graph/); makes the per-page llms.txt pointer absolute') { |v| options[:site_url] = v }
end.parse!

OUT_DIR = options[:out]
BUDGET  = options[:budget]

# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

# GitHub-style heading slug -- matches the fragments downdoc emits for xrefs
# (verified: lowercase; strip everything except word chars, spaces and hyphens;
# spaces -> hyphens; no collapsing of repeated hyphens).
def github_slug(text)
  s = text.to_s.downcase
  s = s.gsub(/[^\p{Word}\s-]/, '')
  s.tr(' ', '-')
end

# Read the master header's `:name: value` attribute lines so they can be passed
# to downdoc (a chapter converted alone would otherwise leave {tpvercheck} etc.
# unsubstituted). Values still containing an attribute reference are skipped.
def master_attributes
  attrs = {}
  File.foreach(MASTER_ADOC) do |line|
    break if line =~ /^=\s/ # stop at the document title; attributes precede it
    if (m = line.match(/^:([A-Za-z][\w-]*):\s+(.*?)\s*$/))
      name, value = m[1], m[2]
      next if value.include?('{') # unresolvable (e.g. draftdate: {localdatetime})
      attrs[name] = value
    end
  end
  attrs
end

# Ordered list of the chapter files, taken from the master's include:: directives.
def chapter_files
  files = []
  File.foreach(MASTER_ADOC) do |line|
    if (m = line.match(/^include::([^\[]+)\[\]/))
      files << File.join(BOOK_DIR, m[1].strip)
    end
  end
  files
end

# True when `line` opens or closes an AsciiDoc delimited block we must not scan
# for headings (listing, literal, example, sidebar, quote, passthrough, table).
DELIMITER_RE = /\A(-{4,}|\.{4,}|\*{4,}|_{4,}|={4,}|\+{4,}|\|={3,})\s*\z/.freeze

# Number of leading '=' in the ATX form for each legacy setext underline char.
SETEXT_LEVEL = { '=' => 1, '~' => 3, '^' => 4, '+' => 5 }.freeze

# downdoc does not support AsciiDoc's legacy two-line (setext) section titles, and
# this book has a couple of stray ones. Rewrite them to ATX in-memory (used by both
# the source parser and the downdoc conversion) so nothing is silently lost.
# '-' underlines are deliberately excluded to avoid colliding with `----` listing
# delimiters; the book uses no '-' setext titles.
def normalize_setext(lines)
  out = []
  open_delim = nil
  i = 0
  while i < lines.length
    line = lines[i]
    if (d = line[DELIMITER_RE, 1])
      if open_delim.nil?
        open_delim = d
      elsif open_delim == d || (open_delim.start_with?('|=') && d.start_with?('|='))
        open_delim = nil
      end
      out << line
      i += 1
      next
    end

    if open_delim.nil? && i + 1 < lines.length
      title = line
      nxt = lines[i + 1]
      if (u = nxt.match(/^([=~^+])\1+$/)) && !title.strip.empty? &&
         (nxt.length - title.strip.length).abs <= 1 &&
         !title.lstrip.start_with?('=', '[', '.', '//', '*', '|', ':', '+', '-')
        out << ('=' * SETEXT_LEVEL[u[1]]) + ' ' + title.strip
        i += 2
        next
      end
    end

    out << line
    i += 1
  end
  out
end

# Parse a chapter's AsciiDoc source (already setext-normalized) into an ordered
# list of headings, each with its explicit [[anchor]] (if any) and any llms-*
# markers placed directly above.
def parse_source_headings(lines)
  headings = []
  open_delim = nil
  lines.each_with_index do |line, i|
    if (d = line[DELIMITER_RE, 1])
      if open_delim.nil?
        open_delim = d
      elsif open_delim == d || (open_delim.start_with?('|=') && d.start_with?('|='))
        open_delim = nil
      end
      next
    end
    next unless open_delim.nil?

    m = line.match(/^(={2,6})\s+(\S.*)$/)
    next unless m

    level = m[1].length
    text  = m[2].strip
    anchor = nil
    markers = {}
    # Walk backward across the contiguous metadata block above the heading.
    j = i - 1
    while j >= 0
      lj = lines[j].strip
      if (a = lj[/^\[\[([^\]]+)\]\]$/, 1])
        anchor ||= a
      elsif (s = lj[%r{^//\s*llms-summary:\s*(.*)$}, 1])
        markers[:summary] = s.strip
      elsif lj =~ %r{^//\s*llms-split\s*$}
        markers[:split] = true
      elsif lj =~ %r{^//\s*llms-allow-oversize\s*$}
        markers[:allow_oversize] = true
      elsif lj.start_with?('//') # other comment - keep scanning
        # no-op
      else
        break
      end
      j -= 1
    end
    headings << { level: level, text: text, anchor: anchor, markers: markers }
  end
  headings
end

# Convert setext-normalized AsciiDoc lines to Markdown text via downdoc.
def convert_chapter(lines, attr_args)
  Dir.mktmpdir do |dir|
    src = File.join(dir, 'in.adoc')
    out = File.join(dir, 'out.md')
    File.write(src, lines.join("\n") + "\n")
    system(DOWNDOC, *attr_args, src, '-o', out, exception: true)
    File.read(out)
  end
end

# Parse converted Markdown into an ordered list of { level, text, line_index },
# ignoring '#'-prefixed lines that fall inside fenced code blocks.
def parse_md_headings(md_lines)
  headings = []
  in_fence = false
  md_lines.each_with_index do |line, i|
    if line.lstrip.start_with?('```')
      in_fence = !in_fence
      next
    end
    next if in_fence

    if (m = line.match(/^(\#{1,6})\s+(\S.*)$/))
      headings << { level: m[1].length, text: m[2].strip, line: i }
    end
  end
  headings
end

# ---------------------------------------------------------------------------
# Pass 1 -- convert every chapter and plan the pages
# ---------------------------------------------------------------------------
attrs = master_attributes
attr_args = attrs.flat_map { |k, v| ['-a', "#{k}=#{v}"] }

pages = []          # { relpath, title, top_level, lines:[md lines], summary, allow_oversize }
slug_to_page = {}   # heading slug   => page relpath (page that contains that heading)
anchor_to_target = {} # explicit anchor id => { page:, slug:, title: }

chapter_files.each do |cfile|
  next unless File.exist?(cfile)

  chapter_lines = normalize_setext(File.readlines(cfile, chomp: true))
  src_headings = parse_source_headings(chapter_lines)
  md_text  = convert_chapter(chapter_lines, attr_args)
  md_lines = md_text.split("\n", -1)
  md_headings = parse_md_headings(md_lines)

  if md_headings.length != src_headings.length
    warn "WARN: heading count mismatch in #{File.basename(cfile)} (source #{src_headings.length}, markdown #{md_headings.length}); matching by order."
  end

  # Merge source metadata (anchor/markers) onto the converted headings by order.
  merged = md_headings.each_with_index.map do |h, idx|
    src = src_headings[idx] || {}
    h.merge(anchor: src[:anchor], markers: src[:markers] || {}, slug: github_slug(h[:text]))
  end
  next if merged.empty?

  chapter = merged.first # the == heading (level 2 in md)
  chapter_slug = chapter[:slug]
  chapter_oversize_ok = chapter[:markers][:allow_oversize]

  # Start with the mandatory split points: the chapter itself and any heading
  # carrying an explicit marker. Then, unless the chapter opts out of sizing,
  # recursively split any slice still over budget at its shallowest inner heading
  # level -- so oversize sections break down without hand-placed markers.
  by_line = merged.to_h { |h| [h[:line], h] }
  cuts = [chapter[:line]]
  cuts.concat(merged.select { |h| h[:markers][:summary] || h[:markers][:split] }.map { |h| h[:line] })
  cuts.uniq!

  unless chapter_oversize_ok
    loop do
      sorted = cuts.sort
      changed = false
      sorted.each_with_index do |start_line, si|
        end_line = si + 1 < sorted.length ? sorted[si + 1] : md_lines.length
        next if md_lines[start_line...end_line].join("\n").bytesize <= BUDGET
        inside = merged.select { |h| h[:line] > start_line && h[:line] < end_line }
        next if inside.empty?
        min_level = inside.map { |h| h[:level] }.min
        new_cuts = inside.select { |h| h[:level] == min_level }.map { |h| h[:line] }
        next if new_cuts.empty?
        cuts.concat(new_cuts)
        cuts.uniq!
        changed = true
      end
      break unless changed
    end
  end

  # Carve the chapter's Markdown into page slices at the split boundaries.
  cuts.sort.each_with_index do |start_line, si|
    end_line = si + 1 < cuts.length ? cuts.sort[si + 1] : md_lines.length
    h = by_line[start_line]
    slice = md_lines[start_line...end_line]

    relpath =
      if start_line == chapter[:line]
        File.join(chapter_slug, 'index.md')
      else
        candidate = File.join(chapter_slug, "#{h[:slug]}.md")
        candidate = File.join(chapter_slug, "#{h[:slug]}-#{si}.md") if pages.any? { |p| p[:relpath] == candidate }
        candidate
      end

    page = {
      relpath: relpath,
      title: h[:text],
      top_level: h[:level],
      lines: slice,
      summary: h[:markers][:summary],
      allow_oversize: h[:markers][:allow_oversize] || chapter_oversize_ok
    }
    pages << page

    # Map every heading that lives inside this slice to this page.
    merged.each do |mh|
      next unless mh[:line] >= start_line && mh[:line] < end_line
      slug_to_page[mh[:slug]] = relpath
      if mh[:anchor]
        anchor_to_target[mh[:anchor]] = { page: relpath, slug: mh[:slug], title: mh[:text] }
      end
    end
  end
end

# ---------------------------------------------------------------------------
# Pass 2 -- promote heading levels, rewrite links, add pointer + summary, write
# ---------------------------------------------------------------------------
def rel_link(from_relpath, to_relpath)
  from_dir = Pathname.new(File.dirname(from_relpath))
  Pathname.new(to_relpath).relative_path_from(from_dir).to_s
end

# Compute the per-page pointer to llms.txt (target/llms.txt lives one level above OUT_DIR).
def llms_pointer(relpath, site_url)
  return "#{site_url.sub(%r{/*$}, '/')}llms.txt" if site_url
  depth = File.dirname(relpath).split('/').reject { |p| p == '.' }.length
  ('../' * (depth + 1)) + 'llms.txt'
end

LINK_RE = /\[([^\]]*)\]\(#([^)\s#]+)\)/.freeze

violations = []
FileUtils.rm_rf(OUT_DIR)
FileUtils.mkdir_p(OUT_DIR)

pages.each do |page|
  shift = page[:top_level] - 1 # promote so the page's top heading becomes '#'
  in_fence = false
  body = page[:lines].map do |line|
    if line.lstrip.start_with?('```')
      in_fence = !in_fence
      next line
    end
    unless in_fence
      # Promote heading levels for this standalone page.
      if (m = line.match(/^(\#{1,6})(\s+\S.*)$/))
        new_level = [m[1].length - shift, 1].max
        line = ('#' * new_level) + m[2]
      end
      # Rewrite intra-doc fragment links so they resolve across pages.
      line = line.gsub(LINK_RE) do
        text = Regexp.last_match(1)
        frag = Regexp.last_match(2)
        if text == frag && (tgt = anchor_to_target[frag]) # unresolved cross-file xref signature
          href = rel_link(page[:relpath], tgt[:page])
          "[#{tgt[:title]}](#{href}##{tgt[:slug]})"
        elsif (tgt = anchor_to_target[frag]) # explicit anchor id with custom text
          href = rel_link(page[:relpath], tgt[:page])
          "[#{text}](#{href}##{tgt[:slug]})"
        elsif (tp = slug_to_page[frag]) # a heading slug living on another page
          if tp == page[:relpath]
            "[#{text}](##{frag})"
          else
            "[#{text}](#{rel_link(page[:relpath], tp)}##{frag})"
          end
        else
          warn "WARN: unresolved link ##{frag} in #{page[:relpath]}"
          Regexp.last_match(0)
        end
      end
    end
    line
  end.join("\n")

  header = +"> For the complete documentation index, see [llms.txt](#{llms_pointer(page[:relpath], options[:site_url])})\n\n"
  header << "<!-- llms-summary: #{page[:summary]} -->\n\n" if page[:summary]
  content = header + body.sub(/\A\n+/, '')

  dest = File.join(OUT_DIR, page[:relpath])
  FileUtils.mkdir_p(File.dirname(dest))
  File.write(dest, content)

  bytes = content.bytesize
  violations << [page[:relpath], bytes] if bytes > BUDGET && !page[:allow_oversize]
end

# ---------------------------------------------------------------------------
# Book landing page (target/md/index.md) -- a simple TOC over the chapters.
# ---------------------------------------------------------------------------
book_title = File.foreach(MASTER_ADOC).find { |l| l =~ /^=\s/ }.to_s.sub(/^=\s*/, '').strip
book_title = 'Practical Gremlin' if book_title.empty?
chapter_index_pages = pages.select { |p| p[:relpath].end_with?('/index.md') }
toc = chapter_index_pages.map { |p| "- [#{p[:title]}](#{p[:relpath]})" }.join("\n")
File.write(File.join(OUT_DIR, 'index.md'), <<~MD)
  > For the complete documentation index, see [llms.txt](../llms.txt)

  # #{book_title}

  This is the agent-friendly Markdown edition of the book. Each chapter and major
  section is available as its own page below; see the linked llms.txt for the full,
  machine-readable index.

  #{toc}
MD

# Ordered manifest so the index generator can preserve book/chapter order (a
# filesystem walk would only give alphabetical order). Read by generate-llms-txt.rb.
manifest = { book_title: book_title, pages: pages.map { |p| { path: p[:relpath], title: p[:title] } } }
File.write(File.join(OUT_DIR, '.llms-manifest.json'), JSON.pretty_generate(manifest))

puts "Wrote #{pages.length + 1} Markdown pages to #{OUT_DIR}"

unless violations.empty?
  warn "\nSize budget (#{BUDGET} bytes) exceeded by #{violations.length} page(s):"
  violations.sort_by { |_, b| -b }.each { |rp, b| warn format('  %8d  %s', b, rp) }
  warn 'Add // llms-summary:/// llms-split markers to split further, or // llms-allow-oversize to exempt.'
  exit 1 if options[:strict]
end
