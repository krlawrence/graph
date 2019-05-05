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

# Helper method to pretty print some headings.
def heading(s):
    print("\n{}".format(s))
    for i in range(len(s)):
        print("-",end="")
    print("")    

# Cities in Texas with one or more airports.
texas = g.V().has('region','US-TX').\
              values('city').\
              dedup().\
              order().\
              toList()

heading("Cities with airports in Texas")      
for t in texas:
    print(t)

# Outgoing route counts for airports in Texas.
# Note the use of the anonymous traversal "__." as well as
# the Scope, Column and Order enums.
runways = g.V().has('region','US-TX').\
                group().by('code').\
                  by(__.out().count()).\
                order(Scope.local).\
                  by(Column.values,Order.decr).\
                next()

heading("Commercial route counts for airports in Texas")      
for k,v in runways.items():
    print(k,v)

# Find the top 10 airports at the highest elevation.
# Note the use of the Order enum
highest = g.V().hasLabel('airport').\
                order().by('elev',Order.decr).\
                limit(10).\
                project('iata','city','elev').\
                 by('code').\
                 by('city').\
                 by('elev').\
                 toList()

heading("Airports at the highest altitude")
for row in highest:
    print("{:3s} {:20s} Elevation {:6d}ft".format(row['iata'],row['city'],row['elev']))


# Shows how to use the Pop enum. Note the use of "as_" and "all_".
pop_test = g.V().has('code','SFO').as_('a').\
                 out().limit(1).as_('a').\
                 select(Pop.all_,'a').\
                 unfold().\
                 values('code').\
                 toList()

heading("Using pop.all on a list")
print(pop_test)

# Shows how to access a vertex label using the T enum.
label_test = g.V().has('code',P.within(['EU','SFO','NA','CUN'])).\
                   group().\
                     by(T.label).\
                     by('desc').\
                   next()\

heading("Grouping by labels")
for k,v in label_test.items():
    print(k,v)

# Shows how to explicitly access the values of a map
group = g.V().has('region','GB-ENG').\
              has('city', P.within('London','Manchester')).\
              group().\
                by('city').\
                by('code').\
              select(Column.values).\
              unfold().\
              toList()

heading("Selecting values from a group")
print(group)

# Airports with the most runways. Note the use of the P enum to
# access the "gte" predicate.
most_runways = g.V().has('runways',P.gte(5)).\
                     order().\
                       by('runways',Order.decr).\
                     local(__.values('code','runways').fold()).\
                     toList()

heading("Airports with the most runways")
for rows in most_runways:
    print(rows[0],rows[1])

# Shortest routes by distance from AUS to WLG.
# Note the use of the Operator enum.
routes = g.withSack(0).\
           V().\
           has('code','AUS').\
           repeat(__.outE().sack(Operator.sum).by('dist').\
                     inV().simplePath()).\
             until(__.has('code','WLG')).\
           limit(10).\
           order().\
             by(__.sack()).\
           local(__.union(__.path().by('code').by('dist'),__.sack()).fold()).\
           toList()

heading("Sack step tests")
for route in routes:
    print(route)

# All done so close the connetion
connection.close()
