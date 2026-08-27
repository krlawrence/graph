#!/usr/bin/env ruby
# frozen_string_literal: true

# generate-llms-txt.rb
# --------------------
# Builds the llms.txt index over the split Markdown pages produced by
# split-markdown.rb, following the Agent Friendly Documentation Spec
# (https://agentdocsspec.com/) -- the same shape TinkerPop's LlmsTxtGenerator
# emits. Kept separate from the splitter so the index can be regenerated with
# absolute URLs at publish time without re-rendering the Markdown.
#
#   # <book title>
#   > <book summary>
#
#   ## <Chapter title>
#   - [Page title](<prefix>md/<path>): <description>
#
# Title comes from each page's first heading; description comes from the page's
# curated `<!-- llms-summary: -->` comment when present, else its first prose
# sentence. Chapter grouping and order follow the splitter's .llms-manifest.json.
#
# Usage: ruby bin/llms/generate-llms-txt.rb [--prefix URL] [--out FILE] [md-root]

require 'json'
require 'optparse'

BOOK_SUMMARY =
  'A practical, example-driven tutorial for learning the Apache TinkerPop graph ' \
  'computing framework and the Gremlin graph traversal language, using the ' \
  'air-routes sample graph.'

SIZE_CAP = 50_000 # spec soft cap for the index body

options = { prefix: '', out: nil }
OptionParser.new do |o|
  o.banner = 'Usage: generate-llms-txt.rb [options] [md-root]'
  o.on('--prefix URL', 'Absolute URL base prepended to every link (e.g. https://krlawrence.github.io/graph/)') { |v| options[:prefix] = v }
  o.on('--out FILE', 'Write the index here (default <md-root>/../llms.txt)') { |v| options[:out] = File.expand_path(v) }
end.parse!

md_root = File.expand_path(ARGV[0] || File.join(File.expand_path('../../target', __dir__), 'md'))
out_file = options[:out] || File.join(File.dirname(md_root), 'llms.txt')
prefix = options[:prefix]
prefix += '/' unless prefix.empty? || prefix.end_with?('/')

# The path segment that reaches a page from the site root (llms.txt sits beside md/).
def link_for(prefix, relpath)
  "#{prefix}md/#{relpath}"
end

# First '#'-level heading text in a page.
def extract_title(text)
  text.each_line do |line|
    return Regexp.last_match(1).strip if line =~ /^#{'#'}{1,6}\s+(\S.*)$/
  end
  nil
end

# Flatten inline Markdown to plain prose for a one-line description.
def flatten(str)
  str.gsub(/\[([^\]]*)\]\([^)]*\)/, '\1') # links -> text
     .gsub(/[`*_>#]/, '')                 # inline code / emphasis / stray markers
     .gsub(/\s+/, ' ').strip
end

# Curated summary comment, else the first prose sentence (skipping the pointer
# blockquote, headings, code fences, tables and admonition markers).
def extract_description(text)
  if (m = text.match(/<!--\s*llms-summary:\s*(.*?)\s*-->/m))
    return flatten(m[1])
  end

  in_fence = false
  para = []
  text.each_line do |raw|
    line = raw.chomp
    if line.lstrip.start_with?('```')
      in_fence = !in_fence
      next
    end
    next if in_fence
    stripped = line.strip
    if stripped.empty?
      break unless para.empty?
      next
    end
    next if stripped.start_with?('#', '>', '<!--', '|', '**') # heading/pointer/comment/table/admonition

    para << stripped
  end
  sentence = flatten(para.join(' '))
  sentence = sentence.split(/(?<=[.!?])\s/).first.to_s
  sentence = sentence[0, 197].rstrip + '...' if sentence.length > 200
  sentence
end

manifest_path = File.join(md_root, '.llms-manifest.json')
abort "Manifest not found at #{manifest_path}; run split-markdown.rb first." unless File.exist?(manifest_path)
manifest = JSON.parse(File.read(manifest_path))

book_title = manifest['book_title'] || 'Practical Gremlin'

# Group pages by chapter (top path segment), preserving manifest order.
chapters = []          # [chapter_slug, ...] in first-seen order
by_chapter = Hash.new { |h, k| h[k] = [] }
manifest['pages'].each do |p|
  relpath = p['path']
  chapter = relpath.split('/').first
  chapters << chapter unless by_chapter.key?(chapter)
  full = File.join(md_root, relpath)
  next unless File.exist?(full)
  body = File.read(full)
  by_chapter[chapter] << {
    relpath: relpath,
    title: p['title'] || extract_title(body) || File.basename(relpath, '.md'),
    description: extract_description(body),
    index: relpath.end_with?('/index.md')
  }
end

out = +"# #{book_title}\n\n> #{BOOK_SUMMARY}\n"
chapters.each do |chapter|
  entries = by_chapter[chapter].sort_by { |e| e[:index] ? 0 : 1 } # chapter index page first
  chapter_title = entries.find { |e| e[:index] }&.dig(:title) || chapter
  out << "\n## #{chapter_title}\n"
  entries.each do |e|
    desc = e[:description].to_s.empty? ? '' : ": #{e[:description]}"
    out << "- [#{e[:title]}](#{link_for(prefix, e[:relpath])})#{desc}\n"
  end
end

File.write(out_file, out)
warn "WARN: llms.txt body is #{out.bytesize} bytes, over the #{SIZE_CAP}-byte soft cap." if out.bytesize > SIZE_CAP
puts "Wrote #{out_file} (#{out.bytesize} bytes, #{manifest['pages'].length} pages)"
