# Practical Gremlin: An Apache TinkerPop Tutorial

![map](https://github.com/krlawrence/graph/blob/main/images/map-with-plane.png?raw=true, "graph picture")

## Welcome!

This repository is the home for the source materials, sample code and examples for the book "Practical Gremlin - An Apache TinkerPop Tutorial". This is also the home for the `air-routes` dataset referenced throughout the book and samples.

### Quick Start - the latest version

Preview releases for the second edition are published as HTML automatically as they are authored and can be found [here and HTML](https://krlawrence.github.io/graph/) and [here as PDF](https://krlawrence.github.io/graph/Practical-Gremlin.pdf). It should be noted when reading this revision of the book that it is still a draft version and under significant modification, so you can expect volatile changes. 

The first edition is still available. To view it in a browser (HTML format) click [here](http://kelvinlawrence.net/book/PracticalGremlin.html) or for a PDF version click [here](http://kelvinlawrence.net/book/PracticalGremlin.pdf). These versions are not updated anymore. You will find other formats including MOBI, EPUB and XML (Docbook) in the [releases](https://github.com/krlawrence/graph/releases)  section. The PDF version is currently the "official" version. It has a better table of contents, page numbers, and some better formatting.

### LATEST NEWS

- [Dec-17-2025] Auto-published the PDF version of book via GitHub Actions as changes arrive to the 'main' branch. 
- [Oct-22-2025] Generated 1.0 of the air-routes dataset. 
- [Jul-17-2024] Auto-published the HTML version of book via GitHub Actions as changes arrive to the 'main' branch. 
- [Sep-23-2023] Work has started on a second edition of the book! See the note below for more details.

### Work has begun on the second edition!

September 23rd 2023

Ever since I began working on Practical Gremlin, I've viewed it as a living book, and have tried to keep up with new features and changes taking place at Apache TinkerPop, updating the manuscript as TinkerPop and Gremlin evolve. However, and this is great to see, the rate and pace at which new features are appearing in Gremlin has steadily increased over the years. This has made it hard to keep up in a "living book" fashion. Moreover, some material currently in Practical Gremlin discusses features and limitations in the language that have since either been improved or deprecated. It's really time to start work on a second edition.  I'm excited to announce that [Stephen Mallette](https://stephen.genoprime.com/) has graciously offered to help with this task. We have created a [V1](https://github.com/krlawrence/graph/tree/v1) branch which archives the first edition progress. Work for the second edition will simply occur on the default branch. Many of the new Gremlin features that need adding to the manuscript are captured in a [tracking issue](https://github.com/krlawrence/graph/issues/115) and there is also a [planning board](https://github.com/krlawrence/graph/projects/2#) that will continue to be used as a way to organize the work ahead. I'm really looking forward to the collaboration with Stephen, and to the production of a fully up-to-date second edition of Practical Gremlin. Please keep an eye on the latest news section here for more updates.

## Releases and change history

The most recent changes and additions are tracked in the [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) file.

**A Special note about releases**

Starting with revision 274 (Dec 24th 2017), all of the output files (XML, EPUB, MOBI, HTML and PDF) will now be stored using Git releases. Going forward, this should reduce the amount of disk space required for people who create forks of this project. The release notes and downloadable materials are located [here](https://github.com/krlawrence/graph/releases).

Details of how to build the various output formats from the AsciiDoc source are contained in the `README.md` file under the *book* folder.

## How this book came to be

I forget exactly when, but over a year ago I started compiling a list of notes, hints and tips, initially for my own benefit, of things I had found poorly explained elsewhere while using graph databases and especially using Apache TinkerPop, JanusGraph and Gremlin. Over time that document grew (and continues to grow) and has effectively become a book. After some encouragement from colleagues I have decided to release it as a living book in an open source venue so that anyone who is interested can read it. It is aimed at programmers and anyone using the Gremlin query language to work with graphs. Lots of code examples, sample queries, discussion of best practices, lessons I learned the hard way etc. are included.

While this book remains a work in progress, indeed some sections are still to be filled in, I think there is enough here that people may find it a useful aid to learning the Gremlin graph traversal and query language.

Thanks to all those that have encouraged me to keep going with this adventure!  

Kelvin R. Lawrence  
October 6th, 2017  

## Introduction

**This book is a work in progress. Feedback (ideally via issue) is very much encouraged and welcomed!**

The title of this book could equally well be "A getting started guide for users of graph databases and the Gremlin query language featuring hints, tips and sample queries". It turns out that is a bit too too long to fit on one line for a heading but in a single sentence that describes the focus of this book pretty well.

The book introduces the Apache TinkerPop Gremlin graph query and traversal language via real examples against a real world graph. They are given as a set of working examples against a graph that is also provided in the sample-data folder. The graph, air-routes.graphml, is a model of the world airline route network between 3,504 airports including 50,637 routes. The examples presented will work unmodified with the air-routes.graphml file loaded into the Gremlin Console running with a TinkerGraph.

## How this site is organized

The book is being written using a text editor in AsciiDoc format. The source manuscript "Practical-Gremlin.adoc" can be found in the /book folder. These files always reflect the most recent updates and will often be ahead of the other formats that are only updated as part of a formal release. You will find formatted HTML and PDF versions of the book in the releases area. Formal releases also include DOCBOOK, EPUB and MOBI versions. These can be viewed using many tools and e-book readers. Note that currently, only the HTML and PDF versions have all the nice colors for source code listings etc.

Included with the book are sample graph data (GraphML) and program files. You will find these, as well as some screen shots and images, and demos in the following folders.

- /book
- /sample-data
- /sample-code
- /images
- /demos

The air-routes data set, along with tools to manipulate it and generate different file formats, is located here:

- /make-route-graph
  
## How the book is organized
This is the current layout of the second edition. As work progresses additional chapters
may be added and the layout further refined.

**Chapter 1 - INTRODUCTION**
- We start our journey with a brief introduction to Apache Tinkerpop and a quick look
  at why Graph databases are of interest to us. We also discuss how the book is
  organized and where to find additional materials; such as sample code and data
  sets. 

**Chapter 2 - GETTING STARTED**
- Many of the examples throughout the book use the Gremlin Console and TinkerGraph,
  and both are introduced in this chapter. We also introduce the air-routes example
  graph - `air-routes.graphml` -  used throughout the book.

**Chapter 3 - WRITING GREMLIN QUERIES**
- Now that the basics have been covered, things start to get a lot more interesting!
  It's time to start writing Gremlin queries. We
  briefly explore how we could have built the 'air-routes' graph using a
  relational database, and then look at how SQL and Gremlin are both similar in some
  ways, and very different in others. We then introduce several of the key Gremlin
  query language '"steps"'. We focus on exploring the graph rather than changing it
  in this chapter.

**Chapter 4 - BEYOND BASIC QUERIES**
- Having now introduced Gremlin in some detail, we introduce the Gremlin steps that
  can be used to create, modify and delete, data. We present a selection of best
  practices and start to explore some more advanced query writing.

**Chapter 5 - MISCELLANEOUS QUERIES AND THE RESULTS THEY GENERATE**
- Using the Gremlin steps introduced in Chapters 3 and 4, we are now ready to use
  what we have learned so far and write queries that analyze the air-routes graph in
  more depth and answer more complicated questions. The material presented includes
  a discussion of analyzing distances, route distribution, and writing geospatial
  queries.

**Chapter 6 - MOVING BEYOND THE GREMLIN CONSOLE**
- The next step in our journey is to move beyond the Gremlin Console and take a look
  at interacting with a TinkerGraph using Java and Groovy applications.

**Chapter 7 - INTRODUCING GREMLIN SERVER**
- Our journey so far has focussed on working with graphs in a "directly attached"
  fashion. We now introduce Gremlin Server as a way to deploy and interact with
  remotely hosted graphs.

**Chapter 8 - COMMON GRAPH SERIALIZATION FORMATS**
- Having introduced Gremlin Server we take a look at some common Graph serialization
  file formats along with coverage of how to use them in the context of TinkerPop
  enabled graphs. We take a close look at the TinkerPop GraphSON (JSON) format which
  is used extensively when using Gremlin queries in conjunction with a Gremlin
  Server.

**Chapter 9 - FURTHER READING**
- Our journey to explore Apache TinkerPop and Gremlin concludes with a look at
  useful sources of further reading. We present l links to useful web sites where you
  can find tools and documentation for many of the topics and technologies covered in
  this book. 
                                                           
---

**NEWS ARCHIVE**
[Sep-22-2023] Added a script to the sample-data folder for producing Cypher versions of the air-routes data.
[Aug-29-2022] Updated versions of the sample data and corresponding demo apps have been uploaded.
[May-04-2022] The latest preview draft of revision 283 (TP 3.5.1) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Jun-28-2020] I have started creating a series of [issues](https://github.com/krlawrence/graph/issues) to capture changes and updates I hope to make soon. The issues contain some interesting examples of both new Gremlin features and some additional queries, some of which are quite advanced, that I intend to add to the manuscript as time allows. The issues have been sorted into a Kanban board which can be found in the Projects area. 
[Oct-26-2019] Revision 282 (TP 3.4.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Aug-31-2021] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Aug-02-2021] The latest preview draft of revision 283 (TP 3.5.1) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.    
[Jul-31-2021] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Jul-10-2021] The latest preview draft of revision 283 (TP 3.4.10) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Mar-21-2021] The latest preview draft of revision 283 (TP 3.4.10) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Mar-14-2021] The latest preview draft of revision 283 (TP 3.4.10) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Aug-28-2020] The latest preview draft of revision 283 (TP 3.4.8) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Feb-01-2020] Updated versions of the sample data and corresponding demo apps have been uploaded.   
[Jan-06-2020] The latest draft of revision 283 (TP 3.4.4 preview) is now available in HTML and PDF versions. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Oct-13-2019] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Oct-26-2019] Revision 282 (TP 3.4.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Apr-27-2019] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Dec-26-2018] Revision 281 (TP 3.3.4) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Dec-24-2018] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Sep-29-2018] Updated versions of the sample data and corresponding demo apps have been uploaded.  
[Jul-28-2018] Revision 280 (TP 3.3.3) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[May-29-2018] Revision 279 (TP 3.3.3) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Mar-28-2018] Revision 278 (TP 3.3.1) was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
[Feb-11-2018] Revision 277 was just published in all formats. See [change history](https://github.com/krlawrence/graph/blob/main/ChangeHistory.md) for details.  
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
