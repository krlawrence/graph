# ---------------------------------------------------------------------------------------
# make-route-sql.rb
#
# This script will build a set of SQL INSERT commands from a list of routes in a CSV style
# file. The CSV file should be from running mrg.rb with the 'hid' and optionally '-big' options
# specified.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
# ---------------------------------------------------------------------------------------
file = ARGV[0]
 
 
if File.exists?(file)
  f = File.open(file,"r")
 
  f.each do |r|
    a = r.chomp().strip().split(',')
 
    puts "INSERT INTO routes"
    puts "  (SRC,DEST,DIST)"
    str = "#{a[0]},#{a[1]},#{a[2]}"
    puts "  VALUES (#{str});"
  end
  f.close
else
  puts "No such file found: #{file}"
end 
