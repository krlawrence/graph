// Create an in memory Janus Graph instance, define the schema and load air-routes
// This is intended to be loaded and run inside the Gremlin Console from the Janus
// Graph download.

println "\n=====================================";[]
println "Creating in-memory Janus Graph instance";[]
println "=====================================\n";[]
// Create a new graph instance
graph = JanusGraphFactory.open('inmemory')

println "\n===============";[]
println "Defining labels";[]
println "===============\n";[]
// Define edge labels and usage
mgmt = graph.openManagement()
mgmt.makeEdgeLabel('route').multiplicity(MULTI).make()
mgmt.makeEdgeLabel('contains').multiplicity(SIMPLE).make()
mgmt.commit()

// Define vertex labels
mgmt = graph.openManagement()
mgmt.makeVertexLabel('version').make()
mgmt.makeVertexLabel('airport').make()
mgmt.makeVertexLabel('country').make()
mgmt.makeVertexLabel('continent').make()
mgmt.commit()

println "\n=============";[]
println "Creating keys";[]
println "=============\n";[]
// Define vertex property keys
mgmt = graph.openManagement()
mgmt.makePropertyKey('code').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('icao').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('type').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('city').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('country').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('region').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('desc').dataType(String.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('runways').dataType(Integer.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('elev').dataType(Integer.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('lat').dataType(Double.class).cardinality(Cardinality.SINGLE).make()
mgmt.makePropertyKey('lon').dataType(Double.class).cardinality(Cardinality.SINGLE).make()
mgmt.commit()

// Define edge property keys
mgmt = graph.openManagement()
mgmt.makePropertyKey('dist').dataType(Integer.class).cardinality(Cardinality.SINGLE).make()
mgmt.commit()

println "\n==============";[]
println "Building index";[]
println "==============\n";[]

// Construct a composite index for a few commonly used property keys
graph.tx().rollback()
mgmt=graph.openManagement()
idx1=mgmt.buildIndex('airportIndex',Vertex.class)
idx2 =mgmt.buildIndex('icaoIndex',Vertex.class)
idx3 =mgmt.buildIndex('cityIndex',Vertex.class)
idx4 =mgmt.buildIndex('runwayIndex',Vertex.class)
iata = mgmt.getPropertyKey('code')
icao = mgmt.getPropertyKey('icao')
city = mgmt.getPropertyKey('city')
rway = mgmt.getPropertyKey('runways')
idx1.addKey(iata).buildCompositeIndex()
idx2.addKey(icao).buildCompositeIndex()
idx3.addKey(city).buildCompositeIndex()
idx4.addKey(rway).buildCompositeIndex()
mgmt.commit()


println "\n=================================";[]
println "Waiting for the index to be ready";[]
println "=================================\n";[]

mgmt.awaitGraphIndexStatus(graph, 'airportIndex').
     status(SchemaStatus.REGISTERED).call()

mgmt.awaitGraphIndexStatus(graph, 'icaoIndex').
     status(SchemaStatus.REGISTERED).call()

mgmt.awaitGraphIndexStatus(graph, 'cityIndex').
     status(SchemaStatus.REGISTERED).call()

mgmt.awaitGraphIndexStatus(graph, 'runwayIndex').
     status(SchemaStatus.REGISTERED).call()

// Once the index is created force a re-index
println "\n===========";[]
println "re-indexing";[]
println "===========\n";[]
mgmt = graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'airportIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('airportIndex'), SchemaAction.REINDEX).get()
mgmt.commit()

mgmt = graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'icaoIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('icaoIndex'), SchemaAction.REINDEX).get()
mgmt.commit()

mgmt = graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'cityIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('cityIndex'), SchemaAction.REINDEX).get()
mgmt.commit()

mgmt = graph.openManagement()
mgmt.awaitGraphIndexStatus(graph, 'runwayIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('runwayIndex'), SchemaAction.REINDEX).get()
mgmt.commit()

// Load the air-routes graph
println "\n========================";[]
println "Loading air-routes graph";[]
println "========================\n";[]
graph.io(graphml()).readGraph('air-routes.graphml')
graph.tx().commit()

// Look at the properties
mgmt = graph.openManagement()
types = mgmt.getRelationTypes(PropertyKey.class) 
types.each{println "$it\t: " + mgmt.getPropertyKey("$it").dataType() + " " + mgmt.getPropertyKey("$it").cardinality()}
mgmt.commit()   

// Setup our traversal object
g = graph.traversal()



