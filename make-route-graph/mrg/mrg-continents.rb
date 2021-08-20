#----------------------------------------------------------------------------------------
# mrg-continents.rb
#
# Continent data.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGContinents

# ---------------------------------------------------------------------------------------
# Continent node data 
#
# The ID field is used during continent node and edge creation. The zero is a placeholder
# that will be updated by the MakeRouteGraph class as part of its initialization. This is
# done as the ID of each continent can vary depending upon the number of airports in the
# graph.
# ---------------------------------------------------------------------------------------
CONTINENTS = Hash["EU"=>["Europe",0],
                  "AF"=>["Africa",0],
                  "NA"=>["North America",0],                                                             
                  "SA"=>["South America",0],
                  "AS"=>["Asia",0],
                  "OC"=>["Oceania",0],
                  "AN"=>["Antarctica",0]]

end
