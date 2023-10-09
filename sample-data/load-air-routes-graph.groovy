// You can use this file to load the air-routes graph from the Gremlin Console
//
// To execute use the console command ":load load-air-routes-graph.groovy"
//
// This file uses the deprecated way of creating a graph and loading data. See
// the file load-air-routes34.groovy to see the currently recommended way.

conf = new BaseConfiguration()
conf.setProperty("gremlin.tinkergraph.vertexIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.edgeIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.vertexPropertyIdManager","LONG")
graph = TinkerGraph.open(conf)

// Change the path below to point to wherever you put the graphml file
graph.io(graphml()).readGraph('/mydata/air-routes.graphml')

g=graph.traversal()
:set max-iteration 1000     
