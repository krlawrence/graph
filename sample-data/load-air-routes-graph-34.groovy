// You can use this file to load the air-routes graph from the Gremlin Console
//
// To execute use the console command ":load load-air-routes-graph-34.groovy"
//
// NOTE: This version is updated from the original load-air-routes-graph.groovy
//       to demonstrate the new (now preferred) way to load a GraphML file
//       using the g.io() approach introduced in Apache TinkerPop version 3.4.

conf = new BaseConfiguration()
conf.setProperty("gremlin.tinkergraph.vertexIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.edgeIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.vertexPropertyIdManager","LONG")
graph = TinkerGraph.open(conf)
g=graph.traversal()

// Change the path below to point to wherever you put the graphml file.

// We need to explicitly tell Gremlin which reader to use as it does not recognize the GraphML
// file extension. It does recognize XML as an extension so an alternative would be to rename
// the file.
g.io("/mydata/air-routes.graphml"). with(IO.reader,IO.graphml).read().iterate()

:set max-iteration 1000    
