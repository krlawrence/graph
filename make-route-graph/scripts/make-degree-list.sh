# ---------------------------------------------------------------------------------------
# make-degree-list.sh
#
# This script will build a list of all of the airports sorted in descending order based
# upon the number of combined (ingoing and outgoing) routes for that airport. If running
# on a mac speficy 'mac' as the first (and only) command line option. This script can be
# run stand alone but is also used by 'make-readme.sh'
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
# ---------------------------------------------------------------------------------------

echo "Working on it..."

mrg='ruby ../mrg/mrg.rb'
$mrg degrees -big -bc > degree-all.lst 

# Use this sort on Linux
if [ "x$1" == "x" ]; then
  sort -bn -t '(' -k2 degree-all.lst |tac|cat -n > xxxx-tmp.tmp
fi

#Use this sort on Mac OSX/BSD
if [ "x$1" == "xmac" ]; then
  sort -bn -t '(' -k2 degree-all.lst |tail -r|cat -n > xxxx-tmp.tmp
fi

echo "
    POS  ID   CODE  TOTAL     DETAILS
" > degree-all-sorted.lst
cat xxxx-tmp.tmp >> degree-all-sorted.lst
rm xxxx-tmp.tmp
rm degree-all.lst

