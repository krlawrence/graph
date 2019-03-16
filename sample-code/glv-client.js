// glv-client.js
//
// Example of using Gremlin from Node.js via a remote connection.
//
// This example assumes you have previously used NPM as follows to install Gremlin support:
//
//    npm install gremlin


// Import the Gremlin specific objects we might need to reference
const gremlin = require('gremlin');
const Graph = gremlin.structure.Graph;
const __ = gremlin.process.statics;
const { t: { id }, order,cardinality,column,scope,P } = gremlin.process;

// Note that if we just wanted to import 'decr' rather than all of order we could do:
// const { order: { decr } } = gremlin.process;

const hostname = 'localhost'
const port=8182

console.log("Creating connection");
wspath = `ws://${hostname}:${port}/gremlin`;

// NOTE: The empty object {} is to work around a bug in the 
//       Gremlin JavaScript 3.3.5 and 3.4 clients.
const DriverRemoteConnection = gremlin.driver.DriverRemoteConnection;
const connection = new DriverRemoteConnection(wspath,{});
const graph = new Graph();
console.log("Connecting to :" + wspath);
const g = graph.traversal().withRemote(connection);
console.log("Connection created");

// Run a few tests that use the objects we imported above
async function runTests() {
  try {
    const texas = await
      g.V().has('region','US-TX').
            values('city').
            order().
            toList();
      console.log("Airports in Texas");      
      console.log(texas);

    const runways = await
      g.V().has('region','US-TX').
            group().by('code').by(__.out().count()).
            order(scope.local).by(column.values,order.decr).
            toList();
      console.log("Route counts for airports in Texas");      
      console.log(runways);

    const most_runways = await
      g.V().has('runways',P.gt(5)).
            order().by('runways',order.decr).
            local(__.values('code','runways').fold()).toList();
      console.log("Airports with the most runways");      
      console.log(most_runways);
  } catch(e) {
      console.error(`Something went wrong:\n ${e}`);
  } finally {
      connection.close();
  }
}

(async function() {
  try {
    console.log("Application starting");
    await runTests();
    console.log("Tests complete");
    process.exit(0);
  } catch (e) {
    console.error(e);
    process.exit(1);
  }
}());




