#----------------------------------------------------------------------------------------
# mrg-utils.rb
#
# Utility methods that help with graph creation and analysis.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGUtils

  # ---------------------------------------------------------------------------------------
  # Take in a number like 12106 and return "12,106"
  # ---------------------------------------------------------------------------------------
  def fmtsep(n) 
    return n.to_s.reverse.gsub(/...(?=.)/,'\&,').reverse
  end   
  
  # ---------------------------------------------------------------------------------------
  # Format a string as an XML comment with a fixed width to help the comment blocks
  # appear neatly aligned. This is primarily used when creating the GraphML files but also
  # when creating HTML+VisJs files.
  # ---------------------------------------------------------------------------------------
  def xmlComment(c)
    s = "<!-- #{c}".ljust(XML_COMMENT_WIDTH - 4) + " -->"
  end

  # ---------------------------------------------------------------------------------------
  # Get the ID in the airports table for the given IATA code. A zero ID means not found.
  # ---------------------------------------------------------------------------------------
  def getAirportId(code)
    n = 0
    AIRPORT_DATA.each do |ap|
      if ap[APT_IATA] == code
        n = ap[0]
        break
      end
    end
    return n
  end
  
  # ---------------------------------------------------------------------------------------
  # Calculate the Haversine (great circle) distances between two lat/lon pairs.
  #
  # The Haversine Formula is discussed here: https://en.wikipedia.org/wiki/Haversine_formula
  #
  # For this computation PI = 3.1415926535  
  # ---------------------------------------------------------------------------------------
  RAD_PER_DEG = 0.017453293  # PI/180  
  RMILES = 3956              # Mean radius of the Earth as it varies by 
                             # 13 miles from the equator (3963) to the poles (3950)
  #RKM = 6367                # If you prefer distances in KM you can use this value for RMILES

  def haversineDistance( lat1, lon1, lat2, lon2 )  
        
    dlon = lon2 - lon1  # Longitude difference (delta)
    dlat = lat2 - lat1  # Latitude difference (delta)
     
    dlon_rad = dlon * RAD_PER_DEG  
    dlat_rad = dlat * RAD_PER_DEG  
    lat1_rad = lat1 * RAD_PER_DEG  
    lon1_rad = lon1 * RAD_PER_DEG
    lat2_rad = lat2 * RAD_PER_DEG  
    lon2_rad = lon2 * RAD_PER_DEG  
     
    a = Math.sin(dlat_rad/2)**2 + Math.cos(lat1_rad) * Math.cos(lat2_rad) * Math.sin(dlon_rad/2)**2  
    c = 2 * Math.asin( Math.sqrt(a))                                     
     
    dMi = RMILES * c  # delta between the two points in miles  

    return dMi
  end

  # ---------------------------------------------------------------------------------------
  # Calculate the haversine distance between two airports for the given IDs.
  #
  # This is the most efficient way to start the calculation as the IDs do not have to be
  # looked up first from an IATA code. If doing a lot of these calculations (e.g. in a
  # loop) this is the best method to use as the starting point.
  # ---------------------------------------------------------------------------------------
  def haversineDistanceById(from,to)
    if (1..AIRPORT_DATA.size).include? from and (1..AIRPORT_DATA.size).include? to
      lat1 = AIRPORT_DATA[from-1][APT_LAT].to_f
      lon1 = AIRPORT_DATA[from-1][APT_LON].to_f
      lat2 = AIRPORT_DATA[to-1][APT_LAT].to_f
      lon2 = AIRPORT_DATA[to-1][APT_LON].to_f
      return haversineDistance(lat1,lon1,lat2,lon2)
    else
      return -1
    end
  end

  # ---------------------------------------------------------------------------------------
  # Calculate the haversine distance between two airports using IATA codes.
  #
  # This entry point is more convenient in many cases where only the airport IATA code is
  # known, but is more expensive as it has to first scan the AIRPORT_DATA table looking
  # for the IDs which is, worst case, an additional O(n) lookup for each.
  # ---------------------------------------------------------------------------------------
  def haversineDistanceByCode(from,to)
    a = getAirportId(from)
    b = getAirportId(to)
    if a!=0 and b!=0
      lat1 = AIRPORT_DATA[a-1][APT_LAT].to_f
      lon1 = AIRPORT_DATA[a-1][APT_LON].to_f
      lat2 = AIRPORT_DATA[b-1][APT_LAT].to_f
      lon2 = AIRPORT_DATA[b-1][APT_LON].to_f
      return haversineDistance(lat1,lon1,lat2,lon2)
    else
      return -1
    end
  end

  # ---------------------------------------------------------------------------------------
  # Pretty print some information about the given airport. The value for 'a' is expected
  # to be a row from the AIRPORT_DATA table. If 'more' is true a nice tabular list is
  # printed. If 'more' is false, a single line, abbreviated form, is printed.
  # ---------------------------------------------------------------------------------------
  def printFormatted(a,more=true)
    if more
      puts "ID        : #{a[APT_ID]}"
      puts "IATA      : #{a[APT_IATA]}"
      puts "ICAO      : #{a[APT_ICAO]}"
      puts "City      : #{a[APT_CITY]}"
      puts "Desc      : #{a[APT_DESC]}"
      puts "Region    : #{a[APT_REG]}"
      puts "Runways   : #{a[APT_RWYS]}"
      puts "Longest   : #{fmtsep(a[APT_LONG])} (ft)"
      puts "Elevation : #{fmtsep(a[APT_ELEV])} (ft)"
      puts "Country   : #{a[APT_CTRY]}"
      puts "Continent : #{a[APT_CONT]}"
      puts "Latitude  : #{a[APT_LAT]}"
      puts "Longitude : #{a[APT_LON]}"
    else
      printf "%4s, %s, %s, %s, %s, %s, %s\n",a[0],a[1],a[2],a[4],a[3],a[5],a[9]
    end
  end

  # ---------------------------------------------------------------------------------------
  # Return the average route distance in the ROUTE_DATA being used.
  # ---------------------------------------------------------------------------------------
  def avgRoute()
    tot = 0.0
    ROUTE_DATA.each do |r|
      tot += r[2]
    end
    return tot/ROUTE_DATA.size
  end

  # ---------------------------------------------------------------------------------------
  # Return the average number of runways in the AIRPORT_DATA being used.
  # ---------------------------------------------------------------------------------------
  def avgRunways()
    tot = 0.0
    AIRPORT_DATA.each do |r|
      tot += r[APT_RWYS]
    end

    return tot/AIRPORT_DATA.size
  end

  # ---------------------------------------------------------------------------------------
  # Return the shortest route information for the ROUTE_DATA being used.
  # ---------------------------------------------------------------------------------------
  def shortestRoute()
    sh = 99999
    frm = 0
    to = 0
    ROUTE_DATA.each do |r|
      if r[RTE_DIST] < sh
        sh = r[RTE_DIST]
        frm = r[RTE_FROM]
        to = r[RTE_TO] 
      end
    end
    return to,frm,sh
  end

  # ---------------------------------------------------------------------------------------
  # Return the longest route information for the ROUTE_DATA being used.
  # ---------------------------------------------------------------------------------------
  def longestRoute()
    lg = 0
    frm = 0
    to = 0
    ROUTE_DATA.each do |r|
      if r[RTE_DIST] > lg
        lg = r[RTE_DIST]
        frm = r[RTE_FROM]
        to = r[RTE_TO] 
      end
    end
    return to,frm,lg
  end

  # ---------------------------------------------------------------------------------------
  # Return code and longest runway in the AIRPORT_DATA being used.
  # ---------------------------------------------------------------------------------------
  def longestRunway()
    rw = 0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LONG] > rw
        rw = a[APT_LONG]
        cod = a[APT_IATA]
      end
    end
    return cod,rw
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and shortest runway in the AIRPORT_DATA being used.
  # ---------------------------------------------------------------------------------------
  def shortestRunway()
    rw = 99999
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LONG] < rw
        rw = a[APT_LONG]
        cod = a[APT_IATA]
      end
    end
    return cod,rw
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and lowest elevetion in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def lowestElevation()
    lw = 99999
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_ELEV] < lw
        lw = a[APT_ELEV]
        cod = a[APT_IATA]
      end
    end
    return cod,lw
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and highest elevetion in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def highestElevation()
    hi = 0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_ELEV] > hi
        hi = a[APT_ELEV]
        cod = a[APT_IATA]
      end
    end
    return cod,hi
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and latitude of the furthest north in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def furthestNorth()
    lat = 0.0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LAT].to_f > lat
        lat = a[APT_LAT].to_f
        cod = a[APT_IATA]
      end
    end
    return cod,lat
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and latitude of the furthest south in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def furthestSouth()
    lat = 90.0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LAT].to_f < lat
        lat = a[APT_LAT].to_f
        cod = a[APT_IATA]
      end
    end
    return cod,lat
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and longitude of the furthest east in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def furthestEast()
    lon = -180.0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LON].to_f > lon
        lon = a[APT_LON].to_f
        cod = a[APT_IATA]
      end
    end
    return cod,lon
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and longitude of the furthest west in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def furthestWest()
    lon = 0.0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LON].to_f < lon
        lon = a[APT_LON].to_f
        cod = a[APT_IATA]
      end
    end
    return cod,lon
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and latitude of the closest to 0 in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def closestToEquator()
    lat = 90.0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LAT].to_f.abs < lat.abs
        lat = a[APT_LAT].to_f
        cod = a[APT_IATA]
      end
    end
    return cod,lat
  end

  # ---------------------------------------------------------------------------------------
  # Return the code and longitude of the closest to 0 in the AIRPORT_DATA being used
  # ---------------------------------------------------------------------------------------
  def closestToGreenwich()
    lon = 90.0
    cod = ""
    AIRPORT_DATA.each do |a|
      if a[APT_LON].to_f.abs < lon.abs
        lon = a[APT_LON].to_f
        cod = a[APT_IATA]
      end
    end
    return cod,lon
  end

  # ---------------------------------------------------------------------------------------
  #  Return the continent with the most airports and the count
  # ---------------------------------------------------------------------------------------
  def calcContinentDegree()
    cont = Hash[]
    maxv = 0
    maxc = ""
    AIRPORT_DATA.each do |a|
      if cont.has_key?(a[APT_CONT])
        cont[a[APT_CONT]] += 1
        if cont[a[APT_CONT]] > maxv
          maxv = cont[a[APT_CONT]]
          maxc = a[APT_CONT]
        end
      else
        cont[a[APT_CONT]] = 1
      end
    end
    return maxv, CONTINENTS[maxc][0]
  end

  # ---------------------------------------------------------------------------------------
  #  Return the region with the most airports and the count
  # ---------------------------------------------------------------------------------------
  def calcRegionDegree()
    regn = Hash[]
    maxv = 0
    id = 1
    AIRPORT_DATA.each do |a|
      if regn.has_key?(a[APT_REG])
        regn[a[APT_REG]] += 1
        if regn[a[APT_REG]] > maxv
          maxv = regn[a[APT_REG]]
          id = a[0]
        end
      else
        regn[a[APT_REG]] = 1
      end
    end
    return maxv, AIRPORT_DATA[id-1][APT_REG]
  end

  # ---------------------------------------------------------------------------------------
  #  Return the country with the most airports and the count
  # ---------------------------------------------------------------------------------------
  def calcCountryDegree()
    ctry = Hash[]
    maxv = 0
    maxc = ""
    AIRPORT_DATA.each do |a|
      if ctry.has_key?(a[9])
        ctry[a[9]] += 1
        if ctry[a[9]] > maxv
          maxv = ctry[a[9]]
          maxc = a[9]
        end
      else
        ctry[a[9]] = 1
      end
    end
    return maxv, COUNTRIES[maxc][0]
  end
  
  # ---------------------------------------------------------------------------------------
  # Calculate the outgoing and incoming route counts for a given airport ID.
  # ---------------------------------------------------------------------------------------
  def calcAirportDegreeForId(id)
    indeg = 0
    outdeg = 0

    ROUTE_DATA.each do |r|
      if r[0] == id
        outdeg += 1
      end
      if r[1] == id
        indeg += 1
      end  
    end
    return outdeg,indeg
  end

  # ---------------------------------------------------------------------------------------
  # Calculate the outgoing and incoming route counts for a given airport IATA code.
  # ---------------------------------------------------------------------------------------
  def calcAirportDegree(code='')
    id = getAirportId(code)
    return calcAirportDegreeForId(id)
  end

  # ---------------------------------------------------------------------------------------
  # Display a table of in and out route counts for each airport or just return the name(s)
  # of the airport(s) with the max degree.  In the latter case, the airport codes are
  # returned as a list containing one or more airports as it is possible to have airports
  # with the same degree value. This second mode is used during statistics computation.
  # When in "table" mode, annotations will also be included showing cases where the 
  # out-degree and in-degree differ. This is sometimes expected as not all air-routes
  # have parallel return routes, however, this can sometimes show up errors in the data
  # also and as such can be a useful data validation aid.
  # ---------------------------------------------------------------------------------------
  def calcAirportDegreeAll(table=true) 
    maxdeg = 0 
    apcode = []

    AIRPORT_DATA.each do |ap|
      id = ap[0]
      code = ap[APT_IATA]
      outdeg,indeg = calcAirportDegree(code)

      if table
        printf "%4i   %3s  (%3d)  out:%3d in:%3d",id,code,outdeg+indeg,outdeg,indeg
        if outdeg == 0 or indeg == 0
          print " <-- zero routes"
        elsif outdeg != indeg 
          print " <-- different (#{(outdeg-indeg).abs})"
        end
        puts
      end
      if outdeg+indeg > maxdeg
        maxdeg = outdeg+indeg
        apcode = [code]
      elsif outdeg+indeg == maxdeg
        apcode += [code]
      end
    end
    return maxdeg,apcode
  end
end
