CSRBench-oracle
===================
Requirements:
 * Java 7+
 * Maven

The project has two main files:
 * (eu.planetdata.srbench.oracle.repository.)SRBenchImporter : imports the RDF input stream in the oracle
 * (eu.planetdata.srbench.oracle.)Oracle: executes the test queries over the RDF stream, produces the results. If a results to be compared is provided, the Oracle verifies that it is correct.

Configuration:
 * The project can be configured editing the setup.properties file. See https://github.com/dellaglio/csrbench-oracle/wiki/Configuration for a description of the properties.

Import the data stream into the oracle:
 * It is done through the SRBenchImporter
 * Data should be described in Turtle and it should be stored in the folder data. Filenames should follow the pattern data_xx.ttl, where xx is the application timestamp associated to the triples in the file.

Execute the Oracle:
 * Run the Oracle. Depending on the setup configuration, for each query to be executed, two possible behaviours are possible:
  * If there is no answer associated to the query, the oracle produces all the possible answers according to the input configuration;
  * If there is an answer associated to the query, the oracle starts to produce an answer and comparing it with the input one. If it finds a match, it stops, otherwise it repeats.
