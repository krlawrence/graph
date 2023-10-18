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
 
g.addV("person").property("name","Albert").as("albert").
  addV("person").property("name","Bill").as("bill").
  addV("person").property("name","Fred").as("fred").
  addV("person").property("name","Janet").as("janet").
  addV("person").property("name","Mary").as("mary").
  addV("person").property("name","Max").as("max").
  addV("person").property("name","Lily").as("lily").
  addV("person").property("name","Peter").as("peter").
  addV("person").property("name","Sarah").as("sarah").
  addV("person").property("name","Susan").as("susan").
  addV("place").property("name","Boston").as("bos").
  addV("place").property("name","Chicago").as("ord").
  addV("place").property("name","Dallas").as("dal").
  addV("place").property("name","Los Angeles").as("lax").
  addV("place").property("name","Miami").as("mia").
  addV("place").property("name","New York City").as("nyc").
  addV("place").property("name","Seattle").as("sea").
  addV("country").property("name","USA").as("usa").
  addE("knows").from("albert").to("mary").
  addE("lives_in").from("albert").to("mia").
  addE("knows").from("bill").to("fred").
  addE("knows").from("bill").to("max").
  addE("knows").from("bill").to("peter").
  addE("lives_in").from("bill").to("mia").
  addE("knows").from("fred").to("mary").
  addE("knows").from("fred").to("janet").
  addE("knows").from("fred").to("bill").
  addE("knows").from("fred").to("max").
  addE("lives_in").from("fred").to("sea").
  addE("knows").from("janet").to("fred").
  addE("knows").from("janet").to("lily").
  addE("lives_in").from("janet").to("dal").
  addE("knows").from("lily").to("janet").
  addE("lives_in").from("lily").to("bos").
  addE("knows").from("mary").to("albert").
  addE("knows").from("mary").to("susan").
  addE("knows").from("mary").to("max").
  addE("knows").from("mary").to("fred").
  addE("lives_in").from("mary").to("nyc").
  addE("knows").from("max").to("bill").
  addE("knows").from("max").to("fred").
  addE("knows").from("max").to("mary").
  addE("knows").from("max").to("peter").
  addE("lives_in").from("max").to("bos").
  addE("knows").from("peter").to("bill").
  addE("knows").from("peter").to("susan").
  addE("knows").from("peter").to("max").
  addE("lives_in").from("peter").to("dal").
  addE("knows").from("susan").to("peter").
  addE("knows").from("susan").to("mary").
  addE("lives_in").from("sarah").to("ord").
  addE("lives_in").from("susan").to("sea").
  addE("city_in").from("bos").to("usa").
  addE("city_in").from("dal").to("usa").
  addE("city_in").from("lax").to("usa").
  addE("city_in").from("mia").to("usa").
  addE("city_in").from("nyc").to("usa").
  addE("city_in").from("ord").to("usa").
  addE("city_in").from("sea").to("usa").iterate()
 
// What does the graph look like?
g.V().order().by('name').outE().inV().path().by('name').by(label)
 
// What is the distribution of relationships?
g.V().hasLabel('person','place').out().groupCount().by('name')
g.V().hasLabel('person','place').in().groupCount().by('name')
 
// What vertices do we have?
g.V().order().by('name').outE().inV().path().by('name').by(label) 
 
// Who does Max know that lives in Miami?
 g.V().has('name','Max').out('knows').where(out('lives_in').values('name').is('Miami')).values('name')
 
// Who lives in New York City?
g.V().hasLabel('person').where(out('lives_in').has('name','New York City')).values('name')
 
// Who are Mary's friends , friends ?
g.V().has('person','name','Mary').as('mary').
      out('knows').out('knows').where(neq('mary')).dedup().values('name')
 
// Who are Mary's friends , friends ? (using match) 
g.V().has('person','name','Mary').
      match(__.as('a').out('knows').as('b')
           ,__.as('b').out('knows').where(neq('a')).as('c')).
      select('c').by('name').dedup() 
 
// Who does Mary know that already know each other?
g.V().has('person','name','Mary').out('knows').as('x').aggregate('maryalreadyknows').
      out('knows').where(within('maryalreadyknows')).path().by('name').from('x')
      
// Who does Mary know whose friends don't know Mary?
g.V().has('person','name','Mary').as('mary').
     out('knows').aggregate('maryalreadyknows').
     out('knows').where(neq('mary')).where(without('maryalreadyknows')).
     values('name').dedup()
    
// Who does Mary know whose friends don't know Mary? (using match)
g.V().has('person','name','Mary').
      match(__.as('mary').out('knows').as('maryalreadyknows')
           ,__.as('maryalreadyknows').out('knows').where(neq('mary')).as('c')
           ,__.not(__.as('mary').out().as('c'))).
      select('c').by('name').dedup()
 
// Which of Mary's friends already know each other and which do not?  
g.V().has('name','Mary').as('mary').out('knows').aggregate('maryknows').
      local(union(identity().values('name'),
            out('knows').where(neq('mary')).where(within('maryknows')).
      values('name')).fold())

