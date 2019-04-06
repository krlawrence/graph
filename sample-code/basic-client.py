# basic-client.py
#
# Connect to a Gremlin Server using a client connection.
#
# Note that in this case the Gremlin queries are sent to  the server as text strings.
# To see an example of using Gremlin byte code instead see the glv-client.py example.
#
# This example code assumes that the GremlinPython library has been installed using:
#
#     pip install gremlinpython
#

# Import some classes we will need to talk to our graph
from gremlin_python.driver import client

# Path to our graph (this assumes a locally running Gremlin Server)
# Note how the path is a Web Socket (ws) connection.
client = client.Client('ws://localhost:8182/gremlin','g')

query = """
g.V().hasLabel('airport').
               sample(30).
               order().by('code').
               local(__.values('code','city').fold()).
               toList()
"""

result = client.submit(query)
future_results = result.all()
results = future_results.result()


client.close()

# Print the results in a tabular form with a row index
for i,c in enumerate(results,1):
    print("%3d %4s %s" % (i,c[0],c[1]))

