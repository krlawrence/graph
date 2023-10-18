// RemoteSimpleClient.java
//
// Simple example of using GremlinServer from a Java client
// This example uses the GraphBinary serialization protocol.
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Generate some interesting statistics about the data in the graph.

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

// Using a static import avoids needing to use the "__." prefix as in "__.out()"
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

public class RemoteSimpleClient
{
  public static void runTests()
  {
    // Create the Cluster object. Note the use of the Serializers object
    // as a shortcut way to ask for Graph Binary rather than having to 
    // create an instance of the serializer ourselves.
    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint("localhost");
    builder.port(8182);
    builder.serializer(Serializers.GRAPHBINARY_V1D0);

    Cluster cluster = builder.create();

    GraphTraversalSource g =
      traversal().withRemote(DriverRemoteConnection.using(cluster));
    
    List<List<Object>> inTexas = 
      g.V().has("airport","region","US-TX").
            local(values("code","city").
            fold()).
            toList();

    for ( List x : inTexas)
    {
      System.out.println(x);
    }  
    
    cluster.close();
  }


  public static void main( String[] args )
  {
    RemoteSimpleClient sc = new RemoteSimpleClient();
    sc.runTests();
  }
}

