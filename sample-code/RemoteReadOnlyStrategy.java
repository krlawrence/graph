// RemoteReadOnlyStrategy.java
//
// Simple example of using GremlinServer from a Java client
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Run a few queries against graph that explore using a ReadOnlyStrategy
//
// With a ReadOnly strategy in place, any attempt to mutate the graph should
// cause an exception to be thrown.

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.verification.ReadOnlyStrategy;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RemoteReadOnlyStrategy
{
  public static void main( String[] args )
  {
    String host = "localhost";
    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint(host);
    builder.port(8182);
    builder.serializer(new GryoMessageSerializerV3d0());

    Cluster cluster = builder.create();

    GraphTraversalSource g =
      EmptyGraph.instance().traversal().
        withRemote(DriverRemoteConnection.using(cluster));
   
   // Create a new traversal source object
    GraphTraversalSource g2;

    // Any attempt to mutate the graph using 'g2' should now
    // cause an exception to be thrown but using 'g' should
    // continue to allow mutations.
    g2 = g.withStrategies(ReadOnlyStrategy.instance());

    Object v1 = g.addV("ROTest").property("p1",1).next();
    Object v2 = g.addV("ROTest").property("p1",1).next();
    g.addE("ROEdgeTest").from(V(v2)).to(V(v1)).iterate();
    
    try
    {
      g2.addV("shouldfail").iterate();
    }
    catch(Exception e)
    {
      System.out.println("Unable to add a vertex");
    }
    try
    {
      g2.V().hasLabel("ROTest").property("p1",2).iterate();
    }
    catch(Exception e)
    {
      System.out.println("Unable update a property");
    }
    try
    {
      g2.V().hasLabel("ROTest").drop().iterate();
    }
    catch(Exception e)
    {
      System.out.println("Unable drop a vertex");
    }
    try
    {
      g2.V().addE("ROEdgeTest2").from(V(v1)).to(V(v2)).iterate();
    }
    catch(Exception e)
    {
      System.out.println("Unable to add an edge");
    }
    try
    {
      g2.E().hasLabel("ROEdgeTest").drop().iterate();
    }
    catch(Exception e)
    {
      System.out.println("Unable to drop an edge");
    }
    // Clean up before we exit.
    g.V().hasLabel("ROTest").drop().iterate();
    g.E().hasLabel("ROTest").drop().iterate();
    cluster.close();
 }
}

