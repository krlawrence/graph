;[] // add-aircraft.groovy
;[] //
;[] // This script is intended to be run within the Gremlin Console.
;[] //
;[] // This script will try to read data from a CSV file (aircraft.csv) and add some 
;[] // aircraft information  to the graph.
;[] //
;[] // It is assumed, but not essential, that the air-routes graph data
;[] // has already been loaded. The first line of the CSV file contains the column
;[] // headers. This will be used as the names of the property keys. A vertex will be
;[] // created for each aircraft type found in the data. The script will
;[] // also create a vertex for each aircraft manufacturer found in the data and
;[] // create edges that connect the manufacturers to the planes they make. The main
;[] // Purpose of this script is to show a CSV file being used and to show queries
;[] // that also create vertices and edges without prior knowledge of exactly what
;[] // data will be loaded from the CSV data.
;[] //
;[] // The ";[]" notation is used to prevent unwanted output from the Gremlin
;[] // Console. 

;[] // Open the CSV file setup a reader
fis = new FileInputStream("aircraft.csv");[]

reader = new BufferedReader(new InputStreamReader(fis));[]

;[] // The first line of the file is assumed to be the column headers that 
;[] // will be used as property keys.
pkeys = reader.readLine().toLowerCase().split(",");[]

;[] // Create a vertex for each aircraft type found in the CSV data
reader.eachLine{line -> def values = line.split(",");
                g.addV("aircraft").
                  property(pkeys[0],values[0]).
                  property(pkeys[1],values[1]).
                  property(pkeys[2],values[2]).
                  property(pkeys[3],values[3]).iterate() }

;[] // Display the vertices that were just created
newverts = g.V().hasLabel("aircraft").valueMap(true).toList();[]
newverts.each{println it};[]

;[] // Create a "maker" vertex for each unique plane maker we find
g.V().hasLabel("aircraft").values("maker").dedup().as('a').
      addV('maker').property('name',select('a')).iterate();[]

;[] // Verify that our new vertices look good
newverts = g.V().hasLabel("maker").valueMap(true).toList();[]
newverts.each{println it};[] 

;[] // Add edges between the maker and the planes they make
g.V().hasLabel('maker').as('m').
  V().hasLabel('aircraft').as('a').
      where('m',eq('a')).by('name').by('maker').
      addE('makes').from('m').to('a').iterate();[]

;[] // Verify that our new edges look good
g.V().hasLabel('maker').
      order().by('name').
      outE('makes').inV().
      path().by('name').by(label).by('desc')

;[] // Review how the graph demographic looks now
g.V().groupCount().by(label)
g.E().groupCount().by(label)      
