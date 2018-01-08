// GraphSearch3.java
//
// Simple example of using TinkerGraph with Java
// This code builds upon the examples shown in GraphSearch and GraphSearch2
// and adds the findLongestRoute method to the class.
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
import org.apache.tinkerpop.gremlin.util.Gremlin;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GraphSearch3 
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
    List result = 
      g.V().has("code",from).outE().as("edge").inV().has("code",to).
            select("edge").by("dist").toList();

    Integer d = ((result.isEmpty()) ? -1 : (Integer)(result.get(0)));        

    return(d);
  }

  // Find the distance of the longest route in the graph
  // Returns a map with keys of from,distance and to
  public Map findLongestRoute()
  {
    // Note how we need to prefix certain things that we would not have to
    // when using the Gremlin console with "__." and "Order."
    Map result = 
      g.V().hasLabel("airport").outE("route").
            order().by("dist",Order.decr).limit(1).
            project("from","distance","to").
            by(__.inV().values("code")).by("dist").by(__.outV().values("code")).next();

    return(result);
  }
  

  // Run some tests
  public static void main(String[] args) 
  {
    // If you want to check your Gremlin version, uncomment the next line
    //System.out.println("Gremlin version is: " + Gremlin.version());

    GraphSearch3 gs = new GraphSearch3();
    boolean loaded = gs.loadGraph("air-routes.graphml");

    if (loaded)
    {
      Map longest = gs.findLongestRoute() ;
      String s = "The longest route in the graph is between " + longest.get("from");
      s += " and " + longest.get("to") + " covering a distance of ";
      s += longest.get("distance") + " miles." ;

      System.out.println(s);

    }
  }      
}
