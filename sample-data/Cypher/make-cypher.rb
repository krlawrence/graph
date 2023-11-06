#----------------------------------------------------------------------------------------
# make-cypher.rb
#
# A small Ruby script that uses the mrg-core classes to generate a set of Cypher clauses
# that can be used to create the air-routes graph.
# 
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
require_relative '../../make-route-graph/mrg/mrg-core.rb'

numAirports = 9999        # Maximum number of aiports to include (there are less than this available).
lastAirportId = 0         # Used to track the ID of the last airport included.
useUppercaseLabels = true # If true, node labels will be camel cased and edge labels will be uppercase,
                          # otherwise all labels will be in lowercase.

# The number of airports to include can be overridden from the command line which is
# useful when wanting to create smaller test graphs. Any second parameter will
# turn off the use of uppercase labels.

if ARGV.size > 0
  numAirports = ARGV[0].to_i

  if ARGV.size > 1
    useUppercaseLabels = false
  end
end

nodeLabels = Hash(
  'airport'   => 'airport',
  'continent' => 'continent',
  'country'   => 'country',
  'version'   => 'version' )

edgeLabels = Hash(
  'route'     => 'route',
  'contains'  => 'contains' )

if useUppercaseLabels
  nodeLabels.each do |k,v|
    nodeLabels[k][0] = nodeLabels[k][0].upcase
  end

  edgeLabels.each do |k,v|
    edgeLabels[k].upcase!
  end
end

mrg = MakeRouteGraph.new(allAirports:true)

airports = mrg.getAirportData(limit:numAirports)

# We need to keep track of how many nodes and edges we create in order to generate,
# consistent and incremental IDs.
c = 1

#
# Build the CREATE clauses for the Airport nodes
#

puts "//Airports\n"

airports.each do |ap|
  id = ap[APT_ID]
  code = ap[APT_IATA]
  icao = ap[APT_ICAO]
  city = ap[APT_CITY]
  desc = ap[APT_DESC]
  region = ap[APT_REG]
  runways = ap[APT_RWYS]
  longest = ap[APT_LONG]
  elev = ap[APT_ELEV]
  country = ap[APT_CTRY]
  continent = ap[APT_CONT]
  lat = ap[APT_LAT]
  lon = ap[APT_LON]
  
  desc.gsub!("'","\\\\'")
  city.gsub!("'","\\\\'")

  str = "CREATE (a#{c}:#{nodeLabels['airport']} {id:'#{id}',code:'#{code}',icao:'#{icao}',city:'#{city}',"
  str << "desc:'#{desc}',region:'#{region}',runways:#{runways},longest:#{longest},elev:#{elev},"
  str << "country:'#{country}',continent:'#{continent}',lat:#{lat},lon:#{lon}"
  str << "})"
  c += 1
  puts str
end

lastAirportId = c - 1

#
# Build the CREATE clauses for the Country nodes
#
puts "\n//Countries\n"

countries = mrg.getCountryData()
countries.each do |k,v|
  code = k
  desc = v[COT_NAME]
  id = v[COT_ID]

  desc.gsub!("'","\\\\'")

  str = "CREATE (a#{c}:#{nodeLabels['country']} {id:'#{id}',code:'#{code}', desc:'#{desc}'})"
  c += 1
  puts str
end

#
# Build the CREATE clauses for the Continent nodes
#
puts "\n//Continents\n"
      
continents = mrg.getContinentData()
lastNodeId = 0
continents.each do |k,v|
  code = k
  desc = v[COT_NAME]
  id = v[COT_ID]

  str = "CREATE (a#{c}:#{nodeLabels['continent']} {id:'#{id}',code:'#{code}', desc:'#{desc}'})"
  c += 1
  puts str
  lastNodeId = id
end

#
# Build the CREATE clause for the Version node
#
      
puts "\n//Version\n"
str = "CREATE (v:#{nodeLabels['version']} {id:'0',code:'#{VERSION}',date:'#{VERSION_DATE}',author:'#{AUTHOR}',"
desc = "Air Routes Data - Version: #{VERSION} Generated: #{VERSION_TIME}"
str << "desc:'#{desc}'})"
puts str

#
# Build the CREATE clause for the route edges
#

puts "\n//Routes\n"

routes = mrg.getRouteData(limit:99999)
str =  "CREATE \n"
c = lastNodeId + 1

routes.each do |r| 
  from = r[RTE_FROM]
  to = r[RTE_TO]
  dist = r[RTE_DIST]
  
  if from <= lastAirportId and to <= lastAirportId
    str << "(a#{from})-[:#{edgeLabels['route']} {id: '#{c}', dist: #{dist}}]->(a#{to}),\n"
    c += 1
  end
end

str[-2] = ' '
puts str

#
# Build the CREATE clause for the contains edges for countries and continents
#
puts "\n//Country and Continent contains Airport\n"

str =  "CREATE \n"

airports.each do |a| 
  country = a[APT_CTRY]
  continent = a[APT_CONT]
  id = a[APT_ID]
  fromCtry = COUNTRIES[country][COT_ID] 
  fromCont = CONTINENTS[continent][COT_ID]
  
  if id <= lastAirportId
    str << "(a#{fromCtry})-[:#{edgeLabels['contains']} {id: '#{c}'}]->(a#{id}),\n"
    c += 1
    str << "(a#{fromCont})-[:#{edgeLabels['contains']} {id: '#{c}'}]->(a#{id}),\n"
    c += 1
  end
end

str[-2] = ' '
puts str            
