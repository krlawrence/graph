#----------------------------------------------------------------------------------------
# mrg-core.rb
#
# Core class central to the MakeRouteGraph (MRG) application (mrg.rb). All operations on
# the data should begin with calls to methods in this class. This class can also be
# instantiated and used independently by other applications.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
require_relative 'mrg-constants'
require_relative 'mrg-utils'
require_relative 'mrg-airports'
require_relative 'mrg-routes'
require_relative 'mrg-countries'
require_relative 'mrg-continents'
require_relative 'mrg-validation'
require_relative 'mrg-graphml'
require_relative 'mrg-html'
require_relative 'mrg-gremlin'
require_relative 'mrg-vis'
include MRGConstants
include MRGAirports
include MRGRoutes
include MRGCountries
include MRGContinents

class MakeRouteGraph
  include MRGUtils
  include MRGVis
  include MRGHtml
  include MRGValidation
  include MRGGraphMLBuilder
  include MRGGremlin
  attr_accessor :big, :gephi, :all, :verbose, :tp3, :tp2
  
  # ---------------------------------------------------------------------------------------
  # Configure instance variables and global data tables.
  # ---------------------------------------------------------------------------------------
  def initialize(allAirports:false, **args)
    @big = allAirports
    @gephi = (args.include?("gephi") ? args["gephi"] : false)
    @tp3 = (args.include?(:tp3) ? args[:tp3] : true)
    @tp2 = (args.include?(:tp2) ? args[:tp2] : false)
    @verbose = (args.include?(:verbose) ? args[:verbose] : false)
    @all = (args.include?(:all) ? args[:all] : false)
    
    # If needed, make sure all airports and routes are loaded.
    if @big
      EXTRA_AIRPORTS.each do |a|
        AIRPORT_DATA << a
      end

      EXTRA_ROUTES.each do |r|
        ROUTE_DATA << r
      end
    end
    
    # Update the COUNTRY and CONTINENTS tables with the appropriate ID values.  This is
    # necessary as the size of the AIRPORT_DATA table can vary (when @big is set or new
    # airports have been added) and these IDs are needed by methods that create edges
    # between airports and continents or countries. Similarly, edge IDs will be generated
    # starting one after the last continent ID.
    id = AIRPORT_DATA[AIRPORT_DATA.size - 1][0] + 1
    COUNTRIES.each do |k,v|
      COUNTRIES[k][1] = id
      id += 1
    end

    CONTINENTS.each do |k,v|
      CONTINENTS[k][1] = id
      id += 1
    end
  end


  # ---------------------------------------------------------------------------------------
  # Return the size of the currently loaded airport data.
  # ---------------------------------------------------------------------------------------
  def getAirportCount()
    return AIRPORT_DATA.size
  end
  
  # ---------------------------------------------------------------------------------------
  # Return the size of the currently loaded route data.
  # ---------------------------------------------------------------------------------------
  def getRouteCount()
    return ROUTE_DATA.size
  end

  # ---------------------------------------------------------------------------------------
  # Display the name and ISO code for each country in the graph.
  # ---------------------------------------------------------------------------------------
  def displayCountries()
    COUNTRIES.each do |k,v|
      printf "%2s,%s\n",k,v[0]
    end
  end

  # ---------------------------------------------------------------------------------------
  # Display any airports found for the given city name.
  # ---------------------------------------------------------------------------------------
  def displayAirportsInCity(city)
    puts "\nAirports in #{city}\n\n"
    ct = 0
    AIRPORT_DATA.each do |a|
      if a[3].upcase == city
        ct += 1
        printFormatted(a,false)
      end
    end
    printf "\n%3d airport(s)\n",ct
  end
  

  # ---------------------------------------------------------------------------------------
  # Display any routes found from the given IATA airport code.
  # ---------------------------------------------------------------------------------------
  def displayRoutesFrom(code)
    ct = 0
    n = getAirportId(code)
    if n != 0
      puts "\nRoutes from #{AIRPORT_DATA[n-1][APT_DESC]} (#{code})\n\n"
      ROUTE_DATA.each do |r|
        if r[0] == n
          ct += 1
          s = AIRPORT_DATA[r[0]-1][APT_IATA]
          d = AIRPORT_DATA[r[1]-1][APT_IATA]
          puts "#{s},#{d} [#{n},#{r[1]},#{r[2]}]"
        end
      end  
      printf "\n%3d route(s)\n",ct
    else
      puts "No airport found for IATA code: #{code}"
    end
  end  

  # ---------------------------------------------------------------------------------------
  # Display each airport code with its corresponding ID.
  # ---------------------------------------------------------------------------------------
  def displayCodeAndId()
    AIRPORT_DATA.each do |a|
      puts "#{a[APT_IATA]},#{a[APT_ID]}"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Display any routes found to the given IATA airport code.
  # ---------------------------------------------------------------------------------------
  def displayRoutesTo(code)
    n = getAirportId(code)
    ct = 0
    if n != 0
      ROUTE_DATA.each do |r|
        if r[1] == n
          ct += 1
          s = AIRPORT_DATA[r[0]-1][1]
          d = AIRPORT_DATA[r[1]-1][1]
          puts "#{s},#{d} [#{r[0]},#{n},#{r[2]}]"
        end
      end  
    end
    printf "\n%3d route(s)\n",ct
  end  
  
  # ---------------------------------------------------------------------------------------
  # Display information about the airport with a given ID.
  # ---------------------------------------------------------------------------------------
  def displayAirportWithId(id)
    if id > 0 and id <= AIRPORT_DATA.size
      printFormatted(AIRPORT_DATA[id-1],true)
    else 
      puts "\nSorry no airport found for ID #{id}"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Display information about the airport with the given IATA or ICAO code if present in
  # the airport data table.
  # ---------------------------------------------------------------------------------------
  def displayAirportWithCode(iata=true, icao=false, code:"")
    found = false
    idx = iata ? 1 : 2
    AIRPORT_DATA.each do |a| 
      if a[idx] == code
        printFormatted(a,true)
        found = true
        break
      end
    end  
    if !found 
      puts "\nSorry no airport found for code #{code}"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Display information about airporte within the given country. 
  # ---------------------------------------------------------------------------------------
  def displayAirportsInCountry(country)
    if !COUNTRIES.has_key?(country)
      puts "\nSorry no country found for code #{country}"
    else
      tmp = COUNTRIES[country][0]
      puts "\nAirports in #{tmp}\n\n"
      n = 0
      ct = 0
      AIRPORT_DATA.each do |a|
        if a[9] == country
        ct += 1
          printFormatted(a,false)
        end
      end
      printf "\n%3d airport(s)\n",ct
    end
  end

  # ---------------------------------------------------------------------------------------
  # Display information about any airports located within a given geographic region.
  # ---------------------------------------------------------------------------------------
  def displayAirportsInRegion(region)
    puts "\nAirports in #{region}\n\n"
    ct = 0
    AIRPORT_DATA.each do |a|
      if a[5] == region
        ct += 1
        printFormatted(a,false)
      end
    end
    printf "\n%3d airport(s)\n",ct
  end

  # ---------------------------------------------------------------------------------------
  # If a route exists between the two given ID values, return its distance.
  # ---------------------------------------------------------------------------------------
  def getDistance(a,b)
    d = 0
    ROUTE_DATA.each do |v|
      if v[0] == a and v[1] == b
        d = v[2]
        break
      end
    end  
    return d
  end

  # ---------------------------------------------------------------------------------------
  # Retrieve the length of a route between two airports. The airport pair should be of the
  # form AUS-SFO. Any character can be used instead of a "-" such as AUS:SFO. If either
  # of the airport codes is unrecognized an error will be displayed. If both airport codes
  # are recognized but no route currently exists, the Haversine distance for the potential
  # route will be calculated and returned.
  # ---------------------------------------------------------------------------------------
  def displayDistance(pair)
    from = pair[0..2]
    to = pair[4..6]
    a = getAirportId(from)
    b = getAirportId(to)
    d = 0
    if a !=0 and b !=0
      d = getDistance(a,b)
    else
      print "Sorry no such airport(s) : "
      if a == 0 then print from; end
      print " "
      if b == 0 then print to; end
      puts "  (#{from}-#{to})"
      return
    end
    
    if d != 0
      puts "#{fmtsep(d)} miles"
    else  
      puts "Sorry no existing route found between #{from} and #{to}"
      dist = haversineDistanceByCode(from,to)
      puts "If the route did exist it would be #{fmtsep(dist.round)} miles.\n"
      puts "==> [#{a},#{b},#{dist.round}],"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Display routes that are LTE or GTE the specified value and mode.
  # ---------------------------------------------------------------------------------------
  def displayConditionalRoutes(mode:DIST_GTE, dist:0)
    i = 0
    ROUTE_DATA.each do |r|
      if mode == DIST_GTE
        if r[2] >= dist
          printf "%3s->%3s %5s\n", AIRPORT_DATA[r[0]-1][APT_IATA],AIRPORT_DATA[r[1]-1][APT_IATA],fmtsep(r[2])
          i += 1
        end
      elsif mode == DIST_LTE
        if r[2] <= dist
          printf "%3s->%3s %5s\n", AIRPORT_DATA[r[0]-1][APT_IATA],AIRPORT_DATA[r[1]-1][APT_IATA],fmtsep(r[2])
          i += 1
        end
      end
    end
    puts "\n#{i} route(s)"
  end

  # ---------------------------------------------------------------------------------------
  # For each route display the airport pair and the distance in one of several forms.
  #
  # This method can also be used if the data contained in the mrg-routes.rb file needs
  # to be regenerated.
  # ---------------------------------------------------------------------------------------
  def displayCalculatedDistances(hid:false, hcode:false)
    ROUTE_DATA.each do |r|
      src = r[0]-1
      dest = r[1]-1
      lat1 = AIRPORT_DATA[src][APT_LAT].to_f
      lon1 = AIRPORT_DATA[src][APT_LON].to_f
      lat2 = AIRPORT_DATA[dest][APT_LAT].to_f
      lon2 = AIRPORT_DATA[dest][APT_LON].to_f
      dist = haversineDistance(lat1,lon1,lat2,lon2)
      if hcode      
        puts "#{AIRPORT_DATA[src][APT_IATA]},#{AIRPORT_DATA[dest][APT_IATA]},#{dist.round}"
      elsif hid
        puts "#{r[0]},#{r[1]},#{dist.round}"
      elsif @verbose
        puts "#{AIRPORT_DATA[src][1]} : (#{lat1},#{lon1}) #{AIRPORT_DATA[dest][1]} : (#{lat2},#{lon2}) ===> #{dist.round}"
      elsif @all
        puts "            [#{r[0]},#{r[1]},#{dist.round}], \##{AIRPORT_DATA[src][1]}-#{AIRPORT_DATA[dest][1]}"
      else
        puts "            [#{r[0]},#{r[1]},#{dist.round}],"
      end
    end
  end


  # ---------------------------------------------------------------------------------------
  # Generate a table of airports along with their incoming and outgoing degree (route
  # count).
  # ---------------------------------------------------------------------------------------
  def displayAirportDegreeAll()
    calcAirportDegreeAll(true)
  end
  
  # ---------------------------------------------------------------------------------------
  # Display the route degree for the given IATA aiport code.
  # ---------------------------------------------------------------------------------------
  def displayAirportDegree(code)
    id = getAirportId(code)
    if id != 0
      outdeg,indeg = calcAirportDegree(code)
      puts "Route conts for #{code} (#{AIRPORT_DATA[id-1][APT_DESC]})"
      puts "routes out: #{outdeg} , routes in: #{indeg}"
    else
      puts "Sorry no airport found with a code of #{code}"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Produce a list of airports in the graph along with a few of their properties. If @all
  # has been set, generate a CSV style output with semi-colon delimiters (as the data
  # contains meaningful commas).
  # ---------------------------------------------------------------------------------------
  def displayAirportsList()
    AIRPORT_DATA.each do |a|
      if @all
        puts "#{a[0]};#{a[1]};#{a[2]};#{a[4]};#{a[3]};#{a[5]};#{a[6]};#{a[7]};#{a[8]};#{a[9]};#{a[10]};#{a[11]};#{a[12]}"
      else
        printFormatted(a,false)
      end
    end  
  end

  # ---------------------------------------------------------------------------------------
  # Generate and display some interesting statistics about the graph.If @all is set, some
  # additional metrics will be calculated.
  # ---------------------------------------------------------------------------------------
  def displayGraphStatistics()
    aptSize = AIRPORT_DATA.size
    rouSize = ROUTE_DATA.size
    couSize = COUNTRIES.size
    conSize = CONTINENTS.size
    rsz = fmtsep(rouSize)
    nc = fmtsep(aptSize + couSize + conSize + 1)
    ec = fmtsep(aptSize * 2 + rouSize)
    puts "\nAir Routes Graph (v#{VERSION}, #{VERSION_DATE}) contains:"
    puts "  #{fmtsep(aptSize)} airports"
    puts "  #{rsz} routes"
    puts "  #{couSize} countries (and dependent areas)"
    puts "  #{conSize} continents"
    puts "  #{nc} total nodes"
    puts "  #{ec} total edges"

    if @all
      puts "\nAdditional observations:"
      f,t,mi = longestRoute()
      frm = AIRPORT_DATA[f-1][APT_IATA]
      to  = AIRPORT_DATA[t-1][APT_IATA]
      puts "  Longest route is between #{frm} and #{to} (#{fmtsep(mi)} miles)" 
      f,t,mi = shortestRoute()
      frm = AIRPORT_DATA[f-1][APT_IATA]
      to  = AIRPORT_DATA[t-1][APT_IATA]
      puts "  Shortest route is between #{frm} and #{to} (#{mi} miles)" 
      avg = avgRoute()
      a = avg.to_i
      b = avg.round(3).to_s.split('.')[1]
      puts "  Average route distance is #{fmtsep(a)}.#{b} miles." 
      cod,lng = longestRunway()
      puts "  Longest runway is #{fmtsep(lng)}ft (#{cod})" 
      cod,sht = shortestRunway()
      puts "  Shortest runway is #{fmtsep(sht)}ft (#{cod})" 
      avg = avgRunways()
      puts "  Average number of runways is #{avg.round(5)}"
      cod,lat = furthestNorth()
      puts "  Furthest North is #{cod} (latitude: #{lat})" 
      cod,lat = furthestSouth()
      puts "  Furthest South is #{cod} (latitude: #{lat})" 
      cod,lon = furthestEast()
      puts "  Furthest East is #{cod} (longitude: #{lon})" 
      cod,lon = furthestWest()
      puts "  Furthest West is #{cod} (longitude: #{lon})" 
      cod,lat = closestToEquator()
      puts "  Closest to the Equator is #{cod} (latitude: #{lat})" 
      cod,lon = closestToGreenwich()
      puts "  Closest to the Greenwich meridian is #{cod} (longitude: #{lon})" 
      cod,hi = highestElevation()
      puts "  Highest elevation is #{cod} (#{fmtsep(hi)} feet)" 
      cod,lw = lowestElevation()
      puts "  Lowest elevation is #{cod} (#{lw} feet)"
      deg,md = calcAirportDegreeAll(false)
      smd=""
      md.each do |tmp|
        smd +="#{tmp},"
      end
      puts "  Maximum airport node degree (routes in and out) is #{deg} (#{smd[0..-2]})"
      deg,nc = calcCountryDegree()
      puts "  Country with the most airports: #{nc} (#{deg})"
      deg,nc = calcContinentDegree()
      puts "  Continent with the most airports: #{nc} (#{deg})"
      avg = (2.0 * rouSize) / aptSize
      printf "  Average degree (airport nodes) is %3.3f\n", avg
      avg = ((2.0 * rouSize) + (2.0 * aptSize)) / (aptSize + couSize + conSize)
      printf "  Average degree (all nodes) is %3.3f\n", avg
    end
  end

  # ---------------------------------------------------------------------------------------
  # Generate the GraphML (XML) that represents the air routes graph of the required size.
  # 
  # Each generator method returns the next available ID so that the components of the
  # generated GraphML can be assured of having sequential IDs. It is done this way as the
  # number of airports can change as new ones are added or old ones are deleted and this
  # avoids having to maintain IDs in the ROUTE_DATA, COUNTRY and CONTINENT tables and
  # adjust them whenevr the airport count changes.
  # ---------------------------------------------------------------------------------------
  def generateGraphML()
    printGraphMLHeader()
    generateGraphMLAirportNodes()
    if @big
      generateGraphMLCountryNodes()
      generateGraphMLContinentNodes()
    end
    nextId = generateGraphMLRouteEdges()
    if @big
      nextId = generateGraphMLCountryEdges(nextId)
      nextId = generateGraphMLContinentEdges(nextId)
    end
    printGraphMLFooter()
  end

  # ---------------------------------------------------------------------------------------
  # Create an HTML table for each airport that includes all property values.
  # ---------------------------------------------------------------------------------------
  def generateHtml()
    createAirportHtmlTable()
  end

  # ---------------------------------------------------------------------------------------
  # Create an HTML table for each airport that includes all property values.
  # ---------------------------------------------------------------------------------------
  def generateGremlin()
    createBatchedGremlinSteps()
  end

  # ---------------------------------------------------------------------------------------
  # Create an HTML file containing VisJs JavaScript for each airport in the selected graph.
  # ---------------------------------------------------------------------------------------
  def generateVis()
    createHtmlWithVisJs()
  end
end
