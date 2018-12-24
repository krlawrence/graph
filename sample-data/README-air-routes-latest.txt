Information about the Air Routes graph.  

Timestamp: Mon, 24 Dec 2018 09:55:39 -0600

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

Air Routes Graph (v0.81, 2018-December-24th) contains:
  3,421 airports
  48,092 routes
  237 countries (and dependent areas)
  7 continents
  3,666 total nodes
  54,934 total edges

Additional observations:
  Longest route is between SIN and EWR (9,523 miles)
  Shortest route is between WRY and PPW (2 miles)
  Average route distance is 1,199.612 miles.
  Longest runway is 18,045ft (BPX)
  Shortest runway is 1,300ft (SAB)
  Average number of runways is 1.42648
  Furthest North is LYR (latitude: 78.2461013793945)
  Furthest South is USH (latitude: -54.8433)
  Furthest East is SVU (longitude: 179.341003418)
  Furthest West is TVU (longitude: -179.876998901)
  Closest to the Equator is MDK (latitude: 0.0226000007242)
  Closest to the Greenwich meridian is LDE (longitude: -0.006438999902457)
  Highest elevation is DCY (14,472 feet)
  Lowest elevation is GUW (-72 feet)
  Maximum airport node degree (routes in and out) is 602 (FRA)
  Country with the most airports: United States (581)
  Continent with the most airports: North America (982)
  Average degree (airport nodes) is 28.116
  Average degree (all nodes) is 28.111
 
 
Here are the top 50 airports with the most routes
 
    POS  ID   CODE  TOTAL     DETAILS

     1	  52   FRA  (602)  out:301 in:301
     2	  51   CDG  (572)  out:286 in:286
     3	  70   AMS  (559)  out:278 in:281 
     4	 161   IST  (554)  out:277 in:277
     5	  80   MUC  (520)  out:260 in:260
     6	  18   ORD  (501)  out:251 in:250 
     7	  64   PEK  (491)  out:245 in:246 
     8	  58   DXB  (488)  out:244 in:244
     9	   1   ATL  (476)  out:238 in:238
    10	   8   DFW  (462)  out:231 in:231
    11	 102   DME  (450)  out:225 in:225
    12	  50   LGW  (438)  out:219 in:219
    13	  49   LHR  (418)  out:209 in:209
    14	  67   PVG  (414)  out:207 in:207
    15	  74   MAD  (412)  out:206 in:206
    16	  94   STN  (410)  out:205 in:205
    17	  13   LAX  (410)  out:205 in:205
    18	  31   DEN  (409)  out:205 in:204 
    19	  84   MAN  (403)  out:202 in:201 
    20	  12   JFK  (401)  out:201 in:200 
    21	  73   BCN  (398)  out:199 in:199
    22	  68   FCO  (396)  out:198 in:198
    23	  11   IAH  (392)  out:196 in:196
    24	  47   YYZ  (388)  out:194 in:194
    25	  75   VIE  (384)  out:192 in:192
    26	  79   BRU  (382)  out:191 in:191
    27	  35   EWR  (380)  out:190 in:190
    28	 198   DUS  (372)  out:186 in:186
    29	  16   MIA  (370)  out:185 in:185
    30	 106   DOH  (362)  out:181 in:181
    31	 103   SVO  (360)  out:180 in:180
    32	  76   ZRH  (356)  out:178 in:178
    33	  60   DUB  (350)  out:175 in:175
    34	 178   CLT  (346)  out:173 in:173
    35	 250   CAN  (338)  out:170 in:168 
    36	  61   HKG  (336)  out:168 in:168
    37	 177   CPH  (332)  out:166 in:166
    38	  93   ARN  (332)  out:166 in:166
    39	  56   SIN  (322)  out:161 in:161
    40	  15   MCO  (316)  out:158 in:158
    41	 346   LED  (308)  out:154 in:154
    42	  30   LAS  (308)  out:154 in:154
    43	   9   FLL  (306)  out:153 in:153
    44	 110   ATH  (303)  out:152 in:151 
    45	 122   ICN  (301)  out:151 in:150 
    46	 200   TXL  (300)  out:151 in:149 
    47	  17   MSP  (300)  out:150 in:150
    48	  23   SFO  (299)  out:149 in:150 
    49	 230   PMI  (298)  out:149 in:149
    50	 101   BKK  (296)  out:148 in:148
 
 
Here are the longest routes currently in the graph
 
SIN->EWR 9,523
EWR->SIN 9,523
DOH->AKL 9,025
AKL->DOH 9,025
PER->LHR 9,009
LHR->PER 9,009
PTY->PEK 8,884
PEK->PTY 8,884
DXB->AKL 8,818
AKL->DXB 8,818
SIN->LAX 8,756
LAX->SIN 8,756
MEX->CAN 8,754
CAN->MEX 8,754
SYD->IAH 8,591
IAH->SYD 8,591
SYD->DFW 8,574
DFW->SYD 8,574
MNL->JFK 8,504
JFK->MNL 8,504
JNB->ATL 8,434
ATL->JNB 8,434
SIN->SFO 8,433
SFO->SIN 8,433
LAX->AUH 8,372
AUH->LAX 8,372
LAX->DXB 8,321
DXB->LAX 8,321
LAX->JED 8,314
JED->LAX 8,314
LAX->DOH 8,287
DOH->LAX 8,287
RUH->LAX 8,246
LAX->RUH 8,246
TLV->SCL 8,208
SCL->TLV 8,208
YVR->MEL 8,197
MEL->YVR 8,197
ORD->AKL 8,186
AKL->ORD 8,186
IAH->DXB 8,150
DXB->IAH 8,150
SFO->AUH 8,139
AUH->SFO 8,139
IAD->HKG 8,135
HKG->IAD 8,135
HKG->DFW 8,105
DFW->HKG 8,105
SFO->DXB 8,085
DXB->SFO 8,085
JFK->HKG 8,054
HKG->JFK 8,054
DFW->AUH 8,053
AUH->DFW 8,053
HKG->EWR 8,047
EWR->HKG 8,047
IAH->DOH 8,030
DOH->IAH 8,030
DXB->DFW 8,022
DFW->DXB 8,022
