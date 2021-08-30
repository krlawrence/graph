# ---------------------------------------------------------------------------------------
# route-distances.rb
#
# Usage: route-check-file.rb <filename>
#
# Use this script to calculate the distance between one or more airport pairs. Each
# route's distance is calculated and no attempt is made to look for an existing route in
# the ROUTE_DATA table.
#
# The routes to in the file should be of the form 'FRA-DXB', with one route per line.
# Running this script is useful when a set of new routes need to be added. The script can
# return a couple of result formats. If "-array" is specified, the results will be in a
# form that is compatible with the ROUTE_DATA table.
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

array = false
if ARGV[1] == '-array'
  array = true
end

mrg = MakeRouteGraph.new(allAirports:true)

routes.each do |r|
  # Get the distance using an IATA route pair like "AUS-SFO"
  from = r[0..2]
  to = r[4..6]
  fromId = mrg.getAirportId(from)
  toId = mrg.getAirportId(to)

  d = mrg.haversineDistanceById(fromId,toId).ceil
  
  if array
    puts "[#{fromId},#{toId},#{d}],"
  else
    puts "#{r.chop} : #{d}"
  end
end
