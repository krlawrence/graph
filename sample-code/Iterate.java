// Iterate.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create a TinkerGraph instance
//   2. Load the air routes graph
//   3. Experiment with maps and iterators

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
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class Iterate 
{
  public static void main(String[] args) 
  {
    // Create a new TinkerGraph and try to load the air-routes graph
    TinkerGraph tg = TinkerGraph.open() ;
    
    System.out.println("\nOpening air-routes.graphml...\n");

    try
    {
      tg.io(IoCore.graphml()).readGraph("air-routes.graphml");
    }
    catch( IOException e )
    {
      System.out.println("Air routes GraphML file not found");
      System.exit(1);
    }
    GraphTraversalSource g = tg.traversal();
    
    // Get an iterator of 30 value maps sampled at random.
    Iterator<Map<Object,Object>> res = 
         g.V().hasLabel("airport").sample(30).valueMap(true);


    // Build an iterator of 30 value maps sampled at random.
    // Note the use of "__." before the call to id().
    Iterator<Map<String,Object>> res2 = 
          g.V().hasLabel("airport").sample(30).
                project("id","iata","city").
                by(__.id()).by("code").by("city");

    
    // For each value map display a few fields.
    
    Map<Object,Object> vmap;
    
    System.out.println("\n*** Output from valueMap() ***\n\n");
    System.out.format("%4s %5s  %5s","ID","IATA","CITY\n");

    while(res.hasNext())
    {
      vmap = res.next();
      System.out.format( "%4s %5s  %-20s\n",
                         vmap.get(T.id),
                         ((List)(vmap.get("code"))).get(0), 
                         ((List)(vmap.get("city"))).get(0));
    }
    

    // Process the map built using project().
    // Note that this time the values are not in lists.
    
    Map<String,Object> vmap2;
    
    System.out.println("\n*** Output from project() ***\n\n");
    System.out.format("%4s %5s  %5s","ID","IATA","CITY\n");
    while(res2.hasNext())
    {
      vmap2 = res2.next();
      //System.out.println("here");
      System.out.format( "%4s %5s  %-20s\n",
                         vmap2.get("id"),
                         vmap2.get("iata"), 
                         vmap2.get("city"));
    }
  }
}
