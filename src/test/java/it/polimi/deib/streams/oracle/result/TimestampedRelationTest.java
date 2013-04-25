package it.polimi.deib.streams.oracle.result;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class TimestampedRelationTest {
	private static final Logger logger = LoggerFactory.getLogger(TimestampedRelationTest.class);
	private TimestampedRelation oracleRel;
	private TimestampedRelation streamProcRel;
	
	private static TimestampedRelation importRelation(String filename){
		try {
			TimestampedRelation ret;
			Configuration relationImporter = new PropertiesConfiguration("timestampedrelation.properties");

			ret = new TimestampedRelation();
			ret.setComputationTimestamp(relationImporter.getLong("timestamp"));
			
			for(String s : relationImporter.getStringArray("sensor")){
				TimestampedRelationElement tre = new TimestampedRelationElement();
				tre.add("sensor", new URIImpl(s));
				tre.setTimestamp(20000);
			}
			return ret;
		} catch (ConfigurationException e) {
			logger.error("Error while reading the configuration file", e);
			fail();
		}
		return null;
	}
	
	@Test public void twoEqualRelationsShouldBeEquals(){
		streamProcRel = importRelation("timestampedrelation.properties");
		oracleRel = importRelation("timestampedrelation.properties");
		assertTrue(oracleRel.equals(streamProcRel));
	}

	@Test public void twoDifferentRelationsShouldNotBeEquals(){
		streamProcRel = importRelation("timestampedrelation.properties");
		oracleRel = importRelation("timestampedrelation.properties");
		TimestampedRelationElement tre = new TimestampedRelationElement();
		tre.add("sensor", "http://ex.org/additionalElement");
		tre.setTimestamp(0);
		oracleRel.addElement(tre);
		assertFalse(oracleRel.equals(streamProcRel));
		assertFalse(streamProcRel.equals(oracleRel));
	}
	
	@Test public void differenceBetweenTwoEqualRelationsProducesAnEmptyRelation(){
		streamProcRel = importRelation("timestampedrelation.properties");
		oracleRel = importRelation("timestampedrelation.properties");
		assertEquals(0, streamProcRel.minus(oracleRel).size());
	}

	@Test public void differenceBetweenTwoDifferentRelationsProducesANonEmptyRelation(){
		streamProcRel = importRelation("timestampedrelation.properties");
		oracleRel = importRelation("timestampedrelation.properties");
		TimestampedRelationElement tre = new TimestampedRelationElement();
		tre.add("sensor", "http://ex.org/additionalElement");
		tre.setTimestamp(0);
		oracleRel.addElement(tre);
		assertEquals(0, streamProcRel.minus(oracleRel).size());
		assertEquals(1, oracleRel.minus(streamProcRel).size());
	}

}
