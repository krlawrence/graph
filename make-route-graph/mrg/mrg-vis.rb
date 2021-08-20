#----------------------------------------------------------------------------------------
# mrg-vis.rb
#
# Methods that create an HTML file containing JavaScript and VisJs code that
# allows the currently selected graph (big or small) to be interacted with
# visually.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGVis
  def createHtmlWithVisJs()
    vwidth  = CANVAS_WIDTH
    vheight = CANVAS_HEIGHT
    nroutes = (@big ? ROUTES : ROUTES_SMALL)
    nports  = (@big ? AIRPORTS : AIRPORTS_SMALL) 
    
    # Generate the HTML header
    puts xmlComment("This is a machine generated file")
    puts xmlComment("Generated at #{VERSION_TIME} using '#{$PROGRAM_NAME}'")
    puts "<html>"
    puts "<head>"
    puts "<title>World air route map (graph)</title>"
    puts "<meta name='author' content='#{AUTHOR}' />"
    puts "<meta name='created' content='#{VERSION_DATE}' />"
    puts "<meta name='version' content='#{VERSION}' />"
    puts "<script type='text/javascript' src='http://kelvinlawrence.net/book/vis.js'></script>"
    puts "<link href='http://kelvinlawrence.net/book/vis.css' rel='stylesheet' type='text/css' />"
    puts 
    puts "<style type='text/css'>"
    puts "    #mynetwork {"
    puts "        width: #{vwidth}px;"
    puts "        height: #{vheight}px;"                                              
    puts "        border: 1px solid lightgray;"
    puts "    }"
    puts "</style>"
    puts "</head>"

    # HTML body
    puts "<body>"
    puts "<table rows='1' cols='2'>"
    puts "<tr>"
    puts "<td style='vertical-align:top'>"
    puts "<input type='text' id='search' maxlength='3' style='width:100%' onkeydown='check(event)'></input><br><br>"
    puts "<button onClick='doSearch()'>Search</button><br><br>"
    puts "<textarea rows='50' cols='20' id='txt1' readOnly='true'>"
    puts "AIR ROUTES v#{VERSION}\n\n"
    puts "#{VERSION_DATE}\n\n"
    puts "#{fmtsep(nports)} Airports"
    puts "#{fmtsep(nroutes)} Routes\n\n"
    puts "Click on an airport node to see route details here."
    puts "\nHover over a node or edge to see more details in a pop-up window."
    puts "\nZoom and pan as needed using mouse and scroll bars or control buttons below the layout."
    puts "\nNode labels will appear as you zoom in closer."
    puts "\nYou can search for an airport by entering a three character IATA code, like DFW or CDG, into the search area."
    puts "</textarea>"              
    puts "<td  style='vertical-align:top'>"
    puts "<div id='mynetwork'></div>"
    puts "</td></tr></table>"
    puts
    puts "<script type='text/javascript'>"   

    # Generate the JavaScript to render the Nodes and Edges in Visjs format.
    # The airports will be layed out using their LAT/LON coordinates within a
    # rectangle defined by vwidth and vheight.
    i = 0
    mx = vwidth/45.0    # X offset multiplier
    my = vheight/22.5   # Y offset multiplier
    xbase = vwidth/2    # X-axis midpoint
    ybase = vheight/2   # Y-axis midpoint

    puts "var nodes = new vis.DataSet(["

    # Generaate the airport nodes in VisJs format along with a tool tip (title) with
    # information about the airport and its route counts.
    AIRPORT_DATA.each do |n|
      lat = n[11].to_f
      lon = n[12].to_f
      x = xbase + (lon) * mx
      y = ybase + (-lat) * my
      title = "<b>#{n[1]} - #{n[3]} (#{n[2]}</b>)<br>#{n[4]}<br>#{COUNTRIES[n[9]][0]} (#{n[5]})"
      outcount, incount = calcAirportDegreeForId(n[0])
      title += "<br>Outgoing routes: #{outcount}"
      title += "<br>Incoming routes: #{incount}"
      title.gsub!("'","")
      print "{id: #{n[0]}, label: '#{n[1]}', title:\"#{title}\",x:#{x},y:#{y}}"

      i += 1
      if i < AIRPORT_DATA.size
        puts","
      else
        puts
      end
    end
    puts"])";

    # Generate the edges in Visjs format along with a tool tip (title) showing the
    # route distance.
    puts"var edges = new vis.DataSet(["
    i = 0
    ROUTE_DATA.each do |e|
      title = "'#{AIRPORT_DATA[e[0]-1][1]}-#{AIRPORT_DATA[e[1]-1][1]} #{e[2]} miles'" 
      print "{from: #{e[0]}, to: #{e[1]},title: #{title}}"
      i += 1
      if i < ROUTE_DATA.size
        puts","
      else
        puts
      end
    end
    puts"]);" 

    # Generate the JavaScript functions that handle user interaction
    puts"    // Setup the VisJs environment by creating a graph of nodes and edges"
    puts"    var container = document.getElementById('mynetwork');"
    puts"    var data = {"
    puts"        nodes: nodes,"
    puts"        edges: edges"
    puts"    };"
    puts
    puts"    // Set the options for how we want our network rendered and how we want it to behave"
    puts "var options = { nodes: {font:{vadjust:0},shadow:true,shape: 'circle', color:{background:'gold',border:'darkgreen'},scaling:{max: 200,min:100},borderWidth:1},"
    puts "                edges: {smooth:{enabled:true,type:'straightCross'},selectionWidth: function (width) {return width*4;},hoverWidth:function (width) {return width*4;},color: {color:'peru',highlight: 'red'}},"
    puts "          interaction: {hover:true, hoverConnectedEdges:true, navigationButtons:true,hideEdgesOnDrag:false},"
    puts "              physics: {enabled: false,adaptiveTimestep:true,stabilization: {enabled:true,iterations: 1}},};"
    puts
    puts "   // Create the VisJs network and register our event handlers"
    puts"    var network = new vis.Network(container, data, options);"   
    puts "    network.on('click', function (params) {"
    puts "        params.event = '[original event]';"
    puts "        current = params.nodes[0];"
    puts "        if (current != null)"
    puts "        {"
    puts "          updateStats(current);"
    puts "        }"
    puts "    })"
    puts
    puts "   // Search the graph for a node matching the entered IATA code"
    puts "   function doSearch()"
    puts "   {"
    puts "     s = document.getElementById('search').value.toUpperCase();"
    puts "     var grp = nodes.get({filter: function (item) {return (item.label == s);}});"
    puts
    puts "     if (grp.length > 0)" 
    puts "     {" 
    puts "       arr = [grp[0].id];"
    puts "       network.selectNodes(arr);"
    puts "       updateStats(grp[0].id);"
    puts "     }"
    puts "     else"
    puts "     {"
    puts "       t = document.getElementById('txt1');"
    puts "       t.value = 'Not found';"
    puts "     }"
    puts "   }"            
    puts 
    puts "  // Put details of each route from this node into the sidebar area"
    puts "  function updateStats(nodeid)"
    puts "  {"
    puts "     routes =  network.getConnectedEdges(''+nodeid);"
    puts "     str = '';"
    puts "     for (i=0; i<routes.length;i++)"
    puts "     {"
    puts "       str += edges.get(routes[i]).title + \"\\n\";"
    puts "     }"
    puts "     t = document.getElementById('txt1');"
    puts "     t.value = routes.length +\" route(s)\\n\\n\"+str.trim().split(\"\\n\").sort().join(\"\\n\");"
    puts "     n = nodes.get(nodeid);"
    puts "     s = document.getElementById('search');"
    puts "     s.value = n.label;"
    puts "  }"
    puts
    puts "  // If the user pressed the enter key in the search field, start the search"
    puts "  function check(e)"
    puts "  {"
    puts "    if (e.keyCode == 13)" 
    puts "    {"
    puts "      doSearch();"        
    puts "    }"
    puts "  }"
    puts

    # All done, close out the HTML file
    puts " </script>"
    puts " </body>"
    puts " </html>" 
  end
end

