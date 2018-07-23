# Change History

Starting with the v275 release, this file will contain a record of the major changes and updates made in each release.

## Release 280 change history (Date TBD)

Here is a link to the [Git diffs](https://github.com/krlawrence/graph/compare/v279-tp.3.3.3...master) between this release and the prior one.

- Broke out the discussion of simulating `startsWith` into its own section.
- Added to the discussion of `cyclicPath`.
- Added text to explain the "__." notation the first time it appears #112.
- Expanded the section that introduces the `path` step #97, #98, #99.
- Added text to better introduce the concept of a `modulator` #99 .
- Added text to introduce the phrase `anonymous traversal` #98
- Expanded the section covering the `cyclicPath` step.
- Expanded the discussion of randomly walking a graph. #105
- Added coverage of the `Direction` Enum. #114
- Expanded and improved the `Calculating vertex degree` section.
- Added the `Cardinality` enum to the tables in section 6.1.5 #110.
- Miscellaneous minor wording updates and corrections.
- Sample code and data improvements.
  - Updated air-routes sample data (475 new routes, 2 new airports).
  - Updated demo apps to reflect the latest data updates.

## Release 279 change history (May 29th 2018)

Here is a link to the [Git diffs](https://github.com/krlawrence/graph/compare/V278-3.3.1...v279-tp.3.3.3) between this release and the prior one.

The examples in this release have been tested against Apache TinkerPop at the 3.3.1, 3.3.2 and at the newly released 3.3.3 level. They have also been tested against JanusGraph 0.2 (where appropriate) which supports TinkerPop at the 3.2.6 level.

Major new features in this release
- Significantly expanded and improved the `subgraph` section.
- Added examples to the `where` `by` section.
- Added more examples of `select` keywords `last`, `first` and `mixed`.
- Added to the section that discusses the `union` step.
- Improved the section dedicated to the `match` step.
- Improved coverage of text comparison predicates.
- Improved the `Working with IDs` section.
- Added examples to the `introducing Path` section.
- Added a new section covering use of `withRemote` with a Gremlin Server.
- Expanded coverage of custom ID values.
- Added a new section showing an example of a `path` step used inside a `groupCount` step.
- Added a new section showing how to deduce a graph's schema.
- Added more examples to Chapter 5.
- Corrected output in Section 3.3.1 to reflect correct version of air-routes. Issue #87.
- Expanded coverage of the results from a `Gremlin Server`.
- Sample code and data improvements
  - Added new script `regression.groovy` to check that the book examples work with the most recent Tinkerpop versions.
  - Added new sample `add-aircraft.Groovy`.
  - Added new sample data `aircraft.csv`.
  - Added new sample `RemoteClient.java`
  - Added new sample `ListAirports.java`.
  - Expanded `CreateGraph.java` sample.
  - Moved `edges.csv` to `sample-data` folder.
  - Provided updated versions of the air-routes data and demo apps (1895 new routes, 21 new airports).
- Miscellaneous issues resolved and other more minor corrections.

## Release 278 change history (March 28th 2018)

Here is a link to the [Git diffs](https://github.com/krlawrence/graph/compare/v277...V278-3.3.1) between this release and the prior one.

This release has been tested against Apache TinkerPop at the 3.3.1 level and JanusGraph 0.2 which supports TinkerPop at the 3.2.6 level.

Major new features in this release
- Added additional background and context to the introduction chapter.
- Added a new section on measuring query performance.
- Added coverage of the new `math` step. Issue #21.
- Added a new Java sample that demonstrates the `math` step (StdDev.java).
- Added coverage of the `optional` step. Issue #40.
- Improved introduction to the `select` step.
- Expanded the "Using 'where' to filter things out of a result" section and the following section that introduces the 'where' and 'by' pattern.
- Expanded the section that introduces the `union` step.
- Added dedicated introduction sections for the `identity` and `constant` steps. Issue #43.
- Added coverage of TinkerGraph indexing.
- Added to the `collections` section.
- Added a new `OLTP vs OLAP` section.
- Added to the `Gremlin Server` section.
- Improved coverage of `cyclicPath`
- Improved coverage of `flatMap`
- Improved coverage of `repeat` and `emit`.
- Added coverage of `toSet` in section 3.
- Expanded the `vertex degree` section.
- Updated the `valueMap` section.
- Added examples of using `from` and `to` with `path`.
- Added example of empty `by` modulator to `path` section.
- Added a section showing how `inject` can supply ID values to `addV`.
- Added a section to introduce the `dedup` step in Section 3.
- Expanded the "Basic statistical and numerical operations"  section.
- Added coverage of dynamically adding labels using `addV` and `addE` - issue #22.
- Added an example of nested `group` steps in section 5.
- Improved existing text and examples in section 5.
- Added more examples to section 5
- Improved coverage of `indexed` and `withIndex`.
- Moved `indexed` and `withIndex` material up to Section 3 (from 5).
- Examples in section 5 now all based on the same air routes graph v0.77 (some used to reflect an older version).
- Added a new Java sample GroupCounts.java
- Added a new Java sample that explores the `sample` step (Iterate.java).
- Expanded the section in chapter 4 that introduces GraphML and Graphson.
- Added coverage of GraphSONMapper to the expanded GraphML/GraphSON section. Issue #51
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
