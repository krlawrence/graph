# ---------------------------------------------------------------------------------------
# routegen.sh
#
# A script to build the air-routes graph in various formats including GraphML, Gremlin
# (Groovy) steps, CSV, RDF and SQL.  This script is most commonly used when a new update
# of the data is going to be published. The README file is also generated as part of this
# process. Some additional files are generated containing information about the graph.
#
# If running on a Mac use 'routegen.sh mac'
#
# This script assumes that the "graphml2csv.py" file has been downloaded from
# https://github.com/awslabs/amazon-neptune-tools/tree/master/graphml2csv and is available
# in the same directory as this script.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
# ---------------------------------------------------------------------------------------
mrg='ruby ../mrg/mrg.rb'

echo "*** Generating GraphML files"
echo "      air-routes-small-latest.graphml"
$mrg graphml -tp3 > air-routes-small-latest.graphml
echo "      air-routes-latest.graphml"
$mrg  graphml -tp3 -big > air-routes-latest.graphml
echo "*** Generating VisJs files"
echo "      air-routes-small-latest-visjs.html"
$mrg vis > air-routes-small-latest-visjs.html
echo "      air-routes-latest-visjs.html"
$mrg vis -big > air-routes-latest-visjs.html
echo "*** Generating airport HTML table"
echo "      airports-latest.html"
$mrg html -big > airports-latest.html
echo "*** Generating airport degree list"
if [ "x$1" == "xmac" ]; then
  ./make-degree-list.sh mac
else
  ./make-degree-list.sh
fi
echo "*** Generating README"
if [ "x$1" == "xmac" ]; then
  ./make-readme.sh mac
else
  ./make-readme.sh
fi
echo "*** Generating CSV Files"
python3 graphml2csv.py -i air-routes-latest.graphml
echo "*** Generating batched Groovy (Gremlin console) steps"
echo "      make-air-routes-latest.groovy"
$mrg gremlin -big  > make-air-routes-latest.groovy
echo "*** Generating RDF Files"
ruby ../../sample-data/RDF/mk-sparql.rb air-routes-latest-nodes.csv > nodes.sparql
echo "      nodes.sparql"
ruby ../../sample-data/RDF/mk-sparql.rb air-routes-latest-edges.csv > edges.sparql
echo "      edges.sparql"
echo "*** Generating SQL Files"
$mrg hid -big > tmp-routes.csv
$mrg hcode -big > tmp-routes-iata.csv
$mrg list -big -all > tmp-airports.csv
apcount=$($mrg apcount -big) 
echo "      airports.sql"
ruby ../../sample-data/SQL/scripts/make-sql.rb tmp-airports.csv 1 $apcount > airports.sql
echo "      routes.sql"
ruby ../../sample-data/SQL/scripts/make-route-sql.rb tmp-routes.csv > routes.sql
echo "      routes-iata.sql"
ruby ../../sample-data/SQL/scripts/make-routes-iata-sql.rb tmp-routes-iata.csv > routes-iata.sql
echo "*** Copying and deleting temporary files"
cp README-temp.txt README-air-routes-latest.txt
rm README-temp.txt
rm tmp-routes.csv
rm tmp-routes-iata.csv
rm tmp-airports.csv

