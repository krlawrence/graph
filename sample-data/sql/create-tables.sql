-- Create the tables used by the air routes data

CREATE TABLE countries
    (CODE      CHAR(2)     NOT NULL,
     DESCR     VARCHAR(50) NOT NULL,
     PRIMARY KEY(CODE));

CREATE TABLE continents
    (CODE      CHAR(2)     NOT NULL,
     DESCR     VARCHAR(50) NOT NULL,
     PRIMARY KEY(CODE));
  
CREATE TABLE airports
    (ID        INTEGER     NOT NULL,
     IATA      CHAR(3)     NOT NULL,
     ICAO      CHAR(4)     NOT NULL,
     CITY      VARCHAR(50),
     DESCR     VARCHAR(80),
     REGION    VARCHAR(6),
     COUNTRY   CHAR(2)     NOT NULL,
     RUNWAYS   INTEGER     NOT NULL,
     LONGEST   INTEGER     NOT NULL,
     ALTITUDE  INTEGER     NOT NULL,
     CONTINENT CHAR(2)     NOT NULL,
     LAT       DOUBLE      NOT NULL,
     LON       DOUBLE      NOT NULL,
     PRIMARY KEY (ID),
     FOREIGN KEY (COUNTRY) REFERENCES countries(CODE),
     FOREIGN KEY (CONTINENT) REFERENCES continents(CODE));

-- From ID to ID routes table
CREATE TABLE routes
    (SRC       INTEGER     NOT NULL,
     DEST      INTEGER     NOT NULL,
     DIST      INTEGER     NOT NULL,
     PRIMARY KEY (SRC,DEST));

-- From IATA to IATA routes table
CREATE TABLE iroutes
    (SRC       CHAR(3)     NOT NULL,
     DEST      CHAR(3)     NOT NULL,
     DIST      INTEGER     NOT NULL,
     PRIMARY KEY (SRC,DEST));
