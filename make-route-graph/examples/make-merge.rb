#----------------------------------------------------------------------------------------
# make-merge.rb
#
# Example of using the MakeRouteGraph class to create a list of maps that are
# compatible with the Gremlin mergeV() step added as par of the Apache TinkerPop 3.6.0
# release. For more details see the TINKERPOP-2681 Jira ticket.
#
# If the data this script creates is used as the source to a Groovy list of maps, then
# a query such as `g.inject(maps).unfold().merge(identity())` can be used to "upsert"
# the data.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
require_relative '../mrg/mrg-core'
require_relative '../mrg/mrg-constants'

make = 5
big = true

if ARGV.size > 0
  make = ARGV[0].to_i
end

mrg = MakeRouteGraph.new(allAirports:big)

set = []
make.times do |idx|
  id = AIRPORT_DATA[idx][APT_ID]
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
