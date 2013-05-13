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

package eu.planetdata.srbench.oracle;

import static org.junit.Assert.fail;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;

import eu.planetdata.srbench.oracle.result.TimestampedRelation;
import eu.planetdata.srbench.oracle.result.TimestampedRelationElement;

public class Utility {
	public static TimestampedRelation importRelation(String filename, Logger logger){
		try {
			TimestampedRelation ret;
			Configuration relationImporter = new PropertiesConfiguration(filename);

			ret = new TimestampedRelation();
			ret.setComputationTimestamp(relationImporter.getLong("timestamp"));
			
			for(String s : relationImporter.getStringArray("sensor")){
				TimestampedRelationElement tre = new TimestampedRelationElement();
				tre.add("sensor", new URIImpl(s));
				//FIXME: should import the element timestamp!
				tre.setTimestamp(relationImporter.getLong("timestamp"));
				ret.addElement(tre);
			}
			return ret;
		} catch (ConfigurationException e) {
			logger.error("Error while reading the configuration file", e);
			fail();
		}
		return null;
	}
}
