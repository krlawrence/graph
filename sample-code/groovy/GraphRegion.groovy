// GraphRegion.groovy
//
// Simple example of using TinkerGraph with Groovy
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Load the air routes graph
//   3. Run some tests that show the where...by and multiple V() constructs

// I have highlighted the places where the Gremlin is slightly different from the 
// Gremlin we can use in the Gremlin Console.


import org.apache.tinkerpop.gremlin.process.traversal.*
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*
import org.apache.tinkerpop.gremlin.util.Gremlin

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.io.IOException

class RegionTest {
    private TinkerGraph graph
    private GraphTraversalSource g

    // Try to create a new graph and load the specified GraphML file
    def loadGraph(name) {
        graph = TinkerGraph.open()
        g = traversal().with(graph)

        try {
            g.io(name).read().iterate()
        } catch (IOException e) {
            println("GraphStats - GraphML file not found")
            return false
        }
        return true
    }

    // Find all airports in the region of the specified airport
    //
    // Note that when used from an application we have to prefix eq with a "P."
    // Also select has to be prefixed by "__."
    def findByRegion(iata) {
        println("\nRegion code lookup for " + iata)

        def list =
                g.V().has("code", iata).values("region").as("r").
                  V().hasLabel("airport").as("a").values("region").
                  where(P.eq("r")).by().
                  local(__.select("a").values("city", "code", "region").fold()).toList()

        list.each { println it }
    }
}

// If you want to check your Gremlin version, uncomment the next line
println("Gremlin version is: ${Gremlin.version()}")

rt = new RegionTest()

if (rt.loadGraph("air-routes.graphml")) {
    rt.findByRegion("NCE")  // Nice
    rt.findByRegion("DEN")  // Denver
    rt.findByRegion("GVA")  // Geneva
    rt.findByRegion("NGS")  // Nagasaki
    rt.findByRegion("CAN")  // Guangzhou
}
