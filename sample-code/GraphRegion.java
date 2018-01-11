// GraphRegion.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Load the air routes graph
//   3. Run some tests that show the where...by and multiple V() constructs

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

public class GraphRegion 
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

  // Find all airports in the region of the specified airport
  //
  // Note that when used from Java we have to prefix eq with a "P."
  // Also select has to be prefixed by "__."
  public void findByRegion(String iata)
  {
    System.out.println("\nRegion code lookup for " + iata );

    List<List<Object>> list =  
    g.V().has("code",iata).values("region").as("r").
      V().hasLabel("airport").as("a").values("region").
      where(P.eq("r")).by().
      local(__.select("a").values("city","code","region").fold()).toList();               
    
    for(List t : list)
    {
      System.out.println(t);
    }
  }

  public static void main(String[] args) 
  {
    // If you want to check your Gremlin version, uncomment the next line
    //System.out.println("Gremlin version is: " + Gremlin.version());

    GraphRegion gr = new GraphRegion() ;

    if (gr.loadGraph("air-routes.graphml"))
    {
      gr.findByRegion("NCE");  // Nice
      gr.findByRegion("DEN");  // Denver
      gr.findByRegion("GVA");  // Geneva
      gr.findByRegion("NGS");  // Nagasaki
      gr.findByRegion("CAN");  // Guangzhou
    }
  }      
}
