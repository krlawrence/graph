#----------------------------------------------------------------------------------------
# make-merge.rb
#
# Example of using the MakeRouteGraph class to create a list of maps that 
# can be used to create the airport nodes using a map injection pattern.
#
# If the data this script creates is used as the source to a Groovy list of maps, then
# a query such as the one below can be used to create the airport nodes.
#
#   g.inject(map).
#     unfold().as('m').
#     addV(select('label')).
#     property(T.id,select('id')).  
#     property('code',select('code')).  
#     property('icao',select('icao')).  
#     property('city',select('city')).
#     property('runways',select('runways')).  
#     property('region',select('region')).  
#     property('country',select('country')).  
#     property('continent',select('continent')).  
#     property('elev',select('elev')).  
#     property('desc',select('desc')).  
#     property('lat',select('lat')).  
#     property('lon',select('lon')).  
#     property('longest',select('longest'))  
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

  str += "['id':'#{id}','label':'airport','code':'#{code}','icao':'#{icao}','city':'#{city}','runways':#{runways},'region':'#{region}',"
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
