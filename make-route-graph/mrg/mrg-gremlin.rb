#----------------------------------------------------------------------------------------
# mrg-gremlin.rb
#
# Generate batched Gremlin steps that can create the air-route graph.
#
# The Gremlin generated assumes it will be loaded via a remotely connected Gremlin
# console. To change this just replace the ":>" prefix with an empty string ""
#
# There is another open source tool called csv-gremlin that can produce similar 
# batches of Gremlin steps from a CSV file. That tool currently has a lot more
# options. This module is intended as a simple way to turn the air-route data
# tables into Gremlin steps.
#
# All IDs are generated as strings containing integers. This can be changed using
# the 'STRING_ID' boolean to generate integer ID values.
#
# Currently the batch size is hard coded in the MRGConstants module
# TODO: LATER: Allow user specified batch sizes at runtime.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGGremlin
  STRING_ID = true

  def makeId(id)
    tid = id
    tid = "'#{id}'" unless not STRING_ID
    return tid
  end

  def createBatchedGremlinSteps(vbatch=VBATCH_SIZE,ebatch=EBATCH_SIZE)
    prefix = ":>"

    # Create a version vertex
    puts "println 'Vertex batch size=#{vbatch} Edge batch size =#{ebatch}';[]"
    puts "start = System.currentTimeMillis();[]"
    gstr = "#{prefix} g.addV('version').property(id,#{makeId(0)})"
    gstr += ".property('type','version')"
    gstr += ".property('code','#{VERSION}')"
    gstr += ".property('date','#{VERSION_TIME}')"
    gstr += ".property('author','#{AUTHOR}')"
    gstr += ".property('desc','Air Routes Data - Version: #{VERSION} Generated: #{VERSION_TIME}; Graph created by #{AUTHOR}; Please let me know of any errors you find in the graph')"
    puts gstr
    
    # Create the airport vertices
    lines = 0
    AIRPORT_DATA.each do |n|
        gstr = (lines %vbatch == 0) ? "#{prefix} g." : "" 
        gstr += "addV('airport').property(id,#{makeId(n[0])})"
        gstr += ".property('type','airport')"
        gstr += ".property('code','#{n[1]}')"
        gstr += ".property('icao','#{n[2]}')"
        gstr += ".property('city',\"#{n[3]}\")"
        gstr += ".property('desc',\"#{n[4]}\")"
        gstr += ".property('region','#{n[5]}')"
        gstr += ".property('runways',#{n[6]})"
        gstr += ".property('longest',#{n[7]})"
        gstr += ".property('elev',#{n[8]})"
        gstr += ".property('country','#{n[9]}')"
        gstr += ".property('lat',#{n[11]})"                       
        gstr += ".property('lon',#{n[12]})"
        print gstr
        lines += 1
        if lines % vbatch == 0
          puts
        else
          print "."
        end
    end
    
    # The next ID should be one more than the last airport ID
    id = AIRPORT_DATA[AIRPORT_DATA.length-1][0] + 1

    # Create the country vertices
    lines = 0
    COUNTRIES.each do |k,v|
      gstr = (lines %vbatch == 0) ? "#{prefix} g." : ""     
      gstr += "addV('country').property(id,#{makeId(v[1])})"
      gstr += ".property('code',\"#{k}\")"
      gstr += ".property('desc',\"#{v[0]}\")"
      print gstr
      lines += 1
      if lines % vbatch == 0
        puts
      else
        print "."
      end
    end
    
    # Create the continent vertices - only 7 so no point batching these.
    CONTINENTS.each do |k,v|
      gstr = "#{prefix} g.addV('continent').property(id,#{makeId(v[1])})"
      gstr += ".property('code',\"#{k}\")"
      gstr += ".property('desc',\"#{v[0]}\")"
      puts gstr
    end

    id = CONTINENTS.values.last[1]
    # Create the route edges
    lines = 0
    ROUTE_DATA.each do |e|
      gstr = (lines %ebatch == 0) ? "#{prefix} g." : "" 
      gstr += "addE('route').property(id,#{makeId(id)})"
      gstr += ".property('dist',#{e[2]}).from(V('#{e[0]}')).to(V('#{e[1]}'))"
      print gstr
      lines += 1
      if lines % ebatch == 0
        puts
      else
        print "."
      end
      id += 1
    end

    # Create the country (contains) edges
    tmp =""
    lines = 0
    begin
      AIRPORT_DATA.each do |ap|
        tmp = ap[9]
        gstr = (lines %ebatch == 0) ? "#{prefix} g." : "" 
        gstr += "addE('contains').property(id,#{makeId(id)})"
        gstr += ".from(V(#{makeId(COUNTRIES[ap[9]][1])})).to(V(#{makeId(ap[0])}))"
        print gstr
        lines += 1
        if lines % ebatch == 0
          puts
        else
          print "."
        end
        id += 1
      end
    rescue
      $stderr.print  "***ERROR: A country code was not recognized: #{tmp}\n\n"
    end

    # Create the continent (contains) edges
    lines = 0
    AIRPORT_DATA.each do |ap|
      gstr = (lines %ebatch == 0) ? "#{prefix} g." : "" 
      gstr += "addE('contains').property(id,#{makeId(id)})"
      gstr += ".from(V(#{makeId(CONTINENTS[ap[10]][1])})).to(V(#{makeId(ap[0])}))"
      print gstr
      lines += 1
      if lines % ebatch == 0
        puts
      else
        print "."
      end
      id += 1
    end
  end
end
