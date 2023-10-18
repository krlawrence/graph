## Sample Code

This folder contains code snippets and samples referenced by the book as well as 
additional sample files. The snippets are organized by programming language in 
the various directories shown. More samples will be added over time.

Some of the samples use data from CSV files that can be found in the 
*'/sample-data'* folder.

### Groovy

Note that files with a ".groovy" extension but with all lowercase names are 
intended to be launched from within the Gremlin Console. Files with a ".groovy" 
extension and capitalized names, such as `TinkerGraphTest.groovy`, are standalone 
groovy applications that should be run from outside of the console using the groovy 
runtime.

### Java

Files with a ".java" extension are standalone Java applications that need to be 
compiled and run using a Java JDK and require the Apache TinkerPop Java JAR files to 
be available.

Files that have "remote" in the name such as `RemoteStats.java` are designed to be 
run against a Gremlin Server rather than a local in memory TinkerGraph. It is 
important to understand the differences between these two environments and that is 
what these samples aim to help with.

### Python

Files with a ".py" extension are stand alone Python applications that require the 
Gremlin Python library to have been installed using PIP or an equivalent method. 

### .NET

Files with a ".cs" extension are C# files that require a Microsoft .Net runtime and 
the Gremlin.Net driver installed via NuGet or similar.

### Javascript

Files with a ".js" extension are JavaScript files designed to be run using Node.js 
and the Gremlin JavaScript driver available from NPM.

### Go

Files with a ".go" extension are Go files designed to be compiled and run using the 
Apache TInkerPop Go client.
