Information about the Air Routes graph data set.

These notes refer to the version of the data set used by the examples in the book.

If you would like instead to work with the most recent version of the data please use
the file called 'air-routes-latest.graphml' and refer to README-air-routes-latest.txt.

Timestamp: Fri, 06 Oct 2017 11:28:40 -0500

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

Air Routes Graph (v0.77, 2017-Oct-06) contains:
  3,374 airports
  43,400 routes
  237 countries (and dependent areas)
  7 continents
  3,619 total nodes
  50,148 total edges

Additional observations:
  Longest route is between DOH and AKL (9,025 miles)
  Shortest route is between WRY and PPW (2 miles)
  Average route distance is 1,164.747 miles.
  Longest runway is 18,045ft (BPX)
  Shortest runway is 1,300ft (SAB)
  Furthest North is LYR (latitude: 78.2461013793945)
  Furthest South is USH (latitude: -54.8433)
  Furthest East is SVU (longitude: 179.341003418)
  Furthest West is TVU (longitude: -179.876998901)
  Closest to the Equator is MDK (latitude: 0.0226000007242)
  Closest to the Greenwich meridian is LDE (longitude: -0.006438999902457)
  Highest elevation is DCY (14,472 feet)
  Lowest elevation is GUW (-72 feet)
  Maximum airport node degree (routes in and out) is 544 (FRA)
  Country with the most airports: United States (579)
  Continent with the most airports: North America (978)
  Average degree (airport nodes) is 25.726
  Average degree (all nodes) is 25.856
 
 
Here are the top 50 airports with the most routes
 
    POS  ID   CODE  TOTAL     DETAILS

     1	  52   FRA  (544)  out:272 in:272
     2	  70   AMS  (541)  out:269 in:272 
     3	 161   IST  (540)  out:270 in:270
     4	  51   CDG  (524)  out:262 in:262
     5	  80   MUC  (474)  out:237 in:237
     6	  64   PEK  (469)  out:234 in:235 
     7	  18   ORD  (464)  out:232 in:232
     8	   1   ATL  (464)  out:232 in:232
     9	  58   DXB  (458)  out:229 in:229
    10	   8   DFW  (442)  out:221 in:221
    11	 102   DME  (428)  out:214 in:214
    12	  67   PVG  (402)  out:201 in:201
    13	  50   LGW  (400)  out:200 in:200
    14	  13   LAX  (390)  out:195 in:195
    15	  74   MAD  (384)  out:192 in:192
    16	  11   IAH  (384)  out:192 in:192
    17	  49   LHR  (382)  out:191 in:191
    18	  73   BCN  (380)  out:190 in:190
    19	  68   FCO  (378)  out:189 in:189
    20	  31   DEN  (376)  out:188 in:188
    21	  12   JFK  (373)  out:187 in:186 
    22	  94   STN  (372)  out:186 in:186
    23	  35   EWR  (364)  out:182 in:182
    24	  84   MAN  (363)  out:182 in:181 
    25	  47   YYZ  (362)  out:181 in:181
    26	  79   BRU  (360)  out:180 in:180
    27	  16   MIA  (342)  out:171 in:171
    28	 178   CLT  (336)  out:168 in:168
    29	 198   DUS  (332)  out:166 in:166
    30	  60   DUB  (330)  out:165 in:165
    31	 250   CAN  (327)  out:164 in:163 
    32	 106   DOH  (326)  out:163 in:163
    33	  75   VIE  (324)  out:162 in:162
    34	 103   SVO  (312)  out:156 in:156
    35	  76   ZRH  (304)  out:152 in:152
    36	  61   HKG  (302)  out:151 in:151
    37	  56   SIN  (292)  out:146 in:146
    38	 177   CPH  (290)  out:145 in:145
    39	  30   LAS  (290)  out:145 in:145
    40	 122   ICN  (288)  out:144 in:144
    41	  17   MSP  (288)  out:144 in:144
    42	 230   PMI  (286)  out:143 in:143
    43	  93   ARN  (286)  out:143 in:143
    44	   9   FLL  (284)  out:142 in:142
    45	  23   SFO  (282)  out:141 in:141
    46	  46   DTW  (274)  out:137 in:137
    47	  10   IAD  (272)  out:136 in:136
    48	 346   LED  (266)  out:133 in:133
    49	 101   BKK  (264)  out:132 in:132
    50	   5   BOS  (260)  out:130 in:130
 
 
