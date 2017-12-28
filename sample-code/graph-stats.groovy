;[] // Small Groovy script that can be used from within the Gremlin console after
;[] // the air-routes graph has been loaded that will provide statistics about the 
;[] // graph. The use of ;[] is just to stop the console printing additional return values.

println "\n\nA few statistics about the air-routes graph";[]
println "===========================================";[]

println "\nDistribution of vertices and edges";[]
verts = g.V().groupCount().by(label).next();[]
edges = g.E().groupCount().by(label).next();[]
println "Vertices : ${verts}";[]
println "Edges    : ${edges}";[]

most = g.V().hasLabel('airport').order().by(out('route').count(),decr).limit(1).
             project('ap','num','city').by('code').by(out('route').count()).by('city').next();[]

println "\nThe airport with the most routes (incoming and outgoing) is ${most['ap']}/${most['city']} with ${most['num']}";[]

longest = g.V().hasLabel('airport').order().by('longest',decr).limit(1).
                project('ap','num','city').by('code').by('longest').by('city').next();[]

println "\nThe longest runway in the graph is ${longest['num']} feet at ${longest['ap']}/${longest['city']}";[]

;[] // A different way of doing the above using two queries. Just to show a different approach
highest = g.V().hasLabel('airport').values('elev').max().next();[]
aptcity = g.V().has('elev',highest).valueMap('code','city').next();[]
println "\nThe highest airport in the graph is ${aptcity['code'][0]}/${aptcity['city'][0]} which is at ${highest} feet above sea level";[]
