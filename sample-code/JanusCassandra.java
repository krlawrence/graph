// JanusCassandra.java
//
// Simple example of using JanusGraph with Java (outside the Gremlin console)
//
// This example does the following:
//   1. Connects to an existing JanusGraph/Cassandra instance
//   2. Runs a few queries against graph  

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.janusgraph.core.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class JanusCassandra 
{
  public static void main(String[] args) 
  {
    System.out.println(" --------------------------");
    System.out.println(" Connecting to Graph       ");
    System.out.println(" --------------------------");
    //JanusGraph jg = JanusGraphFactory.open("janusgraph-cassandra.properties") ;
    JanusGraph jg = JanusGraphFactory.open("janusgraph-cql.properties") ;
    
    GraphTraversalSource g = jg.traversal();
    
    System.out.println(" --------------------------");
    System.out.println(" Sending queries           ");
    System.out.println(" --------------------------");
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

    System.out.println(" --------------------------");
    System.out.println(" Shutting down             ");
    System.out.println(" --------------------------");
    jg.tx().commit();
    jg.close();
    System.exit(0);
  }
}
