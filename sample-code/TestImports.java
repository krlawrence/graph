// TestImports.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create a TinkerGraph instance
//   2. Load the air routes graph
//   3. Demonstrate some queries that show how to do in Java what is a little
//      simpler using the Gremlin console. Mainly shows which Enums, classes and
//      imports are needed.

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.apache.tinkerpop.gremlin.structure.T;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class TestImports
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
  
  // Help make the output easier to read

  private void showHeader(String str)
  {
    String tmp = "***** " +str + " *****" ;
    String tmp2 = "" ;

    for (int i=0; i<tmp.length();i++) {tmp2 += "*";}
    System.out.println(tmp2);
    System.out.println(tmp);
    System.out.println(tmp2+"\n");
  }

  // Demonstrate how to use common Gremlin constructs from Java
  public void runTests()
  {
    // ---------------------------------------------------
    // Examples of using local scope and order with a list
    // ---------------------------------------------------
    List results;

    showHeader("Scope local order");

    results = g.V().has("region","US-TX").values("code").fold().
                    order(Scope.local).toList();

    System.out.println(results + "\n");

    showHeader("Scope.local Order.decr");
    results = g.V().has("region","US-TX").values("code").fold().
                    order(Scope.local).by(Order.decr).toList();

    System.out.println(results + "\n");


    // -------------------------
    // More examples of ordering
    // -------------------------
    
    showHeader("Order.shuffle");

    for (int i=0; i<3; i++)
    {
      results = g.V().has("region","US-TX").values("code").
                      order().by(Order.shuffle).toList();

      System.out.println(results + "\n");
    }

    showHeader("Order.decr");

    results = g.V().has("region","US-TX").values("code").
                    order().by(Order.decr).toList();

    System.out.println(results + "\n");

    showHeader("Order.incr runways at Texas airports");

    results = g.V().has("region","US-TX").values("runways").
                    order().by(Order.incr).toList();

    System.out.println(results+"\n");

    // ----------------------------
    // Examples of using predicates
    // ----------------------------

    
    showHeader("Predicates Texas cities neq('Dallas')");

    results = g.V().has("region","US-TX").has("city", P.neq("Dallas")).
                    values("city").dedup().fold().toList();

    System.out.println(results + "\n");

    showHeader("Predicates Texas cities without('Dallas','Houston','Austin')");
    
    results = g.V().has("region","US-TX").
                    has("city",P.without("Dallas","Houston","Austin")).
                    values("city").dedup().fold().toList();

    System.out.println(results + "\n");


    // --------------------------------------------------
    // Selecting keys and values from maps and properties
    // --------------------------------------------------

    showHeader("Column.keys Austin vertex");
    
    results = g.V().has("airport","code","AUS").valueMap().select(Column.keys).toList();
    System.out.println(results + "\n");

    showHeader("Column.values Austin vertex");

    results = g.V().has("airport","code","AUS").valueMap().select(Column.values).toList();
    System.out.println(results + "\n");

    List<?> props =  
        g.V().has("code","SFO").properties().order().by(T.key).toList();

    showHeader("Order properties by(T.key)");
    props.forEach((p) -> System.out.println(p));
    System.out.println();

    // --------------------
    // Working with labels.
    // --------------------
    
    showHeader("groupCount().by(T.label)");

    results = g.V().groupCount().by(T.label).toList();
    System.out.println(results + "\n");
  }

  public static void main(String[] args) 
  {
    // If you want to check your Gremlin version, uncomment the next line
    //System.out.println("Gremlin version is: " + Gremlin.version());

    TestImports tim = new TestImports();
    boolean loaded = tim.loadGraph("air-routes.graphml");

    if (loaded)
    {
      tim.runTests();
    }
  }      
}
