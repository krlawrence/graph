Information about the Air Routes graph.  

Timestamp: Fri, 28 Sep 2018 19:23:38 -0500

This file contains the following sections
  1. Introduction
  2. Some statistics about the graph


1. INTRODUCTION 

The graph provides a model of the world commercial airline route network. It
remains a  work in progress but I have tried to curate the data carefully.
However,I do not doubt there are errors and omissions that remain. This is
intended  as a learning tool and not for actual travel planning purposes!  If
you spot any errors such as missing routes or routes that are no longer served,
please do let me know.

I have tried to verify that a connection between airports is only given if at
least one commercial airline currently operates scheduled service between them.
Given the dynamic nature of the airline industry where routes are added and
removed on a weekly basis any graph such as this one is, invariably, out of
date the day it is published. That said I have tried to keep the graph as up to
date as I can.  

This graph only models airports and routes it does not attempt to model airlines
or route frequency. For example, the graph can tell you that there is a route
between LHR and JFK that at least one airline operates but not which airlines
fly that route nor how many times a day the route is operated. That is an
exercise for another day and for a bigger graph! The graph also does not
currently contain any aircraft information.  For the most part I have only
included scheduled flights flown by commercial airlines. I have included a few
unusual routes such as the flights from RAF Brize Norton to RAF Ascension Island
continuing on to Mount Pleasant in the Falkland Islands as I believe this
represents a significant route and is a sort of pseudo-scheduled flight. I do
not include routes flown only by freight carriers like FedEx and UPS. I also
have only mostly included airports with at least one route. There are a few
exceptions such as St. Helena which is a new airport with service pending, but
delayed, due to issues with wind shear. Where an airport has no flights but
remains in the graph it is probably because it was served by commercial airlines
at some point. It is also useful for people learning to search graphs to be able
to query for orphan nodes so for that reason as well I have left them in the
graph.

All of this said, I believe, as a learning tool there is plenty in the graph to
facilitate writing some interesting queries and if you are so inclined for
producing nice visuals. I hope people have as much fun playing with the graph
as I have had putting it together.

Please do let me know of any mistakes you find or about airports and/or routes
that are currently missing. I believe I have most of the airports from around
the World that offer scheduled airline service included in the graph at this
point. The one exception is about ten regional airports in China that I
still need to add 
   
If you require more detailed information about the schema of the graph  and
it's overall demographic please see the comments at the top of the
air-routes.graphml file.
 

2. SOME STATISTICS ABOUT THE GRAPH

Air Routes Graph (v0.80, 2018-September-14th) contains:
  3,409 airports
  47,053 routes
  237 countries (and dependent areas)
  7 continents
  3,654 total nodes
  53,871 total edges

Additional observations:
  Longest route is between DOH and AKL (9,025 miles)
  Shortest route is between WRY and PPW (2 miles)
  Average route distance is 1,190.475 miles.
  Longest runway is 18,045ft (BPX)
  Shortest runway is 1,300ft (SAB)
  Average number of runways is 1.42798
  Furthest North is LYR (latitude: 78.2461013793945)
  Furthest South is USH (latitude: -54.8433)
  Furthest East is SVU (longitude: 179.341003418)
  Furthest West is TVU (longitude: -179.876998901)
  Closest to the Equator is MDK (latitude: 0.0226000007242)
  Closest to the Greenwich meridian is LDE (longitude: -0.006438999902457)
  Highest elevation is DCY (14,472 feet)
  Lowest elevation is GUW (-72 feet)
  Maximum airport node degree (routes in and out) is 594 (FRA)
  Country with the most airports: United States (580)
  Continent with the most airports: North America (981)
  Average degree (airport nodes) is 27.605
  Average degree (all nodes) is 27.628
 
 
Here are the top 50 airports with the most routes
 
    POS  ID   CODE  TOTAL     DETAILS

     1	  52   FRA  (594)  out:297 in:297
     2	  51   CDG  (568)  out:284 in:284
     3	  70   AMS  (555)  out:276 in:279
     4	 161   IST  (550)  out:275 in:275
     5	  80   MUC  (514)  out:257 in:257
     6	  18   ORD  (491)  out:246 in:245
     7	  64   PEK  (489)  out:244 in:245
     8	  58   DXB  (486)  out:243 in:243
     9	   1   ATL  (476)  out:238 in:238
    10	   8   DFW  (458)  out:229 in:229
    11	 102   DME  (446)  out:223 in:223
    12	  50   LGW  (430)  out:215 in:215
    13	  67   PVG  (412)  out:206 in:206
    14	  49   LHR  (412)  out:206 in:206
    15	  13   LAX  (408)  out:204 in:204
    16	  74   MAD  (404)  out:202 in:202
    17	  31   DEN  (403)  out:202 in:201
    18	  94   STN  (398)  out:199 in:199
    19	  84   MAN  (397)  out:199 in:198
    20	  73   BCN  (392)  out:196 in:196
    21	  11   IAH  (392)  out:196 in:196
    22	  68   FCO  (390)  out:195 in:195
    23	  12   JFK  (389)  out:195 in:194
    24	  47   YYZ  (384)  out:192 in:192
    25	  79   BRU  (380)  out:190 in:190
    26	  35   EWR  (378)  out:189 in:189
    27	 198   DUS  (368)  out:184 in:184
    28	  16   MIA  (360)  out:180 in:180
    29	 106   DOH  (356)  out:178 in:178
    30	  75   VIE  (352)  out:176 in:176
    31	 178   CLT  (346)  out:173 in:173
    32	  60   DUB  (346)  out:173 in:173
    33	  76   ZRH  (342)  out:171 in:171
    34	 250   CAN  (336)  out:169 in:167
    35	 103   SVO  (330)  out:165 in:165
    36	  93   ARN  (326)  out:163 in:163
    37	  61   HKG  (326)  out:163 in:163
    38	 177   CPH  (324)  out:162 in:162
    39	  56   SIN  (316)  out:158 in:158
    40	 346   LED  (302)  out:151 in:151
    41	   9   FLL  (302)  out:151 in:151
    42	 122   ICN  (301)  out:151 in:150
    43	  30   LAS  (300)  out:150 in:150
    44	  17   MSP  (300)  out:150 in:150
    45	 110   ATH  (299)  out:150 in:149
    46	 230   PMI  (298)  out:149 in:149
    47	  23   SFO  (297)  out:148 in:149
    48	 200   TXL  (292)  out:147 in:145
    49	  15   MCO  (288)  out:144 in:144
    50	 101   BKK  (286)  out:143 in:143
 
 
