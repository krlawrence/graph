// RemoteQueryPool
//                 
// Demonstrates how, using a managed thread pool, to concurrently send queries
// to a Gremlin Server. The queries sent have been selected to take different
// amounts of time to run. By looking at the output, you will be able to
// observe the interleaving of the threads as they handle queries of
// varying complexity. You will notice that the queries that target AGR, take
// longer to run than any other.
//
// This example does the following:
//   1. Creates an ExecutorService pool to manage worker threads.
//   2. Submits queries as quickly as possible from a list to the worker pool.
//   3. Produces formatted output that aids in understanding the lifecycle for
//      each query, and each worker in the thread pool.


// Common TinkerPop imports when sending queries as text strings.
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;  
import org.apache.tinkerpop.gremlin.driver.ResultSet;  
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;

// Java classes commonly used in TinkerPop focussed code.
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Thread;

/*
 * A class to wrap an Executor pool with methods that aid in sending
 * Gremlin queries to a remote server.
 */
class RemoteQueryPool
{
  private final ExecutorService pool;
  private Cluster cluster = null;
  private Client client = null;
  private AtomicInteger counter = new AtomicInteger(1);

  public RemoteQueryPool(int nThreads) 
  {
    log("Pool size set to " + nThreads);
    this.pool = Executors.newFixedThreadPool(nThreads);
  }

  /*
   * Internal method that is called by the public runQuery method as queries
   * are submitted to the execution worker pool.
   */

  private List<Result> submitQuery(String query)
  { 
    String q = "";
    try
    { 
      int c = counter.getAndIncrement();
      q = String.format(" %-17s %-6s  %s",Thread.currentThread().getName(),"["+c+"]",query);

      log("Submitting : " + q );
      final List<Result> result = client.submit(query).all().get();
      log("Completed  : " + q);
      return result;
    }
    catch(Exception e)
    {
      log("An exception was thrown while attempting to execute the query");
      log("" + e);
      log("Error from : " + q);
      return(null);
    }
  }
  
  /*
   * Queries to be executed are submitted to the execution pool via this method.
   * The query is packaged into a Callable form and the result of running the query
   * is returned as a Future.
   */
  public CompletableFuture<List<Result>> runQuery(String query)
  {
    if (client != null)
    {
      CompletableFuture<List<Result>> cf = new CompletableFuture<List<Result>>();
      pool.submit(() -> {cf.complete(submitQuery(query));});
      return cf;
    }
    else
    {
      log("A client must be created before a query can be submitted");
      return(null);
    }
  }

  /*
   * Provides a way to terminate the thread pool when no longer needed.
   */
  public void shutdown()
  {
    log("Executor pool is shutting down");
    pool.shutdown();
  }

  /* 
   * Convenience method to make the output a little prettier. Can be substituted
   * by a real logging interface as needed.
   */
  private void log(String text)
  {
    System.out.println("===> " + text);
  }

  /*
   * Create a connection to the server via a Client object.
   */
  public void createConnection(String host, int port)
  {
    Cluster.Builder builder = Cluster.build();
    log("Creating connection to " + host);

    builder.addContactPoint(host);
    builder.port(port);
    builder.enableSsl(true);
    builder.serializer(Serializers.GRAPHBINARY_V1D0);
    builder.maxContentLength(10*1000*1024);
    builder.minConnectionPoolSize(8);
    builder.maxConnectionPoolSize(64);
    builder.maxInProcessPerConnection(8);
    builder.minInProcessPerConnection(8);
    builder.maxSimultaneousUsagePerConnection(8);

    this.cluster = builder.create();
    this.client = cluster.connect();
  }

  /*
   * Attempt to close down the connection to the server.
   */
  public void closeConnection()
  {
    log("Connection closing");
    try {
      this.client.close();
      this.cluster.closeAsync();
    } 
    catch (Exception e)
    {
      log("Exception closing the connection:" + e);
    }
  }

  // ---------------------------------------------------------------------


  /* 
   * Program entry point.
   */
  public static void main(String[] args)
  {
    int poolSize = 4;
    boolean showResults = false;
    final String host = "krl-6-i1-cluster.cluster-cm9t6tfwbtsr.us-east-1.neptune.amazonaws.com";
    
    // Allow the number of workers to optionally be passed in from the command line. Any
    // second argument present on the command line will enable printing of the query results.
    if (args.length > 0)
    {
      poolSize = Integer.parseInt(args[0]);
    }
    if (args.length > 1)
    {
      showResults = true;
    }

    // A selection of queries to run.
    String [] queries = 
    {
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AGR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','LHR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','FRA')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','WLG')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','SIN')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','HKG')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AGR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','CDG')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AGR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','LGW')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','DUB')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','EDI')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','MCY')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','LAX')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AGR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','MSP')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','YYZ')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AKL')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','OGG')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','WLG')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AGR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AKL')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','BER')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','IST')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','RJK')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','GRU')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AGR')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','MEL')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','MCO')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','AKL')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','BCN')).path().by('code').limit(3).fold()",
      "g.V().has('code','AUS').repeat(out().simplePath()).until(has('code','BNE')).path().by('code').limit(3).fold()"
  };

    List<CompletableFuture<List<Result>>> results = new ArrayList<CompletableFuture<List<Result>>>();
    
    RemoteQueryPool rqp = new RemoteQueryPool(poolSize);
    rqp.createConnection(host,8182);
   
    long start = System.currentTimeMillis();
    
    // Submit each query to the execution pool as fast as possible.
    for (String q : queries)
    {
      results.add(rqp.runQuery(q));
    }

    // Block until all queries have completed, optionally display their results.
    try 
    {
      for( CompletableFuture<List<Result>> result : results )
      {
        if (showResults)
        {
          System.out.println(result.get());
        }
        else
        {
          result.get();
        }
      }
    }
    catch( Exception e)
    {
      System.out.println(e);
    }
    
    long stop = System.currentTimeMillis();
   
    // All done, shut down the pool, close the server connection and calculate how long the 
    // workload took to run.
    rqp.closeConnection();
    rqp.shutdown();
    System.out.println("++++ Total runtime was : " + (stop - start));
  }
}
    
