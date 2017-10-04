// Builds a small ordered Binary (BST) Tree
graph=TinkerGraph.open()
g=graph.traversal()
g.addV(label,'root','data',9).as('root').   
  addV(label,'node','data',5).as('b').   
  addV(label,'node','data',2).as('c').   
  addV(label,'node','data',11).as('d').   
  addV(label,'node','data',15).as('e').   
  addV(label,'node','data',10).as('f').   
  addV(label,'node','data',1).as('g').   
  addV(label,'node','data',8).as('h').   
  addV(label,'node','data',22).as('i').   
  addV(label,'node','data',16).as('j').   
  addE('left').from('root').to('b').
  addE('left').from('b').to('c').
  addE('right').from('root').to('d').
  addE('right').from('d').to('e').
  addE('right').from('e').to('i').
  addE('left').from('i').to('j').
  addE('left').from('d').to('f').
  addE('right').from('b').to('h').
  addE('left').from('c').to('g')


  // Find the largest value in the graph  (using max would be cheating!)  
g.V().hasLabel('root').repeat(out('right')).until(outE('right').count().is(0)).values('data')



// Find the smallest value in the tree (using min would be cheating!)
g.V().hasLabel('root').repeat(out('left')).until(outE('left').count().is(0)).values('data')

           
