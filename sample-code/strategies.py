# strategies.py
#
# Connect to a Gremlin Server using a remote connection and issue some basic queries. 
# Note the way that certain Gremlin steps that share the same name as Python reserved words
# or method names are handled by postfixing an underscore after them as in the case of "as_".
#
# Unlike in the basic-client.py example, the queries sent to the server are sent using
# Gremlin byte code and not sent as text strings.
#
# This example code assumes that the GremlinPython library has been installed using:
#
#     pip install gremlinpython
#
# This sample experiments with the following strategies:
#    SubgraphStrategy
#    ReadOnlyStrategy
#    PartitionStrategy
#
# Import some classes we will need to talk to our graph
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection
from gremlin_python.structure.graph import Graph
from gremlin_python import statics
from gremlin_python.process.graph_traversal import __
from gremlin_python.process.strategies import *
from gremlin_python.process.traversal import *
import sys

# Path to our graph (this assumes a locally running Gremlin Server)
# Note how the path is a Web Socket (ws) connection.
endpoint = 'ws://localhost:8182/gremlin'

# Obtain a graph traversal source using a remote connection
graph=Graph()
connection = DriverRemoteConnection(endpoint,'g')
g = graph.traversal().withRemote(connection)

# Helper method to pretty print some headings.
def heading(s):
    print("\n{}".format(s))
    for i in range(len(s)):
        print("-",end="")
    print("")    

# Create a SubgraphStrategy that only picks up airports in Texas
# and routes between them.
heading('SubgraphStrategy - just Texas airports')
strategy = SubgraphStrategy(vertices=__.has("region","US-TX"), edges=__.hasLabel('route'))
g2 = g.withStrategies(strategy)
verts = g2.V().count().next()
edges = g2.E().count().next()

routes = g2.V().\
         order().\
           by(__.out().count()).\
         group().\
           by('code').\
           by(__.out().count()).\
         order(Scope.local).by(Column.values).\
         next()

print("Found {} airports and {} routes.".format(verts,edges))
for k,v in routes.items():
    print(k,v)


# Create a ReadOnlyStrategy - any attempt to add or change an element 
# using this traversal source should cause an exception to be thrown.
heading('ReadOnlyStrateggy')
g3 = g.withStrategies(ReadOnlyStrategy())
try:
    g3.addV('should_fail').property('p1',1).iterate()
except:
    print('Not allowed to add a new vertex')
    print(sys.exc_info()[0])
    print(sys.exc_info()[1])


# Create a PartitionStrategy that adds a property called "partition"
# to all new elements that are created. 
heading('PartitionStrateggy')
g4 = g.withStrategies(PartitionStrategy(
         partition_key="partition",
         write_partition="a", 
         read_partitions=["a"]))

         
try:
    x = g4.addV('test').property("p1",1).toList()
except:
    print("Exception trying to add a vertex")
    print(sys.exc_info()[0])
    print(sys.exc_info()[1])
finally:
    heading('Closing connection')
    connection.close()

