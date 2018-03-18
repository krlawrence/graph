# Change History

Starting with the v275 release, this file will contain a record of the major changes and updates made in each release.


## Future Release 278 change history (Date TBD)

Here is a link to the [Git diffs](https://github.com/krlawrence/graph/compare/v277...master) between this release and the prior one.

This release has been tested against Apache TinkerPop at the 3.3.1 level and JanusGraph 0.2 which supports TinkerPop at the 3.2.6 level.

Major new features in this release
- Added a new section on measuring query performance.
- Added coverage of the new `math` step. Issue #21.
- Added a new Java sample that demonstrates the `math` step (StdDev.java).
- Added coverage of the `optional` step. Issue #40.
- Improved introduction to the `select` step.
- Added coverage of TinkerGraph indexing.
- Added to the `collections` section.
- Added a new `OLTP vs OLAP` section.
- Added to the `Gremlin Server` section.
- Improved coverage of `cyclicPath`
- Improved coverage of `flatMap`
- Added coverage of `toSet` in section 3.
- Expanded the `vertex degree` section.
- Updated the `valueMap` section.
- Added examples of using `from` and `to` with `path`.
- Added example of empty `by` modulator to `path` section.
- Added a section showing how `inject` can supply ID values to `addV`.
- Added a section to introduce the `dedup` step in Section 3.
- Expanded the "Basic statistical and numerical operations"  section.
- Added coverage of dynamically adding labels using `addV` and `addE` - issue #22.
- Improved existing text and examples in section 5.
- Added more examples to section 5
- Improved coverage of `indexed` and `withIndex`.
- Moved `indexed` and `withIndex` material up to Section 3 (from 5).
- Examples in section 5 now all based on the same air routes graph v0.77 (some used to reflect an older version).
- Added a new Java sample GroupCounts.java
- Added a new Java sample that explores the `sample` step (Iterate.java).
- Added to the `JanusGraph GeoSpatial` section and moved the
 section up a hierarchy level.
- Added a new sample that demonstrates geospatial queries (janus-geoshape.groovy).
- Improved source highlighting for MOBI and EPUB versions.
- Added more examples in various places.
- General housekeeping and tightening up of some wording.
- Added a gitignore file. PR #39


## Release 277 change history (Feb 11, 2018)

This is a fairly substantial update. Lots of new material added to the book and to the sample-code folder.

Here is a link to the [Git diffs between v277 and the prior v276 release](https://github.com/krlawrence/graph/compare/v276...v277)

Major new features in this release.
- Wrote the first draft of the `Gremlin Server` section.
- Added examples of the JSON that is returned from different Gremlin Server queries.
- Added a new section dedicated to discussing steps that generate `collections` and how to operate on them.
- Added a new section dedicated to the `sack` step.
- Added additional coverage of the `local` step and scope in general.
- Added some focused coverage of `reducing barrier` steps.
- Added coverage of the new `skip` step.
- Added to the JanusGraph `cardinality` section.
- Added to the JanusGraph `transactions` section.
- Added to the JanusGraph `geospatial API` section.
- Significantly added to the using Gremlin from Java sections.
- Added detailed coverage of Classses and Enums that you need to include when writing a Java or Groovy app that are hidden from you when using the Gremlin Console.
- Added to the using Gremlin from Groovy sections.
- Added coverage of JanusGraph storage options.
- Added a new section showing how to use Cassandra from a Docker Container with JanusGraph.
- Added a sample that shows how to load the air routes graph into JanusGraph backed by Cassandra.
- Added several new Java and Groovy samples to the sample-code folder.
- Added a new Ruby sample that can connect to a Gremlin Server
- Improved some existing sample apps.
- Enabled icons for tips and notes boxes.
- Did quite a bit of cleaning up of the text overall and corrected some mistakes and typos.

## Release 276 change history (Jan 12, 2018)

Here is a link to the [Git diffs between v276 and the prior v275 release](https://github.com/krlawrence/graph/compare/v275...v276)

Major features in this Release
- Renamed the book to "Practical Gremlin".
- Added Tip boxes pointing to relevant samples throughout the book.
- Added to the section on the `where` step.
- Improved section on boolean steps.
- Added more Java samples to the sample-code folder.
- Significantly improved coverage of the `repeat` step.
- Expanded coverage of working with properties.
- Added coverage of the `explain` step.
- Removed use of deprecated `addV` form.
- Added introductory text to make GitHub material easier to find.
- Cleanup and fixing of typos.

## Release 275 change history (Jan 3, 2018)

Major features in this release
- Added a new section pointing to where the samples are located.
- Added to latitude/longitude section.
- Added to and improved Section 5 (Misc queries)
- Expanded JanusGraph indexing section.
- Added new samples to sample-code folder.
- Improved several existing samples in the sample-code folder.
- Switched to Asciidoctor-PDF to generate PDF output.
- Some general tidy up and other improvements.

## Original upload (Oct 4, 2017)

The initial manuscript and sample programs were uploaded on Oct 4th 2017. To track changes since then but before the v275 release please refer to the commit history.
