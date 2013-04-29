rdfstream-validator
===================
Requirements:
 * Java 7+
 * Maven

The project has two main files:
 * SRBenchImporter: imports the RDF input stream in the oracle
 * Oracle: executes the test queries over the RDF stream, produces the results. If a results to be compared is provided, the Oracle verifies that it is correct.

Configuration:
 * The project can be configured editing the setup.properties file
