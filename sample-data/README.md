This folder contains the GraphML file for the air-routes graph and other sample data. The air-routes graph is referenced throughout the book.

Two versions of the air-routes data set files are provided. The file *'air-routes.graphml'* contains the full graph containing over 40,000 routes and over 3,300 airports. The file air-routes-small.graphml just has routes between 46 airports all located in the US.
All of the examples in the book are based on *'air-routes.graphml'*.

The small script called *'load-air-routes-graph.groovy'* can be used to load the graph into a TinkerGraph from within the Gremlin Console. Edit the script before using it to adjust the location of where you put the GraphML file on your system.

There is also a small Groovy script in the *'/sample-code'* folder called *'graph-stats.groovy'* that can be run from within the Gremlin console to produce some statistics about the graph similar to those found in the book and in the *'README-air-routes.txt file'*.

The *'aircraft.csv'* file is intended to be used with the *'add-aircraft.groovy'* sample that is located in the *'/sample-code'* folder.

The *'edges.csv'* file is intended to be used with the *'GraphFromCSV.java'* sample that is located in the *'/sample-code'* folder.

The two files *'air-routes-latest.graphml'* and *'air-routes-small-latest.graphml'* contain additional routes and airports added since the original version was uploaded. They are provided in case anyone wants to experiment with some more recent data. The latest data set includes 21 additional airports and 1,921 additional routes.

Please check back regularly to see more updates as they are uploaded.
