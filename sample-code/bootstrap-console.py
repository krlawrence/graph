# Configure the environment so the Python Console can connect to
# a Gremlin Server using gremlin-python and a web socket connection.
#
# To use this script start Python using:
#   python3 -i bootstrap-console.py

from gremlin_python import statics
from gremlin_python.structure.graph import Graph
from gremlin_python.process.graph_traversal import __
from gremlin_python.process.strategies import *
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection
from gremlin_python.process.traversal import * 
import os

# This script assumes that two environment variables have been defined
# containing the DNS name of the Gremlin Server and the port it is listening on.
# If the environment variables are not found, defaults will be used.
skey = "GREMLIN_SERVER_NAME"
pkey = "GREMLIN_SERVER_PORT"

server = os.environ[skey] if skey in os.environ else 'localhost'
port = os.environ[pkey] if pkey in os.environ else '8182'

endpoint = 'ws://' + server + ':' + port + '/gremlin'
print(endpoint)

graph=Graph()
connection = DriverRemoteConnection(endpoint,'g')
g = graph.traversal().withRemote(connection)
