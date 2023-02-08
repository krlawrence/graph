#----------------------------------------------------------------------------------------
# make-merge.rb
#
# Example of using the MakeRouteGraph class to create a list of maps that are
# compatible with the Gremlin mergeV() step added as part of the Apache TinkerPop 3.6.0
# release. For more details see the TINKERPOP-2681 Jira ticket.
#
# If the data this script creates is used as the source to a Groovy list of maps, then
# a query such as `g.inject(maps).unfold().mergeV()` can be used to "upsert"
# the data.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#
# LATER: Use getAirportData() rather than explicit AIRPORT_DATA.
#        Add steps to include the version, continent, and country nodes.
#----------------------------------------------------------------------------------------
require_relative '../mrg/mrg-core'
require_relative '../mrg/mrg-constants'

make = 5          # Number of airports to include, can be command line overridden.
big = true        # Use the full airport set
stringId = false  # Set to true if you prefer string ID values, can be command line overridden.

if ARGV.size > 0
  make = ARGV[0].to_i

  if ARGV.size > 1
    stringId = ARGV[1] == 's' ? true : false
  end  
end

mrg = MakeRouteGraph.new(allAirports:big)

set = []
make.times do |idx|
  id = stringId ? "'#{AIRPORT_DATA[idx][APT_ID]}'" : AIRPORT_DATA[idx][APT_ID] 
  code = AIRPORT_DATA[idx][APT_IATA]
  icao = AIRPORT_DATA[idx][APT_ICAO]
  runways = AIRPORT_DATA[idx][APT_RWYS]
  longest = AIRPORT_DATA[idx][APT_LONG]
  elev = AIRPORT_DATA[idx][APT_ELEV]
  lat = AIRPORT_DATA[idx][APT_LAT]
  lon = AIRPORT_DATA[idx][APT_LON]
  continent =  AIRPORT_DATA[idx][APT_CONT]
  country = AIRPORT_DATA[idx][APT_CTRY] 
  region = AIRPORT_DATA[idx][APT_REG] 
  city = AIRPORT_DATA[idx][APT_CITY] 
  desc = AIRPORT_DATA[idx][APT_DESC]
  city.gsub!("'","\\\\'")
  desc.gsub!("'","\\\\'")
  
  if idx == 0
    str = '['
  else
    str = ''
  end

  str += "[(T.id):#{id},(T.label):'airport','code':'#{code}','icao':'#{icao}','city':'#{city}','runways':#{runways},'region':'#{region}',"
  str += "'country':'#{country}','continent':'#{continent}','elev':#{elev},'desc':'#{desc}','lat':#{lat},'lon':#{lon},"
  str += "'longest':#{longest}]"

  if idx < (make-1)
    str += ','
  else
    str += ']'
  end
  set << str
end
puts set
