#!/bin/bash

set -e

# Validate asciidoc structure and formatting
./bin/check.sh

# create the target directory for where the book will build
rm -rf target
mkdir target

echo "*** Producing HTML ***"
asciidoctor book/Practical-Gremlin.adoc -o target/Practical-Gremlin.html

# asciidoctor won't properly insert the title image and reformatted title so we do
# that manually here
search_string="<h1>PRACTICAL GREMLIN: An Apache TinkerPop Tutorial<\/h1>"
replace_string='<img src="PRACTICAL-GREMLIN-2nd-edition.png">\n<h1>PRACTICAL GREMLIN:<br\/>An Apache TinkerPop Tutorial<\/h1>'
sed -i "s/$search_string/${replace_string}/g" "target/Practical-Gremlin.html"

cp target/Practical-Gremlin.html target/index.html
cp images/PRACTICAL-GREMLIN-2nd-edition.png target/

echo "*** Producing DOCBOOK ***"
asciidoctor -n -b docbook -d book book/Practical-Gremlin.adoc -o target/krltemp.xml
sed -e s/language=\"groovy\"/language=\"java\"/ target/krltemp.xml > target/Practical-Gremlin.xml
rm target/krltemp.xml
echo "*** Producing EPUB ***"
pandoc -f docbook -t epub -N --number-sections --top-level-division=chapter --toc --toc-depth=4 target/Practical-Gremlin.xml -o target/Practical-Gremlin.epub
echo "*** Producing MOBI ***"
ebook-convert target/Practical-Gremlin.epub target/Practical-Gremlin.mobi
echo "*** Producing PDF ***"
asciidoctor-pdf book/Practical-Gremlin.adoc -o target/Practical-Gremlin.pdf
