// RemoteStats.java
//
// Simple example of using GremlinServer from a Java client
// This code performs the same queries that GraphStats.java does 
// but to a remote endpoint rather than a local in-memory graph.
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Generate some interesting statistics about the data in the graph.

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

// Using a static import avoids needing to use the "__." prefix as in "__.out()"
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

public class RemoteStats
{
  public static void main( String[] args )
  {
    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint("localhost");
    builder.port(8182);
    builder.serializer(new GryoMessageSerializerV1d0());

    Cluster cluster = builder.create();

    GraphTraversalSource g =
      EmptyGraph.instance().traversal().
        withRemote(DriverRemoteConnection.using(cluster));

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
    Map <String,?> most = g.V().hasLabel("airport").
                            order().by(bothE("route").count(),Order.decr).limit(1).
                            project("ap","num","city").by("code").by(bothE("route").count()).
                            by("city").next();
    
    String s = "" + most.get("ap") + "/" + most.get("city");
    Long r = (Long) most.get("num");
    System.out.println("\nThe airport with the most routes is " + s + " with " + r + " routes") ;


    // Find the airports with the most routes overall (incoming + outgoing)

    System.out.println("\nTop 20 airports by total routes");
    System.out.println("===============================");
    
    List<Map<String,Object>> top = 
      g.V().hasLabel("airport").
            order().by(both("route").count(),Order.decr).limit(20).
            project("ap","num","city").by("code").by(both("route").count()).by("city").
            toList();
   
    // Either of these can be used
    //for(Map a: top) { System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"), a.get("num"));}
    top.forEach((a) -> System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"),a.get("num")));
    


    // Find the airports with the most outgoing routes

    System.out.println("\nTop 20 airports by outgoing routes");
    System.out.println("==================================");
    
    List<Map<String,Object>> topout = 
      g.V().hasLabel("airport").
            order().by(out("route").count(),Order.decr).limit(20).
            project("ap","num","city").by("code").by(out("route").count()).by("city").
            toList();
   
    topout.forEach((a) -> System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"),a.get("num")));

    // Find the airports with the most incoming routes
    
    System.out.println("\nTop 20 airports by incoming routes");
    System.out.println("==================================");
    
    List<Map<String,Object>> topin = 
      g.V().hasLabel("airport").
            order().by(in("route").count(),Order.decr).limit(20).
            project("ap","num","city").by("code").by(in("route").count()).by("city").
            toList();
   
    topin.forEach((a) -> System.out.format("%4s %12s %4d\n",a.get("ap"),a.get("city"),a.get("num")));


    // Find the longest route in the graph

    Map <String,?> longroute = 
      g.E().hasLabel("route").
            order().by("dist",Order.decr).limit(1).
            project("from","to","num").
            by(inV().values("city")).by(outV().values("city")).by("dist").next();

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
                  order().by(out("contains").count(),Order.decr).limit(1).
                  project("num","cont").
                    by(out("contains").count()).
                    by("desc").next();

    System.out.println("The continent with the most airports: " + 
                       cmost.get("cont") + " (" + 
                       cmost.get("num") +")");               


    // Find the country with the most airports

    cmost = g.V().hasLabel("country").
                  order().by(out("contains").count(),Order.decr).limit(1).
                  project("num","country").
                    by(out("contains").count()).
                    by("desc").next();

    System.out.println("The country with the most airports: " + 
                       cmost.get("country") + " (" + 
                       cmost.get("num") +")");               
  
    cluster.close();
  }
}

