// GraphStats.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create a TinkerGraph instance
//   2. Load the air routes graph
//   3. Display some statistics about the graph

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

public class GraphStats 
{
  public static void main(String[] args) 
  {
    // Create a new TinkerGraph and try to load the air-routes graph
    TinkerGraph tg = TinkerGraph.open() ;
    
    System.out.println("\nOpening air-routes.graphml");

    try
    {
      tg.io(IoCore.graphml()).readGraph("air-routes.graphml");
    }
    catch( IOException e )
    {
      System.out.println("GraphStats - Air routes GraphML file not found");
      System.exit(1);
    }
    GraphTraversalSource g = tg.traversal();
                   
    // Run some queries and display a few statistics

    System.out.println("Collecting stats");
    
    System.out.println("\nDistribution of vertices and edges");
    System.out.println("----------------------------------");

    // Display some basic demographics
    // Note that label has to be prefixed by "T."
    Map verts = g.V().groupCount().by(T.label).next();
    Map edges = g.E().groupCount().by(T.label).next();
    System.out.println("Vertices : " + verts);
    System.out.println("Edges    : " + edges);

    // Find the airport with the most overall routes.
    // Note that we have to use the "__." prefix for some steps and that "decr"
    // has to be prefixed by "Order".
    Map <String,?> most = g.V().hasLabel("airport").
                            order().by(__.bothE("route").count(),Order.decr).limit(1).
                            project("ap","num","city").by("code").by(__.bothE("route").count()).
                            by("city").next();
    
    String s = "" + most.get("ap") + "/" + most.get("city");
    Long r = (Long) most.get("num");
    System.out.println("\nThe airport with the most routes is " + s + " with " + r + " routes") ;



    // Find the airports with the most routes overall (incoming + outgoing)

    System.out.println("\nTop 20 airports by total routes");
    System.out.println("===============================");
    
    List<Map<String,Object>> top = 
      g.V().hasLabel("airport").
            order().by(__.both("route").count(),Order.decr).limit(20).
            project("ap","num","city").by("code").by(__.both("route").count()).by("city").
            toList();
   
    // Either of these can be used
    //for(Map a: top) { System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"), a.get("num"));}
    top.forEach((a) -> System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"),a.get("num")));
    


    // Find the airports with the most outgoing routes

    System.out.println("\nTop 20 airports by outgoing routes");
    System.out.println("==================================");
    
    List<Map<String,Object>> topout = 
      g.V().hasLabel("airport").
            order().by(__.out("route").count(),Order.decr).limit(20).
            project("ap","num","city").by("code").by(__.out("route").count()).by("city").
            toList();
   
    topout.forEach((a) -> System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"),a.get("num")));

    // Find the airports with the most incoming routes
    
    System.out.println("\nTop 20 airports by incoming routes");
    System.out.println("==================================");
    
    List<Map<String,Object>> topin = 
      g.V().hasLabel("airport").
            order().by(__.in("route").count(),Order.decr).limit(20).
            project("ap","num","city").by("code").by(__.in("route").count()).by("city").
            toList();
   
    topin.forEach((a) -> System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"),a.get("num")));


    // Find the longest route in the graph

    Map <String,?> longroute = 
      g.E().hasLabel("route").
            order().by("dist",Order.decr).limit(1).
            project("from","to","num").
            by(__.inV().values("city")).by(__.outV().values("city")).by("dist").next();

    System.out.println("\nThe longest route in the graph is " + longroute.get("num") +   
                       " miles between " + longroute.get("from") + " and " + longroute.get("to"));

    // Find the longest runway in the graph

    Map <String,?> longest = 
      g.V().hasLabel("airport").order().by("longest",Order.decr).limit(1).
            project("ap","num","city").by("code").by("longest").by("city").next();  

    System.out.println("The longest runway in the graph is " + longest.get("num") + " feet at " +
                        longest.get("city") + "/" + longest.get("ap"));        

    
    // Find the continent with the most airports
    
    Map <String,?> cmost; 
    
    cmost = g.V().hasLabel("continent").
                  order().by(__.out("contains").count(),Order.decr).limit(1).
                  project("num","cont").
                    by(__.out("contains").count()).
                    by("desc").next();

    System.out.println("The continent with the most airports: " + 
                       cmost.get("cont") + " (" + 
                       cmost.get("num") +")");               


    // Find the country with the most airports

    cmost = g.V().hasLabel("country").
                  order().by(__.out("contains").count(),Order.decr).limit(1).
                  project("num","country").
                    by(__.out("contains").count()).
                    by("desc").next();

    System.out.println("The country with the most airports: " + 
                       cmost.get("country") + " (" + 
                       cmost.get("num") +")");               
  }
}
