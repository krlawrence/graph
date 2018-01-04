// Create a TinkerGraph instance
// Load the air routes graph
// Display some statistics about the graph

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

public class GraphStats 
{
  public static void main(String[] args) 
  {
    TinkerGraph tg = TinkerGraph.open() ;
    
    System.out.println("Opening air-routes.graphml");

    try
    {
      tg.io(IoCore.graphml()).readGraph("air-routes.graphml");
    }
    catch( IOException e )
    {
      System.out.println("GraphStats - Air routes GraphML file not found");
      System.exit(0);
    }
    GraphTraversalSource g = tg.traversal();

    System.out.println("Collecting stats");

    Map <String,?> most = g.V().hasLabel("airport").
                            order().by(__.bothE("route").count(),Order.decr).limit(1).
                            project("ap","num","city").by("code").by(__.bothE("route").count()).
                            by("city").next();
    
    String s = "" + most.get("ap") + "/" + most.get("city");
    Long r = (Long) most.get("num");
    System.out.println("The airport with the most routes is " + s + " with " + r + " routes") ;
  }
  
}
