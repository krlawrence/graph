// RemoteVerticesFromMap.java
//                 
// Simple examples of seeding a traversal with a map of new vertex values.
//
// This example does the following:
//   1. Configures a new Cluster object using GraphBinary to connect
//      to a Gremlin Server.
//   2. Uses an injected map of values to create a set of new vertices.
//

// Common TinkerPop imports, all may not be needed.

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

// Imports specific to serialization of Traversals and graph elements
import org.apache.tinkerpop.gremlin.groovy.jsr223.GroovyTranslator;
import org.apache.tinkerpop.gremlin.groovy.jsr223.GroovyTranslator.DefaultTypeTranslator;
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
 * Simple class that manages the data creation and connections.
 *
 * @author Kelvin R. Lawrence
 */
public class RemoteVerticesFromMap {
    // Values for id, label, name, breed.
    static final String[][] values = {
            {"dog1", "dog", "Kim", "Beagle"},
            {"dog2", "dog", "Max", "Mixed"},
            {"dog3", "dog", "Toby", "Golden Retriever"},
            {"dog4", "dog", "Brandy", "Golden Retriever"},
            {"dog5", "dog", "Shadow", "Mixed"},
            {"dog6", "dog", "Scamp", "King Charles Spaniel"},
            {"dog7", "dog", "Rocket", "Golden Retriever"},
            {"dog8", "dog", "Dax", "Mixed"},
            {"dog9", "dog", "Baxter", "Mixed"},
            {"dog10", "dog", "Zoe", "Corgi"}};
    private Cluster cluster;
    private GraphTraversalSource gts;
    private DriverRemoteConnection drc;

    /*
     * Start testing.
     */
    public static void main(String[] args) {
        RemoteVerticesFromMap vfm = new RemoteVerticesFromMap();
        vfm.runTests();
    }

    /*
     * Create a map of dog vertex elements and add the vertices to the graph.
     */
    public void runTests() {
        System.out.println("\nData to insert");
        System.out.println("==============\n");
        List<Map> data = new ArrayList<Map>();
        for (String[] row : values) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", row[0]);
            map.put("label", row[1]);
            map.put("name", row[2]);
            map.put("breed", row[3]);
            data.add(map);
            System.out.println(map);
        }
        //System.out.println(list);
        GraphTraversalSource g = createConnection();

        g.inject(data).unfold().as("nodes").
          addV(select("nodes").select("label")).as("new_node").
            property(T.id, select("nodes").select("id")).
            property("name", select("nodes").select("name")).
            property("breed", select("nodes").select("breed")).
            id().toList();

        // Make sure the vertices were added correctly
        System.out.println("\nResults from the graph");
        System.out.println("======================\n");

        List<Map<Object, Object>> results =
                g.V().hasLabel("dog").valueMap(true).by(unfold()).toList();

        results.forEach((r) -> {
            System.out.println(r);
        });

        closeConnection();
    }

    /*
     * Create a connection to the server and return a 'g' object.
     */
    public GraphTraversalSource createConnection() {
        Cluster.Builder builder = Cluster.build();
        builder.addContactPoint("localhost");
        builder.port(8182);
        builder.serializer(Serializers.GRAPHBINARY_V1D0);

        this.cluster = builder.create();
        this.drc = DriverRemoteConnection.using(cluster);
        this.gts = traversal().withRemote(drc);

        return (gts);
    }

    /*
     * Close the connection.
     */
    public void closeConnection() {
        System.out.println("Connection closing");
        try {
            this.drc.close();
            this.cluster.closeAsync();
        } catch (Exception e) {
            System.out.println("Exception closing the connection:" + e);
        }
    }
}
