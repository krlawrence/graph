
# MakeRouteGraph - Core Ruby Classes and Modules

This folder contains the Ruby classes and modules that provide the foundation for the `air-routes` dataset. The `mrg.rb` file provides a stand alone application that can be used to build the `air-routes` graph and query the `air-routes` data. The `MakeRouteGraph` class, located in the `mrg-core.rb` file can be used to build custom Ruby applications that work with this data. The `air-routes` GraphML and CSV files, located in the `sample-data` folder are built using this code base. The `MakeRouteGraph` class includes methods to not only build the `air-routes` graph but also to query it, analyze it and validate it for correctness.

The application can be run using:

```
ruby mrg.rb command [option|value]
```

The list of available commands and options can be obtained using :

`ruby mrg.rb -?` or `ruby mrg.rb -help`



```
$ ruby mrg -?


MakeRouteGraph version: 0.88 , 2021-Sep-1

Usage: mrg.rb command [options|value]
Use -? to get additional help.

Available commands and options:

Graph Construction
------------------
  graphml        Generate GraphML
  -tp3           Generate GraphML that is compatible with Tinkerpop 3 (the default)
  -tp2           Generate GraphML that is compatible with Tinkerpop 2 (less useful these days)
  -gephi         Produce additional GraphML labels compatible with Gephi.
  gremlin        Produce a script of Gremlin commands that can create the graph.
  list           Display a list of airport ID,IATA,ICAO,description,city,region and country.
                 If -all is also specified then all of the properties for each airport are listed
                 in a form that can easily be parsed by CSV style processors.

Graph Selection
---------------
  -big           Build (or search) the bigger (full) worldwide route graph.
                 The default (tiny) graph just has 46 airports and 1390 routes, all in the USA.
                 The full graph contains 3503 airports and 50532 routes.

Graph Information
-----------------
  stats          Display stats about the number of airports, routes etc.
                 If -all is also specified additional graph analysis will be performed.

  apcount        Return the count of available airports. Use -big to count all airports.
  rtcount        Return the count of available routes. Use -big to count all routes.

  degree [iata]  Display the in and out degree (route count) for the given airport IATA code.
                 The in and out values may not always match as not all routes have return flights.
                 (-big will be assumed)

  degrees        Display a list of the in and out degree (route count) for each airport.
                 The in and out values may not always match as not all routes have return flights.

  countries      Display a list of countries currently available in the graph.
                 If -all is specified also include the generated country ID.
  country [cry]  Display all airports in country 'cry' (must be the only option, -big will be assumed)
                 example: country US. The country code is an ISO 3166-1 two letter code.
  continents     Display a list of the continent codes and names.
                 If -all is specified also include the generated continent ID.

  region [reg]   Display all airports in region 'reg' (-big will be assumed)
                 Example: region US-TX. Region uses the ISO 3166-2 code.

  city [cty]     Display all airports in city 'cty' (-big will be assumed)
                 Example: city London.

  iata [iata]    Display details about the aiport for the given IATA code (-big will be assumed)
                 Example: iata LGW
  icao [icao]    Display details about the aiport for the given ICAO code (-big will be assumed)
                 Example: icao EGKK
  id [id]        Display details about the aiport for the given ID (-big will be assumed)
                 Example: id 50
  apcode         Display a table of airport codes and IDs

  far [iata]     Display information about the airport farthest away from the given IATA code.
                 The calculation is based on actual distance and not existing routes.
                 (-big will be assumed).

Scope Modifiers
----------------
  -all           Do additional processing.
                 Currently affects: check,stats,list,hruby,countries and continents.
  -v             Enable verbose mode. Only affects hruby currently.

Graph Data Validation
---------------------
  check          Check the graph for possible errors in the airport definitions.
                 If -all is also used the routes (edges) as well as the airports
                 (vertices) will be checked and a test for duplicate (invalid)
                 airport vertices will also be run. It is recommended that a full
                 check is always run before a graph is generated, especially if any
                 data has been modified.

Route Analysis
--------------
  to [iata]      Display all routes to airport 'iata' (-big will be assumed)
                 Example: to LHR
  from [iata]    Display all routes from airport 'iata' (-big will be assumed)
                 example: from LHR
  dist [i1-i2]   Display the distance between two airports specified using the
                 IATA codes with a hyphen between.
                 Exampe: dist AUS-JFK
  gte [n]        Display all routes that are greater than or equal to 'n' in length.
                 Exampe: gte 8000 (-big will be assumed).
  lte [n]        Display all routes that are less than or equal to 'n' in length.
                 Exampe: lte 500 (-big will be assumed).
  hruby          Produce a Ruby style array of Great Circle distances calculated
                 using the Haversine formula. If -all is also specified, add a comment
                 showing the IATA code for each airport.
  hid            Like hruby but output is in a format that is better suited to CSV processing.
  hcode          Like hid but output uses IATA codes instead of ID values.

Visual Formats - viewable in a browser
--------------------------------------
  html           An HTML file is generated with the airport data in a table.
  vis            A Visjs Javascript/HTML file is generated instead of GraphML.
                 The HTML file draws an interactive map with the airports laid
                 out according to LAN/LON coordinates

Getting Help
------------
  -? or -help    Display this help.
```
