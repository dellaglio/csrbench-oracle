package eu.planetdata.srbench.oracle.result;


import org.junit.Test;
import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.Utility;
import eu.planetdata.srbench.oracle.result.OutputStreamResult;

public class OutputStreamResultTest {
	private static final Logger logger = LoggerFactory.getLogger(OutputStreamResultTest.class);

	private OutputStreamResult oracleResult;
	private OutputStreamResult processorResult;
	
	@Test public void aResultShouldContainItself(){
		oracleResult = new OutputStreamResult();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		assertTrue(oracleResult.contains(oracleResult));
	}
	
	@Test public void aResultShouldContainItself2(){
		oracleResult = new OutputStreamResult();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		processorResult = new OutputStreamResult();
		processorResult .addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		assertTrue(oracleResult.contains(processorResult));
	}
	
	@Test public void differentOrderShouldProduceFalse(){
		oracleResult = new OutputStreamResult();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		
		processorResult = new OutputStreamResult();
		processorResult .addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		assertFalse(oracleResult.contains(processorResult));
	}
	
	@Test public void subsequenceInTheHeadShouldProduceTrue(){
		oracleResult = new OutputStreamResult();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		processorResult = new OutputStreamResult();
		processorResult .addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		assertTrue(oracleResult.contains(processorResult));
	}
	
	@Test public void subsequenceInTheTailShouldProduceTrue(){
		oracleResult = new OutputStreamResult();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		processorResult = new OutputStreamResult();
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		assertTrue(oracleResult.contains(processorResult));
	}
}
