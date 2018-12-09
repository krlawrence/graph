// quick-social2.groovy
//
// Creates a simple social graph that can be useful for doing some
// simple testing and experimenting. A few example queries are also
// included. This will work unchanged from the Gremlin console if
// using a TinkerGraph or with any other TinkerPop graph database if
// you remove the first two lines and the comment lines.

// Remove these two lines if using a graph db other than TinkerGraph.
graph=TinkerGraph.open()
g=graph.traversal()

g.addV("person").property("name","Fred").as("fred").
  addV("person").property("name","Mary").as("mary").
  addV("person").property("name","Bill").as("bill").
  addV("person").property("name","Janet").as("janet").
  addV("person").property("name","Max").as("max").
  addV("person").property("name","Susan").as("susan").
  addV("person").property("name","Peter").as("peter").
  addV("person").property("name","Lily").as("lily").
  addV("place").property("name","New York City").as("nyc").
  addV("place").property("name","Boston").as("bos").
  addV("place").property("name","Dallas").as("dal").
  addV("place").property("name","Seattle").as("sea").
  addV("place").property("name","Miami").as("mia").
  addE("knows").from("bill").to("fred").
  addE("knows").from("bill").to("max").
  addE("lives_in").from("bill").to("mia").
  addE("knows").from("fred").to("mary").
  addE("knows").from("fred").to("janet").
  addE("knows").from("fred").to("bill").
  addE("lives_in").from("fred").to("sea").
  addE("knows").from("janet").to("fred").
  addE("knows").from("janet").to("lily").
  addE("lives_in").from("janet").to("dal").
  addE("knows").from("lily").to("janet").
  addE("lives_in").from("lily").to("bos").
  addE("knows").from("mary").to("susan").
  addE("knows").from("mary").to("max").
  addE("knows").from("mary").to("fred").
  addE("lives_in").from("mary").to("nyc").
  addE("knows").from("max").to("bill").
  addE("knows").from("max").to("mary").
  addE("knows").from("max").to("peter").
  addE("lives_in").from("max").to("bos").
  addE("knows").from("peter").to("susan").
  addE("knows").from("peter").to("max").
  addE("lives_in").from("peter").to("dal").
  addE("knows").from("susan").to("peter").
  addE("knows").from("susan").to("mary").
  addE("lives_in").from("susan").to("sea").iterate()

// What does the graph look like?
g.V().order().by('name').outE().inV().path().by('name').by(label)

g.V().out().groupCount().by('name')
g.V().in().groupCount().by('name')

// Who does Max know that lives in Miami?
 g.V().has('name','Max').out('knows').where(out('lives_in').values('name').is('Miami')).values('name')

// Who lives in New York City?
g.V().hasLabel('person').where(out('lives_in').has('name','New York City')).values('name')
