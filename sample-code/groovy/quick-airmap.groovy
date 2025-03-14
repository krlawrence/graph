graph = TinkerGraph.open()
g = traversal().with(graph)
g.addV('airport').property('code', 'AUS').as('aus').
        addV('airport').property('code', 'DFW').as('dfw').
        addV('airport').property('code', 'LAX').as('lax').
        addV('airport').property('code', 'JFK').as('jfk').
        addV('airport').property('code', 'ATL').as('atl').
        addE('route').from('aus').to('dfw').
        addE('route').from('aus').to('atl').
        addE('route').from('atl').to('dfw').
        addE('route').from('atl').to('jfk').
        addE('route').from('dfw').to('jfk').
        addE('route').from('dfw').to('lax').
        addE('route').from('lax').to('jfk').
        addE('route').from('lax').to('aus').
        addE('route').from('lax').to('dfw')

g.V().valueMap(true)
g.V().outE().inV().path().by('code').by()
