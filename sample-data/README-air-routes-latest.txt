Information about the Air Routes graph.  

Timestamp: Sat, 27 Apr 2019 10:03:59 -0500

This file contains the following sections
  1. Introduction
  2. Some statistics about the graph

The latest data set adds 63 additional airports and 5,838 additional routes to
the original air-routes.graphml data set. The latest data set also incorporates
updates such as the new Istanbul Airport opening. All distances have been
updated of the location change. This update also corrects the number of runways
at Chicago O'Hare (ORD) to 7 rather than the 8 previously used based on latest
airport charts.

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

Air Routes Graph (v0.82, 2019-April-27th) contains:
  3,437 airports
  49,238 routes
  237 countries (and dependent areas)
  7 continents
  3,682 total nodes
  56,112 total edges

Additional observations:
  Longest route is between SIN and EWR (9,523 miles)
  Shortest route is between WRY and PPW (2 miles)
  Average route distance is 1,204.759 miles.
  Longest runway is 18,045ft (BPX)
  Shortest runway is 1,300ft (SAB)
  Average number of runways is 1.42624
  Furthest North is LYR (latitude: 78.2461013793945)
  Furthest South is USH (latitude: -54.8433)
  Furthest East is SVU (longitude: 179.341003418)
  Furthest West is TVU (longitude: -179.876998901)
  Closest to the Equator is MDK (latitude: 0.0226000007242)
  Closest to the Greenwich meridian is LDE (longitude: -0.006438999902457)
  Highest elevation is DCY (14,472 feet)
  Lowest elevation is GUW (-72 feet)
  Maximum airport node degree (routes in and out) is 612 (FRA)
  Country with the most airports: United States (582)
  Continent with the most airports: North America (983)
  Average degree (airport nodes) is 28.652
  Average degree (all nodes) is 28.620
 
 
Here are the top 50 airports with the most routes
 
    POS  ID   CODE  TOTAL     DETAILS

     1	  52   FRA  (612)  out:306 in:306
     2	 161   IST  (608)  out:304 in:304
     3	  51   CDG  (580)  out:290 in:290
     4	  70   AMS  (559)  out:278 in:281 
     5	  80   MUC  (530)  out:265 in:265
     6	  18   ORD  (507)  out:254 in:253 
     7	  64   PEK  (493)  out:246 in:247 
     8	  58   DXB  (490)  out:245 in:245
     9	   8   DFW  (484)  out:242 in:242
    10	   1   ATL  (482)  out:241 in:241
    11	  50   LGW  (456)  out:228 in:228
    12	 102   DME  (454)  out:227 in:227
    13	  49   LHR  (434)  out:217 in:217
    14	  13   LAX  (424)  out:212 in:212
    15	  67   PVG  (418)  out:209 in:209
    16	  31   DEN  (417)  out:209 in:208 
    17	  94   STN  (416)  out:208 in:208
    18	  84   MAN  (415)  out:208 in:207 
    19	  74   MAD  (414)  out:207 in:207
    20	  73   BCN  (404)  out:202 in:202
    21	  12   JFK  (401)  out:201 in:200 
    22	  68   FCO  (400)  out:200 in:200
    23	  75   VIE  (398)  out:199 in:199
    24	  11   IAH  (392)  out:196 in:196
    25	  47   YYZ  (390)  out:195 in:195
    26	 198   DUS  (386)  out:193 in:193
    27	  16   MIA  (386)  out:193 in:193
    28	  79   BRU  (384)  out:192 in:192
    29	  35   EWR  (384)  out:192 in:192
    30	 177   CPH  (380)  out:190 in:190
    31	 106   DOH  (366)  out:183 in:183
    32	 103   SVO  (362)  out:181 in:181
    33	 178   CLT  (358)  out:179 in:179
    34	  60   DUB  (358)  out:179 in:179
    35	  76   ZRH  (356)  out:178 in:178
    36	  61   HKG  (346)  out:173 in:173
    37	 250   CAN  (342)  out:172 in:170 
    38	 122   ICN  (336)  out:168 in:168
    39	  93   ARN  (334)  out:167 in:167
    40	  56   SIN  (332)  out:166 in:166
    41	  15   MCO  (318)  out:159 in:159
    42	  30   LAS  (314)  out:157 in:157
    43	   9   FLL  (314)  out:157 in:157
    44	 346   LED  (310)  out:155 in:155
    45	  17   MSP  (308)  out:154 in:154
    46	 230   PMI  (304)  out:152 in:152
    47	 101   BKK  (304)  out:152 in:152
    48	 110   ATH  (303)  out:152 in:151 
    49	  23   SFO  (303)  out:151 in:152 
    50	 200   TXL  (302)  out:152 in:150 
 
 
Here are the longest routes currently in the graph
 
60 route(s)
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

 
 
