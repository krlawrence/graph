#----------------------------------------------------------------------------------------
# mrg-graphml.rb
#
# Methods that create the GraphML (XML) version of the air routes data.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGGraphMLBuilder
  
  # ---------------------------------------------------------------------------------------
  # Create the appropriate GraphML header for the version of the graph being generated.
  # 
  # The header includes quite a detailed description of the GraphML file's contents.
  # ---------------------------------------------------------------------------------------
  def printGraphMLHeader()
    puts "<?xml version='1.0' ?>"
    puts xmlComment("*" * 81)
    puts xmlComment("Sample file containing selected air routes between selected airports.")
    puts xmlComment("Intended for learning purposes only and not for travel planning!")
    puts xmlComment(" ")
    puts xmlComment("Author: #{AUTHOR}")
    puts xmlComment("Graph version : #{VERSION}   Date: #{VERSION_DATE}")
    puts xmlComment(" ")
    puts xmlComment("Please send details of errors in the graph or suggestions to:")
    puts xmlComment("e-mail: gfxman@yahoo.com , twitter: @gfxman")
    puts xmlComment(" ")
    puts xmlComment("PLEASE NOTE")
    puts xmlComment("The data in this graph is all available in the public domain but assembling it")
    puts xmlComment("all took considerable work. While I have tried to make the data as accurate")
    puts xmlComment("as possible I am sure errors remain. This data is intended as a learning aid ")
    puts xmlComment("only and not for any commercial use.")
    puts xmlComment(" ")
    puts xmlComment("Significant amounts of my own research has gone into into the creation of this")
    puts xmlComment("graph. Aviation has always been a hobby of mine and this has been an interesting")
    puts xmlComment("spare time project. I have tried to error check and curate the data, which was a")
    puts xmlComment("really big job, but there are almost certainly still errors in here. As time")
    puts xmlComment("allows, new routes and airports are added and this graph remains a work in")
    puts xmlComment("progress and is updated on a 'best can do' basis.")
    puts xmlComment(" ")
    puts xmlComment("Given that airlines are adjusting many routes on a weekly basis across the globe")
    puts xmlComment("a static graph such as this one will always be out of date the day it is")
    puts xmlComment("released! Thankfully new airports open less often and I hope I am more or less")
    puts xmlComment("including the majority of airports that have scheduled airline service.")
    puts xmlComment(" ")
    puts xmlComment("The data does not include many airports without commercial passenger service as")
    puts xmlComment("the focus of this project was on passenger routes. Likewise, the data does not")
    puts xmlComment("currently include seaplane bases or heliports even if they offer scheduled")
    puts xmlComment("passenger service. Airports that are classified as only supporting general")
    puts xmlComment("aviation (private planes) and cargo are not included in the graph. However,")
    puts xmlComment("you will find some airports with no flights. This is mainly caused by those")
    puts xmlComment("airports having had commercial service at some prior time and I decided")
    puts xmlComment("to leave them in the graph in case one day service resumes.")
    puts xmlComment(" ")
    puts xmlComment("This graph only models airports and routes. It does not attempt to model airlines")
    puts xmlComment("or route frequency. For example, the graph can tell you that there is a route")
    puts xmlComment("between LHR and JFK that at least one airline operates but not which airlines")
    puts xmlComment("fly that route nor how many times a day the route is operated. That is an")
    puts xmlComment("exercise for another day and for a bigger graph! The graph also does not")
    puts xmlComment("currently contain any aircraft information.  For the most part I have only")
    puts xmlComment("included scheduled flights flown by commercial airlines. I have included a few")
    puts xmlComment("unusual routes such as the flights from RAF Brize Norton to RAF Ascension Island")
    puts xmlComment("continuing on to Mount Pleasant in the Falkland Islands as I believe this")
    puts xmlComment("represents a significant route and is a sort of pseudo-scheduled flight. I do")
    puts xmlComment("not include routes flown only by freight carriers like FedEx and UPS. I also")
    puts xmlComment("have only mostly included airports with at least one route. There are a few")
    puts xmlComment("exceptions such as St. Helena which is a new airport with service pending, but")
    puts xmlComment("delayed, due to issues with wind shear. Where an airport has no flights but")
    puts xmlComment("remains in the graph it is probably because it was served by commercial airlines")
    puts xmlComment("at some point. It is also useful for people learning to search graphs to be able")
    puts xmlComment("to query for orphan nodes so for that reason as well I have left them in the")
    puts xmlComment("graph.")
    puts xmlComment(" ")
    puts xmlComment("All of this said, I believe, as a learning tool there is plenty in the graph to")
    puts xmlComment("facilitate writing some interesting queries and if you are so inclined for")
    puts xmlComment("producing nice visuals. I hope people have as much fun playing with the graph")
    puts xmlComment("as I have had putting it together.")
    puts xmlComment(" ")
    puts xmlComment("Route distances were calculated using a standard Haversine Great Circle formula")
    puts xmlComment("This file is built using Ruby scripts that process all the collected data that")
    puts xmlComment("I have amassed!")
    puts xmlComment(" ")
    puts xmlComment("The graph has a simple schema as you will see from the GraphML markup below.")
    puts xmlComment(" ")
    puts xmlComment("There are four basic node types:")
    puts xmlComment("1. Airport   - Contains properties like code (eg DFW), city, lat, lon etc.")
    puts xmlComment("2. Country   - A convenient way to find all airports in a specific country.")
    puts xmlComment("3. Continent - As above but for continents.")
    puts xmlComment("4. Version   - A single stand alone node used to version the graph.")
    puts xmlComment(" ")
    puts xmlComment("There are two edge types:")
    puts xmlComment("1. Route    - Represents link between two airports with the distance in Great")
    puts xmlComment("              Circle miles as a property.")
    puts xmlComment("2. Contains - Connects countries and continents with airports.")
    puts xmlComment(" ")
    if @big
      puts xmlComment("This version of the graph contains #{AIRPORTS} airports and #{ROUTES} routes.")
    else
      puts xmlComment("This version of the graph contains #{AIRPORTS_SMALL} airports and #{ROUTES_SMALL} routes.")
    end
    puts xmlComment(" ")
    puts xmlComment("*" * 81)
    puts "<graphml xmlns='http://graphml.graphdrawing.org/xmlns'>"

    if @gephi
      puts "  <key id='label' for='node' attr.name='label'  attr.type='string'></key>"
    end

    puts "  <key id='type'    for='node' attr.name='type'    attr.type='string'></key>"
    puts "  <key id='code'    for='node' attr.name='code'    attr.type='string'></key>"
    puts "  <key id='icao'    for='node' attr.name='icao'    attr.type='string'></key>"
    puts "  <key id='desc'    for='node' attr.name='desc'    attr.type='string'></key>"
    puts "  <key id='region'  for='node' attr.name='region'  attr.type='string'></key>"
    puts "  <key id='runways' for='node' attr.name='runways' attr.type='int'></key>"
    puts "  <key id='longest' for='node' attr.name='longest' attr.type='int'></key>"
    puts "  <key id='elev'    for='node' attr.name='elev'    attr.type='int'></key>"
    puts "  <key id='country' for='node' attr.name='country' attr.type='string'></key>"
    puts "  <key id='city'    for='node' attr.name='city'    attr.type='string'></key>"
    puts "  <key id='lat'     for='node' attr.name='lat'     attr.type='double'></key>"
    puts "  <key id='lon'     for='node' attr.name='lon'     attr.type='double'></key>"
    puts "  <key id='author'  for='node' attr.name='author'  attr.type='string'></key>"
    puts "  <key id='date'    for='node' attr.name='date'    attr.type='string'></key>"
    puts "  <key id='dist'    for='edge' attr.name='dist'    attr.type='int'></key>"

    if @tp3
      puts "  <key id='labelV'  for='node' attr.name='labelV'  attr.type='string'></key>"
      puts "  <key id='labelE'  for='edge' attr.name='labelE'  attr.type='string'></key>"
    end

    puts

    if @gephi
      puts "  <key attr.name='Edge Label' attr.type='string' for='edge' id='edgelabel'/>"
    end
    puts "  <graph id='routes' edgedefault='directed'>"
  end

  # ---------------------------------------------------------------------------------------
  # Close out the XML document
  # ---------------------------------------------------------------------------------------
  def printGraphMLFooter()
    puts "  </graph>"
    puts "</graphml>"
  end

  # ---------------------------------------------------------------------------------------
  # Generate the XML entries for the version and airport node types.
  # ---------------------------------------------------------------------------------------
  def generateGraphMLAirportNodes()
    # Construct a version node so that the graph version is part of the graph itself.
    puts"    <node id='0'>"
    if @gephi
      puts"      <data key='label'>VER</data>"
    end
    if @tp3
      puts"      <data key='labelV'>version</data>"
    end                                           
    puts"      <data key='type'>version</data>"
    puts"      <data key='code'>#{VERSION}</data>"
    puts"      <data key='date'>#{VERSION_TIME}</data>"
    puts"      <data key='author'>#{AUTHOR}</data>"
    puts"      <data key='desc'>Air Routes Data - Version: #{VERSION} Generated: #{VERSION_TIME}; Graph created by #{AUTHOR}; Please let me know of any errors you find in the graph or routes that should be added.</data>"
    puts"    </node>"  

    # Now construct the airport nodes.
    #
    # NOTE: the continent value from the airports data is not stored in the airport node 
    # but in a separate node that is created later.
    AIRPORT_DATA.each do |n|
      puts"    <node id='#{n[0]}'>"
      if @gephi
        puts"      <data key='label'>#{n[1]}</data>"
      end

      if @tp3
       puts"      <data key='labelV'>airport</data>"
      end

      puts"      <data key='type'>airport</data>"
      puts"      <data key='code'>#{n[1]}</data>"
      puts"      <data key='icao'>#{n[2]}</data>"
      puts"      <data key='city'>#{n[3]}</data>"
      puts"      <data key='desc'>#{n[4]}</data>"
      puts"      <data key='region'>#{n[5]}</data>"
      puts"      <data key='runways'>#{n[6]}</data>"
      puts"      <data key='longest'>#{n[7]}</data>"
      puts"      <data key='elev'>#{n[8]}</data>"
      puts"      <data key='country'>#{n[9]}</data>"
      puts"      <data key='lat'>#{n[11]}</data>"
      puts"      <data key='lon'>#{n[12]}</data>"
      puts"    </node>"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Generate the XML entries for the country node types.
  # ---------------------------------------------------------------------------------------
  def generateGraphMLCountryNodes()
    COUNTRIES.each do |k,v|
      puts"    <node id='#{v[1]}'>"
      puts"      <data key='type'>country</data>"
      puts"      <data key='code'>#{k}</data>"
      puts"      <data key='desc'>#{v[0]}</data>"
      if @gephi
        puts"      <data key='label'>#{k}</data>"
      end
      if @tp3
        puts"      <data key='labelV'>country</data>"
      end
      puts"    </node>"
    end
  end

  # ---------------------------------------------------------------------------------------
  # Generate the XML entries for the continent node types.
  # ---------------------------------------------------------------------------------------
  def generateGraphMLContinentNodes()
    CONTINENTS.each do |k,v|
      puts"    <node id='#{v[1]}'>"
      puts"      <data key='type'>continent</data>"
      puts"      <data key='code'>#{k}</data>"
      puts"      <data key='desc'>#{v[0]}</data>"
      if @gephi
        puts"      <data key='label'>#{k}</data>"
      end

      if @tp3
        puts"      <data key='labelV'>continent</data>"
      end
      puts"    </node>"  
    end
  end  

  # ---------------------------------------------------------------------------------------
  # Generate the XML entries for the route edges.
  # ---------------------------------------------------------------------------------------
  def generateGraphMLRouteEdges()
    id = CONTINENTS.values.last[1] + 1
    puts
    ROUTE_DATA.each do |e|
      if @tp3
        puts "    <edge id='#{id}' source='#{e[0]}' target='#{e[1]}'>"
        puts "      <data key='labelE'>route</data>"
      else
        puts "    <edge id='#{id}' source='#{e[0]}' target='#{e[1]}' label='route'>"
      end
      
      if @gephi
        puts "      <data key='edgelabel'>#{e[2]}</data>"
      end 

      puts "      <data key='dist'>#{e[2]}</data>"
      puts "    </edge>"
      id += 1
    end
    return id
  end

  # ---------------------------------------------------------------------------------------
  # Generate the XML entries for the country (contains) edges.
  # ---------------------------------------------------------------------------------------
  def generateGraphMLCountryEdges(id)
    tmp = ""
    begin
      AIRPORT_DATA.each do |ap|
        tmp = ap[9]
        if @tp3
          puts "    <edge id='#{id}' source='#{COUNTRIES[tmp][1]}' target='#{ap[0]}'>"
          puts "      <data key='labelE'>contains</data>"
        else
          puts "    <edge id='#{id}' source='#{COUNTRIES[tmp][1]}' target='#{ap[0]}' label='contains'>"
        end
        puts "    </edge>"
        id += 1
      end
    rescue
      $stderr.print  "***ERROR: A country code was not recognized: #{tmp}\n\n"
    end
    return id
  end

  # ---------------------------------------------------------------------------------------
  # Generate the XML entries for the continent (contains) edges.
  # ---------------------------------------------------------------------------------------
  def generateGraphMLContinentEdges(id)
    AIRPORT_DATA.each do |ap|
      if @tp3
        puts "    <edge id='#{id}' source='#{CONTINENTS[ap[10]][1]}' target='#{ap[0]}'>"
        puts "      <data key='labelE'>contains</data>"
      else
        puts "    <edge id='#{id}' source='#{CONTINENTS[ap[10]][1]}' target='#{ap[0]}' label='contains'>"
      end
        puts "    </edge>"
      id += 1
    end
    return id
  end
end
