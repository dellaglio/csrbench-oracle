package it.polimi.deib.streams.oracle.result;

import it.polimi.deib.streams.oracle.Utility;

import org.junit.Test;
import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputStreamResultTest {
	private static final Logger logger = LoggerFactory.getLogger(OutputStreamResultTest.class);

	private OutputStreamResult oracleResult;
	private OutputStreamResult processorResult;
	
	@Test public void aResultShouldContainItself(){
		oracleResult = new OutputStreamResult(false);
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		assertTrue(oracleResult.contains(oracleResult));
	}
	
	@Test public void aResultShouldContainItself2(){
		oracleResult = new OutputStreamResult(false);
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		processorResult = new OutputStreamResult(false);
		processorResult .addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		assertTrue(oracleResult.contains(processorResult));
	}
	
	@Test public void differentOrderShouldProduceFalse(){
		oracleResult = new OutputStreamResult(false);
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		
		processorResult = new OutputStreamResult(false);
		processorResult .addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		assertFalse(oracleResult.contains(processorResult));
	}
	
	@Test public void subsequenceInTheHeadShouldProduceTrue(){
		oracleResult = new OutputStreamResult(false);
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		processorResult = new OutputStreamResult(false);
		processorResult .addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		assertTrue(oracleResult.contains(processorResult));
	}
	
	@Test public void subsequenceInTheTailShouldProduceTrue(){
		oracleResult = new OutputStreamResult(false);
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		
		processorResult = new OutputStreamResult(false);
		processorResult .addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		processorResult .addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		assertTrue(oracleResult.contains(processorResult));
	}
}
