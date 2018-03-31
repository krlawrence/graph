// ListAirports.java
//
// Another simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Creates an empty TinkerGraph instance
//   2. Loads the air-routes.graphml file
//   3. Displays information about selected airports.
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
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.configuration.BaseConfiguration;

public class ListAirports
{
  private TinkerGraph tg;
  private GraphTraversalSource g;

  // -------------------------------------------------------------
  // Try to create a new graph and load the specified GraphML file
  // -------------------------------------------------------------
  public boolean loadGraph(String name)
  {
    // Make sure index ID values are set as LONG values.
    // If this is not done, when we try to sort results by vertex
    // ID later it will not sort the way you would expect it to.

    BaseConfiguration conf = new BaseConfiguration();
    conf.setProperty("gremlin.tinkergraph.vertexIdManager","LONG");
    conf.setProperty("gremlin.tinkergraph.edgeIdManager","LONG");
    conf.setProperty("gremlin.tinkergraph.vertexPropertyIdManager","LONG");
    
    // Create a new instance that uses this configuration.
    tg = TinkerGraph.open(conf) ;
    
    // Load the graph and time how long it takes.
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

  // ----------------------------------------------
  // Display some information about the selected 
  // number of airports. A value of -1 means select
  // all airports.
  // ----------------------------------------------
  public void listAirports(int max)
  {
    if (max < -1 ) return;

    // Try to find the requested number of airports.
    // Note the use of the "__." and "Order" prefixes.
    List<Vertex> vlist = 
      g.V().hasLabel("airport").
            order().by(__.id(),Order.incr).
            limit(max).
            toList();

    Long   id;   // Vertex ID
    String iata; // 3 character IATA code.
    String icao; // 4 character ICAO code.
    String city; // City the airport is in.
    String desc; // Airport description.
    String ctry; // 2 character country code.
    String rgn;  // 5 or 6 character Region code 

    for (Vertex v : vlist)
    {
      id   = (Long)v.id();
      iata = (String)v.values("code").next();
      icao = (String)v.values("icao").next();
      city = (String)v.values("city").next();
      desc = (String)v.values("desc").next();
      ctry = (String)v.values("country").next();
      rgn  = (String)v.values("region").next();

      System.out.format("%5d %3s %4s  %2s  %6s  %15s  %-50s\n",
                        id,iata,icao,ctry,rgn,city,desc);
    }
  }

  // ---------------------------------------
  // Try to load a graph and run a few tests
  // ---------------------------------------
  public static void main(String[] args) 
  {
    int required = 10;
    boolean failed = false;

    try
    {
      if (args.length > 0) required = Integer.parseInt(args[0]);
    }
    catch (Exception e)
    {
      failed = true;
    }

    if (failed || required < -1)
    {
      System.out.println("Argument should be -1, 0 or any positive integer");
      System.exit(1);
    }

    ListAirports lapt = new ListAirports();

    if ( lapt.loadGraph("air-routes.graphml"))
    {
      lapt.listAirports(required);
    }

  }
}
