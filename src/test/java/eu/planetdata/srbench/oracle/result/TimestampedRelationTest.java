/*******************************************************************************
 * Copyright 2013 Politecnico di Milano, Universidad Politécnica de Madrid
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 * Authors: Daniele Dell'Aglio, Jean-Paul Calbimonte, Marco Balduini,
 * 			Oscar Corcho, Emanuele Della Valle
 ******************************************************************************/

package eu.planetdata.srbench.oracle.result;


import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.Utility;
import eu.planetdata.srbench.oracle.result.TimestampedRelation;
import eu.planetdata.srbench.oracle.result.TimestampedRelationElement;

import static org.junit.Assert.*;

public class TimestampedRelationTest {
	private static final Logger logger = LoggerFactory.getLogger(TimestampedRelationTest.class);
	private TimestampedRelation oracleRel;
	private TimestampedRelation streamProcRel;

	@Before
	public void setup(){
		streamProcRel = Utility.importRelation("timestampedrelation20.properties", logger);
		oracleRel = Utility.importRelation("timestampedrelation20.properties", logger);
	}
	
	@Test public void twoEqualRelationsShouldBeEquals(){
		assertTrue(oracleRel.equals(streamProcRel));
	}

	@Test public void twoDifferentRelationsShouldNotBeEquals(){
		TimestampedRelationElement tre = new TimestampedRelationElement();
		tre.add("sensor", "http://ex.org/additionalElement");
		tre.setTimestamp(0);
		oracleRel.addElement(tre);
		assertFalse(oracleRel.equals(streamProcRel));
		assertFalse(streamProcRel.equals(oracleRel));
	}
	
	@Test public void differenceBetweenTwoEqualRelationsProducesAnEmptyRelation(){
		assertEquals(0, streamProcRel.minus(oracleRel).size());
	}

	@Test public void differenceBetweenTwoDifferentRelationsProducesANonEmptyRelation(){
		TimestampedRelationElement tre = new TimestampedRelationElement();
		tre.add("sensor", "http://ex.org/additionalElement");
		tre.setTimestamp(0);
		oracleRel.addElement(tre);
		assertEquals(0, streamProcRel.minus(oracleRel).size());
		assertEquals(1, oracleRel.minus(streamProcRel).size());
	}

}
