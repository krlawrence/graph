#----------------------------------------------------------------------------------------
# mrg-constants.rb
#
# Constants used by the MakeRouteGraph application
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGConstants
  VERSION = "1.0"
  VERSION_DATE = "2025-Oct-22"
  VERSION_TIME = Time.new.utc 
  AUTHOR = "Kelvin R. Lawrence" 

  AIRPORTS = 3504       # Number of airports in the full graph 
  ROUTES =  50637       # Number of routes in the full graph 
  AIRPORTS_SMALL =  46  # Number of airports in the small graph
  ROUTES_SMALL  = 1390  # Number of routes in the small graph 
  CANVAS_WIDTH  = 2000  # Used by VisJS layout (a 2:1 ratio between width and height works best) 
  CANVAS_HEIGHT = 1000  # Used by VisJS layout 
  # Use this canvas size if  you prefer not to have nearby airport nodes overlap
  # the downside is the map will require a lot more panning and zooming to use.  
  #CANVAS_WIDTH  = 12000 
  #CANVAS_HEIGHT = 6000 

  VBATCH_SIZE = 50      # Batch size to be used in 'gremlin' mode for vertices.  
  EBATCH_SIZE = 100     # Batch size to be used in 'gremlin' mode for edges.  

  # Mode flags for some distance methods
  DIST_GTE = 1
  DIST_LTE = 2

  # Columns (fields) in the AIRPORT_DATA table
  APT_ID   = 0  #  ID                                                                        
  APT_IATA = 1  #  IATA code                                                           
  APT_ICAO = 2  #  ICAO code                                                           
  APT_CITY = 3  #  City name
  APT_DESC = 4  #  Airport description,                                         
  APT_REG  = 5  #  Geographical region code (within a country such as US-TX)    
  APT_RWYS = 6  #  Number of runways                                            
  APT_LONG = 7  #  Longest paved runway in feet                                 
  APT_ELEV = 8  #  Elevation (AMSL) in feet                                     
  APT_CTRY = 9  #  ISO 3166 2 character country code                            
  APT_CONT = 10 #  Continent code                                               
  APT_LAT  = 11 #  Latitude                                                            
  APT_LON  = 12 #  Longitude                                                           
  
  # Columns (fields) in the ROUTE_DATA table
  RTE_FROM = 0  # ID of 'from' airport
  RTE_TO   = 1  # ID of 'to' airport
  RTE_DIST = 2  # Distance in miles

  # Columns (fields) in the values fields for the COUNTRIES and 
  # CONTINENTS hash tables.
  COT_NAME = 0  # Country/Continent name
  COT_ID   = 1  # Generated ID

  XML_COMMENT_WIDTH = 90 # Width of comment lines in generated XML files
end
