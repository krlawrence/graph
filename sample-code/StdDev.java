// StdDev.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Creates an empty TinkerGraph instance
//   2. Loads the air-routes.graphml file
//   3. Runs a few experiments that use the math step.
//
// NOTE: This code requires a TinkerPop version of at least 3.3.1
//
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.apache.tinkerpop.gremlin.util.Gremlin;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class StdDev
{
  private TinkerGraph tg;
  private GraphTraversalSource g;

  // -------------------------------------------------------------
  // Try to create a new graph and load the specified GraphML file
  // -------------------------------------------------------------
  public boolean loadGraph(String name)
  {
    tg = TinkerGraph.open() ;
    
    System.out.println("Loading " + name);
    long t1 = System.currentTimeMillis();
    System.out.println(t1);

    try
    {
      tg.io(IoCore.graphml()).readGraph(name);
    }
    catch( IOException e )
    {
      System.out.println("ERROR - GraphML file not found or invalid.");
      return false;
    }

    long t2 = System.currentTimeMillis();
    System.out.println(t2 + "(" + (t2-t1) +")");
    System.out.println("Graph loaded\n");
    g = tg.traversal();
    return true;
  }

  // ---------------------
  // Run a few experiments
  // ---------------------
  public void runTests()
  {
    // Calculate the average number of runways per airport.
    Number mean=g.V().hasLabel("airport").values("runways").mean().next();

    // Calculate number of airports in the graph.
    Long count = g.V().hasLabel("airport").count().next();

   // Calculate the standard deviation from the mean for runways
    Double stddev = 
      g.withSideEffect("m",mean).
        withSideEffect("c",count).
        V().hasLabel("airport").values("runways").
        math("(_ - m)^2").sum().math("_ / c").math("sqrt(_)").next();  

    System.out.println("Number of airports : " + count);    
    System.out.println("Average number of runways : " + mean);    
    System.out.println("Standard deviation : " + stddev);    
  }

  // ---------------------------------------
  // Try to load a graph and run a few tests
  // ---------------------------------------
  public static void main(String[] args) 
  {
    StdDev sd = new StdDev();

    if ( sd.loadGraph("air-routes.graphml"))
    {
      sd.runTests();
    }
  }
}
