//// RemoteBulkSetTest
//
// Simple example of using GremlinServer from a Java client
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Experiment with Text Predicates and a BulkSet.

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.driver.Tokens;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import org.apache.tinkerpop.gremlin.process.traversal.TextP;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.BulkSet;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RemoteBulkSetTest
{
  public static void main( String[] args )
  {                       
    String host;

    if (args.length > 0)
    {
      host = args[0];
    }
    else
    {
      host = "localhost";
    }

    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint(host);
    builder.port(8182);
    builder.serializer(new GryoMessageSerializerV3d0());

    Cluster cluster = builder.create();

    // Create a new traversal source object
    GraphTraversalSource g = traversal().
        withRemote(DriverRemoteConnection.using(cluster));
    
    // Find all cities with names that start with "Lon"
    // and return the results as a BulkSet.
    BulkSet cities =
      g.V().has("airport","city",TextP.startingWith("Lon")).
            values("city").toBulkSet();

    System.out.println(cities.asBulk());
    System.out.println("Overall size: " + cities.size());
    System.out.println("Unique size : " + cities.uniqueSize());
    System.out.println("London      : " + cities.get("London"));

    cluster.close();
  }
}

