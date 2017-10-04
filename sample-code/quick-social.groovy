graph=TinkerGraph.open()
g=graph.traversal()

g.addV("A").as("a").
  addV("B").as("b").
  addV("C").as("c").
  addV("D").as("d").
  addV("E").as("e").
  addV("F").as("f").
  addE("knows").from("a").to("b").
  addE("knows").from("a").to("c").
  addE("knows").from("a").to("d").
  addE("knows").from("c").to("d").
  addE("knows").from("c").to("e").
  addE("knows").from("d").to("f")
