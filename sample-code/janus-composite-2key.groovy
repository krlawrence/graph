// This code is intended to be used within the Gremlin Console while using JanusGraph
// You can invoke it using  :load janus-composite-2key.groovy
//
// The code below will create an index that can help queries such as:
//    g.V().has('city','London').has('country','CA')

// Make sure no other transactions are active
graph.tx().rollback() 

// Start a new management transaction
mgmt = graph.openManagement()

// Find the property keys that we need to index
city = mgmt.getPropertyKey('city')
country = mgmt.getPropertyKey('country')

// Create a new index and add our keys
index = mgmt.buildIndex('byCityAndCountry', Vertex.class)
index.addKey(country).addKey(city).buildCompositeIndex()

// Wait for our new index to be ready
mgmt.awaitGraphIndexStatus(graph, 'byCityAndCountry').call()

// Force a re-index
mgmt = graph.openManagement()
mgmt.updateIndex(mgmt.getGraphIndex("byCityAndCountry"), SchemaAction.REINDEX).get()
mgmt.commit()

// All done
mgmt.commit()

// Test out our new index. The profile step will show us which index was used
g.V().has('city','London').has('country','CA').profile()
graph.tx().commit()
