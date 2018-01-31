// TinkerGraphTest.groovy
//
// Simple example of using TinkerGraph with Groovy
//
// This example does the following:
//   1. Create a TinkerGraph instance
//   2. Load the air routes graph
//   3. Run a few queries against the graph.

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.*;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.Gremlin;
import java.io.IOException;

// Query the version of Gremlin/TinkerPop that we are using
println "Gremlin version is ${Gremlin.version()}"

// Create a new, empty, TinkerGraph instance
def tg = TinkerGraph.open() 

// Try to load the air routes data
println "Loading the air-routes graph...\n"

try
{
  tg.io(IoCore.graphml()).readGraph("air-routes.graphml");
} 
catch (IOException e)
{
  println "Could not load the graph file"
  System.exit(1);
}

// Get our graph traversal source object
def g = tg.traversal()

// Now let's run some queries

// Retrieve the properties associated with the AUS airport
def aus = g.V().has('code','AUS').valueMap().next()

println aus

// What city is AUS located in?
def city = aus['city']
println "\nThe AUS airport is in ${city[0]}\n"

aus.each {println "${it.key} : ${it.value[0]}"}

// How many outgoing routes are there from DFW?
def n = g.V().has("code","DFW").out().count().next() 
println "\nThere are  ${n} routes from Dallas"

// Where can I fly to from DFW?
def fromDfw = g.V().has("code","DFW").out().values("code").toList()
println "\nHere are the places you can fly to from DFW\n"
println fromDfw

// Where in the USA can I fly to from LHR?
def lhrToUsa = g.V().has("code","LHR").outE().inV().
                     has("country","US").limit(10).
                     path().by("code").by("dist").toList()

println "\nFrom LHR to airports in the USA (only 10 shown)\n"
//println lhrToUsa
lhrToUsa.each {println it}

// Selected routes from San Antonio
def routes = []
g.V().has("code","SAT").out().path().by("icao").limit(10).fill(routes);
println "\nRoutes from San Antonio (only 10 shown)\n"
routes.each {println it}

// Retrieve the vertex representing the FRA airport.
// Then display the keys and values contained in the vertex.
def v = g.V().has('code','FRA').next()
println "\nKeys found in the FRA vertex"
println v.keys()
println "\nValues found in the FRA vertex"
v.values().each{println it}

// If I start in AUS, and only stop at most once on the way, where in 
// England can I get to?  Note the use of "__." to prefix the
// call to the out() step.

def eng = g.V().has("code","AUS").repeat(__.out()).emit().times(2).
                has("region","GB-ENG").dedup().values("code").toList();

println "\nAirports in England reachable with no more than one stop from AUS"
println "\n${eng}\n"  


