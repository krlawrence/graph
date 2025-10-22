#----------------------------------------------------------------------------------------
# rev-codes.rb
# 
# Usage: rev-codes.rb filename
#
# Takes a file containing lines of the form LHR-JFK and reverses the codes to yield
# JFK-LHR etc. This is useful when perparing to add new routes which may or not exist and
# need checking, then possibly, creating. As most, but by no means all routes have return
# flights, when adding new routes I often make a list of the routes in one direction and
# then use this tool to quickly create the list of reverse direction routes. With all of
# the routes assembled into one file a tool such as 'route-check-file.rb' can be used to
# automatically generate the Ruby style '[from,to,distance],' array entries that can then
# be added to the ROUTE_DATA table quite quickly
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
file = ARGV[0]

if File.exist?(file)
  f = File.open(file,"r")

  f.each do |r|
    e = r.strip().split('-') 
    puts  "#{e[1]}-#{e[0]}"    
  end
else
    puts "file not found"
end



