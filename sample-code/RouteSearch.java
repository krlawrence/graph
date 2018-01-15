
// RouteSearch.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Load the air-routes graph
//   3. Prompt the user for input about routes to look for
//   4. Look for any routes that match the criteria

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
import java.io.Console;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class RouteSearch
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
  

  // Look for up to 'max' routes between the specified airports with no more 
  // than the specified number of stops.
  // 
  // Will return one of:
  //   null          - one or both airport codes were invalid
  //   empty list    - No routes were found
  //   list of paths - routes that were found

  // We have to prefix out() with "__." when calling it from Java and
  // we have to prefix within() with "P."
  public List<Path> findRoutes(String from, String to, int max, int stops)
  {
    // Check that both airports exist so we don't waste time looking for
    // routes to non existent airports.
    Long check = g.V().has("code",P.within(from,to)).count().next();
    if (check !=2) return null;

    // Look for routes matching the specified parameters
    List<Path> result = 
           g.V().has("code",from).
           repeat(__.out().simplePath()).times(stops+1).emit().has("code",to).
           path().by("code").limit(max).toList();

    return result;
  }

  // Run some tests
  public static void main(String[] args) 
  {
    // If you want to check your Gremlin version, uncomment the next line
    //System.out.println("Gremlin version is: " + Gremlin.version());

    RouteSearch rs = new RouteSearch();
    boolean loaded = rs.loadGraph("air-routes.graphml");

    if (loaded)
    {
      boolean done = false;
      while( !done )
      {
        System.out.println("\nEnter from and to airport codes, eg DFW");
        Console console = System.console();
        System.out.print("From : ");
        String from = console.readLine().toUpperCase();
        System.out.print("To   : ");
        String to = console.readLine().toUpperCase();
        System.out.print("Maximum number of routes to look for : ");
        int max = Integer.parseInt(console.readLine());
        System.out.print("Maximum number of stops : ");
        int stops = Integer.parseInt(console.readLine());

        List<Path> list;
        list = rs.findRoutes(from,to,max,stops) ;
        if (list == null)
        {
          System.out.println("\nPlease enter valid airport codes");
        }
        else if (list.isEmpty())
        {
          System.out.println("\nSorry, no routes were found, try more stops");
        }
        else
        {
          list.forEach((v) -> System.out.println(v));
        }
        
        System.out.print("\nAnother search (Y/N)? : ");
        String again = console.readLine().trim().toUpperCase();
        
        if (again.equals("N"))
        {
          done = true;
        }
      }
    }
  }      
}
