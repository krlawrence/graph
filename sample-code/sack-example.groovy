[]; // Display up to 20 routes, with one stop, from Austin to London Heathrow.
[]; // The output for each route will include the codes for each airport and the
[]; // total distance. The results will be sorted by shortest overall distance.

println "\n\nShortest routes from AUS to LHR with one stop";[]
println "---------------------------------------------";[]

routes =  g.withSack(0).V().
          has('code','AUS').
          outE().sack(sum).by('dist').
          inV().outE().sack(sum).by('dist').
          inV().has('code','LHR').sack().
          order().by(incr).limit(20).
          path().by('code').by('dist').by('code').by('dist').by('code').by().toList();[]
          
routes.each(){println "${it[0]} --> ${it[2]} --> ${it[4]} ${it[5]} miles"}[];
