// Iterate.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create a TinkerGraph instance
//   2. Load the air routes graph
//   3. Experiment with maps and iterators
//   4. Do some privitive analysis of how random the sample step is.

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
      System.out.println("Air routes GraphML file not found or invalid");
      System.exit(1);
    }
    GraphTraversalSource g = tg.traversal();
    
    // Default sample size can be overriden by command line parameter.
    int sample_size = 30 ;

    try 
    {
      if (args.length > 0) sample_size = Integer.parseInt(args[0]);
    }
    catch (Exception e)
    {
      System.out.println("Unrecognized value:(" + args[0] + "). Using defaults");
    }

    // Get an iterator of 30 value maps sampled at random.
    Iterator<Map<Object,Object>> res = 
         g.V().hasLabel("airport").sample(sample_size).valueMap(true);


    // Build an iterator of 30 projected maps from a random sample of airports.
    // Note the use of "__." before the call to id().
    
    Iterator<Map<String,Object>> res2 = 
          g.V().hasLabel("airport").sample(sample_size).
                project("id","iata","city").
                by(__.id()).by("code").by("city");
    

    // To experiment with the coin step you can use the code below.
    /*
    Iterator<Map<String,Object>> res2 = 
          g.V().hasLabel("airport").coin(0.5).limit(sample_size).
                project("id","iata","city").
                by(__.id()).by("code").by("city");
    */

    // For each value map display a few fields.
    // Note how for property values we have to process them as lists.
    
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
    // Just for fun let's also see howthe sample was distributed.
    
    Map<String,Object> vmap2;
    int low = 0, medium = 0, high = 0;

    System.out.println("\n*** Output from project() ***\n\n");
    System.out.format("%4s %5s  %5s","ID","IATA","CITY\n");
    
    Long num_airports = g.V().hasLabel("airport").count().next();
    Long low_bar = num_airports/3;
    Long med_bar = low_bar * 2;

    while(res2.hasNext())
    {

      vmap2 = res2.next();
      int id = Integer.parseInt((String)(vmap2.get("id")));

      System.out.format( "%4d %5s  %-20s\n",
                         id,
                         vmap2.get("iata"), 
                         vmap2.get("city"));

      // Track sample distribution                   
      if (id < low_bar)
      {
        low += 1;
      }
      else if (id < med_bar)
      {
        medium += 1;
      }
      else
      {
        high += 1;
      }
    }
    
    
    System.out.println("\nGraph contains " + num_airports + " airports.");
    System.out.println("Low bar= " + low_bar + " med bar=" + med_bar);
    System.out.println("\nLow=" + low + "  Medium=" + medium + "  High=" + high);
  }
}
