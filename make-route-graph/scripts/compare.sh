#----------------------------------------------------------------------------------------
# compare.sh
#
# Usage: compare.sh iata-code
#
# For the given IATA code, create two files and diff them. This is useful when
# looking for unexpected incoming and outgoing route differences for an airport.
#
# Two temporary files will be created for outgoing and incoming routes.
# For example, running 'compare.sh LAX' will create two files called LAXout.lst and
# LAXin.lst. LAXin.lst is processed by awk to reorder the columns to match those
# of LAXout.lst. Those two files are then processed by diff to find any mismatches.
#
# This script is useful if after running 'mrg.rb degrees -big' differences are
# flagged where not expected (perhaps after adding a new airports or additional
# routes). Take note that there are many cases in the air-routes dataset where such
# differences already exist and are expected.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
#----------------------------------------------------------------------------------------

mrg='ruby ../mrg/mrg.rb'

$mrg from $1 | sort > $1out.lst
$mrg to $1 | sort > $1in.lst
cat $1in.lst | awk 'BEGIN {FS="(,| |\\[)"}{print $2","$1,"["$5","$4","$6}END{}' > $1-$1.lst
diff -ay $1out.lst $1-$1.lst
rm $1out.lst
rm $1in.lst
rm $1-$1.lst
