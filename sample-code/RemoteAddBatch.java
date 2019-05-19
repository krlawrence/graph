// RemoteAddBatch.java
//
// Simple example of using GremlinServer from a Java client
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Create a GraphTraversal and incrementally add to it before 
//      sending the traversal to the server for execution.

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV3d0;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RemoteAddBatch
{
  public static void main( String[] args )
  {
    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint("localhost");
    builder.port(8182);
    builder.serializer(new GraphSONMessageSerializerV3d0());

    Cluster cluster = builder.create();

    GraphTraversalSource g =
      EmptyGraph.instance().traversal().
        withRemote(DriverRemoteConnection.using(cluster));

    // Instead of building the traversal all in one go, the example
    // below shows how to create a traversal and incrementally add to it.
    // This is extremely useful when building up batches of steps programmatically
    // to send to a server as a single query.
    GraphTraversal t = g.addV("person");
    t.property(T.id,1000000).property("name","Stew").property("age",23);
    t.addV("person");
    t.property(T.id,1000001).property("name","Stevie").property("age",28);
    t.addV("person");
    t.property(T.id,1000002).property("name","Patrick").property("age",18);
    t.addV("person");
    t.property(T.id,1000003).property("name","Patricia").property("age",41);

    // Now execute the traversal.
    t.iterate();

    // Make sure the vertices and properties were successfully added to the graph.
    List<Map<Object,Object>> results = 
        g.V().hasLabel("person").valueMap(true).toList();

    for(Map m : results)
    {
      System.out.println(m);
      //m.forEach((k,v) -> System.out.println(k + ":" + v));
      //System.out.println();
    }

    // Delete the vertices we just created as this is only a test.
    g.V(1000000,1000001,1000002,1000003).drop().iterate();

    // All done, close the connection.
    cluster.close();
  }
}
