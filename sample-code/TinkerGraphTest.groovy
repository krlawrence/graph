// Simple set of examples of using TinkerGraph from a Groovy application.

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

println "Gremlin version is ${Gremlin.version()}"

def tg = TinkerGraph.open() 

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
def g = tg.traversal()

def aus = g.V().has('code','AUS').valueMap().next()

println aus

def city = aus['city']
println "\nThe AUS airport is in ${city[0]}\n"

aus.each {println "${it.key} : ${it.value[0]}"}

def n = g.V().has("code","DFW").out().count().next() 
println "\nThere are  ${n} routes from Dallas"

def fromAus = g.V().has("code","AUS").out().values("code").toList()
println "\nHere are the places you can fly to from Austin\n"
println fromAus

def lhrToUsa = g.V().has("code","LHR").outE().inV().
                     has("country","US").limit(10).
                     path().by("code").by("dist").toList()

println "\nFrom LHR to airports in the USA (only 10 shown)\n"
//println lhrToUsa
lhrToUsa.each {println it}

def routes = []
g.V().has("code","SAT").out().path().by("icao").limit(10).fill(routes);
println "\nRoutes from San Antonio (only 10 shown)\n"
routes.each {println it}

def v = g.V().has('code','FRA').next()
println "\nKeys found in the FRA vertex"
println v.keys()

def eng = g.V().has("code","AUS").repeat(__.out()).times(2).
                has("region","GB-ENG").dedup().values("code").toList();

println "\nAirports in England reachable with no more than one stop from AUS"
println "\n${eng}\n"  


