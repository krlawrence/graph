# MakeRouteGraph - Scripts
The scripts in this folder can be used to help expand and also build the `air-routes` dataset.
Each file includes detailed comments that explain its purpose.


# Generating all formats of the air-route data (routegen.sh)

Many of the files in the `sample-data` folder, as well as files in the `demos` folder, can be produced by running the `routegen.sh` script. 


The script builds the air-routes graph in various formats including GraphML, Gremlin (Groovy) steps, CSV, RDF and SQL.  This script is most commonly used when a new update of the data is going to be published. The `README-air-routes-latest.txt` file is also generated as part of this
process. Some additional files are generated containing information about the graph. Not all of the generated files are currently pushed back into this repo (they are flagged with an asterisk (*) below. 

If running on a Mac use `routegen.sh mac`.

The script assumes that the "graphml2csv.py" file has been downloaded from
https://github.com/awslabs/amazon-neptune-tools/tree/master/graphml2csv and is available in the same directory as the script.

When run, the following files will be generated:

- air-routes-small-latest.graphml
- air-routes-latest.graphml
- air-routes-small-latest-visjs.html
- air-routes-latest-visjs.html
- airports-latest.html
- degree-all-sorted.lst (*)
- air-routes-latest-edges.csv
- air-routes-latest-nodes.csv
- make-air-routes-latest.groovy (*)
- nodes.sparql
- edges.sparql
- airports.sql
- routes.sql
- routes-iata.sql
- README-air-routes-latest.txt  
