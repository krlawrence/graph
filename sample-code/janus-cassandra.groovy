;[]// Create a Janus Graph instance and connect it to a Cassandra back end. Next
;[]// define the schema and index and load air-routes graph data.  This is
;[]// intended to be loaded and run inside the Gremlin Console from the Janus
;[]// Graph download.  Usage :load janus-cassandra.groovy
;[]// Note that the ;[] is used purely to stop unwanted console output.

println "\n==============================================";[]
println "Creating Cassandra backed Janus Graph instance";[]
println "==============================================\n";[]
;[]// Create a new graph instance
;[]// Use the following line to use CQL
;[]//graph = JanusGraphFactory.open('conf/janusgraph-cql.properties')

;[]// Use the following line to use Thrift. Thrift is disabled by default but
;[]// can be enabled using Nodetool or using the CASSANDRA_START_RPC=true
;[]// environment variable.
graph = JanusGraphFactory.open('conf/janusgraph-cassandra.properties')
println "\n===============";[]
println "Defining labels";[]
println "===============\n";[]
;[]// Define edge labels and usage
mgmt = graph.openManagement()
mgmt.makeEdgeLabel('route').multiplicity(MULTI).make()
mgmt.makeEdgeLabel('contains').multiplicity(SIMPLE).make()

;[]// Define vertex labels
mgmt.makeVertexLabel('version').make()
mgmt.makeVertexLabel('airport').make()
mgmt.makeVertexLabel('country').make()
mgmt.makeVertexLabel('continent').make()

println "\n=============";[]
println "Creating keys";[]
println "=============\n";[]
;[]// Define vertex property keys
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

;[]// Define edge property keys
mgmt.makePropertyKey('dist').dataType(Integer.class).cardinality(Cardinality.SINGLE).make()

println "\n==============";[]
println "Building index";[]
println "==============\n";[]

;[]// Construct a composite index for a few commonly used property keys
graph.tx().rollback();[]

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

println "\n==================";[]
println "Committing changes";[]
println "==================\n";[]
mgmt.commit()


println "\n=================================";[]
println "Waiting for the index to be ready";[]
println "=================================\n";[]

mgmt.awaitGraphIndexStatus(graph, 'airportIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'icaoIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'cityIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'runwayIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'countryIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'regionIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'typeIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'distIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()


;[]// Load the air-routes graph and display a few statistics.
;[]// Not all of these steps use the index so Janus Graph will give us some warnings.
println "\n========================";[]
println "Loading air-routes graph";[]
println "========================\n";[]
graph.io(graphml()).readGraph('air-routes.graphml')
graph.tx().commit()

println "\n==========================";[]
println "Acquiring traversal source";[]
println "==========================\n";[]
;[]// Setup our traversal source object
g = graph.traversal()

println "\n==========================";[]
println "Preparing graph statistics";[]
println "==========================\n";[]
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

;[]// Look at the properties, just as an example of how to do it!
println "\n========================";[]
println "Retrieving property keys";[]
println "========================\n";[]
mgmt = graph.openManagement();[]
types = mgmt.getRelationTypes(PropertyKey.class);[] 
types.each{println "$it\t: " + mgmt.getPropertyKey("$it").dataType() + " " + mgmt.getPropertyKey("$it").cardinality()};[]
mgmt.commit();[]


;[]// Query the indexes we created as an example of how to do it!
println "\n==================";[]
println "Retrieving indexes";[]
println "==================\n";[]
mgmt = graph.openManagement();[]
v_idxes = mgmt.getGraphIndexes(Vertex.class);[]
e_idxes = mgmt.getGraphIndexes(Edge.class);[]
v_idxes.each {println "Name: ${it.name()}, Keys: ${it.getFieldKeys()}, Composite: ${it.isCompositeIndex()}, Backing: ${it.getBackingIndex()}"};[]
e_idxes.each {println "Name: ${it.name()}, Keys: ${it.getFieldKeys()}, Composite: ${it.isCompositeIndex()}, Backing: ${it.getBackingIndex()}"};[]
mgmt.commit();[]


println "\n===============";[]
println "Tasks completed";[]
println "===============\n";[]



