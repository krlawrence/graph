// RemoteTests.cs
//
// Simple examples of using C# and Gremlin .Net to connect to a remote endpoint.
//
// This example does the following: 
//   1. Configures a remote connection to a Gremlin Server endpoint.  
//   2. Run some sample Gremlin queries that demonstrate usage of the
//      Gremlin .Net client.
// 
// The Gremlin.Net client can be installed several ways. One way, using the "dotnet"
// command line tool: 
//
//        dotnet add package Gremlin.Net
//
//  NOTE (1): To keep things uncluttered, this sample application shows one example of
//  catching an Exception. There are other places where an exception could be thrown,
//  such as when adding a vertex and the ID is already in use. In production code any
//  places where an exception could be thrown should be handled appropriately.
//
//  NOTE (2): This sample creates a vertex using a string value for the ID. If the
//  graph database you are using does not support user provided, string IDs, you will
//  need to change it to something appropriate for your environment.

// Commonly needed Gremlin imports
using Gremlin.Net;
using Gremlin.Net.Driver;
using Gremlin.Net.Driver.Remote;
using Gremlin.Net.Process;
using Gremlin.Net.Process.Traversal;
using Gremlin.Net.Structure;
using Gremlin.Net.Process.Traversal.Strategy.Decoration;
using static Gremlin.Net.Process.Traversal.AnonymousTraversalSource;
using static Gremlin.Net.Process.Traversal.__;
using static Gremlin.Net.Process.Traversal.P;
using static Gremlin.Net.Process.Traversal.Order;
using static Gremlin.Net.Process.Traversal.Operator;
using static Gremlin.Net.Process.Traversal.Pop;
using static Gremlin.Net.Process.Traversal.Scope;
using static Gremlin.Net.Process.Traversal.TextP;
using static Gremlin.Net.Process.Traversal.Column;
using static Gremlin.Net.Process.Traversal.Direction;
using static Gremlin.Net.Process.Traversal.Cardinality;
using static Gremlin.Net.Process.Traversal.T;
using static Gremlin.Net.Structure.Graph;

using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace gremlinTests
{
  class RemoteTests
  {
    // Simple method to help print results returned as lists.
    static void PrintList<T>(IList<T> items)
    {
      foreach(T item in items)
      {
        Console.WriteLine($" {item}");
      }
    }

    static void PrintDict<K,V>(IDictionary<K,V> dict)
    {
      foreach (K key in dict.Keys)
      {
        Console.WriteLine($" {key} ==> {dict[key]}");
      }
    }

    // Run some tests
    static void Main(string[] args)
    {
      Console.WriteLine("\nStarting tests\n");
      
      GraphTraversalSource g = null;
      GremlinClient gremlinClient = null;

      // For testing, the default connection pool settings are fine.  To specify
      // custom values it is necessary to create a ConnectionPoolSettings object and
      // pass it to the GremlinClient upon creation.  The values used below are in
      // fact the default values but can be changed as needed.

      ConnectionPoolSettings cpSettings = new ConnectionPoolSettings();
      cpSettings.PoolSize = 4;
      cpSettings.MaxInProcessPerConnection = 32;
      cpSettings.ReconnectionAttempts = 4;
      cpSettings.ReconnectionBaseDelay = new TimeSpan((long)(1E9/100)) ;
      
      // Establish a connection to the server
      try
      {
        var gremlinServer = new GremlinServer("localhost", 8182, enableSsl: false);

        // It is only required to pass a value for connectionPoolSettings if you want
        // to override any of the default values. Other options, such as which
        // serializer and deserializer to use, can also be specified as parameters to
        // the GremlinClient. The parameter names are graphSONReader and
        // graphSONWriter. At time of writing the defaults specify GraphSON3Reader
        // and GraphSON3Writer and this code does not override those settings.
        
        gremlinClient = new GremlinClient(gremlinServer, connectionPoolSettings:cpSettings);

        var remoteConnection = new DriverRemoteConnection(gremlinClient, "g");
        g = Traversal().WithRemote(remoteConnection);
      }
      catch( Exception e)
      {
        Console.WriteLine($"Failed to create a connection:\n ==> {e.Message}");
        System.Environment.Exit(1);
      }

      //
      // If we were able to open a connection, start running some tests.
      //

      // Count some vertices as a simple test
      Console.WriteLine("\nVertex Count tests");
      Console.WriteLine("------------------");
      
      var ct = g.V().Limit<Vertex>(5000).Count().Next();
     
      Console.WriteLine($"The count was {ct}");

      // Retrieve a few values from vertices
      Console.WriteLine("\nValues step tests");
      Console.WriteLine("-----------------");
     
      var cities = 
        g.V().
          Has("airport","region","US-TX").
          Limit<Vertex>(10).
          Values<string>("city").
          ToList();

      PrintList<string>(cities);


      // Experiment with Path results
      Console.WriteLine("\nPath tests");
      Console.WriteLine("----------");
      
      var paths = 
        g.V().Has("code","AUS").
              Out("route").
              Path().
                By("code").
              Limit<Path>(5).
              ToList();
      
      PrintList<Path>(paths);

      Console.WriteLine();

      paths = 
        g.V().
          Has("code","AUS").
          OutE("route").
          InV().
          Path().
            By("code").
            By("dist").
          Limit<Path>(5).
          ToList();
      
      PrintList<Path>(paths);

      // Experiment with Repeat..Until
      Console.WriteLine("\nRepeat..Until tests");
      Console.WriteLine("-------------------");

      paths = 
        g.V().
          Has("code","AUS").
          Repeat(Out().SimplePath()).
          Until(Has("code","WLG")).
          Path().
            By("code").
          Limit<Path>(5).
          ToList();          
       
       PrintList<Path>(paths);

      // Experiment with a ValueMap step. This will generate a map result
      // represented as a .Net Dictionary.   
      Console.WriteLine("\nValueMap test");
      Console.WriteLine("-------------");
      
      var vmap =  
        g.V().
          Has("code","AUS").
          ValueMap<string,object>().
            By(Unfold<object[]>()).
          Next();

      PrintDict<string,object>(vmap);

      //
      // Experiment with a GroupCount step. This will also generate a map result
      // represented as a .Net Dictionary..
      //
      Console.WriteLine("\nUsing GroupCount to find label distribution");
      Console.WriteLine("-------------------------------------------");
      
      var labelMap = g.V().GroupCount<string>().By(T.Label).Next();
      
      PrintDict<string,long>(labelMap);
      //foreach (string key in labelMap.Keys)
      //{
      //  Console.WriteLine($" {key} ==> {labelMap[key]}");
      //}

      //Find routes with no corresponding return flight.
      Console.WriteLine("\nWhere, Not and As tests (finding flights with no corresponding return)");
      Console.WriteLine("----------------------------------------------------------------------");
     
      paths = 
        g.V().
          HasLabel("airport").As("a").
          Out("route").
          Where(Not(Out("route").As("a"))).
          Path().
            By("code").
          ToList();
      
      var found = paths.Count;
      Console.WriteLine($" Found {found} one-way only routes");

      if (found >= 10)
      {
        found = 10; 
      }
      Console.WriteLine($" Here are {found} of them");
      PrintList<Path>(paths.Take(found).ToList());

      // As the prior query but using a match step
      Console.WriteLine("\nPerforming the previous count using a Match step");
      Console.WriteLine("------------------------------------------------");
      var count = 
        g.V().
          HasLabel("airport").
          Match<Vertex>(__.As("a").Out("route").As("b"),
                        __.Not(__.As("b").Out("route").As("a"))).
          Count().
          Next();

      Console.WriteLine($"The count using a match step is {count}");

      // Instantiate a subgraph strategy and try counting vertices
      Console.WriteLine("\nWithStrategies tests");
      Console.WriteLine("--------------------");
      
      var g2 = g.WithStrategies(new SubgraphStrategy(vertices: Has("region","US-TX")));
      var  ct2 = g2.V().Count().Next();
      
      Console.WriteLine($" The count was {ct2}");

      // Instantiate a partition strategy and try another count
      string[] regions = {"US-TX"};
      
      var g3 = g.WithStrategies(new PartitionStrategy(
                   partitionKey: "region", readPartitions: regions)); 
      
      var  ct3 = g3.V().Count().Next();
      
      Console.WriteLine($" The count was {ct3}");


      // Create a vertex and some properties
      Console.WriteLine("\nAddV and Property tests");
      Console.WriteLine("-----------------------");
      var vertex =
        g.AddV("Dog").
            Property(T.Id,"dog-1").
            Property("name","Max").
            Property(Set,"likes","Toys").
            Property(Set,"likes","Digging holes").
            Property(Set,"likes","Long walks").
            Property(Set,"likes","Chasing squirrels").
          Id().
          Next();

      Console.WriteLine($" New vertex created with ID '{vertex}'");

      var max = g.V("dog-1").ValueMap<string,object[]>().Next();
     
      Console.WriteLine($" The dog with ID {vertex} is called {max["name"][0]}");
      Console.WriteLine(" Max likes:");
      
      foreach (string like in max["likes"])
      {
        Console.WriteLine($"   {like}");
      }


      // All done clean up
      Console.WriteLine("\nDeleting the vertex. Closing connection.");
      Console.WriteLine("----------------------------------------");
      g.V(vertex).Drop().Iterate();

      gremlinClient.Dispose();
      Console.WriteLine(" All done!");
    }
  }
}
