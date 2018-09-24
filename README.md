# GGIT

This application is a version/revision control system for graph databases.

Gradle will be looked into to make it easier to build this project.

Software Requirements (Pre-Gradle)
===
This software is built on
- Neo4j Community Edition
- Neo4j Spatial
- Java

Instructions for setting up Neo4j Community Edition
===
- Visit https://neo4j.com/download-center/#releases to download the latest version of Neo4j Community Edition.
- Extract the .zip file that you just downloaded into, preferably, your user directory.
- The extraction nests a 'neo4j-community-*version#*' directory inside of itself.

### Setting up Neo4j Browser
The Neo4j Browser is built into the Neo4j Community Edition download. This browser is hosted at 'localhost:7474/browser/'.

Open the 'conf' directory within 'neo4j-community-*version#*'.
- On line 9, uncomment this line by removing the '#'.
- On line 12, you should see '#dbms.directories.data=data'. Change this line to be 'dbms.directories.data={location of directory holding graph database for browser use (dir/graph.db)}'.

We are going to set up a PowerShell script to stream line the use of the browser. To set up the PS script, open Windows PowerShell ISE, copy the following code, and replace the curly braces with the appropriate directories:
```bash
rm {(0) location of graph database for browser use (graph.db)} -r -fo;
cp {(1) location of project graph database (graph.db)} {(0) location of graph database for browser use (graph.db)} -r;
bin\neo4j console
```
Save this script to the deepest 'neo4j-community-*version#*' folder. This folder also holds the 'README.txt' file for Neo4j.
You must always run this script in this folder.

Instructions for setting up Neo4j Spatial
===
Neo4j Spatial will allow for spatial querying.

# Running GGIT

To run GGIT, 'cd' into the GGIT directory and do the following commands
```bash
javac GGIT.java
java GGIT {command}
```

Current Commands
===
(None of the following commands are currently supported.)
* init
* clone
* add
* commit
* push
* status
* remote
* checkout
* branch
* pull
* merge
* diff
* log
* fetch
* reset
* grep