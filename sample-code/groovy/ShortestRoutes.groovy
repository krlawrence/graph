// ShortestRoutes.groovy
//
// Simple example of using TinkerGraph with Groovy
//
// This example does the following:
//   1. Create an empty TinkerGraph instance
//   2. Load the air routes graph
//   3. Run some tests that how to calculate shortest routes using a sack. 

// I have highlighted the places where the Gremlin is slightly different from the 
// Gremlin we can use in the Gremlin Console.


import org.apache.tinkerpop.gremlin.process.traversal.*
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.io.IOException

class Routes {
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

    // Find the (maximum of 10) shortest routes with one stop
    // between the two specified airports.
    //
    // Note the use of the prefixes Operator and Order)
    def shortestRouteOneStop(from, to) {
        def list = g.withSack(0).
                     V().has('code', from).
                     outE().sack(Operator.sum).by('dist').
                     inV().outE().sack(Operator.sum).by('dist').
                     inV().has('code', to).sack().
                     order().by(Order.asc).limit(10).
                     path().
                       by('code').by('dist').by('code').by('dist').by('code').by().
                     toList()

        list.each {
            printf("%3s %4d %3s %4d %3s %4d\n",
                    it[0], it[1], it[2], it[3], it[4], it[5])
        }
    }

}

// If you want to check your Gremlin version, uncomment the next line
//println("Gremlin version is: ${Gremlin.version()}");

rt = new Routes()

if (rt.loadGraph("air-routes.graphml")) {
    rt.shortestRouteOneStop("AUS", "LHR")
    println()
    rt.shortestRouteOneStop("CDG", "BNE")
    println()
    rt.shortestRouteOneStop("HKG", "SJC")
}
