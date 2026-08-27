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

# Advertise the agent-friendly Markdown index (llms.txt) from the HTML so agents
# can discover it. The link is relative so it resolves both locally and on the
# published site (index.html and llms.txt both live at the site root).
sed -i 's#</head>#<link rel="alternate" type="text/markdown" title="llms.txt" href="llms.txt">\n</head>#' target/index.html

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

echo "*** Producing MARKDOWN + LLMS.TXT ***"
# check.sh already ran above; make-llms.sh only needs to build and validate.
# Requires Node.js (downdoc + afdocs); skip gracefully if it is not installed so
# the rest of the book build still succeeds.
if command -v downdoc >/dev/null 2>&1 || command -v npm >/dev/null 2>&1; then
    ./bin/make-llms.sh --no-check
else
    echo "Skipping: downdoc/npm (Node.js) not found. Install Node.js to build llms.txt."
fi
