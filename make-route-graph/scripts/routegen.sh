# ---------------------------------------------------------------------------------------
# routegen.sh
#
# A script to build the air-routes graph in various formats such as GraphML, Gremlin
# (Groovy) steps and CSV.  This script is most commonly used when a new update of the
# data is going to be published. The README file is also generated as part of this
# process. Some additional files are generated containing information about the
# graph.
#
# If running on a Mac use 'routegen.sh mac'
#
# This script assumes that the graphml2csv.py file has been downloaded from
# https://github.com/awslabs/amazon-neptune-tools/tree/master/graphml2csv and is
# available.
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
echo "      air-routes-small-visjs-latest.html"
$mrg vis > air-routes-small-visjs-latest.html
echo "      air-routes-visjs-latest.html"
$mrg vis -big > air-routes-visjs-latest.html
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
$mrg gremlin -big  > make-air-routes-latest.groovy
echo "*** Copying and deleting temporary files"
cp README-temp.txt README-air-routes-latest.txt
rm README-temp.txt

