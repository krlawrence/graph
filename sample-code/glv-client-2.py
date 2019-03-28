# glv-client-2.py
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


texas = g.V().has('region','US-TX').\
              values('city').\
              dedup().\
              order().\
              toList()

print("\nCities with airports in Texas\n");      
for t in texas:
    print(t)

highest = g.V().hasLabel('airport').\
                order().by('elev',Order.decr).\
                limit(10).\
                project('iata','city','elev').\
                 by('code').\
                 by('city').\
                 by('elev').\
                 toList()

print("\nAirports at the highest altitude\n")
for row in highest:
    print("{:3s} {:20s} Elevation {:6d}ft".format(row['iata'],row['city'],row['elev']))

# Shows how to use the Pop enum. Note the use of "as_"
pop_test = g.V().has('code','SFO').as_('a').\
                 out().limit(1).as_('a').\
                 select(Pop.all_,'a').\
                 unfold().\
                 values('code').\
                 toList()

print("\nUsing pop.all on a list\n");
print(pop_test);

label_test = g.V().has('code',P.within(['EU','SFO','NA','CUN'])).\
                   group().\
                     by(T.label).\
                     by('desc').\
                   next()\

print("\nGrouping by labels\n")
for k,v in label_test.items():
    print(k,v)


most_runways = g.V().has('runways',P.gte(5)).\
                     order().\
                       by('runways',Order.decr).\
                     local(__.values('code','runways').fold()).\
                     toList()

print("\nAirports with the most runways\n")      
for rows in most_runways:
    print(rows[0],rows[1])

# All done so close the connetion
connection.close()
