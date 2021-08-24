# ---------------------------------------------------------------------------------------
# make-sql.rb
#
# This script will build a set of SQL INSERT commands from a list of airports in a CSV
# style file. The CSV file (actually semi-colon delimitted) can be created using mrg.rb
# with the 'list -all' and optionally '-big', options specified.
#
# Author: Kelvin R Lawrence  27th November-2013 - Present
# ---------------------------------------------------------------------------------------
file = ARGV[0]

startx = 1
endx = 46

startx = ARGV[1].to_i unless ARGV[1] == nil
endx   = ARGV[2].to_i unless ARGV[2] == nil

if File.exists?(file)
  f = File.open(file,"r")

  f.each do |r|
    a = r.chomp().strip().split(';')
    a.each{|s| s.gsub!("\'","\'\'") unless not s.instance_of? String}
    if a[0].to_i < startx
      next
    elsif a[0].to_i > endx
      exit
    end

    puts "INSERT INTO airports"
    puts "  (ID,IATA,ICAO,CITY,DESCR,REGION,RUNWAYS,LONGEST,ALTITUDE,COUNTRY,CONTINENT,LAT,LON)"
    str = "#{a[0]},'#{a[1]}','#{a[2]}','#{a[4]}','#{a[3]}','#{a[5]}',#{a[6]},#{a[7]},#{a[8]},'#{a[9]}','#{a[10]}',#{a[11]},#{a[12]}"
    puts "  VALUES (#{str});"
  end
  f.close
else
  puts "No such file found: #{file}"
end  



