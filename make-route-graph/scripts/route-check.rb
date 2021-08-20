# ---------------------------------------------------------------------------------------
# route-check.rb
#
# A simple example of how to instantiate the MakeRouteGraph class and get the distance for
# an existing route or the calculated distance for a route not yet in the ROUTE_DATA
# table. This example just reads from an array but could easily be changed to read from a
# file ('see route-check-file.rb'). This is useful when a set of potential new routes need
# to be checked to see if they already exist. If a route does not exist, the result from a
# call to 'displayDistance' will include an array style tuple that can be copied and
# pasted into the ROUTE_DATA table should the route need adding.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
# ---------------------------------------------------------------------------------------

require_relative '../mrg/mrg-core'

routes =
  [['AUS','SFO'],
   ['AUS','SYD'],
   ['CDG','DXB']]

mrg = MakeRouteGraph.new(allAirports:true)

routes.each do |r|
  puts
  
  # Get the distance using an IATA route pair like "AUS-SFO"
  puts "#{r[0]}-#{r[1]}"
  mrg.displayDistance("#{r[0]}-#{r[1]}")

  # Get the distance using ID values
  a = mrg.getAirportId(r[0])
  b = mrg.getAirportId(r[1])
  puts "#{r[0]}-#{r[1]} ==> #{mrg.getDistance(a,b)}"
  
  # Get the distance using IATA codes
  puts "#{r[0]}-#{r[1]} ==> #{mrg.haversineDistanceByCode(r[0],r[1])}"
end
