#----------------------------------------------------------------------------------------
# farthest-from.rb
#
# Example of using the MakeRouteGraph class to find the airport that is farthest from
# the given one,regardless of whether or not a route exists between them.
#
# Note that helper methods are also available that can be used instead, namely
#   displayFarthestFrom
#   farthestAwayByCode
#
# but this example shows a simple implementation of the same functionality offered
# by those methods.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
require_relative '../mrg/mrg-core'

from = ARGV[0] 
if from == nil
  puts "An IATA code to start from is required"
  exit
end

big = false
summary = true

ARGV.each do |arg|
  case arg
    when "-big" then big = true
    when "-nosum" then summary = false
  end
end

mrg = MakeRouteGraph.new(allAirports:big)

max = 0      # Maximum distance found
maxt = ""    # To airport for the current maximum

fromId = mrg.getAirportId(from)

AIRPORT_DATA.each do |a|
  if a[APT_ID] == fromId
    next
  else
    toId = a[APT_ID]
    dist = mrg.haversineDistanceById(fromId,toId)
    if dist > max
      max = dist
      maxt = toId
    end
  end
end

# Print a summary if required
to = AIRPORT_DATA[maxt-1][APT_IATA]
if summary
  puts
  mrg.printFormatted(AIRPORT_DATA[fromId-1])
  puts("** ----- **")
  mrg.printFormatted(AIRPORT_DATA[maxt-1])
  puts("** ----- **")
end
puts "\n#{from}-#{to} ==> #{max.ceil(2)} miles"

