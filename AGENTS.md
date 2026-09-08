# AGENTS.md

Guidance for AI agents working in this repository.

## What this repository is

This repository is the source for the book **"Practical Gremlin: An Apache TinkerPop
Tutorial"** by Kelvin R. Lawrence and Stephen P. Mallette. It is a book, not a
software project. The primary deliverable is prose. Code exists here in service of
the prose - as examples in the manuscript, as runnable samples under `sample-code/`,
and as the build/validation tooling under `bin/`.

The current work is the **second edition**, which lives on the `main` branch and
targets the Apache TinkerPop release named by the `:tpvercheck:` attribute in
`book/Practical-Gremlin.adoc` (see "Technical accuracy" below). The first edition is
archived on the `v1` and `first-edition` branches and must not be edited.

Treat the book as a *living book*: it is published continuously from `main`, so every
commit should leave the manuscript in a publishable state.

## Repository layout

| Path | Contents |
| --- | --- |
| `book/` | The AsciiDoc manuscript. This is where nearly all editing happens. |
| `book/Practical-Gremlin.adoc` | Master document: attributes, title, and `include::` list. |
| `book/Section-*.adoc` | One file per chapter, included by the master document. |
| `bin/` | Build and validation scripts. |
| `bin/llms/` | Ruby scripts that produce the Markdown mirror and `llms.txt`. |
| `sample-code/` | Runnable samples: `dotnet`, `golang`, `groovy`, `java`, `javascript`, `python`, `ruby`. |
| `sample-data/` | The `air-routes` data set and other sample graphs, in several formats. |
| `make-route-graph/` | Tooling that generates the `air-routes` data set. |
| `demos/` | Standalone vis.js visualizations of the graph. |
| `images/` | Cover art and figures. |
| `target/` | Build output. Git-ignored, never commit it. |

## Building the book

The build depends on Asciidoctor (Ruby), and optionally on Pandoc, Calibre, and
Node.js for the additional formats. Run everything from the **repository root**.

```shell
# Full build: validation, then HTML, DocBook, EPUB, MOBI, PDF, Markdown + llms.txt
./bin/make-book.sh

# Just the agent-friendly Markdown mirror and llms.txt index
./bin/make-llms.sh
```

Single formats can be produced directly from the `book/` directory:

```shell
asciidoctor Practical-Gremlin.adoc          # HTML
asciidoctor-pdf Practical-Gremlin.adoc      # PDF
```

`bin/make-book.sh` deletes and recreates `target/`, so do not put anything of value
there. If Node.js is not installed, the Markdown/`llms.txt` step is skipped
gracefully and the rest of the build still succeeds.

CI (`.github/workflows/build-asciidoc.yml`) rebuilds and republishes to GitHub Pages
on every push to `main`, so a broken build is immediately a broken published book.

## Validation - run this before every commit

```shell
./bin/check.sh                 # cross-references + code block formatting
./bin/check.sh --check-urls    # additionally verifies every URL in the manuscript
```

`check.sh` runs two checks, both of which gate CI:

- **`check-refs.sh`** - every `<<anchor>>` cross-reference must have a matching
  `[[anchor]]` definition somewhere in `book/*.adoc`.
- **`check-formatting.sh`** - every `[source,...]` line must be immediately followed
  by a `----` delimiter, and every code block must be closed.

The URL check is slow and network-dependent; run it when adding or changing links.

If you touched the Markdown target, also run `./bin/validate-llms.sh`, which serves
the built site locally and validates it against the
[Agent Friendly Documentation Spec](https://agentdocsspec.com/) with a pinned version
of `afdocs`.

## Technical accuracy: TinkerPop is the source of truth

**Nothing in this book may contradict the official Apache TinkerPop documentation.**
When adding or changing anything about Gremlin semantics, step behavior, defaults,
deprecations, or availability, verify it against the reference documentation first.

The book is bound to one TinkerPop release, recorded in the `:tpvercheck:` attribute
in `book/Practical-Gremlin.adoc`. **That attribute is the single source of truth for
the version.** This file deliberately does not restate the number, because a second
copy would silently go stale the moment the book moves to a new release. Read it at
the start of any work that touches version-sensitive material:

```shell
grep '^:tpvercheck:' book/Practical-Gremlin.adoc
```

Then verify against the documentation for *that* release, substituting the value for
`<version>`: `https://tinkerpop.apache.org/docs/<version>/llms.txt`

So when `:tpvercheck:` reads `3.8.2`, the agent-friendly index to consult is
https://tinkerpop.apache.org/docs/3.8.2/llms.txt - that is an illustration of the
substitution, not a pinned value. Always resolve it from the attribute.

Further rules:

- In manuscript prose, write `{tpvercheck}` rather than hard-coding the version
  number, so the text tracks the attribute. Hard-code a version only when stating a
  historical fact that will not move, such as "TinkerPop 3.8.0 was released November
  2025" or a note about when a step first appeared.
- Do not invent steps, options, predicates, or output formats. If a claim cannot be
  verified against TinkerPop's documentation or by actually running the query, do not
  make it.
- Query results shown in the book were generated by actually running the queries
  against the targeted TinkerPop release and air-routes 1.0. Do not hand-write or
  guess output. If output cannot be produced by running the query, say so rather
  than fabricating it.
- Where a feature arrived in a specific release, note it. The second edition
  deliberately avoids dwelling on ancient version history, but flagging "this step
  was added in 3.7.0" is helpful and consistent with existing text.
- Distinguish verifying from linking. Verify facts against the pinned
  `:tpvercheck:` release as above, but when adding a documentation *link to the
  manuscript*, use the `current` path (`/docs/current/reference/`), matching the
  existing links in the book.

## Writing style and voice

The book has a distinct, consistent voice. Preserve it. New prose should be
indistinguishable from what surrounds it.

**Pronouns.** The book is written in the first person plural. "We" is the author and
reader travelling together - "we will look at", "our query", "let us know what we get
back". "You" addresses the reader directly for things the reader does - "you will
notice", "if you have used SQL before", "your own graph". First person singular ("I")
does not appear in second-edition prose; keep it out.

**Tone.** Conversational, patient, and encouraging, but never chatty or jokey. The
book is a tutorial written by someone sitting next to you. It admits when something is
tricky, and it says so plainly. Occasional light enthusiasm is in keeping ("things
start to get a lot more interesting!"), but use it sparingly.

**Contractions.** Mostly expanded forms: "we will", "it is", "do not", "cannot". The
notable exception is "let's", which is used freely and idiomatically.

**Spelling.** US English - "color", "behavior", "analyze".

**Signposting.** The book constantly tells the reader where they are and where they
are going. Chapters and sections open by recapping what came before and previewing
what follows. Forward and backward references are explicit, e.g. "we will look at
those uses a little later", "as discussed in the "<<gremlininstall>>" section".

**Sentences and paragraphs.** Moderate length, plainly constructed. Paragraphs
typically run three to six sentences and carry one idea. Avoid dense technical
run-ons; the book teaches, it does not specify.

**Introducing examples.** This is the book's core rhythm, and it is very consistent:

1. A sentence or two of prose explaining *what we are about to do and why*.
2. The code block.
3. Prose explaining what came back and what is notable about it, often naming the
   step just used and pointing forward to related material.

Typical lead-ins: "The query below will return...", "We can do this in Gremlin using a
query like the one below", "Here is what we get back from the query", "The next two
queries combine the previous two into a single query". Never drop a code block in
without introducing it, and never leave a result unexplained.

**Referring to Gremlin steps and values in prose.** Use single quotes, not backticks:
the 'has' step, the 'air-routes' graph, the 'code' property. Backticks are reserved
almost entirely for file and path names - `air-routes.graphml`, `sample-code/groovy`,
`conf/gremlin-server.yaml`. This is unusual, but it is the established convention
throughout the manuscript; follow it.

**Admonitions.** `NOTE:` for facts worth pulling out of the flow (version caveats,
documentation links, "this chapter is about X, jump to Y if you want Z"). `TIP:` for
mnemonics and practical advice ("if it helps with remembering..."). Both are used
often. `WARNING:`/`CAUTION:` are rare - reserve them for genuine footguns such as
memory-intensive traversals.

## AsciiDoc conventions

**Line width.** Prose is hard-wrapped at **85 columns**. `Section-Writing-Gremlin-Queries.adoc`
carries a vim modeline (`// vim: set tw=85 cc=+1 wrap spell redrawtime=20000:`) that
records this. Match the wrapping of surrounding text; do not reflow whole paragraphs
unnecessarily, as that creates noisy diffs. Code blocks and `// llms-summary:`
comments are exempt.

**Headings.**

- `==` chapter title, ALL CAPS (e.g. `== WRITING GREMLIN QUERIES`)
- `===` section, sentence case
- `====` subsection, sentence case, frequently phrased as a question
  (e.g. `==== Which airports have no routes?`)

Do not go deeper than `====`; the table of contents is configured for that depth.

**Anchors and cross-references.** Give every `===` and `====` heading an anchor on the
line immediately above it:

```asciidoc
[[noroutes]]
==== Which airports have no routes?
```

Anchor IDs are short, lowercase, no separators (`noroutes`, `runwaydist`,
`gremlininstall`). Reference them as `"<<noroutes>>" section` - the quotes and the
trailing word "section" are the house style. Never remove an anchor that is still
referenced; `check-refs.sh` will catch it, but renaming anchors gratuitously also
breaks external links into the published HTML.

**`llms-summary` markers.** Above section headings, a `// llms-summary: <text>`
comment supplies the curated one-line description used by the Markdown mirror and
`llms.txt`, and forces a page split at that heading:

```asciidoc
// llms-summary: Introduces Gremlin as TinkerPop's graph traversal language, contrasting 'out' hops and 'repeat' with SQL joins.
[[gremlinintro]]
=== Introducing Gremlin
```

Every `Section-*.adoc` file needs one above its chapter heading. Add them to new
top-level sections too. Write them as a single descriptive sentence in the book's
voice, on one line (they are exempt from the 85-column rule). Related markers:
`// llms-split` forces a split without a description, and `// llms-allow-oversize`
exempts a page from the 50KB budget. If `split-markdown.rb` reports a page over
budget, prefer adding a summary marker at a natural heading over exempting the page.

## Code example formatting

This is what `check-formatting.sh` enforces, plus the conventions it cannot check.

**Structure.** The `[source,...]` attribute line must be immediately followed by
`----`, with no blank line between them, and the block must be closed with a matching
`----` with no trailing whitespace:

```asciidoc
[source,groovy]
----
// Find the DFW vertex
g.V().has('code','DFW')
----
```

**Language tags.** Use the tag that matches the content. Established usage, by
frequency: `groovy` (the overwhelming default for Gremlin queries and their output),
`text` (ASCII tables, plain output), `java`, `json`, `shell`, `sql`, `yaml`, `xml`,
`ruby`. Write `[source,groovy]` with no space after the comma.

**Comments inside blocks.** Many examples open with a `//` comment stating what the
query does, phrased as a question or an imperative:

```asciidoc
[source,groovy]
----
// What property values are stored in the DFW vertex?
g.V().has('airport','code','DFW').values()
----
```

Use this whenever a block contains more than one query, or when the accompanying prose
does not already make the intent unmistakable. When a block holds several related
queries, separate them with a blank line and give each its own comment.

**Showing results.** Two patterns are both in use:

- Results in a separate block, introduced by prose ("Here is what we get back from
  the query.").
- Results in the same block, separated from the query by a blank line - common when
  showing several short query/result pairs together.

Do not prefix examples with `gremlin>` or `==>`. Those appear only in the handful of
places that deliberately reproduce a literal Gremlin Console or Gremlin Server
session, such as the installation walkthrough. Everywhere else, queries and results
are shown bare.

**Query formatting.** Match the manuscript's Gremlin style: single quotes for string
literals, no space after commas in step arguments (`has('airport','code','DFW')`).
Keep queries within the code block width used nearby; for long traversals, use the
multi-line continuation styles the book itself documents rather than letting a line
run very long.

**Examples must run.** Every Gremlin example should work unmodified against
`air-routes.graphml` loaded into a TinkerGraph in the Gremlin Console, unless the
surrounding text says otherwise. Prefer the well-worn airports the book already uses
(DFW, AUS, LHR, SYD) over introducing new ones without reason.

## Sample code and data

- Samples live under `sample-code/<language>/`. If a code example in the manuscript
  is also a runnable sample, keep the two in sync.
- Do not modify files in `sample-data/` casually. The `air-routes` data set is
  versioned (currently 1.0, the version that ships with TinkerPop as of 3.8.0) and
  every result printed in the book was generated against it. Changing the data invalidates output
  throughout the manuscript.
- `make-route-graph/` regenerates the data set. Treat it as a deliberate, separate
  piece of work.

## Change history and commits

- Record substantive changes in `ChangeHistory.md` under the current unreleased
  section, as a short bullet. Reference the GitHub issue number where one exists
  (`Issue #228`). Typo fixes and minor rewording do not need an entry.
- Commit subjects are short, capitalized, and describe the change plainly - "Added a
  section about writing idiomatic Gremlin", "Fixed broken link", "Punctuation,
  spelling, grammar, consistency". No conventional-commit prefixes.
- `README.md` has a "LATEST NEWS" section for reader-visible milestones such as a new
  published format. Use it only for things a reader would care about.

## Things to avoid

- Editing the first edition on the `v1` or `first-edition` branches.
- Committing anything under `target/`.
- Fabricating query output, step behavior, or version availability.
- Reflowing or reformatting large stretches of unchanged prose.
- Renaming or deleting anchors that other sections or external links depend on.
- Switching inline prose references from single quotes to backticks, or otherwise
  "modernizing" the established markup conventions.
- Adding a code block without introducing it in prose first.
- Committing without running `./bin/check.sh`.
