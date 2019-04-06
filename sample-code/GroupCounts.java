// GroupCounts.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Creates an empty TinkerGraph instance
//   2. Loads the air-routes.graphml file
//   3. Runs a few queries that use the groupCount step
//      against the newly created graph.
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

public class GroupCounts
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

    // Calculate how long it took to load the graph
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
    // Count vertices and edges grouped by labels. 
    // This example shows how a HashMap can be passed to a query
    // using a side effect.
    final Map<String, Long> map = new HashMap<>();
    g.withSideEffect("m", map).V().groupCount("m").by(T.label).iterate();
    g.withSideEffect("m", map).E().groupCount("m").by(T.label).iterate();
    map.forEach((k,v)->System.out.format("%10s :%5d\n",k,v));

    // Count airports in the UK and Ireland by region also
    // using a side effect.
    System.out.println();
    final Map<String, Long> eumap = new HashMap<>();
    g.withSideEffect("m", eumap).
      V().has("code",P.within("UK","IE")).out().
          groupCount("m").by("region").iterate();

    eumap.forEach((k,v)->System.out.format("%10s :%3d\n",k,v));

    // Count airports in the US by region and sort the results
    // by descending counts. This time the map is populated 
    // using the query result rather than as a side effect.
    System.out.println();
    Map<Object, Long> usmap = new HashMap<>();
    usmap = g.V().has("code","US").out().
      groupCount().by("region").order(Scope.local).
      by(Column.values, Order.decr).next();  

    usmap.forEach((k,v)->System.out.format("%10s :%3d\n",k,v));

  }

  // ---------------------------------------
  // Try to load a graph and run a few tests
  // ---------------------------------------
  public static void main(String[] args) 
  {
    GroupCounts grp = new GroupCounts();

    if ( grp.loadGraph("air-routes.graphml"))
    {
      grp.runTests();
    }
  }
}
