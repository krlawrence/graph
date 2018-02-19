echo "*** Producing HTML ***"
asciidoctor Gremlin-Graph-Guide.adoc
echo "*** Producing DOCBOOK ***"
asciidoctor -n -b docbook -d book Gremlin-Graph-Guide.adoc -o krltemp.xml 
sed -e s/language=\"groovy\"/language=\"java\"/ krltemp.xml > Gremlin-Graph-Guide.xml
rm krltemp.xml
echo "*** Producing EPUB ***"
pandoc -f docbook -t epub -N --number-sections --chapters --toc --toc-depth=4 Gremlin-Graph-Guide.xml -o Gremlin-Graph-Guide.epub
echo "*** Producing MOBI ***"
ebook-convert Gremlin-Graph-Guide.epub Gremlin-Graph-Guide.mobi
echo "*** Producing PDF ***"
asciidoctor-pdf Gremlin-Graph-Guide.adoc
