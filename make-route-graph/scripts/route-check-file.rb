# ---------------------------------------------------------------------------------------
# route-check-file.rb
#
# Usage: route-check-file.rb <filename>
#
# A simple example of how to instantiate the MakeRouteGraph class and get the distance for
# an existing route or the calculated distance for a route not yet in the ROUTE_DATA
# table. This script first looks for a file passed in as an argument. If not found the
# hard coded list of routes will be used. See also 'route-check.rb' for a similar set of
# examples.
#
# The routes to check in the file should be of the form 'FRA-DXB', with one route per
# line. Running this script is useful when a set of potential new routes need
# to be checked to see if they already exist. If a route does not exist, the result from a
# call to 'displayDistance' will include an array style tuple that can be copied and
# pasted into the ROUTE_DATA table should the route need adding.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
# ---------------------------------------------------------------------------------------

require_relative '../mrg/mrg-core'

if ARGV[0] != nil
  fn = ARGV[0]
  if File.exist? fn
    routes = File.open(ARGV[0])
  else
    puts "No such file found: #{fn}"
    exit
  end
else
  routes =
    ['AUS-SFO',
     'AUS-SYD',
     'FRA-DXB',
     'CDG-DXB']
end

mrg = MakeRouteGraph.new(allAirports:true)

routes.each do |r|
  # Get the distance using an IATA route pair like "AUS-SFO"
  puts "\n#{r}"
  mrg.displayDistance(r)
end
