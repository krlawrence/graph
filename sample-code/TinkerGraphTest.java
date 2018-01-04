// Simple set of examples of using TinkerGraph from a Java application.
// This program just uses the in memory storage model. The air-routes graph
// is loaded but a schema is not created by this code. There are other examples
// provided where the schema is created.
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
    TinkerGraph tg = TinkerGraph.open() ;
    
    try
    {
      tg.io(IoCore.graphml()).readGraph("air-routes.graphml");
    }
    catch( IOException e )
    {
      System.out.println("GraphTest - Air routes GraphML file not found");
      System.exit(0);
    }
    GraphTraversalSource g = tg.traversal();
    
    Map<String,?> aus = g.V().has("code","AUS").valueMap().next();
    System.out.println(aus);

    List city = (List)(aus.get("city"));
    System.out.println("The AUS airport is in " + city.get(0));

    aus.forEach( (k,v) -> System.out.println(k + ": "+ v));

    Long n = g.V().has("code","DFW").out().count().next();
    System.out.println("There are " + n + " routes from Dallas");

    List fromAus = (g.V().has("code","AUS").out().values("code").toList());
    System.out.println(fromAus);

    List <Path> lhrToUsa = g.V().has("code","LHR").outE().inV().
                                 has("country","US").limit(5).
                                 path().by("code").by("dist").toList();

    lhrToUsa.forEach((k) -> System.out.println(k));

    ArrayList <Path> routes = new ArrayList<>();
    g.V().has("code","SAT").out().path().by("icao").fill(routes);
    System.out.println(routes);

    System.out.println("*******************");
    Vertex v = g.V().has("code","FRA").next();
    System.out.println(v.keys());
    Set <String> s = v.keys();
    for (String k : s) 
    {
      System.out.println( k + "\t: " + v.property(k).value());
    }
    System.out.println("*******************");

    List eng = g.V().has("code","AUS").repeat(__.out()).times(2).
               has("region","GB-ENG").dedup().values("code").toList();

    System.out.println(eng);
  }
}
