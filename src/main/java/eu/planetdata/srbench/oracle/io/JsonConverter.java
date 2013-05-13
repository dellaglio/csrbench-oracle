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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.io.serializer.LiteralSerializer;
import eu.planetdata.srbench.oracle.io.serializer.OutputStreamResultSerializer;
import eu.planetdata.srbench.oracle.io.serializer.TimestampedRelationElementSerializer;
import eu.planetdata.srbench.oracle.io.serializer.TimestampedRelationSerializer;
import eu.planetdata.srbench.oracle.io.serializer.URISerializer;
import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;
import eu.planetdata.srbench.oracle.result.TimestampedRelation;
import eu.planetdata.srbench.oracle.result.TimestampedRelationElement;

public class JsonConverter {
	private ObjectMapper mapper;
	private static final Logger logger=LoggerFactory.getLogger(JsonConverter.class); 
	
	public JsonConverter(){
		mapper = new ObjectMapper();

		SimpleModule oracleModule = new SimpleModule("OracleModule", new Version(1, 0, 0, null));
		oracleModule.addSerializer(new URISerializer());
		oracleModule.addSerializer(new LiteralSerializer());
		oracleModule.addSerializer(new TimestampedRelationElementSerializer());
		oracleModule.addSerializer(new TimestampedRelationSerializer());
		oracleModule.addSerializer(new OutputStreamResultSerializer());

		oracleModule.addDeserializer(StreamProcessorOutput.class, new JsonDeserializer<StreamProcessorOutput>(){
			@Override
			public StreamProcessorOutput deserialize(JsonParser jp, DeserializationContext ctxt)
					throws IOException, JsonProcessingException {

				StreamProcessorOutput ret = new StreamProcessorOutput();
				
				jp.nextValue();

				JsonNode relations = jp.readValueAsTree();
				//get the timestamped relations
				Iterator<JsonNode> rit = relations.iterator();
				while(rit.hasNext()){
					TimestampedRelation tr = new TimestampedRelation();
					JsonNode n = rit.next();
					tr.setComputationTimestamp(n.get("timestamp").asLong());
					JsonNode results = n.get("results").get("bindings");
					JsonNode varsNode = n.get("head").get("vars");
					String[] vars = new String[varsNode.size()]; 
					for(int i = 0; i < varsNode.size(); i++)
						vars[i]=varsNode.get(i).asText();

					Iterator<JsonNode> bit = results.iterator();
					ValueFactory vf = new ValueFactoryImpl();
					if(!bit.hasNext()){
						long timestamp = n.get("timestamp").asLong();
						TimestampedRelationElement tre = null;
						tre = TimestampedRelationElement.createEmptyTimestampedRelationElement(timestamp);
						tr.addElement(tre);
					}
					while(bit.hasNext()){
						TimestampedRelationElement tre = null;
						tre = new TimestampedRelationElement();

						JsonNode element = bit.next();
						long timestamp = element.get("timestamp").asLong();
						tre.setTimestamp(timestamp);
						for(String var : vars){
							JsonNode value = element.get("binding").get(var);
							if(value.get("type").asText().equals("uri")){
								URI uri = vf.createURI(value.get("value").asText()); 
								tre.add(var, uri);
							} else if(value.get("type").asText().equals("literal")){
								Literal literal;
								if(value.get("datatype")!=null)
									literal = vf.createLiteral(value.get("value").asText(), vf.createURI(value.get("datatype").asText()));
								else if(value.get("xml:lang")!=null)
									literal = vf.createLiteral(value.get("value").asText(), value.get("xml:lang").asText());
								else
									literal = vf.createLiteral(value.get("value").asText());
								tre.add(var, literal);							
							} else throw new RuntimeException("not a uri or literal");
						}
						tr.addElement(tre);
					}
					ret.addRelation(tr);
				}
				return ret;
			}
		});
		mapper.registerModule(oracleModule);
	}
	
	public StreamProcessorOutput decodeJson(InputStream is){
		try {
			StreamProcessorOutput osr;
			osr = mapper.readValue(is, StreamProcessorOutput.class);
			logger.debug("Parsed stream: {}", osr);
			return osr;
		} catch (IOException e) {
			logger.error("Error while decoding the Json", e);
		}
		return null;
	}
	
	public void encodeJson(OutputStream os, StreamProcessorOutput res){
		try {
			mapper.writeValue(os, res);
		} catch (IOException e) {
			logger.error("Error while decoding the Json", e);
		}
	}
}
