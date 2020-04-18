// RemoteWriteText.java
//
// Simple examples of serialzing graph traversals back to text strings.
//
// This example does the following:
//   1. Configures a new Cluster object using GraphBinary to connect
//      to a Gremlin Server.
//   2. Demonstrates different ways to turn traversals back to human readable
//      text strings that can be used with the Gremlin Console. This is useful
//      when doing things such as debugging machine generated graph traversals.

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
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

public class RemoteWriteText
{
  public static void runTests()
  {
    // Configure the settings we need and create a connection to our Gremlin Server endpoint
    Cluster.Builder builder = Cluster.build();
    builder.addContactPoint("localhost");
    builder.port(8182);
    builder.serializer(Serializers.GRAPHBINARY_V1D0);

    Cluster cluster = builder.create();

    GraphTraversalSource g =
      traversal().withRemote(DriverRemoteConnection.using(cluster));
    

    // Simple traversal we can use for testing a few things
    Traversal t = 
      g.V().has("airport","region","US-TX").
            local(values("code","city").
            fold());

    // Graphson encoding of the same query
    String json = 
        "{\"@type\":\"g:Bytecode\",\"@value\":{\"step\":[[\"V\"]," 
      + "[\"has\",\"airport\",\"region\",\"US-TX\"],[\"local\","
      + "{\"@type\":\"g:Bytecode\",\"@value\":{\"step\":"
      + "[[\"values\",\"code\",\"city\"],[\"fold\"]]}}]]}}" ;
    
    //
    // Examples of serializing traverals and queries different ways follow
    //

    // Generate the text form of the query from a Traversal
    String query;
    query = GroovyTranslator.of("g").
            translate(t.asAdmin().getBytecode());
    
    System.out.println("\nResults from GroovyTranslator on a traversal");
    System.out.println(query);

    // Generate the text form of the query from a literal JSON bytecode string
    ObjectMapper mapper = 
      GraphSONMapper.
      build().
      version(GraphSONVersion.V3_0).
      create().
      createMapper();

    try 
    {
      query = GroovyTranslator.of("g").
                translate( mapper.
                  readValue(json, Bytecode.class)  );
    
      System.out.println("\nResults from GroovyTranslator on literal JSON");
      System.out.println(query);
    }
    catch (Exception e)
    {
      System.out.println("Exception trying to convert JSON traversal\n" + e);
    }
    

    // The steps that follow turn  a traversal into JSON bytecode and then turn
    // that into text.  Given GroovyTranslator can do this all in one step this
    // is provided simply as an example of how you can get the JSON form of a
    // traversal and then optionally convert that to text.
    
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    
    GraphSONWriter gsw = GraphSONWriter.build().create();
    
    try
    {
      //gsw.writeObject(System.out,t);
      gsw.writeObject(bytes,t);
      System.out.println("\nResults from GraphSONWriter on a Traversal");
      System.out.println(bytes.toString());
      
      query = GroovyTranslator.of("g").
                translate( mapper.
                  readValue(bytes.toString(), Bytecode.class)  );
      
      System.out.println("\nResults from GroovyTranslator on a Byte Stream");
      System.out.println(query);
    }
    catch( Exception e )
    {
      System.out.println("Exception trying to convert a traversal\n" + e);
    }

    // All done
    cluster.close();
  }
                                                        
  // Let the fun begin!
  public static void main( String[] args )
  {
    RemoteWriteText wt = new RemoteWriteText();
    wt.runTests();
  }
}

/**
 * The results when the code is run should look like this:
 *
 *
 * Results from GroovyTranslator on a traversal
 * g.V().has("airport","region","US-TX").local(__.values("code","city").fold())
 * 
 * Results from GroovyTranslator on literal JSON
 * g.V().has("airport","region","US-TX").local(__.values("code","city").fold())
 * 
 * Results from GraphSONWriter on a Traversal
 * {"@type":"g:Bytecode","@value":{"step":[["V"],["has","airport","region","US-TX"],["local",{"@type":"g:Bytecode","@value":{"step":[["values","code","city"],["fold"]]}}]]}}
 * 
 * Results from GroovyTranslator on a Byte Stream
 * g.V().has("airport","region","US-TX").local(__.values("code","city").fold())
 */


