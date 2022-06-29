echo "*** Producing HTML ***"
asciidoctor Gremlin-Graph-Guide-ja.adoc
echo "*** Producing DOCBOOK ***"
asciidoctor -n -b docbook -d book Gremlin-Graph-Guide-ja.adoc -o krltemp.xml 
sed -e s/language=\"groovy\"/language=\"java\"/ krltemp.xml > Gremlin-Graph-Guide-ja.xml
rm krltemp.xml
echo "*** Producing EPUB ***"
pandoc -f docbook -t epub -N --number-sections --top-level-division=chapter --toc --toc-depth=4 Gremlin-Graph-Guide-ja.xml -o Gremlin-Graph-Guide-ja.epub
echo "*** Producing MOBI ***"
ebook-convert Gremlin-Graph-Guide-ja.epub Gremlin-Graph-Guide-ja.mobi
echo "*** Producing PDF ***"
asciidoctor-pdf -a pdf-theme=default-with-fallback-font -a scripts=cjk Gremlin-Graph-Guide-ja.adoc
