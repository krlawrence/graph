// RemotePartitionStrategy.java
//
// Simple example of using GremlinServer from a Java client
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Run a few queries against graph that explore using a PartitionStrategy

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.PartitionStrategy;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RemotePartitionStrategy
{
  public static void main( String[] args )
  {
    String host = "localhost";
    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint(host);
    builder.port(8182);
    builder.serializer(new GryoMessageSerializerV3d0());

    Cluster cluster = builder.create();

    // Create a new traversal source object
    GraphTraversalSource g =
      EmptyGraph.instance().traversal().
        withRemote(DriverRemoteConnection.using(cluster));
    
    // Make sure our connection is working.
    System.out.println(g.V().count().next());
    
    PartitionStrategy strategyP1 = 
      PartitionStrategy.build().
        partitionKey("partition").
        writePartition("p1").
        readPartitions("p1").create();

    // Test adding elements one by one
    GraphTraversalSource g2 = g.withStrategies(strategyP1);
    Object v1 = g2.addV("person").id().next();
    Object v2 = g2.addV("person").id().next();
    Object e1 = g2.addE("knows").from(V(v1)).to(V(v2)).id().next();
    
    // Test adding all the elements in one query.
    g2.addV("person").as("a").addV("person").addE("knows").to("a").iterate();

    // Add a vertex without a partition key
    g.addV("person").id().iterate();

    // Should be 4, not 5.
    System.out.println(g.V().has("partition","p1").count().next());

    // Inspect the elements we created.
    List<Map<Object,Object>> verts = g2.V().hasLabel("person").valueMap(true).toList();
    verts.forEach((m) -> System.out.println(m));

    List<Map<Object,Object>> edges = g2.E().hasLabel("knows").valueMap(true).toList();
    edges.forEach((m) -> System.out.println(m));

    // All done, close the connection.
    cluster.close();
  }
}

