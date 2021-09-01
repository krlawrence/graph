#----------------------------------------------------------------------------------------
# mrg.rb
#
# This file provides the application entry point and argument parser. The code
# in this file is responsible for parameter validation, help text presentation
# and invoking the appropriate methods within the MakeRoutGraph core class.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------

require_relative 'mrg-core'
require_relative 'mrg-constants'
include MRGConstants

gephi      = false  # If set build Gephi compatible labels when generating GraphML
graphml    = false  # If set generate GraphML output
tp3        = true   # If set build GraphML for Tinkerpop 3 (the default).
tp2        = false  # If set build GraphML for Tinkerpop 2. (legacy mode not that useful anymore) 
big        = false  # If set generate the full graph with more routes and airports
apcode     = false  # If set do not build graph just produce a mapping table of airports and IDs
vis        = false  # If set generate Visjs output instead of GraphML
html       = false  # If set generate an HTML format table of the airports
help       = false  # If set display just help info
usage      = false  # If set display basic usage info
haversine  = false  # If set use the Haversine formula to emit Great Circles distances (miles)
hid        = false  # If set works like hv but output is more suitable for a CSV parser.
hcode      = false  # If set works like hx but output uses IATA codes and not ID numbers.
verbose    = false  # If set Enable verbose output. Useful for debugging. Only affects -hv currently
check      = false  # If set check the airport definitions for possible errors.                
all        = false  # If set do additional processing for -check and -stats
from       = false  # If set display all routes to the airport specified in param (forces bc to be on)
list       = false  # If set display a list of airport codes and descriptions
stats      = false  # If set display details about the number of airports and routes etc.
degree     = false  # If set display the in and out degree for a specific airport
degrees    = false  # If set display the in and out degree of each airport
country    = false  # If set display airports contained within the specified country
countries  = false  # If set display the list of countries
continents = false  # If set display the list of continents
iata       = false  # If set display details of the airport with the given IATA code
icao       = false  # If set display details of the airport with the given ICAO code
findid     = false  # If set display details of the airport with the given ID
region     = false  # If set display airports in the specified region
city       = false  # If set display airports with the given city name
distance   = false  # If set return distance for specified route.
gte        = false  # If set return routes greater or equal to specified value
lte        = false  # If set return routes greater or equal to specified value
gremlin    = false  # If set generate a Gremlin script that builds the graph.
farthest   = false  # If set display the farthest away airport from the given IATA code
apcount    = false  # If set return the current airport count.
rtcount    = false  # If set return the current route count.
param      = ""     # Country or code to be used when -country, -region, -to or -from is set

# ---------------------------------------------------------------------------------------
# Display some basic usage information for case where no options are specified
# ---------------------------------------------------------------------------------------
def displayUsage()
  puts
  puts "Usage: #{$PROGRAM_NAME} command [options|value]"
  puts "Use -? to get additional help."
  puts
end

def displayHelp()
  puts
  puts "MakeRouteGraph version: #{VERSION} , #{VERSION_DATE}"
  displayUsage()
  puts "Available commands and options:"
  puts
  puts "Graph Construction"
  puts "------------------"
  puts "  graphml        Generate GraphML"
  puts "  -tp3           Generate GraphML that is compatible with Tinkerpop 3 (the default)"
  puts "  -tp2           Generate GraphML that is compatible with Tinkerpop 2 (less useful these days)"
  puts "  -gephi         Produce additional GraphML labels compatible with Gephi."
  puts "  gremlin        Produce a script of Gremlin commands that can create the graph."
  puts "  list           Display a list of airport ID,IATA,ICAO,description,city,region and country."
  puts "                 If -all is also specified then all of the properties for each airport are listed"
  puts "                 in a form that can easily be parsed by CSV style processors."
  puts
  puts "Graph Selection"
  puts "---------------"
  puts "  -big           Build (or search) the bigger (full) worldwide route graph."
  puts "                 The default (tiny) graph just has #{AIRPORTS_SMALL} airports and #{ROUTES_SMALL} routes, all in the USA." 
  puts "                 The full graph contains #{AIRPORTS} airports and #{ROUTES} routes."
  puts
  puts "Graph Information"
  puts "-----------------"
  puts "  stats          Display stats about the number of airports, routes etc."
  puts "                 If -all is also specified additional graph analysis will be performed."
  puts
  puts "  apcount        Return the count of available airports. Use -big to count all airports."
  puts "  rtcount        Return the count of available routes. Use -big to count all routes."
  puts
  puts "  degree [iata]  Display the in and out degree (route count) for the given airport IATA code."
  puts "                 The in and out values may not always match as not all routes have return flights."
  puts "                 (-big will be assumed)"
  puts
  puts "  degrees        Display a list of the in and out degree (route count) for each airport."
  puts "                 The in and out values may not always match as not all routes have return flights."
  puts
  puts "  countries      Display a list of countries currently available in the graph."
  puts "                 If -all is specified also include the generated country ID."
  puts "  country [cry]  Display all airports in country 'cry' (must be the only option, -big will be assumed)"
  puts "                 example: country US. The country code is an ISO 3166-1 two letter code."
  puts "  continents     Display a list of the continent codes and names."
  puts "                 If -all is specified also include the generated continent ID."
  puts
  puts "  region [reg]   Display all airports in region 'reg' (-big will be assumed)"
  puts "                 Example: region US-TX. Region uses the ISO 3166-2 code."
  puts
  puts "  city [cty]     Display all airports in city 'cty' (-big will be assumed)"
  puts "                 Example: city London."
  puts
  puts "  iata [iata]    Display details about the aiport for the given IATA code (-big will be assumed)"
  puts "                 Example: iata LGW"
  puts "  icao [icao]    Display details about the aiport for the given ICAO code (-big will be assumed)"
  puts "                 Example: icao EGKK"
  puts "  id [id]        Display details about the aiport for the given ID (-big will be assumed)"
  puts "                 Example: id 50"
  puts "  apcode         Display a table of airport codes and IDs"
  puts
  puts "  far [iata]     Display information about the airport farthest away from the given IATA code."
  puts "                 The calculation is based on actual distance and not existing routes."
  puts "                 (-big will be assumed)."
  puts
  puts "Scope Modifiers"
  puts "----------------"
  puts "  -all           Do additional processing."
  puts "                 Currently affects: check,stats,list,hruby,countries and continents."
  puts "  -v             Enable verbose mode. Only affects hruby currently."
  puts
  puts "Graph Data Validation"
  puts "---------------------"
  puts "  check          Check the graph for possible errors in the airport definitions."
  puts "                 If -all is also used the routes (edges) as well as the airports"
  puts "                 (nodes) will be checked and a test for duplicate (invalid)"
  puts "                 airport nodes will also be run. It is recommended that a full"
  puts "                 check is always run before a graph is generated, especially if any"
  puts "                 data has been modified."
  puts
  puts "Route Analysis"
  puts "--------------"
  puts "  to [iata]      Display all routes to airport 'iata' (-big will be assumed)"
  puts "                 Example: to LHR"
  puts "  from [iata]    Display all routes from airport 'iata' (-big will be assumed)"
  puts "                 example: from LHR"
  puts "  dist [i1-i2]   Display the distance between two airports specified using the"
  puts "                 IATA codes with a hyphen between."
  puts "                 Exampe: dist AUS-JFK"
  puts "  gte [n]        Display all routes that are greater than or equal to 'n' in length."
  puts "                 Exampe: gte 8000 (-big will be assumed)."
  puts "  lte [n]        Display all routes that are less than or equal to 'n' in length."
  puts "                 Exampe: lte 500 (-big will be assumed)."
  puts "  hruby          Produce a Ruby style array of Great Circle distances calculated"
  puts "                 using the Haversine formula. If -all is also specified, add a comment"
  puts "                 showing the IATA code for each airport."
  puts "  hid            Like hruby but output is in a format that is better suited to CSV processing."
  puts "  hcode          Like hid but output uses IATA codes instead of ID values."
  puts
  puts "Visual Formats - viewable in a browser"
  puts "--------------------------------------"
  puts "  html           An HTML file is generated with the airport data in a table."
  puts "  vis            A Visjs Javascript/HTML file is generated instead of GraphML."
  puts "                 The HTML file draws an interactive map with the airports laid"
  puts "                 out according to LAN/LON coordinates"
  puts
  puts "Getting Help"
  puts "------------"
  puts "  -? or -help    Display this help."
  puts
  exit
end


# ---------------------------------------------------------------------------------------
# Process command line arguments.
#
# If no arguments are given just show the help.
#
# Some arguments need to be treated differently and must be the first argument
# provided. We handle those first. Lastly we handle the more generic (modifier style) 
# arguments.
#
# TODO: LATER: Use a custom built argument parser class.
# ---------------------------------------------------------------------------------------
command = ARGV[0]
if command == nil
  usage = true
elsif command =~ /^from$|^to$|^degree$|^country$|^iata$|^icao$|^region$|^dist$|^gte$|^lte$|^id$|^city$|^far$/
  if ARGV[1] == nil
    puts "A value must be specified: #{$PROGRAM_NAME} #{ARGV[0]} <value>"                                           
    exit
  else  
    big = true
    case command
      when "region" then region = true
      when "city" then city = true
      when "from" then from = true
      when "to" then to = true
      when "degree" then degree = true
      when "country" then country = true
      when "iata" then iata = true
      when "dist" then distance = true
      when "gte" then gte = true
      when "lte" then lte = true
      when "id" then findid = true
      when "far" then farthest = true
      else icao = true
    end
  end
  param = ARGV[1]
  if findid
    param = ARGV[1].to_i
  else  
    param = param.upcase unless ARGV[1] == "none"
  end
else
  ARGV.each do |p|
    case p
      when "graphml" then graphml = true
      when "-gephi" then gephi = true
      when "-tp3" then tp3 = true; tp2 = false
      when "-tp2" then tp2 = true; tp3 = false
      when "-big" then big = true
      when "apcode" then apcode = true
      when "vis" then vis = true
      when "html" then html = true
      when "hruby" then haversine = true
      when "hid" then haversine,hid = true,true
      when "hcode" then haversine,hcode = true,true
      when "-v" then verbose = true
      when "list" then list = true
      when "degrees" then degrees = true
      when "stats" then stats = true
      when "countries" then countries = true
      when "continents" then continents = true
      when "check" then check = true
      when "-all" then all = true
      when "gremlin" then gremlin = true
      when "apcount" then apcount = true
      when "rtcount" then rtcount = true
      when "-?","-help" then help = true
    end
  end
end

if usage
  displayUsage()
  exit
elsif help
  displayHelp()
  exit
end

mrg = MakeRouteGraph.new(allAirports:big,verbose:verbose,all:all,tp3:tp3,tp2:tp2,gephi:gephi)

case
  when stats
    mrg.displayGraphStatistics()
  when apcount
    puts(mrg.getAirportCount())
  when rtcount
    puts(mrg.getRouteCount())
  when country
    mrg.displayAirportsInCountry(param) 
  when countries
    mrg.displayCountries(all) 
  when continents
    mrg.displayContinents() 
  when city
    mrg.displayAirportsInCity(param)
  when from
    mrg.displayRoutesFrom(param)
  when to
    mrg.displayRoutesTo(param)
  when farthest
    mrg.displayFarthestFrom(param)
  when findid
    mrg.displayAirportWithId(param)
  when region
    mrg.displayAirportsInRegion(param)
  when distance
    mrg.displayDistance(param) 
  when (iata or icao)
    mrg.displayAirportWithCode(iata=iata, icao=icao, code:param)
  when gte
    mrg.displayConditionalRoutes(mode:DIST_GTE, dist:param.to_i)
  when lte
    mrg.displayConditionalRoutes(mode:DIST_LTE, dist:param.to_i)
  when haversine
    mrg.displayCalculatedDistances(hid:hid,hcode:hcode)
  when degree
    mrg.displayAirportDegree(param)
  when degrees
    mrg.displayAirportDegreeAll()
  when list
    mrg.displayAirportsList()
  when graphml
    mrg.generateGraphML()
  when apcode
    mrg.displayCodeAndId()
  when html
    mrg.generateHtml()
  when gremlin
    mrg.generateGremlin()
  when vis
    mrg.generateVis()
  when check
    mrg.runValidationChecks(fullChecks:all)
  else
    puts "Unrecognized command: #{command}"
end
