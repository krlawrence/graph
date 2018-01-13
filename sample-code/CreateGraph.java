// CreateGraph.java
//
// Simple example of using TinkerGraph with Java
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
import java.util.HashMap;
import java.util.Set;

public class CreateGraph 
{
  public static void main(String[] args) 
  {
    // If you want to check your Gremlin version, uncomment the next line
    //System.out.println("Gremlin version is: " + Gremlin.version());

    // Create a new (empty) TinkerGrap
    TinkerGraph tg = TinkerGraph.open() ;
    
    // Create a Traversal source object
    GraphTraversalSource g = tg.traversal();
                   
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
      addE("route").from("lax").to("dfw").iterate();
  
    //System.out.println(g);
    //System.out.println(g.V().valueMap(true).toList());

    // Simple example of how to work with the results we get back from a query

    List<Map<Object,Object>> vm = new ArrayList<Map<Object,Object>>() ;
    
    vm = g.V().valueMap(true).toList();

    // Dislpay the code property as well as the label and id.
    for( Map m : vm)
    {
      System.out.println(((List)(m.get("code"))).get(0) + " " + m.get(T.id) + " " + m.get(T.label));
    }

    // Display the routes in the graph we just created

    List<Path> paths = new ArrayList<Path>();

    paths = g.V().out().path().by("code").toList();

    for (Path p : paths)
    {
      System.out.println(p.toString());
    }
  }      
}
