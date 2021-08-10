# Simple Ruby script that given the air-routes CSV files can produce
# a set of SPARQL INSERT DATA commands to create an quivalent RDF
# graph that uses named graphs to manage edge properties.
#
require 'csv'

INTEGERS = ['BYTE','SHORT','INT','LONG']
FLOATS = ['FLOAT','DOUBLE']
BOOLS = ['BOOL','BOOLEAN']
DATES = ['DATE']
SPACES = " " * 4

def printHeader()
  puts 'PREFIX v: <http://aws.amazon.com/neptune/vocab/v01/>'
  puts 'PREFIX r: <http://kelvinlawrence.net/air-routes/resource/>'
  puts 'PREFIX rdfsyn: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>'
  puts 'PREFIX dtp: <http://kelvinlawrence.net/air-routes/datatypeProperty/>'
  puts 'PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>'
  puts 'PREFIX c: <http://kelvinlawrence.net/air-routes/class/>'
  puts 'PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>'
  puts 'PREFIX op: <http://kelvinlawrence.net/air-routes/objectProperty/>'
  puts ''
  puts 'INSERT DATA {'
end

def printVertexHeader()
  puts '  GRAPH v:DefaultNamedGraph {'
end

def printVertexFooter()
  puts "    }"
  puts "}"
end

def printEdgesFooter()       
  puts "}"
end

def processProperties(row,prefix,suffix)
  row.headers.each do |h|
    line = ""
    tmp = h.split(':')
    if  ['~label','~id','~from','~to'].include? h 
      next
    else
      cell = row[h]
      if cell != nil
        line += "dtp:#{tmp[0]} \"#{cell}\""
        if tmp.size > 1
          u = tmp[1].upcase
          case 
            when INTEGERS.include?(u)
              line += "^^xsd:integer"
            when FLOATS.include?(u)
              line += "^^xsd:double"
            when BOOLS.include?(u)
              line += "^^xsd:boolean"
            when DATES.include?(u)
              line += "^^xsd:date"
          end
        end
      end
    end
    if line != ""
      line = prefix + line + suffix
      puts line
    end
  end
end


def processVertexRows(t)
  t.each do |row|
    id = row['~id']
    label = row['~label']
    label[0] = label[0].upcase
    s = "r:#{id}"
    puts "#{SPACES}#{s} rdfsyn:type c:#{label} ."
    puts "#{SPACES}#{s} rdfs:label \"#{row['code:string']}\" ."
    processProperties(row,"#{SPACES}#{s} "," .")
  end
end


def processEdgeRows(t)
  t.each do |row|
    id = row['~id']
    label = row['~label']
    from = row['~from']
    to = row['~to']
    s = "r:#{id}"
    puts"#{SPACES}GRAPH #{s} { r:#{from} op:#{label} r:#{to} . }"
    label[0] = label[0].upcase
    processProperties(row,"#{SPACES}GRAPH v:DefaultNamedGraph {#{s} "," . }")
  end
end


# Main entry point

fname = ARGV[0]
if not File.exist?(fname)
  puts "No such file #{fname}"
  return
else
  table = CSV.parse(File.read(fname), headers: true)
end

printHeader()

if table.headers.include? "~from"
  processEdgeRows(table)
  printEdgesFooter  
else
  printVertexHeader()
  processVertexRows(table)
  printVertexFooter  
end

