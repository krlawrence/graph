// RemoteSubmitQuery
//                 
// A simple class that demonstrates submitting queries as text strings rather than using 
// a DriverRemoteConnection and submitting queries as bytecode.
//
// This example does the following:
//   1. Creates a list of queries a user can pick from 
//   2. Submits the selected query and shows the Result object(s) that come back from the
//      server. This can be a useful learning tool showing where classes such as
//      ReferenceVertex are used when working with a Gremlin Server.
//
// Not all of the imports below are used by this exmple but in general they represent a
// commonly needed set when creating a TinkerPop application.

// Common TinkerPop imports, all may not be needed.

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
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

// Imports specific to serialization of Traversals and graph elements
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONVersion;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;
import org.apache.tinkerpop.gremlin.process.traversal.Bytecode;

// Using a static import avoids needing to use the "__." prefix as in "__.out()"
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

// Java classes commonly used in TinkerPop focussed code.
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

/*
 * Simple class that  allows experimentation with submitting Gremlin queries
 * over a web socket connection but as text strings rather than bytecode.
 *
 * @author Kelvin R. Lawrence
 */
public class RemoteSubmitQuery {
    String[] queries =
            {
                    "g.V().count()",
                    "g.V('3')",
                    "g.V('1','2','3','4').values('city')",
                    "g.V().groupCount().by(label)",
                    "g.V().has('code',within('AUS','SEA','BOS','JFK','LHR')).valueMap()",
                    "g.V().has('code',within('AUS','SEA','BOS','JFK','LHR')).elementMap()",
                    "g.V().hasLabel('airport').sample(30).group().by('country').by('city')",
                    "g.V().has('code','SAF').out()",
                    "g.V().has('code','SAF').out().tree()",
                    "g.V().has('code','SAF').out().path().by('code')",
                    "g.V().hasLabel('airport').limit(20).valueMap()",
                    "g.V().has('code',within('DFW','DXB','AKL','FRA'))." +
                            "repeat(outE().subgraph('sg').inV()).times(2).cap('sg')",
                    "g.V().outE().subgraph('sg').inV().cap('sg')"
            };
    private Cluster cluster;
    private Client client;

    /*
     * Start testing.
     */
    public static void main(String[] args) {
        int queryId = -1;
        boolean valid = true;

        RemoteSubmitQuery rsq = new RemoteSubmitQuery();
        int available = rsq.numQueries();

        if (args.length > 0) {
            if ("-?".equals(args[0])) {
                rsq.listQueries();
                System.exit(0);
            } else {
                try {
                    queryId = Integer.parseInt(args[0]);
                } catch (NumberFormatException nfe) {
                    valid = false;
                }
            }
        }
        if (valid && queryId >= 0 && queryId < available) {
            rsq.runTests(queryId);
        } else {
            System.out.println("Please enter a number between 0 and " + (available - 1) +
                    " or enter '-?' to see a list of available queries");
            System.exit(0);
        }
    }

    /*
     * Return the number of queries available
     */
    public int numQueries() {
        return queries.length;
    }

    /*
     * List the queries available with their index numbers
     */
    public void listQueries() {
        int n = 0;
        System.out.println("\n");
        for (String q : queries) {
            System.out.printf("%3d : %s\n", n, q);
            n += 1;
        }
    }

    /*
     * Kick off the tests that need to run
     */
    public void runTests(int queryToRun) {
        Client c = createClient();

        String query = queries[queryToRun];
        System.out.println("\n========\n" + query + "\n========\n");

        try {
            List<Result> results = client.submit(query).all().get();
            for (Result res : results) {
                System.out.println(res);
            }
        } catch (Exception e) {
            System.out.println("Exception while submitting the query:\n" + query + "\n" + e);
        }
        closeConnection();
    }

    /*
     * Create a connection to the server and return a Client object.
     */
    public Client createClient() {
        Cluster.Builder builder = Cluster.build();

        // Replace the hostname with the name of the server you are connecting to.
        builder.addContactPoint("localhost");
        builder.port(8182);
        builder.enableSsl(true);
        builder.serializer(Serializers.GRAPHBINARY_V1D0);
        builder.maxContentLength(10 * 1024 * 1024);
        this.cluster = builder.create();
        this.client = cluster.connect();
        return (this.client);
    }

    /*
     * Close the connection.
     */
    public void closeConnection() {
        System.out.println("Connection closing");
        try {
            this.client.close();
            this.cluster.closeAsync();
        } catch (Exception e) {
            System.out.println("Exception while closing the connection:" + e);
        }
    }
}
