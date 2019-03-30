// RemoteSubgraphStrategy.java
//
// Simple example of using GremlinServer from a Java client
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Run a few queries against graph that explore using a SubGraphStrategy

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.event.MutationListener;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.SubgraphStrategy;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.EventStrategy;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.verification.ReadOnlyStrategy;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RemoteSubgraphStrategy
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
   
   // Create a new traversal source object
    GraphTraversalSource g2;

    // Create a strategy that filters out anything without
    // a region code of 'US-TX'
    g2 = g.withStrategies(
             SubgraphStrategy.build().
               vertices(has("region","US-TX")).create());
    
    // How many airports are there in Texas?
    System.out.println(g2.V().count().next());
   
   cluster.close();
  }
}

