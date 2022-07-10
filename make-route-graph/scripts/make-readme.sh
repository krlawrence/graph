#----------------------------------------------------------------------------------------
# make-readme.sh
#
# This script will produce a README text file that has information in it about the
# airports and routes contained in the graph.  If running on a Mac speficy 'mac' as the
# first (and only) command line option. This script is used whenever the air-route data is
# updated in the sample-data folder to produce a new 'README-air-routes-latest.txt' file.
# I usually remove sections 3 and 4 when doing that to keep the file size down.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------
mrg='ruby ../mrg/mrg.rb'
fn='README-temp.txt'

# Airport and route counts from the original book version so we can calculte the deltas.
orga=3374 
orgr=43400

# Current counts
cura=$($mrg apcount -big)
curr=$($mrg rtcount -big)

# Deltas
deltaa=$((cura-orga))
deltar=$((curr-orgr))

# Use this date command on Linux
if [ "x$1" == "x" ]; then
  dt=$(date -R)
fi

# Use this date command if running on Mac OS or BSD
if [ "x$1" == "xmac" ]; then
  dt=$(date -u)
fi

echo "Working on it..."

echo "Information about the Air Routes graph.  

Timestamp: $dt

This file contains the following sections
  1. Introduction
  2. Some statistics about the graph
  3. List of all the airports currently in the graph
  4. List of all the routes currently in the graph


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
removed on a weekly basis any graph such as this one is, invariably, out of date
the day it is published. That said I have tried to keep the graph as up to date
as I can.  

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
producing nice visuals. I hope people have as much fun playing with the graph as
I have had putting it together.
   
If you require more detailed information about the schema of the graph  and
it's overall demographic please see the comments at the top of the
air-routes.graphml file.

Again, please do let me know, by opening an issue or via e-mail, of any mistakes
you find or about airports and/or routes that are currently missing.

Kelvin R. Lawrence
(Graph and Aviation enthusiast)" > $fn


echo " 

2. SOME STATISTICS ABOUT THE GRAPH" >> $fn
$mrg stats -big -all >> $fn 
echo "
This version has $deltaa more airports, and $deltar more routes than the original." >> $fn
echo " " >> $fn
echo " " >> $fn
echo "Here are the top 50 airports with the most routes" >> $fn
echo " 
    POS  ID   CODE  TOTAL     DETAILS
" >> $fn
$mrg degrees -big > all-degree.tmp

# Use this line on GNU/Linux/Cygwin versions of 'sort'
if [ "x$1" == "x" ]; then
  sort all-degree.tmp -b -n -t '(' --key=2 |tail -n50 |tac| cat -n >> $fn
fi

# Use this line on Mac/BSD versions of 'sort' 
if [ "x$1" == "xmac" ]; then
  sort all-degree.tmp -b -n -t  '(' --key=2 | tail -r -n50 |  cat -n >> $fn
fi

echo " " >> $fn
echo " " >> $fn
echo "Here are the longest routes currently in the graph" >> $fn
echo " " >> $fn 
$mrg gte 8000 | sort -t ' ' -k2 -r  >> $fn


echo " " >> $fn
echo " " >> $fn
echo "3. LIST OF ALL THE AIRPORTS CURRENTLY IN THE GRAPH" >> $fn
echo " " >> $fn
echo "The graph contains the following airports" >> $fn
echo " " >> $fn
$mrg list -big >> $fn
echo " " >> $fn
echo " " >> $fn
echo "4. LIST OF ALL THE ROUTES CURRENTLY IN THE GRAPH" >> $fn
echo " " >> $fn
echo "The graph contains the following routes" >> $fn
echo " " >> $fn
$mrg hruby -big -all >> $fn
echo " " >> $fn
rm all-degree.tmp
