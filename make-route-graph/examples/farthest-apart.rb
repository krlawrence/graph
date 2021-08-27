#----------------------------------------------------------------------------------------
# farthest-apart.rb
#
# Example of using the MakeRouteGraph class to find the two airports,
# regardless of whether or not a route exists, that are farthest apart
# geographically.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
require_relative '../mrg/mrg-core'
require 'time'

CHECKPOINT = 50

big = false
verbose = false
summary = true

ARGV.each do |arg|
  case arg
    when "-v" then verbose = true
    when "-big" then big = true
    when "-nosum" then summary = false
  end
end

CHECKPOINT = ARGV[3].to_i

mrg = MakeRouteGraph.new(allAirports:big)

max = 0      # Maximum distance found
maxf = ""    # From airport for the current maximum
maxt = ""    # To airport for the current maximum
c = 1        # Tracks the next airport to compare with
col = 0      # Used to track column wrapping in verbose mode

t1 = Time.now()

# For each airport calculate the distance the every other airport and
# record the information if the distance is a new maxximum. By having
# 'c' track the first airport we need to compare the current airport
# to we avoid comparing any two airports twice.
AIRPORT_DATA.each do |a|
  c.upto AIRPORT_DATA.size()-1 do |x|
    from = a[APT_ID]
    to = AIRPORT_DATA[x][APT_ID]
    dist = mrg.haversineDistanceById(from,to)
    if dist > max
      max = dist
      maxf = from
      maxt = to
    end
  end

  # In verbose mode print periodic updates as the search progresses
  # showing the current farthest apart pair. The output also includes
  # the interval number and the time that interval took. As the search
  # approaches completion the time will decrease.
  if verbose
    if c % CHECKPOINT == 0 or c == 1 or c == AIRPORT_DATA.size
      t2 = Time.now
      d = t2 - t1
      t1 = Time.now
      snapshot = sprintf("%4d=%3.3f<%3s-%05d-%3s>  ",
                         c,
                         d.ceil(2),
                         AIRPORT_DATA[maxf-1][APT_IATA], 
                         max.ceil,
                         AIRPORT_DATA[maxt-1][APT_IATA])
      print snapshot
      col += 1
      if col == 4
        puts
        col = 0
      end
    end
  end
  c += 1
end

# Print a summary if required
if summary
  puts
  mrg.printFormatted(AIRPORT_DATA[maxf-1])
  puts("** ----- **")
  mrg.printFormatted(AIRPORT_DATA[maxt-1])
  from = AIRPORT_DATA[maxf-1][APT_IATA]
  to = AIRPORT_DATA[maxt-1][APT_IATA]
  puts("** ----- **")
  puts "#{from}-#{to} ==> #{max.ceil(2)} miles"
end
