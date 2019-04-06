
// GraphFromCSV.java
//
// Simple example of using TinkerGraph with Java
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Reads simple edge lists from a CSV file
//   3. Creates a graph - avoiding creating any duplicate vertices or edges
//   4. Displays information about the graph that was created

// The csv file is expected to be of the form:
//   Kelvin,knows,Jack
//   Jack,knows,Baxter
//
// Each name in the CSV file is assumed to be unique for this simple examplei, so for
// example only one node will be created for Jack in the example above.

// I have highlighted any places where the Gremlin is slightly different from the 
// Gremlin we can use in the Gremlin Console.

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.util.Gremlin;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.io.*;

public class GraphFromCSV
{
 
  private TinkerGraph tg;
  private GraphTraversalSource g;

  // Try to create a new empty graph instance
  public boolean createGraph()
  {
    tg = TinkerGraph.open() ;
    g = tg.traversal();
    
    if (tg == null || g==null)
    {
      return false;
    }
    return true;
  }

  // Add the specified vertices and edge. Do not add anything that 
  // already exists.
  public boolean addElements(String name1, String label, String name2)
  {
    if (tg == null || g==null)
    {
      return false;
    }

    // Create a node for 'name1' unless it exists already
    Vertex v1 = 
      g.V().has("name",name1).fold().
            coalesce(__.unfold(),__.addV().property("name",name1)).next();

    // Create a node for 'name2' unless it exists already
    Vertex v2 = 
      g.V().has("name",name2).fold().
            coalesce(__.unfold(),__.addV().property("name",name2)).next();

    // Create an edge between 'name1' and 'name2' unless it exists already
    g.V().has("name",name1).out(label).has("name",name2).fold().
          coalesce(__.unfold(),
                   __.addE(label).from(__.V(v1)).to(__.V(v2))).iterate();

    return true;
  }

  public void displayGraph()
  {
    Long c;
    c = g.V().count().next();
    System.out.println("Graph contains " + c + " vertices");
    c = g.E().count().next();
    System.out.println("Graph contains " + c + " edges");

    List<Path> edges = g.V().outE().inV().path().by("name").by().toList();

    for (Path p : edges)
    {
      System.out.println(p);
    }
  }


  // Open the sample csv file and build a graph based on its contents

  public static void main(String[] args) 
  {
    GraphFromCSV gcsv = new GraphFromCSV();
    
    if (gcsv.createGraph())
    {
      try 
      {
        String line;
        String [] values;

        FileReader fileReader = new FileReader("edges.csv");

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while((line = bufferedReader.readLine()) != null) 
        {
          //System.out.println(line);
          values = line.split(",");
          gcsv.addElements(values[0],values[1],values[2]);
        }

        gcsv.displayGraph();
        bufferedReader.close();         
      }
      catch( Exception e ) 
      {
        System.out.println("Unable to open file" + e.toString());
        //e.printStackTrace();
      }
    }  
  }      
}
