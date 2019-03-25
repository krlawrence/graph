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
const { t,order,cardinality,column,scope,pop,operator,P } = gremlin.process;

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
      g.V().has('runways',P.gte(5)).
            order().by('runways',order.decr).
            local(__.values('code','runways').fold()).toList();
      console.log("Airports with the most runways");      
      console.log(most_runways);

    const highest = await
       g.V().hasLabel('airport').
             order().by('elev',order.decr).
             limit(10).
             project('iata','city','elev').
               by('code').
               by('city').
               by('elev').
               toList()
      console.log("Airports at the highest altitude");      
      console.log(highest);

      const pop_test = await
        g.V().has('code','SFO').as('a').
              out().limit(1).as('a').
              select(pop.all,'a').
              toList();
      console.log("Using pop.all on a list");
      console.log(pop_test);

      const label_test = await
        g.V().has('code',P.within(['EU','SFO','NA','MEX'])).
              group().by(t.label).by('desc').
              toList();
      console.log("Grouping by labels");
      console.log(label_test);
      
      const routes = await
        g.withSack(0).V().
              has('code','AUS').
              repeat(__.outE().sack(operator.sum).by('dist').inV().simplePath()).
                until(__.has('code','WLG')).
              limit(10).
              order().by(__.sack()).
              local(__.union(__.path().by('code').by('dist'),__.sack()).fold()).
              toList();
      console.log("\nSack step tests");
      for (let i=0; i<routes.length;i++) {
          console.log(routes[i][0].objects + ' Total: ' + routes[i][1])}

      const max_depth = await
        g.withSack(0).V().
              has('code','AUS').
              repeat(__.outE().sack(operator.sum).by('dist').inV().simplePath()).
                emit(__.has('code','WLG')).
                times(4).
              has('code','WLG').
              limit(10).
              order().by(__.sack()).
              local(__.union(__.path().by('code').by('dist'),__.sack()).fold()).
              toList();
      console.log("\nSack and emit step tests");
      for (let i=0; i<max_depth.length;i++) {
          console.log(max_depth[i][0].objects + ' Total: ' + max_depth[i][1])}     
  } catch(e) {
      console.error(`Something went wrong:\n ${e}`);
  } finally {
      console.log("Closing connection");
      await connection.close();
  }
}

(async function() {
  try {
    await runTests();
    console.log("\nTests complete");
    process.exit(0);
  } catch (e) {
    console.error(e);
    process.exit(1);
  }
}());


