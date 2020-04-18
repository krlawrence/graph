// Builds a small ordered Binary (BST) Tree

graph=TinkerGraph.open()
g=graph.traversal()

g.addV('root').property('data',9).as('root').
  addV('node').property('data',5).as('b').
  addV('node').property('data',2).as('c').
  addV('node').property('data',11).as('d').
  addV('node').property('data',15).as('e').
  addV('node').property('data',10).as('f').
  addV('node').property('data',1).as('g').
  addV('node').property('data',8).as('h').
  addV('node').property('data',22).as('i').
  addV('node').property('data',16).as('j').
  addV('node').property('data',7).as('k').
  addV('node').property('data',51).as('l').  
  addV('node').property('data',13).as('m'). 
  addV('node').property('data',4).as('n'). 
  addE('left').from('root').to('b').
  addE('left').from('b').to('c').
  addE('right').from('root').to('d').
  addE('right').from('d').to('e').
  addE('right').from('e').to('i').
  addE('left').from('i').to('j').
  addE('left').from('d').to('f').
  addE('right').from('b').to('h').
  addE('left').from('h').to('k').
  addE('right').from('i').to('l').
  addE('left').from('e').to('m').
  addE('right').from('c').to('n').
  addE('left').from('c').to('g').iterate()

// Find the largest value in the graph  (using max would be cheating!)  
g.V().hasLabel('root').repeat(out('right')).until(outE('right').count().is(0)).values('data')

// Find the smallest value in the tree (using min would be cheating!)
g.V().hasLabel('root').repeat(out('left')).until(outE('left').count().is(0)).values('data')
