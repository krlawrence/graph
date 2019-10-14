# Practical Gremlin: An Apache TinkerPop Tutorial


![map](https://github.com/krlawrence/graph/raw/master/images/GremlinEaselNoText.png?raw=true, "graph picture")

## Welcome!

This repository is the new home for the source materials, sample code and examples for the book "Practical Gremlin - An Apache TinkerPop Tutorial"

**Quick Start**  

To read the latest snapshot of the book right away in a browser (HTML format) click [here](http://kelvinlawrence.net/book/Gremlin-Graph-Guide.html) or for a PDF version click [here](http://kelvinlawrence.net/book/Gremlin-Graph-Guide.pdf). These snapshots are updated regularly. You will find other formats including MOBI, EPUB and XML (Docbook) in the [releases](https://github.com/krlawrence/graph/releases)  section. Formal releases will be published periodically assuming there is enough new material to make it worthwhile. If you want to see the absolute latest updates you can always browse the Asciidoc source file (Gremlin-Graph-Guide.adoc) in the /book folder. The PDF version is currently the "official" version. It has a better table of contents, some better formatting and a much nicer title page!

**LATEST NEWS:**  

[Oct-13-2019] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Dec-26-2018] Revision 281 (TP 3.3.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.    

## Releases and change history

The most recent changes and additions are now being tracked in the [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) file.

**A Special note about releases**

Starting with revision 274 (Dec 24 2017), all of the output files (XML, EPUB, MOBI, HTML and PDF) will now be stored using Git releases. Going forward, this should reduce the amount of disk space required for people who create forks of this project. The release notes and dowloadable materials are located [here](https://github.com/krlawrence/graph/releases).

NOTE: In order to prune the unwanted files from the project the commit history for the output files had to be removed as part of making the v274 release. If you had previously cloned or forked this project please create a new clone or fork. Sorry for the inconvenience but this will get you back approximately 60% (27+ mb) of the disk space that was being taken up and will help anyone else making a clone.


Details of how to build the various output formats from the Asciidoc source are contained in the README.md under the *book* folder.

## How this book came to be

I forget exactly when, but over a year ago I started compiling a list of notes, hints and tips, initially  for my own benefit, of things I had found poorly explained elsewhere while using graph databases and especially using Apache TinkerPop, Janus Graph and Gremlin. Over time that document grew (and continues to grow) and has effectively become a book.  After some encouragement from colleagues I have decided to release it as a living book in an open source venue so that anyone who is interested can read it. It is aimed at programmers and  anyone using the Gremlin query language to work with graphs. Lots of code examples, sample queries,  discussion of best practices, lessons I learned the hard way etc. are included.

While this book remains a work in progress, indeed some sections are still to be filled in, I think there is enough here that people may find it a useful aid to learning the Gremlin graph traversal and query language.

Thanks to all those that have encouraged me to keep going with this adventure!  

Kelvin R. Lawrence  
October 6th, 2017  

## Introduction

**This book is a work in progress. Feedback (ideally via issue) is very much encouraged and welcomed!**

The title of this book could equally well be "A getting started guide for users of graph databases and the Gremlin query language featuring hints, tips and sample queries". It turns out that is a bit too too long to fit on one line for a heading but in a single sentence that describes the focus of this book pretty well.

The book introduces the Apache TinkerPop 3 Gremlin graph query and traversal language via real examples against a real world graph. They are given as a set of working examples against a graph that is also provided in the sample-data folder. The graph, air-routes.graphml, is a model of the world airline route network between 3,367 airports including 43,160 routes. The examples we present will work unmodified with the air-routes.graphml file loaded into the Gremlin console running with a TinkerGraph.

## How this site is organized

The book is being written using a text editor in AsciiDoc format. The source manuscript "Gremlin-Graph-Guide.adoc" can be found in the /book folder. This file will always reflect the most recent updates and will often be ahead of the other formats that will only be updated as part of a formal release. You will find formatted HTML and PDF versions of the book in the releases area.  I have also provided DOCBOOK, EPUB and MOBI versions as part of each release. These can be viewed using many tools and e-book readers. Note that currently, only the HTML and PDF versions have all the nice colors for source code listings etc.

Included with the book are sample graph data (GraphML) and program files. You will find these, as well as some screen shots and images, and demos in the following folders.

- /book
- /sample-data
- /sample-code
- /images
- /demos

## How the book is organized

Chapter 1 - INTRODUCTION
- We start off by briefly doing a recap on why Graph databases are of interest to us
  and discuss some good use cases for graphs.
  
Chapter 2 - GETTING STARTED
- In Chapter 2 we introduce several of the components of Apache TinkerPop 3 and we
  also introduce the air-routes.graphml file that will be used as the graph we base
  most of our examples on.
  
Chapter 3 - WRITING GREMLIN QUERIES
- In Chapter 3 we start discussing how to use the Gremlin graph traversal and
  query language to interrogate the air-routes graph. We begin by comparing how we
  could have built the air-routes graph using a more traditional relational database
  and then look at how SQL and Gremlin are both similar in some ways and very
  different in others. For the rest of the Chapter, we introduce several of
  the key Gremlin methods, or as they are often called, "steps". We
  mostly focus on reading the graph (not adding or deleting things) in this Chapter.
  
Chapter 4 - BEYOND BASIC QUERIES
- In Chapter 4 we move beyond just reading the graph and describe how to add nodes,
  edges and properties as well as how to delete and update them. We also present a
  discussion of various best practices.We also start to explore some slightly more
  advanced topics in this chapter.
  
Chapter 5 - MISCELLANEOUS QUERIES AND THE RESULTS THEY GENERATE
- In Chapter 5 we focus on using what we have covered in the prior Chapters to write
  queries that have a more real world feel. We present a lot more examples of the
  output from running queries in this Chapter. We also start to discuss topics such
  as analyzing distances, route distribution and writing geospatial queries.
  
Chapter 6 - MOVING BEYOND THE CONSOLE AND TINKERGRAPH
- In Chapter 6 we move beyond our focus on the Gremlin Console and TinkerGraph. We
  start by looking at how you can write stand alone Java and Groovy applications that
  can work with a graph. We then introduce Janus Graph and take a fairly detailed
  look at its capabilities such as support for transactions, schemas and external
  indexes. We also explore various technology choices for back end persistent store
  and index as well as introducing the Gremlin Server.
  
Chapter 7 - COMMON GRAPH SERIALIZATION FORMATS
- In Chapter 7 We have provided a discussion of some common Graph serialization file
  formats along with a discussion of how to use them in the context of TinkerPop 3
  enabled graphs.
  
Chapter 8 - FURTHER READING
- We finish up by providing several links to useful web sites where you can find
  tools and documentation for many of the technologies covered in this document.

**NEWS ARCHIVE:**  

[Apr-27-2019] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Dec-24-2018] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Sep-29-2018] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Jul-28-2018] Revision 280 (TP 3.3.3) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.     
[May-29-2018] Revision 279 (TP 3.3.3) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.    
[Mar-28-2018] Revision 278 (TP 3.3.1) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Feb-11-2018] Revision 277 was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Jan-12-2018] Revision 276 was just published in all formats. Many updates to book and samples.  
[Jan-12-2018] Based on feedback I have decided to rename the book "Practical Gremlin" (see issue #29)  
[Jan-06-2018] Several new Java samples have been added to the sample-code directory and others improved.  
[Jan-03-2018] Revision 275 was just published in all formats. Lots of updates to book and sample code.  
[Dec-24-2017] Revision 274 was just published in all formats. Now using releases to store output files.  
[Dec-12-2017] Revision 273 was just published in all formats. Fixes issue #12. Also added additonal clarifications.  
[Nov-23-2017] Revision 272 was just published in all formats. Many updates to sections 3 and 4.  
[Nov-03-2017] Revision 271 was just published in all formats. Several improvements and additions.  
[Oct-27-2017] Revision 270 was just published in all formats. Fixes issue #6 and adds more to Janus section.  
[Oct-23-2017] Quite a lot has been added to the Janus Graph section - more to come soon  
[Oct-15-2017] Experimental - The /book folder now includes DOCBOOK, EPUB and MOBI format versions of the book.  
[Oct-10-2017] Several sections have been improved, I also made updates to reflect changes made in Tinkerpop 3.3
