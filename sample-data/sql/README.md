
# Air routes SQL data

Chapter 3 of the book includes a brief discussion of the differences and similarities between SQL and Gremlin. The SQL files in this folder and the examples below were created to help build the examples in that chapter. Each file has been tested using Sqlite version 3 but should work with minor or no modifications in other SQL databases. Versions of this data have also been tested using MySQL. The data model is deliberately quite simple. No attempt was made to remove duplication and not all tables have primary keys that match their Gremlin equivalent structures (for example the edge ID values used in the Gremlin data are not used in the route tables). You will find examples showing the contents of each table in the `sample-queries.txt` file. The following files are included in this folder.

|File|Purpose|
|--|--|
|[airports.sql](airports.sql)|SQL inserts for the airport data.|
|[continents.sql](continents.sql)|SQL inserts for continent data.|
|[countries.sql](countries.sql)|SQL inserts for the country data.|
|[create-tables.sql](create-tables.sql)|Creates all of the tables.|
|[routes-iata.sql](routes-iata.sql)|SQL inserts for the routes between airport using IATA codes.|
|[routes.sql](routes.sql)|SQL inserts for routes between airports using ID values.|
|[sample-queries.txt](sample-queries.txt)|A text file containing queries like the ones shown below.|

A few example queries that were run using Sqlite version 3 are shown below.

### Selected airports

```sql
select a.iata, a.city, a.descr from airports a limit 10;
```

```
+------+-----------------+----------------------------------------------------+
| IATA |      CITY       |                       DESCR                        |
+------+-----------------+----------------------------------------------------+
| ATL  | Atlanta         | Hartsfield - Jackson Atlanta International Airport |
| ANC  | Anchorage       | Anchorage Ted Stevens                              |
| AUS  | Austin          | Austin Bergstrom International Airport             |
| BNA  | Nashville       | Nashville International Airport                    |
| BOS  | Boston          | Boston Logan                                       |
| BWI  | Baltimore       | Baltimore/Washington International Airport         |
| DCA  | Washington D.C. | Ronald Reagan Washington National Airport          |
| DFW  | Dallas          | Dallas/Fort Worth International Airport            |
| FLL  | Fort Lauderdale | Fort Lauderdale/Hollywood International Airport    |
| IAD  | Washington D.C. | Washington Dulles International Airport            |
+------+-----------------+----------------------------------------------------+
```

### Cities starting with "Dal"

```sql
select iata,city from airports where city like 'Dal%' limit 10;
```

```
+------+-------------+
| IATA |    CITY     |
+------+-------------+
| DFW  | Dallas      |
| DAL  | Dallas      |
| DLM  | Dalaman     |
| DLC  | Dalian      |
| MHC  | Dalcahue    |
| DLI  | Dalat       |
| DLZ  | Dalanzadgad |
+------+-------------+
```

### Between six and eight runways

```sql
select iata,runways from airports where runways between 6 and 8 order by runways;
```

```
+------+---------+
| IATA | RUNWAYS |
+------+---------+
| BOS  | 6       |
| DEN  | 6       |
| DTW  | 6       |
| AMS  | 6       |
| DFW  | 7       |
| ORD  | 7       |
+------+---------+
```

### Airports close to the Greenwich Meridien

```sql
select IATA,CITY,REGION,LON from airports
where LON between -0.2 and 0.2 order by LON;
```

```
+------+-------------------------+--------+--------------------+
| IATA |          CITY           | REGION |        LON         |
+------+-------------------------+--------+--------------------+
| LGW  | London                  | GB-ENG | -0.190277993679047 |
| AZR  | Adrar                   | DZ-01  | -0.186414003372192 |
| ACC  | Accra                   | GH-AA  | -0.166786000132561 |
| LDE  | Tarbes/Lourdes/Pyrénées | FR-N   | -0.006438999902457 |
| CDT  | Castellón de La Plana   | ES-V   | 0.0261109992862    |
| LCY  | London                  | GB-ENG | 0.055278           |
| LEH  | Le Havre/Octeville      | FR-Q   | 0.0880559980869293 |
| MUW  | Mascara                 | DZ-29  | 0.147141993045807  |
| DOL  | Deauville               | FR-P   | 0.154305994511     |
+------+-------------------------+--------+--------------------+
```
### Airports with the continent description added

```sql
select airports.iata,airports.descr,continents.descr as Continent from airports
join continents on airports.continent=continents.code
limit 5 ;
```

```
+------+----------------------------------------------------+----------------+
| IATA |                       DESCR                        |   Continent    |
+------+----------------------------------------------------+----------------+
| ATL  | Hartsfield - Jackson Atlanta International Airport | North America  |
| ANC  | Anchorage Ted Stevens                              | North America  |
| AUS  | Austin Bergstrom International Airport             | North America  |
| BNA  | Nashville International Airport                    | North America  |
| BOS  | Boston Logan                                       | North America  |
+------+----------------------------------------------------+----------------+
```
### A simple view based on the prior example

```sql
create view test as select airports.iata,airports.descr,continents.descr as continent from airports join continents on airports.continent=continents.code;
select * from test where continent != "North America" limit 10;
```

```
+------+-----------------------------------------+-----------+
| IATA |                  DESCR                  | continent |
+------+-----------------------------------------+-----------+
| LHR  | London Heathrow                         | Europe    |
| LGW  | London Gatwick                          | Europe    |
| CDG  | Paris Charles de Gaulle                 | Europe    |
| FRA  | Frankfurt am Main                       | Europe    |
| HEL  | Helsinki Ventaa                         | Europe    |
| NRT  | Tokyo Narita                            | Asia      |
| SYD  | Sydney Kingsford Smith                  | Oceania   |
| SIN  | Singapore, Changi International Airport | Asia      |
| MEL  | Melbourne International Airport         | Oceania   |
| DXB  | Dubai International Airport             | Asia      |
+------+-----------------------------------------+-----------+
```
### The longest routes from Austin by joining tables

```sql
select a.iata,r.dist,a2.iata from airports a
join routes r on a.id = r.src
join airports a2 on a2.id = r.dest
where a.IATA = 'AUS'
order by r.dist desc
limit 10;
```

```
+------+------+------+
| IATA | DIST | IATA |
+------+------+------+
| AUS  | 5294 | FRA  |
| AUS  | 4921 | LGW  |
| AUS  | 4901 | LHR  |
| AUS  | 1768 | SEA  |
| AUS  | 1712 | PDX  |
| AUS  | 1695 | BOS  |
| AUS  | 1671 | YYC  |
| AUS  | 1660 | PVD  |
| AUS  | 1518 | JFK  |
| AUS  | 1501 | EWR  |
+------+------+------+
```
### The longest routes in the `iroutes` table

```sql
select * from iroutes order by iroutes.dist desc limit 10;
```

```
+-----+------+------+
| SRC | DEST | DIST |
+-----+------+------+
| EWR | SIN  | 9523 |
| SIN | EWR  | 9523 |
| AKL | DOH  | 9025 |
| DOH | AKL  | 9025 |
| LHR | PER  | 9009 |
| PER | LHR  | 9009 |
| PEK | PTY  | 8884 |
| PTY | PEK  | 8884 |
| DXB | AKL  | 8818 |
| AKL | DXB  | 8818 |
+-----+------+------+
```
### 3-hop routes from Austin to Agra

```sql
select a.IATA ,r.DIST, a2.IATA, r2.DIST,a3.IATA,r3.DIST,a4.IATA,
       r3.DIST+r2.DIST+r.DIST as TOTAL from airports a
join routes r on a.id = r.SRC
join airports a2 on a2.id = r.DEST
join routes r2 on a2.id = r2.SRC
join airports a3 on a3.id = r2.DEST
join routes r3   on a3.id = r3.SRC
join airports a4 on a4.id = r3.DEST
where a.IATA = 'AUS' and a4.IATA = 'AGR'
order by TOTAL asc
limit 10;
```

```
+------+------+------+------+------+------+------+-------+
| IATA | DIST | IATA | DIST | IATA | DIST | IATA | TOTAL |
+------+------+------+------+------+------+------+-------+
| AUS  | 1357 | YYZ  | 7758 | BOM  | 644  | AGR  | 9759  |
| AUS  | 1501 | EWR  | 7790 | BOM  | 644  | AGR  | 9935  |
| AUS  | 1518 | JFK  | 7782 | BOM  | 644  | AGR  | 9944  |
| AUS  | 5294 | FRA  | 4080 | BOM  | 644  | AGR  | 10018 |
| AUS  | 4901 | LHR  | 4479 | BOM  | 644  | AGR  | 10024 |
+------+------+------+------+------+------+------+-------+  
```
