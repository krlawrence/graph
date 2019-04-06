// CreateGraph.groovy
//
// Simple example of using TinkerGraph with Groovy (outside the Gremlin console)
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Create some nodes and vertices
//   3. Run a few queries against the newly created graph

// I have highlighted the places where the Gremlin is slightly different from the 
// Gremlin we can use in the Gremlin Console.

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.util.Gremlin;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

// Create a new (empty) TinkerGrap
TinkerGraph tg = TinkerGraph.open()

// Create a Traversal source object
GraphTraversalSource g = tg.traversal()
               
// Add some nodes and vertices - Note the use of "iterate".
g.addV("airport").property("code","AUS").as("aus").
  addV("airport").property("code","DFW").as("dfw").
  addV("airport").property("code","LAX").as("lax").
  addV("airport").property("code","JFK").as("jfk").
  addV("airport").property("code","ATL").as("atl").
  addE("route").from("aus").to("dfw").
  addE("route").from("aus").to("atl").
  addE("route").from("atl").to("dfw").
  addE("route").from("atl").to("jfk").
  addE("route").from("dfw").to("jfk").
  addE("route").from("dfw").to("lax").
  addE("route").from("lax").to("jfk").
  addE("route").from("lax").to("aus").
  addE("route").from("lax").to("dfw").iterate()

// Display the vertices created, note we have to use the "T." prefix
// for label and id as they are not stored as regular strings.

vm = g.V().valueMap(true).toList()
vm.each {println("${it[T.id]}  ${it.code[0]} ${it[T.label]}")}

/* Just for fun!
for (v in vm)
{
  v.keySet().each {print "${v[it]} "}
  println()
}
*/

edges = g.E().count().next()
verts = g.V().count().next()

println "The graph has $verts vertices"
println "The graph has $edges edges"


