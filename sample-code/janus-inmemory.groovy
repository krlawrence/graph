// Create an in memory Janus Graph instance, define the schema and index and load air-routes.
// This is intended to be loaded and run inside the Gremlin Console from the Janus
// Graph download. Usage :load janus-inmemory.groovy

println "\n=======================================";[]
println "Creating in-memory Janus Graph instance";[]
println "=======================================\n";[]
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

idx1 = mgmt.buildIndex('airportIndex',Vertex.class)
idx2 = mgmt.buildIndex('icaoIndex',Vertex.class)
idx3 = mgmt.buildIndex('cityIndex',Vertex.class)
idx4 = mgmt.buildIndex('runwayIndex',Vertex.class)
idx5 = mgmt.buildIndex('countryIndex',Vertex.class)
idx6 = mgmt.buildIndex('regionIndex',Vertex.class)
idx7 = mgmt.buildIndex('typeIndex',Vertex.class)
idx8 = mgmt.buildIndex('distIndex',Edge.class)

iata = mgmt.getPropertyKey('code')
icao = mgmt.getPropertyKey('icao')
city = mgmt.getPropertyKey('city')
rway = mgmt.getPropertyKey('runways')
ctry = mgmt.getPropertyKey('country')
regn = mgmt.getPropertyKey('region')
type = mgmt.getPropertyKey('type')
dist = mgmt.getPropertyKey('dist')

idx1.addKey(iata).buildCompositeIndex()
idx2.addKey(icao).buildCompositeIndex()
idx3.addKey(city).buildCompositeIndex()
idx4.addKey(rway).buildCompositeIndex()
idx5.addKey(ctry).buildCompositeIndex()
idx6.addKey(regn).buildCompositeIndex()
idx7.addKey(type).buildCompositeIndex()
idx8.addKey(dist).buildCompositeIndex()

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

mgmt.awaitGraphIndexStatus(graph, 'countryIndex').
     status(SchemaStatus.REGISTERED).call()

mgmt.awaitGraphIndexStatus(graph, 'regionIndex').
     status(SchemaStatus.REGISTERED).call()

mgmt.awaitGraphIndexStatus(graph, 'typeIndex').
     status(SchemaStatus.REGISTERED).call()

mgmt.awaitGraphIndexStatus(graph, 'distIndex').
     status(SchemaStatus.REGISTERED).call()

// Once the index is created force a re-index Note that a reindex is not strictly
// necessary here. It could be avoided by creating the keys and index as part of the
// same transaction. I did it this way just to show an example of re-indexing being
// done. A reindex is always necessary if the index is added after data has been
// loaded into the graph.

println "\n===========";[]
println "re-indexing";[]
println "===========\n";[]
mgmt = graph.openManagement()

mgmt.awaitGraphIndexStatus(graph, 'airportIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('airportIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'icaoIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('icaoIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'cityIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('cityIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'runwayIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('runwayIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'countryIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('countryIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'regionIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('regionIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'typeIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('typeIndex'), SchemaAction.REINDEX).get()

mgmt.awaitGraphIndexStatus(graph, 'distIndex').call()
mgmt.updateIndex(mgmt.getGraphIndex('distIndex'), SchemaAction.REINDEX).get()

mgmt.commit()

// Load the air-routes graph and display a few statistics.
// Not all of these steps use the index so Janus Graph will give us some warnings.
println "\n========================";[]
println "Loading air-routes graph";[]
println "========================\n";[]
graph.io(graphml()).readGraph('air-routes.graphml')
graph.tx().commit();[]

// Setup our traversal source object
g = graph.traversal()

// Display a few statistics
apt = g.V().has('type','airport').count().next();[]
cty = g.V().has('type','country').count().next();[]
cnt = g.V().has('type','continent').count().next();[]
rts = g.E().hasLabel('route').count().next();[]
edg = g.E().count().next();[]

println "Airports   : $apt";[]
println "Countries  : $cty";[]
println "Continents : $cnt";[]
println "Routes     : $rts";[]
println "Edges      : $edg";[]

// Look at the properties, just as an exampl of how to do it!
println "\n========================";[]
println "Retrieving property keys";[]
println "========================\n";[]
mgmt = graph.openManagement()
types = mgmt.getRelationTypes(PropertyKey.class);[] 
types.each{println "$it\t: " + mgmt.getPropertyKey("$it").dataType() + " " + mgmt.getPropertyKey("$it").cardinality()};[]
mgmt.commit()   



