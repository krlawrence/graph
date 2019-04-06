// GraphSearch2.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Load the air-routes graph
//   3. Perform some searches against the graph
//   4. Handle error conditions where graph elements are not found

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
import org.apache.tinkerpop.gremlin.util.Gremlin;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GraphSearch2 
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
      System.out.println("GraphStats - GraphML file not found");
      return false;
    }
    g = tg.traversal();
    return true;
  }
  
  // Return the distance between two airports.
  // Input parameters are the airport IATA codes.
  // If no route exists between the specified airports return -1
  public Integer getDistance(String from, String to)
  {
    //The coalesce step avoids an exception when no result is found
    //Integer d = (Integer) 
    //  g.V().has("code",from).outE().as("edge").inV().has("code",to).
    //        select("edge").by("dist").fold().
    //        coalesce(__.unfold(),__.constant(-1)).next();

    // Using toList() is another way to check for no result
    List result = 
      g.V().has("code",from).outE().as("edge").inV().has("code",to).
            select("edge").by("dist").toList();

    Integer d = ((result.isEmpty()) ? -1 : (Integer)(result.get(0)));        

/*
    if (result.isEmpty())
    {
      System.out.println("No results were found");
    }
    else
    {
      System.out.println("The distance is " + result.get(0));
    }
*/
    return(d);
  }

  // Run some tests
  public static void main(String[] args) 
  {
    // If you want to check your Gremlin version, uncomment the next line
    //System.out.println("Gremlin version is: " + Gremlin.version());

    GraphSearch2 gs = new GraphSearch2();
    boolean loaded = gs.loadGraph("air-routes.graphml");

    // The data below contains a non existent route between XXX and YYY to test the
    // error handling when a query returns no data.
    if (loaded)
    {
      String[][] places = {{"AUS","LHR"},{"JFK","PHX"},{"SYD","LAX"},
                           {"PEK","HND"},{"HKG","MEL"},{"MIA","SFO"},
                           {"MNL","BKK"},{"XXX","YYY"},{"DOH","JNB"},
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
