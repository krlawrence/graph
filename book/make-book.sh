echo "*** Producing HTML ***"
asciidoctor Practical-Gremlin.adoc
echo "*** Producing DOCBOOK ***"
asciidoctor -n -b docbook -d book Practical-Gremlin.adoc -o krltemp.xml
sed -e s/language=\"groovy\"/language=\"java\"/ krltemp.xml > Practical-Gremlin.xml
rm krltemp.xml
echo "*** Producing EPUB ***"
pandoc -f docbook -t epub -N --number-sections --top-level-division=chapter --toc --toc-depth=4 Practical-Gremlin.xml -o Practical-Gremlin.epub
echo "*** Producing MOBI ***"
ebook-convert Practical-Gremlin.epub Practical-Gremlin.mobi
echo "*** Producing PDF ***"
asciidoctor-pdf Practical-Gremlin.adoc
