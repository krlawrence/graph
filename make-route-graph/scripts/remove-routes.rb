
# ---------------------------------------------------------------------------------------
# remove-routes.rb
#
# Usage: remove-routes.rb <filename> <id>
#
# Sometimes it is necessary to remove all routes from or to a given airport from the
# ROUTE_DATA table. This does not happen very often but it can. This most often happens
# when an airport closes and all routes move to the new airport. As one example, this 
# happened when the new Berlin (BER) airport opened and the older TXL and SXF airports
# were closed. This script will remove any route for the given ID from the file of
# routes passed in. The file is typically of the form used in the ROUTE_DATA table. A
# typical workflow that can be used to remove routes is to copy the routes from
# ROUTE_DATA to a temporary file, run this script against the file and copy the remaining
# routes back to ROUTE_DATA.
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
  puts "A filename is required"
  exit
end

if ARGV[1] == nil
  puts "An airport ID is required"
else
  id = ARGV[1]
end

mrg = MakeRouteGraph.new(allAirports:true)

routes.each do |r|
  x = r.strip()
  if x.include?("[#{id},") or x.include?(",#{id},")
    next
  else
    puts r
  end
end
