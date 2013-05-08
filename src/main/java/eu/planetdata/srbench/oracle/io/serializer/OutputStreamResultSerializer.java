package eu.planetdata.srbench.oracle.io.serializer;


import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;

public class OutputStreamResultSerializer extends SerializerBase<StreamProcessorOutput> {

	public OutputStreamResultSerializer() {
		super(StreamProcessorOutput.class, true);
	}

	@Override
	public void serialize(StreamProcessorOutput ors, JsonGenerator jGen, SerializerProvider sPro)
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
		jGen.writeObjectField("relations", ors.getResultRelations());
		jGen.writeEndObject();	
	}
}
