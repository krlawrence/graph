// RemoteClient.java
//
// Simple example of using GremlinServer from a Java client
//
// This example does the following:
//   1. Configure a new Cluster object
//   2. Use that cluster to connect to a Gremlin Server
//   3. Run a few queries against graph

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RemoteClient
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

    List <Map<String,Object>> vmaps =
      g.V().has("airport","region","GB-ENG").limit(10).valueMap().toList();
    
    System.out.println("\n\nThe following airports were found\n");
    for (Map <String,Object> m : vmaps)
    {
      ArrayList code = (ArrayList) m.get("code");
      ArrayList desc = (ArrayList) m.get("desc");
      System.out.println(code.get(0) + " , " + desc.get(0));
    }

    cluster.close();
  }
}

// The output should look something like this

/*
The following airports were found

LEQ , Land's End Airport
LGW , London Gatwick
MAN , Manchester Airport
LHR , London Heathrow
LCY , London City Airport
STN , London Stansted Airport
EMA , East Midlands Airport
LPL , Liverpool John Lennon Airport
LBA , Leeds Bradford Airport
NCL , Newcastle Airport
*/
