
Information about the most recent Air Routes graph data set.

Timestamp: Thu, 31 May 2018 09:35:19 -0500

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

Air Routes Graph (v0.79, 2018-May-31) contains:
  3,397 airports
  45,845 routes
  237 countries (and dependent areas)
  7 continents
  3,642 total nodes
  52,639 total edges

Additional observations:
  Longest route is between DOH and AKL (9,025 miles)
  Shortest route is between WRY and PPW (2 miles)
  Average route distance is 1,186.279 miles.
  Longest runway is 18,045ft (BPX)
  Shortest runway is 1,300ft (SAB)
  Average number of runways is 1.4292
  Furthest North is LYR (latitude: 78.2461013793945)
  Furthest South is USH (latitude: -54.8433)
  Furthest East is SVU (longitude: 179.341003418)
  Furthest West is TVU (longitude: -179.876998901)
  Closest to the Equator is MDK (latitude: 0.0226000007242)
  Closest to the Greenwich meridian is LDE (longitude: -0.006438999902457)
  Highest elevation is DCY (14,472 feet)
  Lowest elevation is GUW (-72 feet)
  Maximum airport node degree (routes in and out) is 590 (FRA)
  Country with the most airports: United States (579)
  Continent with the most airports: North America (980)
  Average degree (airport nodes) is 26.991
  Average degree (all nodes) is 27.049
 
 
Here are the top 50 airports with the most routes
 
    POS  ID   CODE  TOTAL     DETAILS

     1	  52   FRA  (590)  out:295 in:295
     2	  51   CDG  (556)  out:278 in:278
     3	  70   AMS  (553)  out:275 in:278
     4	 161   IST  (548)  out:274 in:274
     5	  80   MUC  (506)  out:253 in:253
     6	  18   ORD  (486)  out:243 in:243
     7	  64   PEK  (485)  out:242 in:243
     8	  58   DXB  (480)  out:240 in:240
     9	   1   ATL  (474)  out:237 in:237
    10	   8   DFW  (444)  out:222 in:222
    11	 102   DME  (436)  out:218 in:218
    12	  50   LGW  (424)  out:212 in:212
    13	  67   PVG  (408)  out:204 in:204
    14	  49   LHR  (406)  out:203 in:203
    15	  13   LAX  (402)  out:201 in:201
    16	  74   MAD  (400)  out:200 in:200
    17	  73   BCN  (390)  out:195 in:195
    18	  31   DEN  (390)  out:195 in:195
    19	  94   STN  (388)  out:194 in:194
    20	  68   FCO  (388)  out:194 in:194
    21	  11   IAH  (388)  out:194 in:194
    22	  12   JFK  (387)  out:194 in:193
    23	  84   MAN  (385)  out:193 in:192
    24	  79   BRU  (378)  out:189 in:189
    25	  35   EWR  (374)  out:187 in:187
    26	  47   YYZ  (370)  out:185 in:185
    27	 198   DUS  (356)  out:178 in:178
    28	  16   MIA  (356)  out:178 in:178
    29	 106   DOH  (348)  out:174 in:174
    30	 178   CLT  (340)  out:170 in:170
    31	  75   VIE  (340)  out:170 in:170
    32	  60   DUB  (338)  out:169 in:169
    33	 250   CAN  (331)  out:166 in:165
    34	  76   ZRH  (330)  out:165 in:165
    35	 103   SVO  (320)  out:160 in:160
    36	 177   CPH  (318)  out:159 in:159
    37	  93   ARN  (318)  out:159 in:159
    38	  61   HKG  (318)  out:159 in:159
    39	  56   SIN  (312)  out:156 in:156
    40	 346   LED  (300)  out:150 in:150
    41	  17   MSP  (298)  out:149 in:149
    42	 122   ICN  (296)  out:148 in:148
    43	   9   FLL  (296)  out:148 in:148
    44	  30   LAS  (294)  out:147 in:147
    45	 230   PMI  (292)  out:146 in:146
    46	  23   SFO  (290)  out:145 in:145
    47	 101   BKK  (286)  out:143 in:143
    48	 110   ATH  (281)  out:141 in:140
    49	  46   DTW  (280)  out:140 in:140
    50	  10   IAD  (276)  out:138 in:138
