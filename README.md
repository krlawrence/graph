# Practical Gremlin: An Apache TinkerPop Tutorial


![map](https://github.com/krlawrence/graph/raw/master/images/GremlinEaselNoText.png?raw=true, "graph picture")

## Welcome!

This repository is the home for the source materials, sample code and examples for the book "Practical Gremlin - An Apache TinkerPop Tutorial". This is also the home for the `air-routes` dataset referenced throughout the book and samples.

**Quick Start - the latest version**  

To read the latest snapshot of the book right away in a browser (HTML format) click [here](http://kelvinlawrence.net/book/PracticalGremlin.html) or for a PDF version click [here](http://kelvinlawrence.net/book/PracticalGremlin.pdf). These snapshots are updated regularly. You will find other formats including MOBI, EPUB and XML (Docbook) in the [releases](https://github.com/krlawrence/graph/releases)  section. Formal releases will be published periodically assuming there is enough new material to make it worthwhile. If you want to see the absolute latest updates you can always browse the Asciidoc source file (Gremlin-Graph-Guide.adoc) in the /book folder. The PDF version is currently the "official" version. It has a better table of contents, some better formatting and a much nicer title page!

**LATEST NEWS:**  
- [May-04-2022] The latest preview draft of revision 283 (TP 3.5.1) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
- [Aug-31-2021] Updated versions of the sample data and corresponding demo apps have been uploaded. 
- [Jun-28-2020] I have started creating a series of [issues](https://github.com/krlawrence/graph/issues) to capture changes and updates I hope to make soon. The issues contain some interesting examples of both new Gremlin features and some additional queries, some of which are quite advanced, that I intend to add to the manuscript as time allows. The issues have been sorted into a Kanban board which can be found in the Projects area. 
- [Oct-26-2019] Revision 282 (TP 3.4.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  


## Releases and change history

The most recent changes and additions are now being tracked in the [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) file.

**A Special note about releases**

Starting with revision 274 (Dec 24 2017), all of the output files (XML, EPUB, MOBI, HTML and PDF) will now be stored using Git releases. Going forward, this should reduce the amount of disk space required for people who create forks of this project. The release notes and dowloadable materials are located [here](https://github.com/krlawrence/graph/releases).

NOTE: In order to prune the unwanted files from the project the commit history for the output files had to be removed as part of making the v274 release. If you had previously cloned or forked this project please create a new clone or fork. Sorry for the inconvenience but this will get you back approximately 60% (27+ mb) of the disk space that was being taken up and will help anyone else making a clone.


Details of how to build the various output formats from the Asciidoc source are contained in the README.md under the *book* folder.

## How this book came to be

I forget exactly when, but over a year ago I started compiling a list of notes, hints and tips, initially  for my own benefit, of things I had found poorly explained elsewhere while using graph databases and especially using Apache TinkerPop, Janus Graph and Gremlin. Over time that document grew (and continues to grow) and has effectively become a book. After some encouragement from colleagues I have decided to release it as a living book in an open source venue so that anyone who is interested can read it. It is aimed at programmers and  anyone using the Gremlin query language to work with graphs. Lots of code examples, sample queries,  discussion of best practices, lessons I learned the hard way etc. are included.

While this book remains a work in progress, indeed some sections are still to be filled in, I think there is enough here that people may find it a useful aid to learning the Gremlin graph traversal and query language.

Thanks to all those that have encouraged me to keep going with this adventure!  

Kelvin R. Lawrence  
October 6th, 2017  

## Introduction

**This book is a work in progress. Feedback (ideally via issue) is very much encouraged and welcomed!**

The title of this book could equally well be "A getting started guide for users of graph databases and the Gremlin query language featuring hints, tips and sample queries". It turns out that is a bit too too long to fit on one line for a heading but in a single sentence that describes the focus of this book pretty well.

The book introduces the Apache TinkerPop 3 Gremlin graph query and traversal language via real examples against a real world graph. They are given as a set of working examples against a graph that is also provided in the sample-data folder. The graph, air-routes.graphml, is a model of the world airline route network between 3,367 airports including 43,160 routes. The examples we present will work unmodified with the air-routes.graphml file loaded into the Gremlin console running with a TinkerGraph.

## How this site is organized

The book is being written using a text editor in AsciiDoc format. The source manuscript "Gremlin-Graph-Guide.adoc" can be found in the /book folder. This file will always reflect the most recent updates and will often be ahead of the other formats that will only be updated as part of a formal release. You will find formatted HTML and PDF versions of the book in the releases area. I have also provided DOCBOOK, EPUB and MOBI versions as part of each release. These can be viewed using many tools and e-book readers. Note that currently, only the HTML and PDF versions have all the nice colors for source code listings etc.

Included with the book are sample graph data (GraphML) and program files. You will find these, as well as some screen shots and images, and demos in the following folders.

- /book
- /sample-data
- /sample-code
- /images
- /demos

## How the book is organized

Chapter 1 - INTRODUCTION
- I start off by briefly doing a recap on why Graph databases are of interest to us
  and discuss some good use cases for graphs. I also provide pointers to the sample
  programs and other additional materials referenced by the book.  
  
Chapter 2 - GETTING STARTED
- In Chapter two I introduce several of the components of Apache TinkerPop and
  also introduce the `air-routes.graphml` file that will be used as the graph the
  majority of examples shown in this book are based on.  
  
Chapter 3 - WRITING GREMLIN QUERIES
- In Chapter three things start to get a lot more interesting! I start discussing
  how to use the Gremlin graph traversal and
  query language to interrogate the 'air-routes' graph. I begin by comparing how we
  could have built the 'air-routes' graph using a more traditional relational database
  and then look at how SQL and Gremlin are both similar in some ways and very
  different in others. For the rest of the Chapter, I introduce several of
  the key Gremlin methods, or as they are often called, '"steps"'. I
  mostly focus on reading the graph (not adding or deleting things) in this Chapter.  
  
Chapter 4 - BEYOND BASIC QUERIES
- In Chapter four the focus moves beyond just reading the graph and I describe how to add
  vertices (nodes), edges and properties as well as how to delete and update them.
  I also present a discussion of various best practices. I also start to explore
  some slightly more advanced topics in this chapter.  
  
Chapter 5 - MISCELLANEOUS QUERIES AND THE RESULTS THEY GENERATE  
- In Chapter five I focus on using what has been covered in the prior Chapters to write
  queries that have a more real-world feel. I present a lot more examples of the
  output from running queries in this Chapter. I also start to discuss topics such
  as analyzing distances, route distribution and writing geospatial queries.  
  
Chapter 6 - MOVING BEYOND THE CONSOLE AND TINKERGRAPH  
- In Chapter six I start to expand the focus to concepts beyond using the Gremlin
  Console and a TinkerGraph. I
  start by looking at how you can write standalone Java and Groovy applications that
  can work with a graph. I then introduce JanusGraph and take a fairly detailed
  look at its capabilities such as support for transactions, schemas and indexes.
  Various technology choices for back end persistent stores
  and indexes are explored along the way.  
  
Chapter 7 - INTRODUCING GREMLIN SERVER  
- In Chapter seven, Gremlin Server is introduced. I begin to explore connecting to
  and working with a remote graph both from the Gremlin Console and the command line
  as well as from code. When this book was first released, the majority of "real
  world" use cases
  focussed on directly attached or even in memory graphs. As Apache TinkerPop has
  evolved, it has become a lot more common to connect to a graph remotely via a
  Gremlin Server.  
  
Chapter 8 - COMMON GRAPH SERIALIZATION FORMATS  
- In Chapter eight a discussion is presented of some common Graph serialization file
  formats along with coverage of how to use them in the context of TinkerPop 3
  enabled graphs.  
  
Chapter 9 - FURTHER READING  
- I finish up by providing several links to useful web sites where you can find
  tools and documentation for many of the topics and technologies covered in this book.
                                                           

**NEWS ARCHIVE:**  
[Aug-02-2021] The latest preview draft of revision 283 (TP 3.5.1) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.    
[Jul-31-2021] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Jul-10-2021] The latest preview draft of revision 283 (TP 3.4.10) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Mar-21-2021] The latest preview draft of revision 283 (TP 3.4.10) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Mar-14-2021] The latest preview draft of revision 283 (TP 3.4.10) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Aug-28-2020] The latest preview draft of revision 283 (TP 3.4.8) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Feb-01-2020] Updated versions of the sample data and corresponding demo apps have been uploaded.   
[Jan-06-2020] The latest draft of revision 283 (TP 3.4.4 preview) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Oct-13-2019] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Oct-26-2019] Revision 282 (TP 3.4.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
[Apr-27-2019] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Dec-26-2018] Revision 281 (TP 3.3.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/master/ChangeHistory.md) for details.  
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
