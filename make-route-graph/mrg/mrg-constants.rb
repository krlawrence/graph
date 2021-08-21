#----------------------------------------------------------------------------------------
# mrg-constants.rb
#
# Constants used by the MakeRouteGraph application
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
module MRGConstants
  VERSION = "0.87" 
  VERSION_DATE = "2021-Aug-20" 
  VERSION_TIME = Time.new.utc 
  AUTHOR = "Kelvin R. Lawrence" 

  AIRPORTS = 3502       # Number of airports in the full graph 
  ROUTES =  50660       # Number of routes in the full graph 
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
    
  XML_COMMENT_WIDTH = 90 # Width of comment lines in generated XML files
end
