// GraphSearch.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Load the air-routes graph
//   3. Perform some searches against the graph

// I have highlighted any places where the Gremlin is slightly different from the 
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

public class GraphSearch 
{
  private TinkerGraph tg;
  private GraphTraversalSource g;

  // Try to create a new graph and load the specified GraphML file
  public boolean loadGraph(String name)
  {
    tg = TinkerGraph.open() ;
    
    try
    {
      tg.io(IoCore.graphml()).readGraph(name);
    }
    catch( IOException e )
    {
      System.out.println("GraphStats - Air routes GraphML file not found");
      return false;
    }
    g = tg.traversal();
    return true;
  }

  // Return the distance between two airports.
  // Input parameters are the airport IATA codes.
  public Integer getDistance(String from, String to)
  {
    Integer d = (Integer) 
      g.V().has("code",from).outE().as("edge").inV().has("code",to).
            select("edge").by("dist").next();

    return(d);
  }

  // Run some tests
  public static void main(String[] args) 
  {
    GraphSearch gs = new GraphSearch();
    boolean loaded = gs.loadGraph("air-routes.graphml");

    if (loaded)
    {
      String[][] places = {{"AUS","LHR"},{"JFK","PHX"},{"SYD","LAX"},
                           {"PEK","HND"},{"HKG","MEL"},{"MIA","SFO"},
                           {"MNL","BKK"},{"DXB","DFW"},{"DOH","JNB"},
                           {"NRT","FRA"},{"AMS","GVA"},{"CDG","SIN"}};

      System.out.println("\n\nFrom    To   Distance");
      System.out.println("=====================");

      Integer dist;
      
      for (String[] p : places)
      {
        dist = gs.getDistance(p[0],p[1]);
        System.out.format("%4s   %4s   %5d\n",p[0],p[1],dist) ;
      }  
    }
  }      
}
