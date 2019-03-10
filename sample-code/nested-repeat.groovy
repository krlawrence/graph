// Experiments with the nested repeat feature added in TinkerPop 3.4.0
//
// NOTE: To run you will need the Gremlin Console at the TinkerPop 3.4.0
//       level or higher. As other vendors upgrade to TinkerPop 3.4.0
//       you mayl be able to use nested repeats on those platforms also.

conf = new BaseConfiguration()
conf.setProperty("gremlin.tinkergraph.vertexIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.edgeIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.vertexPropertyIdManager","LONG")

graph = TinkerGraph.open(conf)
g = graph.traversal()

g.addV('person').property('name','Amy').as('amy').
  addV('person').property('name','Bill').as('bill').
  addV('person').property('name','Sam').as('sam').
  addV('person').property('name','Eric').as('eric').
  addV('person').property('name','Frank').as('frank').
  addV('person').property('name','Janet').as('janet').
  addV('person').property('name','Marie').as('marie').
  addV('person').property('name','Mark').as('mark').
  addV('person').property('name','Pam').as('pam').
  addV('person').property('name','Peter').as('peter').
  addV('city').property('name','Austin').as('austin').
  addV('city').property('name','Brighton').as('brighton').
  addV('city').property('name','Canterbury').as('canterbury').
  addV('city').property('name','Miami').as('miami').
  addV('city').property('name','New York City').as('nyc').
  addV('county').property('name','Dade').as('dade').
  addV('county').property('name','East Sussex').as('esussex').
  addV('county').property('name','Kent').as('kent').
  addV('county').property('name','New York County').as('nycc').
  addV('county').property('name','Travis').as('travis').
  addV('state').property('name','Florida').as('florida').
  addV('state').property('name','New York').as('ny').
  addV('state').property('name','Texas').as('texas').
  addV('country').property('name','USA').as('usa').
  addV('country').property('name','England').as('england').
  addE('knows').from('bill').to('amy').
  addE('knows').from('bill').to('eric').
  addE('knows').from('bill').to('frank').
  addE('knows').from('bill').to('sam').
  addE('knows').from('eric').to('pam').
  addE('knows').from('eric').to('marie').
  addE('location').from('amy').to('canterbury').
  addE('location').from('bill').to('austin').
  addE('location').from('eric').to('austin').
  addE('location').from('frank').to('miami').
  addE('location').from('janet').to('brighton').
  addE('location').from('marie').to('canterbury').
  addE('location').from('mark').to('miami').
  addE('location').from('pam').to('nyc').
  addE('location').from('peter').to('brighton').
  addE('location').from('sam').to('brighton').
  addE('part_of').from('austin').to('travis').
  addE('part_of').from('brighton').to('esussex').
  addE('part_of').from('canterbury').to('kent').
  addE('part_of').from('miami').to('dade').
  addE('part_of').from('nyc').to('nycc').
  addE('part_of').from('nycc').to('ny').
  addE('part_of').from('travis').to('texas').
  addE('part_of').from('dade').to('florida').
  addE('part_of').from('esussex').to('england').
  addE('part_of').from('kent').to('england').
  addE('part_of').from('florida').to('usa').
  addE('part_of').from('texas').to('usa').
  addE('part_of').from('ny').to('usa').
  iterate()

// Check the relationships we just created.
g.V().hasLabel('person').
      repeat(outE().inV()).
        until(__.not(out())).
      path().by('name').by(label)
/*
==>[Amy,location,Canterbury,part_of,Kent,part_of,England]
==>[Bill,location,Austin,part_of,Travis,part_of,Texas,part_of,USA]
==>[Bill,knows,Amy,location,Canterbury,part_of,Kent,part_of,England]
==>[Bill,knows,Sam,location,Brighton,part_of,East Sussex,part_of,England]
==>[Bill,knows,Eric,location,Austin,part_of,Travis,part_of,Texas,part_of,USA]
==>[Bill,knows,Eric,knows,Pam,location,New York City,part_of,New York,part_of,USA]
==>[Bill,knows,Frank,location,Miami,part_of,Dade,part_of,Florida,part_of,USA]
==>[Sam,location,Brighton,part_of,East Sussex,part_of,England]
==>[Eric,location,Austin,part_of,Travis,part_of,Texas,part_of,USA]
==>[Eric,knows,Pam,location,New York City,part_of,New York,part_of,USA]
==>[Frank,location,Miami,part_of,Dade,part_of,Florida,part_of,USA]
==>[Janet,location,Brighton,part_of,East Sussex,part_of,England]
==>[Marie,location,Canterbury,part_of,Kent,part_of,England]
==>[Mark,location,Miami,part_of,Dade,part_of,Florida,part_of,USA]
==>[Pam,location,New York City,part_of,New York,part_of,USA]
==>[Peter,location,Brighton,part_of,East Sussex,part_of,England]
*/                                                                 

// Bill's friends in the USA
g.V().has('name','Bill').
      out('knows').as('friend').
      repeat(out('location')).
        until(repeat(out('part_of')).
          emit(has('name','USA'))).
      path().from('friend').by('name') 
//[Frank,Miami]
//[Eric,Austin] 

//Bill's friends in England
g.V().has('name','Bill').
      out('knows').as('friend').
      repeat(out('location')).
        until(repeat(out('part_of')).
          emit(has('name','England'))).
      path().from('friend').by('name')  
//[Sam,Brighton]
//[Amy,Canterbury] 

// We could get the same result using a where step but only because
// we have a fixed number of out steps before  the where.
g.V().has('name','Bill').
      out('knows').as('friend').
      out('location').
         where(repeat(out('part_of')).
           emit(has('name','England'))).
         path().from('friend').by('name')
//[Sam,Brighton]
//[Amy,Canterbury]  


// This will not get us any results
g.V().has('name','Bill').
      out('knows').as('friend').
      repeat(out('location').
         where(repeat(out('part_of')).
           emit(has('name','England')))).
         path().from('friend').by('name') 


// Similar but without a nested repeat.
// Notice the difference. We get the whole path back.
g.V().has('name','Bill').
      out('knows').as('friend').
      out('location').
         repeat(out('part_of')).
           emit(has('name','England')).
         path().from('friend').by('name')
//[Amy,Canterbury,Kent,England]         
//[Sam,Brighton,East Sussex,England]  


