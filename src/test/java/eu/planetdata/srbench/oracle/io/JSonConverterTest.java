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

package eu.planetdata.srbench.oracle.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.Utility;
import eu.planetdata.srbench.oracle.io.JsonConverter;
import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;

public class JSonConverterTest {
	private static final Logger logger = LoggerFactory.getLogger(JSonConverterTest.class);

	@Test public void shouldEncodeResults(){
		StreamProcessorOutput oracleResult = new StreamProcessorOutput();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		JsonConverter converter = new JsonConverter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		converter.encodeJson(baos, oracleResult);
		System.out.println(baos.toString());
		assertEquals(baos.toString(), "{\"relations\":[{\"head\":{\"vars\":[\"sensor\"]},\"timestamp\":18000,\"results\":{\"bindings\":[{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1166\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0837\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0834\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1193\"}}}]}},{\"head\":{\"vars\":[\"sensor\"]},\"timestamp\":19000,\"results\":{\"bindings\":[{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0835\"}}},{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"}}},{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0834\"}}},{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1167\"}}}]}}]}");
	}
	
	@Test public void shouldDecodeResults(){
		JsonConverter converter = new JsonConverter();
		
		String s = "{\"relations\":[{\"head\":{\"vars\":[\"sensor\"]},\"timestamp\":18000,\"results\":{\"bindings\":[{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1166\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0837\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0834\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1193\"}}}]}},{\"head\":{\"vars\":[\"sensor\"]},\"timestamp\":19000,\"results\":{\"bindings\":[{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0835\"}}},{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"}}},{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0834\"}}},{\"timestamp\":19000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1167\"}}}]}}]}";
		InputStream is = new ByteArrayInputStream(s.getBytes());
		StreamProcessorOutput result = converter.decodeJson(is);
		
		StreamProcessorOutput oracleResult = new StreamProcessorOutput();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));

		assertEquals(result, oracleResult);
	}
	
	@Test public void shouldDecodeResultsInFile(){
		JsonConverter converter = new JsonConverter();				

		String s = "{\"results\":[{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":13500,\"results\":{\"bindings\":[{\"timestamp\":13500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"83.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C1192_2004_08_08_07_05_00\"}}}]}},{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":15500,\"results\":{\"bindings\":[{\"timestamp\":15500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0837\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"97.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C0837_2004_08_08_07_15_00\"}}}]}},{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":16500,\"results\":{\"bindings\":[{\"timestamp\":16500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"83.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C1192_2004_08_08_07_20_00\"}}}]}},{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":18500,\"results\":{\"bindings\":[{\"timestamp\":18500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0837\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"97.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C0837_2004_08_08_07_30_00\"}}}]}},{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":19500,\"results\":{\"bindings\":[{\"timestamp\":19500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"83.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C1192_2004_08_08_07_35_00\"}}}]}},{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":21500,\"results\":{\"bindings\":[{\"timestamp\":21500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0837\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"97.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C0837_2004_08_08_07_45_00\"}}}]}},{\"head\":{\"vars\":[\"sensor\",\"value\",\"obs\"]},\"timestamp\":22500,\"results\":{\"bindings\":[{\"timestamp\":22500,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"},\"value\":{\"type\":\"literal\",\"datatype\":\"http://www.w3.org/2001/XMLSchema#string\",\"value\":\"83.0\"},\"obs\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/Observation_AirTemperature_C1192_2004_08_08_07_50_00\"}}}]}}]}";
		InputStream is = new ByteArrayInputStream(s.getBytes());
		StreamProcessorOutput expectedResult = converter.decodeJson(is);

		StreamProcessorOutput result = converter.decodeJson(
				getClass().getClassLoader().getResourceAsStream("testresult.json"));
		
		assertEquals(expectedResult, result);
		
	}
}
