# Air Route data - and more!
This folder contains several files of sample data in various formats including the GraphML file for the `air-routes` graph. The `air-routes` graph is referenced throughout the book.

## About the data referenced in the book

Two versions (sizes) of the air-routes data set files are provided. The file `air-routes.graphml` contains the full graph containing over 40,000 routes and over 3,300 airports. The file `air-routes-small.graphml` just has routes between 46 airports all located in the US.
All of the examples in the book are based on `air-routes.graphml`. These are the versions used by examples in the book. This way, even as the data is updated (see "Latest data" below), the examples in the book can always be verified against these original versions.

The small script called *'load-air-routes-graph.groovy'* can be used to load the graph into a TinkerGraph from within the Gremlin Console. Edit the script before using it to adjust the location of where you put the GraphML file on your system.

There is also a small Groovy script in the *'/sample-code'* folder called `graph-stats.groovy` that can be run from within the Gremlin console to produce some statistics about the graph similar to those found in the book and in the `README-air-routes.txt` file.

The *'aircraft.csv'* file is intended to be used with the `add-aircraft.groovy` sample that is located in the *'/sample-code'* folder.

The `edges.csv` file is intended to be used with the `GraphFromCSV.java` sample that is located in the *'/sample-code'* folder.

## Latest data

The two files `air-routes-latest.graphml` and `air-routes-small-latest.graphml` contain additional routes and airports added since the original version was uploaded. They are provided in case anyone wants to experiment with some more recent data. The latest data set adds 130 airports and 7,237 routes to the original `air-routes.graphml` data set.

## Additional formats

This folder also contains two CSV files to go along withe the GraphML files. They are called `air-routes-latest-nodes.csv` and `air-routes-latest-edges.csv`. The CSV files were produced using the open source [GraphML2CSV tool](https://github.com/awslabs/amazon-neptune-tools/tree/master/graphml2csv). You may need to edit the first line (header) of each CSV file depending on your graph database and toolset. 

It is also possible to turn the CSV files into batches of Gremlin `addV` and `addE` commands using the [csv-gremlin tool](https://github.com/awslabs/amazon-neptune-tools/blob/master/csv-gremlin/README.md). This provides yet another way that the `air-routes` data set can be loaded into a TinkerPop compatible graph store.

To allow for some interesting comparisons in diferent ways of modeling the data, this folder also contains sub-folders containing RDF and SQL versions of the `air-routes` dataset.
The RDF data was created using the Ruby script that can be found in the `sample-data/RDF` folder. You may also be interested in the [AWS CSV2RDF](https://github.com/aws/amazon-neptune-csv-to-rdf-converter) tool which can turn CSV files into NQuad format RDF files.

Please check back periodically to find any additional updates.
