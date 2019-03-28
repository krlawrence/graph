# glv-client.py
#
# Connect to a Gremlin Server using a remote connection and issue some basic queries.
#
# Unlike in the basic-client.py example, the queries sent to the server are sent using
# Gremlin byte code and not sent as text strings.
#
# This example code assumes that the GremlinPython library has been installed using:
#
#     pip install gremlinpython
#

# Import some classes we will need to talk to our graph
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection
from gremlin_python.structure.graph import Graph
from gremlin_python import statics
from gremlin_python.process.graph_traversal import __
from gremlin_python.process.strategies import *
from gremlin_python.process.traversal import *

# Path to our graph (this assumes a locally running Gremlin Server)
# Note how the path is a Web Socket (ws) connection.
endpoint = 'ws://localhost:8182/gremlin'

# Obtain a graph traversal source using a remote connection
graph=Graph()
connection = DriverRemoteConnection(endpoint,'g')
g = graph.traversal().withRemote(connection)

# Sample 30 airports and return their code and city values.
# The returned values will be packaged as a list containing 30 small lists.
sample = g.V().hasLabel('airport'). \
               sample(30). \
               order().by('code'). \
               local(__.values('code','city').fold()). \
               toList()

# Print the results in a tabular form with a row index
# for ctr, c in enumerate(sample,1):
i = 1
for c in sample:
    print("%3d %4s %s" % (i,c[0],c[1]))
    i += 1

# All done so close the connetion
connection.close()
