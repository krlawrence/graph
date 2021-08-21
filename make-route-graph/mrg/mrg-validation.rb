#----------------------------------------------------------------------------------------
# mrg-validation.rb
#
# Checks to validate that the airport and route data is correct.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGValidation
  # ---------------------------------------------------------------------------------------
  # Check the airport data for common errors. Optionally also validate the route data.
  #
  # As a best practice these checks should be run whenever any airport or route data is
  # modified. Remember to run the checks against the full data set not the tiny one using
  # the -all and -big options if run from mrg.rb
  # ---------------------------------------------------------------------------------------
  def runValidationChecks(fullChecks: false)
    bad = false
    # Check for some basic potential problem conditions with the airport properties.
    # A warning does not automatically mean the data is incorrect but a second verification
    # is encouraged for such cases. The current data will generate some warnings.
    # All reported errors should be rectified before generating any GraphML or other files
    # that represent the air-routes graph.
    puts "#########################"
    puts "### Checking airports ###"
    puts "#########################\n\n"
    i = 1
    warn = 0
    err = 0
    AIRPORT_DATA.each do |ap|
      valid = true
      if ap[1]  == '' then puts "***E: IATA code missing"; valid=false;err +=1;end
      if ap[2]  == '' then puts "***E: ICAO code missing"; valid=false;err +=1;end
      if ap[2].size != 4then puts "***W: ICAO code not four characters"; valid=false;warn +=1;end
      if ap[3]  == '' then puts "***E: City name missing"; valid=false;err +=1;end
      if ap[4]  == '' then puts "***E: Airport name missing"; valid=false;err +=1;end
      if !COUNTRIES.has_key?(ap[9]) then puts "***E: Country code not recognized (#{ap[9]})"; valid=false;err +=1;end
      if ap[5]  == '' then puts "***E: Region code missing"; valid=false;err +=1;end
      if ap[6]  == 0  then puts "***E: Runway count zero"; valid=false;err +=1;end
      if ap[6]  > 7  then puts "***W: Runway count seems high (#{ap[6]})"; valid=false;warn +=1;end
      if ap[7]  == 0  then puts "***E: Runway length zero"; valid=false;err +=1;end
      if ap[7]  < 1500  then puts "***W: Runway length seems short (< 1,500ft)"; valid=false;warn +=1;end
      if ap[7]  > 16500 then puts "***W: Runway length seems long (> 16,500ft)"; valid=false;warn +=1;end
      if ap[8]  == 0  then puts "***W: Elevation zero (could be OK)"; valid=false;warn +=1; end
      if ap[8]  > 13500 then puts "***W: Elevation seems high (> 13,500ft)"; valid=false;warn +=1;end
      if ap[8]  < -100 then puts "***W: Elevation seems low (< -100ft)"; valid=false;warn +=1;end
      if ap[9]  == '' then puts "***E: Country code missing"; valid=false;err +=1;end
      if ap[10] == '' then puts "***E: Continent code missing"; valid=false;err +=1;end
      if ap[11] == '' then puts "***E: Latitude missing"; valid=false;err +=1;end
      if ap[11].to_f > 90.0 or ap[11].to_f < -90.0 then puts "***E: Latitude invalid"; valid=false;err +=1;end
      if ap[12] == '' then puts "***E: Longitude missing"; valid=false;err +=1;end
      if ap[11].to_f > 180.0 or ap[12].to_f < -180.0 then puts "***E: Latitude invalid"; valid=false;err +=1;end
      if !valid
        bad = true
        puts "For entry [#{ap[0]},#{ap[1]},#{ap[2]},#{ap[3]},#{ap[4]},#{ap[5]},#{ap[6]},#{ap[7]},#{ap[8]},#{ap[9]},#{ap[10]},#{ap[11]},#{ap[12]}]\n\n"
      end 

      # Check for duplicate airport IDs. It is really important to catch these as if not fixed,
      # they will cause distance and route calculations to be wrong.
      if fullChecks
        i.upto(AIRPORT_DATA.size-1) do |ix|
          if AIRPORT_DATA[ix][0] == ap[0] or AIRPORT_DATA[ix][1] == ap[1]
            puts "***E: Duplicate airport ID or IATA code found [#{ap[0]},#{ap[1]}]\n\n"
            err +=1;
            bad=true
          end  
        end
        i += 1
      end
    end

    # Check the routes for zero distance or duplicate routes
    if fullChecks
      puts "###############################################"
      puts "### Checking routes (this may take a while) ###"
      puts "###############################################\n\n"
      i=1
      ROUTE_DATA.each do |rt|
        if rt[2] == 0 then puts "\n***E: Distance is zero for entry [#{rt[0]},#{rt[1]},#{rt[2]}]"
          bad=true
          err +=1;
        end 
          i.upto(ROUTE_DATA.size-1) do |ix|
            if ROUTE_DATA[ix][1] == rt[1] and ROUTE_DATA[ix][0] == rt[0]
              puts "***E: Duplicate route found for entry  [#{rt[0]},#{rt[1]},#{rt[2]}]"
              bad=true
              err +=1;
            end  
          end
        i += 1
        if i % 250 == 0 then print ".";end
      end
      puts
    end

    if !bad 
      puts "No potential issues were found"
    else
      puts "Error(s) : #{err}  Warning(s) : #{warn}"
    end
  end
end
