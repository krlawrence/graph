// A very basic gremlingo stub/skeleton that shows how to connect to a remote server.
package main

/*
 * This code assumes the mod file has been created using something
 * similar to:
 *
 * go mod init my-project
 * go get github.com/apache/tinkerpop/gremlin-go
 * go mod tidy
 *
 */

import (
	"fmt"
	"github.com/apache/tinkerpop/gremlin-go/driver"
	"log"
	"time"
)

// Server and port we want to connect to
const Host = "<put your host name here>"
const Port = 8182

// Define a few shortcuts to commonly used Gremlin constructs
const Id = gremlingo.Id
const GremlinErrors = gremlingo.Error

var Traversal = gremlingo.Traversal_

type GraphTraversalSource = gremlingo.GraphTraversalSource
type DriverRemoteConnection = gremlingo.DriverRemoteConnection
type DriverRemoteConnectionSettings = gremlingo.DriverRemoteConnectionSettings

//
// customSettings: We can optionally provide a call back function that will be called by
// NewDriverRemoteConnection to allow us to override any of the default settings.
//
func customSettings(settings *DriverRemoteConnectionSettings) {
	settings.LogVerbosity = GremlinErrors
	fmt.Println(settings)
}

//
// createConnection: Creates a connection to a remote endpoint and returns a
// GraphTraversalSource that can be used to submit Gremlin queries.
//
func createConnection(host string, port int) (*GraphTraversalSource, *DriverRemoteConnection, error) {
	var g *GraphTraversalSource
	var drc *DriverRemoteConnection
	var err error

	endpoint := fmt.Sprintf("wss://%s:%d/gremlin", host, port)
	log.Println("Attempting to connect to : " + endpoint)

	// Establish a new connection and catch any errors that may occur
	drc, err = gremlingo.NewDriverRemoteConnection(endpoint, customSettings)

	if err != nil {
		log.Fatalln(err)
	} else {
		g = Traversal().WithRemote(drc)
	}
	return g, drc, err
}

//
// runTests: Send a query to the server and process the result.
//
func runTests(g *GraphTraversalSource) {
	result, err := g.V().Count().Next()
	if err != nil {
		log.Println(err)
	} else {
		log.Println("Raw result object:", result)
		count, _ := result.GetInt64()
		log.Println("The count is :", count)
	}
}

//
// main: Program entry point. Create the connection, run some tests, shutdown.
//
func main() {
	// Create a graph traversal source object, after which we are ready to submit queries.
	g, drc, err := createConnection(Host, Port)

	if err != nil {
		log.Println("Error creating the connection - program terminating", err)
		return
	}

	// The connection will close when this function ends.
	defer drc.Close()

	start := time.Now()

	runTests(g)

	duration := time.Now().Sub(start)
	log.Println("Duration : ", duration)
}
