#----------------------------------------------------------------------------------------
# mrg-history.rb
#
# A file to maintain the histroy comment block from the original V1 scripts
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
# MakeRouteGraph
#

# This project has evolved over may years. Throughout that time I have maintained
# this history as part of the code. Now that the code is significantly refactored I
# am keeping it here (in its own file) so that there is a record of how the data set
# and related tools have evolved over time.
#
# Among other things, MakeRouteGraph can create a GraphML file that suitable for use
# with Gremlin/TinkerPop enabled databases and tools like Gephi. The graph, which is
# only for learning purposes, contains information about selected routes between some
# of the major US airports.  The mileage data is approximate and may not match what
# an airline would give you in a frequent flyer program for flying that route!
#
#
# Author: Kelvin R. Lawrence     27th November-2013 - 26th August-2021
#
# History:
#   25-Nov-2013  -KRL-  Created it.                                                            v0.01
#    8-May-2014  -KRL-  Additional routes added                                                v0.02
#    4-Mar-2015  -KRL-  Added runway info. Added Tinkerpop 3 support (switch)                  v0.03
#   11-Mar-2015  -KRL-  Added more airports and routes. Added country info  and node type.
#                       placeholders to allow for future expansion of the graph.               v0.04 
#   29-Jun-2015  -KRL-  Added support for Latitude/Longitude of airports.                      v0.05 
#   24-Aug-2015  -KRL-  Added more airports. Fixed incorrect country code for Algeria.         v0.06
#    3-Nov-2015  -KRL-  Added more airports, Fiji and support for VisJs output.                v0.07
#    9-Nov-2015  -KRL-  Added separate city (municipality) property for airport nodes.         v0.08
#   11-Nov-2015  -KRL-  Added Haversine Great Circle capability. All routes now have lengths.  v0.09
#   12-Nov-2015  -KRL-  Added more airports/routes/countries.                                  v0.10
#   24-Nov-2015  -KRL-  Added more airports/countries/routes.                                  v0.11            
#    9-Dec-2015  -KRL-  Added all missing airports/routes/countries from LHR. Big update.      v0.20            
#   17-Feb-2016  -KRL-  Added more routes/airports from DFW. Added BOG/CO routes/country.      v0.21,0.22            
#   25-Feb-2016  -KRL-  Added more airports in MX,BZ,US and routes to/from them. Finished DFW  v0.23           
#   29-Feb-2016  -KRL-  Added more airports and routes. Added a versioning node.               v0.24
#    2-Mar-2016  -KRL-  Added more airports and routes. Fixed broken round trips RDU,SFO,ORD   v0.25
#    3-Mar-2016  -KRL-  Added more airports and routes.                                        v0.26
#    8-Mar-2016  -KRL-  Added lots more airports and routes including finished LCY routes.     v0.27
#   17-Mar-2016  -KRL-  Big update. Finished LGW routes. Added 7 countries, 53 airports.      
#                       Lots of missing routes added between existing airports.                v0.30
#   23-Mar-2016  -KRL-  Continued fixing missing routes between existing airports.             v0.31 
#                       Added all missing routes to/from AMS and the related route networks.   v0.32
#   25-Mar-2016  -KRL-  Added all missing routes to/from MIA and the related route networks.   v0.33,0.34
#    4-Apr-2016  -KRL-  Added more airports and routes. Added help text to the GraphML.        v0.35     
#   14-Apr-2016  -KRL-  Added more airports and routes. Fixed some errors. Added countries.    v0.36,0.37
#   27-Apr-2016  -KRL-  Added more airports and routes. Finished all ATL routes.               v0.38
#   24-May-2016  -KRL-  Big update. Airports and routes. Added -country,-iata,-icao,-region    v0.40,0.41
#   27-May-2016  -KRL-  Additional airports and routes added. Fixed zero route errors.         v0.42
#   27-Jun-2016  -KRL-  Big update. Over 1400 airports added. Added more stats.                v0.50
#   30-Jun-2016  -KRL-  Added some missing routes, deleted some dead routes.                   v0.51
#    7-Jul-2016  -KRL-  Fixed some errors in property data for a few airports.                 v0.52
#   27-Jul-2016  -KRL-  Updated the long names of a few airports.                              v0.53  
#   19-Aug-2016  -KRL-  Removed dead routes. Added some newly launched routes. Fixed errors.   
#                       Added several of the few airports that were still missing.             v0.54
#   23-Aug-2016  -KRL-  Added newly announced routes for this week. Added 4 airports.          
#                       Corrected 3-char ICAO codes to 4-char or 'none' instead.               v0.55
#   25-Aug-2016  -KRL-  Added a few more of the remaining airports.                            v0.56
#    6-Sep-2016  -KRL-  Added five airports and some newly launched routes.                    
#                       Added -gte and -lte options.                                           v0.57
#   13-Sep-2016  -KRL-  Added newly announced routes. Enhanced -dist and -h.                   v0.58
#   22-Sep-2016  -KRL-  Added newly announced routes and three airports.                       v0.59
#   24-Sep-2016  -KRL-  Added newly announced routes. Added -id option.                        v0.60
#    3-Oct-2016  -KRL-  Added newly announced routes and two airports.                         v0.61
#   10-Oct-2016  -KRL-  Added newly announced routes and one airport.                          v0.62
#   30-Oct-2016  -KRL-  Added routes. Updated FUE and CBT runway info.  Added USA airport.     v0.63
#   02-Nov-2016  -KRL-  Added newly announced routes.                                          v0.64
#   11-Nov-2016  -KRL-  Added newly announced routes. Added DAM                                v0.65
#   21-Nov-2016  -KRL-  Added 14 new route pairs (28 routes). ORD Runways 9-->8                v0.66
#   30-Nov-2016  -KRL-  Removed MSP<-->NRT (now MSP<-->HND). Added NAL, VDA and SZY.           v0.67
#                       Added 61 new route pairs (122 routes).
#   11-Dec-2016  -KRL-  Added Added several airports. TWB flights moved to WTB.                v0.68
#                       Added 375 route pairs (750 routes).
#   12-Dec-2016  -KRL-  Added 11 route pairs (22 routes)                                       v0.69
#   21-Dec-2016  -KRL-  Added 196 route pairs (392 routes). Added more airports in China.      v0.70
#   30-Dec-2016  -KRL-  Added 166 route pairs, 311 total routes. Deleted some dead routes.     v0.71
#                       Updated runway info for AUS & MED. All new VisJs algorithms.
#   18-Jan-2017  -KRL-  Added 161 route pairs (325 total routes).                              v0.72
#    1-Feb-2017  -KRL-  Added 35 route pairs (70 total routes).                                v0.73
#   15-Feb-2017  -KRL-  Added 27 route pairs (54 total routes). Added THG,TDK,XTO,CCL,TQP      v0.74
#                       Added search feature to generated HTML/VisJS app.                           
#   16-Jun-2017  -KRL-  Added 853 route pairs (1707 total routes). Made TP3 the default.       v0.75
#                       Added RDP,IFP,KXK,GMQ,GYU,SCV,VIT,IQM,KJH,HXD,AHJ,CWC,
#                             BZK,UZR,BBL,MOO,VTB
#                       Updated descriptions for DCA,TPA,FLL,CID,IAD,HKG and more.
#                       Added APT and REG constants to reduce duplication in airport list.
#                       Added -html option to produce table of airport data in HTML.
#                       Added -hx,hy options to output routes in a CSV friendly format.
#                       Changed LGB runways from 5 to 3.
#    7-Sep-2017  -KRL-  Added 391 route pairs (779 total routes).                              v0.76
#                       Added FIE,LWK,ANE,URO,LOV,OHS,FOA,PSV
#    6-Oct-2017  -KRL-  Added 64 route pairs (128 routes) Added NBZ airport.                   v0.77
#                       ** This version of the data was put into GitHub with the 
#                          first upload of the book.
#    3-May-2018  -KRL-  Added 942 route pairs (1895 routes)                 .                  v0.78
#                       Added WOL,ONG,ENI,IGT,GAY,MBX,YHG,EPA,JSK,KLF,CAT,BGC,PRM,
#                             VRL,VSF,CNQ,YAZ,OHD,MKZ,CHR and TLK.
#                       Added '-city' option.
#                       ** This version of the data was put into GitHub on May 7th 2018.
#   31-May-2018  -KRL-  Added 311 route pairs (475 total routes).                              v0.79
#                       Added GRS and HHQ.
#                       This version of the data was put into GitHub on May 31st 2018.
#   28-Sep-2018  -KRL-  Added 603 route pairs (1208 total routes).                             v0.80
#                       Added GME,CRV,NLI,EKA,CVQ,MJK, GOB,AWA,JRH,BYK,DTB and TKQ.
#                       ** This version of the data was put into GitHub on Sep 29th 2018.
#   24-Dec-2018  -KRL-  Added 305 route pairs (610 total routes).                              v0.81
#                       Added AEH,SRH,DSS,MQQ,FYT,ERH,DRT,BAR,USJ,TYL,CNN and JAE.
#                       ** This version of the data was put into GitHub on Dec 24th 2018.
#   27-Apr-2019  -KRL-  Added 554 route pairs (1112 total routes).                             v0.82
#                       Added INF,KLB,VDO,RMU,PAE,KUA,MRQ,SHI,BXG,UAR,TRA,ISL,SXZ,MJI,USQ and XSP.
#                             IST moved to LTFM from LSTA (now ISL)  on Apr-6th-2019
#                             Recalculated all Haversine distances.
#                             Changed ORD runways from 8 to 7 per latest charts.
#                             Added 'Kuala Lumpur' to city name for SZB/Subang. 
#                       Added '-gremlin' option.
#                       ** This version of the data was put into GitHub on Apr 27th 2019.
#   13-Oct-2019  -KRL-  Added 493 route pairs (994 total routes).                              v0.83
#                       Added ZTA,BFM,NUK,DBB,GGR,KJT,TSL,PKX,ZBX,DBC,KET,KFE,KHM,KTR,SQD,
#                              BPG,CQA,DOD,SEU,NRR,LNL,XAI,YYA,BQJ,DPT
#                       Removed (HPN-HYA [32,1084,182])
#                       ** This version of the data was put into GitHub on Oct 14th 2019.
#   29-Jan-2020  -KRL-  Added 176 route pairs (352 total routes).                              v0.84
#                       Added AKR,APK,BWB,CEH,CMK,CRC,EUA,GYG,GYZ,HFN,HPA,HZK,
#                             LIX,LRV,MYC,NFO,SFD,UMS,VAV,VCV,VUU,WZA,XWA,YEI,
#                             ZZU,YIA,NUL,KMV,KHT,SYS,AAA,GBI,KVO
#                       ** This version of the data was put into GitHub on Feb 1st 2020.
#    1-Feb-2020  -KRL-  Added 18 route pairs (36 total routes).                                v0.85
#                       Added FAC,RRR,NAU,PKP,FGU
#                       ** This version of the data was not put into GitHub. 
#   31-Jul-2021  -KRL-  Added 20 route pairs (40 total routes)                                 v0.86
#                       ** This version of the data was put into GitHub on Jul 31st 2021.
#   31-Aug-2021  -KRL-  Refactored the original Ruby scripts into better organized             v0.87
#                       classes and modules.
#                       Added BER/EDDB, changed the ICAO for SXF to 'none' to avoid conflict
#                       with BER but left in the dataset. Marked SXF and TXL as closed. Left 
#                       the region for SXF as DE-BR but used the new DE-BB for BER. Moved
#                       all TXL and SXF routes to BER and recomputed distances. This
#                       reduced the total routes in the graph from 50,660 to 50,532
#                       Added 2 route pairs (4 total routes) 
#                       ** This version of the data was put into GitHub on Aug 31st 2021.
