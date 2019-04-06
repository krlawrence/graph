// TinkereGraphTest.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Creates an empty TinkerGraph instance
//   2. Loads the air-routes.graphml file
//   3. Runs a few queries against the newly created graph    .
//
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class TinkerGraphTest 
{
  public static void main(String[] args) 
  {
    // If you want to check your gremlin version, uncomment the following line
    //System.out.println("Gremlin version is " + Gremlin.version());
    
    // Create a new TinkerGraph instance and try to load the air-routes data
    
    TinkerGraph tg = TinkerGraph.open() ;
    
    try
    {
      tg.io(IoCore.graphml()).readGraph("air-routes.graphml");
    }
    catch( IOException e )
    {
      System.out.println("TinkerGraphTest - Air routes GraphML file not found");
      System.exit(0);
    }
    
    // Create our graph traversal source
    
    GraphTraversalSource g = tg.traversal();
    
    // Run a few simple tests

    // Get a value map for the properties associated with the AUS airport
    Map<String,?> aus = g.V().has("code","AUS").valueMap().next();
    System.out.println(aus);

    // Which city is the AUS airport located in?
    List city = (List)(aus.get("city"));
    System.out.println("\nThe AUS airport is in " + city.get(0) + "\n");

    // Display the property keys and values
    aus.forEach( (k,v) -> System.out.println(k + ": "+ v));

    // How many routes are there from DFW?
    Long n = g.V().has("code","DFW").out().count().next();
    System.out.println("\nThere are " + n + " routes from Dallas\n");

    // Where do the routes go to?
    List fromDfw = g.V().has("code","DFW").out().values("code").toList();
    System.out.println(fromDfw);

    // Where in the USA can I fly to from LHR?
    List <Path> lhrToUsa = g.V().has("code","LHR").outE().inV().
                                 has("country","US").
                                 path().by("code").by("dist").toList();

    System.out.println("\nRoutes from LHR to the USA\n");

    lhrToUsa.forEach((k) -> System.out.println(k));

    // If I start in AUS, and only stop once on the way, where in 
    // England can I get to?  Note the use of "__." to prefix the
    // call to the out() step.
    List <Object> eng = 
       g.V().has("code","AUS").repeat(__.out()).times(2).
             has("region","GB-ENG").values("city").dedup().toList();

    System.out.println("\nPlaces in England I can get to with one stop from AUS.\n");
    eng.forEach( (p) -> System.out.print(p + " "));
    System.out.println();
  }
}
