#----------------------------------------------------------------------------------------
# make-merge.rb
#
# Example of using the MakeRouteGraph class to create a list of maps that are
# compatible with the Gremlin mergeE() step added as part of the Apache TinkerPop 3.6.0
# release. For more details see the TINKERPOP-2681 Jira ticket.
#
# If the data this script creates is used as the source to a Groovy list of maps, then
# a query such as `g.inject(maps).unfold().mergeE()` can be used to "upsert"
# the data.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#
# LATER: Add steps to include the contains edges for continent and country nodes.
#----------------------------------------------------------------------------------------
require_relative '../mrg/mrg-core'
require_relative '../mrg/mrg-constants'

make = 5          # Number of routes to include, can be command line overridden.
big = true        # Use the full airport set
stringId = false  # Set to true if you prefer string ID values

if ARGV.size > 0
  make = ARGV[0].to_i

  if ARGV.size > 1
    stringId = ARGV[1] == 's' ? true : false
  end
end

mrg = MakeRouteGraph.new(allAirports:big)
total = mrg.getRouteCount()
routes = mrg.getRouteData(limit:total)

set = []
base = AIRPORT_DATA.size + COUNTRIES.size + CONTINENTS.size + 1

make = total unless make < total

make.times do |idx|
  id = stringId ? "'#{base + idx}'" : base + idx 
  to = stringId ? "'#{routes[idx][RTE_TO]}'" : routes[idx][RTE_TO]  
  from = stringId ? "'#{routes[idx][RTE_FROM]}'" : routes[idx][RTE_FROM] 
  dist = routes[idx][RTE_DIST]   
  
  if idx == 0
    str = '['
  else
    str = ''
  end

  str += "[(T.id):#{id},(T.label):'route',(OUT):#{from},(IN):#{to},'dist':#{dist}]"

  if idx < (make-1)
    str += ','
  else
    str += ']'
  end
  set << str
end
puts set
