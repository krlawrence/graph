;[] // graph-stats.groovy
;[] //
;[] // This script is intended to be run within the Gremlin Console.
;[] //
;[] // Small Groovy script that can be used from within the Gremlin console after
;[] // the air-routes graph has been loaded that will provide statistics about the 
;[] // graph. This script demonstrates different ways of doing similar things
;[] //
;[] // From the console use :load graph-stats.groovy to run it.
;[] //
;[] // The ";[]" notation is used to prevent unwanted output from the Gremlin
;[] // Console. 

println "\n\nA few statistics about the air-routes graph";[]
println "===========================================";[]

println "\nDistribution of vertices and edges";[]
println "----------------------------------";[]
verts = g.V().groupCount().by(label).next();[]
edges = g.E().groupCount().by(label).next();[]
println "Vertices : ${verts}";[]
println "Edges    : ${edges}";[]

most = g.V().hasLabel('airport').order().by(bothE('route').count(),decr).limit(1).
             project('ap','num','city').by('code').by(bothE('route').count()).by('city').next();[]

println "\nThe airport with the most routes (incoming and outgoing) is ${most['ap']}/${most['city']} with ${most['num']}";[]


println "\nTop 20 airports ordered by overall routes";[]
println "-----------------------------------------";[]
most = g.V().hasLabel('airport').order().by(both('route').count(),decr).limit(20).
                     project('ap','num','city').by('code').by(both('route').count()).by('city').toList();[]

most.each {printf("%4s  %15s %5d\n", it.ap, it.city,  it.num)};[]


println "\nTop 20 airports ordered by number of outgoing routes";[]
println "----------------------------------------------------";[]
most = g.V().hasLabel('airport').order().by(out('route').count(),decr).limit(20).
                     project('ap','num','city').by('code').by(out('route').count()).by('city').toList();[]

most.each {printf("%4s  %15s %5d\n", it.ap, it.city, it.num)};[]

println "\nTop 20 airports ordered by number of incoming routes";[]
println "----------------------------------------------------";[]
most = g.V().hasLabel('airport').order().by(__.in('route').count(),decr).limit(20).
                     project('ap','num','city').by('code').by(__.in('route').count()).by('city').toList();[]

most.each {printf("%4s  %15s %5d\n", it.ap, it.city,  it.num)};[]


longroute = g.E().hasLabel('route').order().by('dist',decr).limit(1).
                  project('from','to','num').
                  by(inV().values('code')).by(outV().values('code')).by('dist').next();[]

println "\nThe longest route in the graph is ${longroute['num']} miles between ${longroute['from']} and ${longroute['to']}";[]

shortroute = g.E().hasLabel('route').order().by('dist',incr).limit(1).
                   project('from','to','num').
                   by(inV().values('code')).by(outV().values('code')).by('dist').next();[]

println "The shortest route in the graph is ${shortroute['num']} miles between ${shortroute['from']} and ${shortroute['to']}";[]


meanroute = g.E().hasLabel('route').values('dist').mean().next().round(4);[]
println "The average route distance is ${meanroute} miles";[]

println "\nTop 20 routes in the graph by distance";[]
println "--------------------------------------";[]

routes = g.E().hasLabel('route').order().by('dist',decr).limit(40).
               project('a','b','c').
               by(inV().values('code')).by('dist').by(outV().values('code')).
               filter(select('a','c')).where('a',lt('c')).toList();[]
               
routes.each {printf("%4s  %4d %4s\n", it.a, it.b,  it.c)};[]


longest = g.V().hasLabel('airport').order().by('longest',decr).limit(1).
                project('ap','num','city').by('code').by('longest').by('city').next();[]

println "\nThe longest runway in the graph is ${longest['num']} feet at ${longest['ap']}/${longest['city']}";[]

shortest = g.V().hasLabel('airport').order().by('longest',incr).limit(1).
                 project('ap','num','city').by('code').by('longest').by('city').next();[]

println "The shortest runway in the graph is ${shortest['num']} feet at ${shortest['ap']}/${shortest['city']}";[]

;[] // A different way of doing the above type of query using two queries. 
;[] // Just to show a different approach
highest = g.V().hasLabel('airport').values('elev').max().next();[]
aptcity = g.V().has('elev',highest).valueMap('code','city').next();[]
println "\nThe highest airport in the graph is ${aptcity['code'][0]}/${aptcity['city'][0]} which is at ${highest} feet above sea level";[]

lowest = g.V().hasLabel('airport').order().by('elev',incr).limit(1).
                project('ap','num','city').by('code').by('elev').by('city').next();[]

ab = "above";[]
if (lowest['num'] < 0) ab = "below";[] 
println "The lowest airport in the graph is ${lowest['ap']}/${lowest['city']} which is at ${lowest['num']} feet ${ab} sea level";[]


;[] // Here is an example of using the group step to perform similar tasks
continents = g.V().hasLabel('continent').group().by('desc').by(out().count()).
                                         order(local).by(values,decr).next();[]


println("\nNumber of airports in each continent");[]
println("------------------------------------");[]
continents.each {printf("%15s  %4d\n",it.key,it.value)};[]
println "";[]


countries = g.V().hasLabel('country').order().by(outE().count(),decr).limit(20).
                  project('airports','name').by(outE().count()).by('desc').toList();[]

println("\nCountries with the most airports");[]
println("--------------------------------");[]
countries.each {printf("%20s  %4d\n",it.name,it.airports)};[]
println "";[]
