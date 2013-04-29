package it.polimi.deib.streams.oracle.io;

import java.io.IOException;
import java.util.Iterator;

import it.polimi.deib.streams.oracle.Utility;
import it.polimi.deib.streams.oracle.io.serializer.OutputStreamResultSerializer;
import it.polimi.deib.streams.oracle.io.serializer.TimestampedRelationElementSerializer;
import it.polimi.deib.streams.oracle.io.serializer.TimestampedRelationSerializer;
import it.polimi.deib.streams.oracle.io.serializer.URISerializer;
import it.polimi.deib.streams.oracle.result.OutputStreamResult;
import it.polimi.deib.streams.oracle.result.TimestampedRelation;
import it.polimi.deib.streams.oracle.result.TimestampedRelationElement;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSonConverterTest {
	private static final Logger logger = LoggerFactory.getLogger(JSonConverterTest.class);

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();

		String s = "{\"relations\":[{\"head\":{\"vars\":[\"sensor\"]},\"timestamp\":18000,\"results\":{\"bindings\":[{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1166\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0837\"}}},{\"timestamp\":18000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"}}},{\"timestamp\":20000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0834\"}}},{\"timestamp\":20000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1193\"}}}]}},{\"head\":{\"vars\":[\"sensor\"]},\"timestamp\":19000,\"results\":{\"bindings\":[{\"timestamp\":20000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0835\"}}},{\"timestamp\":20000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1192\"}}},{\"timestamp\":20000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C0834\"}}},{\"timestamp\":20000,\"binding\":{\"sensor\":{\"type\":\"uri\",\"value\":\"http://knoesis.wright.edu/ssw/System_C1167\"}}}]}}]}";

		SimpleModule oracleModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
		oracleModule.addSerializer(new URISerializer());
		oracleModule.addSerializer(new TimestampedRelationElementSerializer());
		oracleModule.addSerializer(new TimestampedRelationSerializer());
		oracleModule.addSerializer(new OutputStreamResultSerializer());

		oracleModule.addDeserializer(OutputStreamResult.class, new JsonDeserializer<OutputStreamResult>(){
			@Override
			public OutputStreamResult deserialize(JsonParser jp, DeserializationContext ctxt)
					throws IOException, JsonProcessingException {

				OutputStreamResult ret = new OutputStreamResult();
				
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
					while(bit.hasNext()){
						TimestampedRelationElement tre = new TimestampedRelationElement();

						JsonNode element = bit.next();
						tre.setTimestamp(element.get("timestamp").asLong());
						for(String var : vars){
							JsonNode value = element.get("binding").get(var);
							if(value.get("type").asText().equals("uri")){
								URI uri = new URIImpl(value.get("value").asText());
								tre.add(var, uri);
							}
							else
								throw new RuntimeException("not a uri");
						}
						tr.addElement(tre);
					}
					ret.addRelation(tr);
				}
				return ret;
			}
		});
		mapper.registerModule(oracleModule);

		OutputStreamResult oracleResult = new OutputStreamResult();
		oracleResult.addRelation(Utility.importRelation("timestampedrelation18.properties", logger));
		oracleResult.addRelation(Utility.importRelation("timestampedrelation19.properties", logger));
		//		oracleResult.addRelation(Utility.importRelation("timestampedrelation20.properties", logger));
		try {
			mapper.writeValue(System.out, oracleResult);
//			OutputStreamResult osr = mapper.readValue(s, OutputStreamResult.class);
//			logger.info("Parsed stream: {}", osr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
