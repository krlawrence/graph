#----------------------------------------------------------------------------------------
# route-distance.rb
#
# Given a route such as AUS-DFW-LHR-BLR, calculate the total distance and the distance
# of each individual hop. The code also checks for valid routes and non-existent airports.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
require_relative '../mrg/mrg-core.rb'

ERROR_NO_INPUT    = 0
ERROR_BAD_PATH    = 1
ERROR_BAD_AIRPORT = 2
ERROR_NO_ROUTE    = 3

EXAMPLE = "AUS-DFW-LHR"
ERROR_MESSAGES = [
  "Please enter a route such as #{EXAMPLE}",
  "Routes must only contain 3 digit airport codes separated by hyphens, such as #{EXAMPLE}",
  "Airport not found",
  "No route exists between this airport pair"]

def errorExit(reason, optional="")
  puts "#{ERROR_MESSAGES[reason]} #{optional}"
  exit
end

#
# Start working on the provided route
#
path = nil
path = ARGV[0].upcase unless ARGV[0] == nil  
if path == nil
  errorExit(ERROR_NO_INPUT)
end

#
# Make sure the route provided conforms to the expected pattern of AAA-BBB-CCC etc.
#
valid = (path =~ /^[aA-zZ]{3}(?=((-[aA-zZ]{3})+)$)/)

if valid == nil
  errorExit(ERROR_BAD_PATH)
end

#
# If we got this far we have a valid route format. However, it could still
# contain airport codes not in the data set.
#

mrg = MakeRouteGraph.new(allAirports:true)

puts "Processing route #{path}"

ports = path.split('-')

ids = []
ports.each do |code|
  id = mrg.getAirportId(code)
  if id == 0 
    errorExit(ERROR_BAD_AIRPORT, code) 
  end
  ids << id
end

#
# If we got this far we have a set of valid airports. Time to calculate the total
# distance. We may still encounter errors if a route pair does not have any direct
# flights connecting them together.
#
distances = []
i = 0
while i < ids.length-1
  distance = mrg.getDistance(ids[i],ids[i+1]) 
  pair = "#{ports[i]}-#{ports[i+1]}" 
  if distance == 0
    errorExit(ERROR_NO_ROUTE, pair)
  end
  puts "#{pair}= #{mrg.fmtsep(distance)}"
  distances << distance
  i += 1
end
total = distances.sum
puts "Total distance : #{mrg.fmtsep(total)}"
