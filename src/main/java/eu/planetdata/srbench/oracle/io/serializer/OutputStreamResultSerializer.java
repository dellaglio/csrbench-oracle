package eu.planetdata.srbench.oracle.io.serializer;


import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

import eu.planetdata.srbench.oracle.result.OutputStreamResult;

public class OutputStreamResultSerializer extends SerializerBase<OutputStreamResult> {

	public OutputStreamResultSerializer() {
		super(OutputStreamResult.class, true);
	}

	@Override
	public void serialize(OutputStreamResult ors, JsonGenerator jGen, SerializerProvider sPro)
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
		jGen.writeObjectField("relations", ors.getResultRelations());
		jGen.writeEndObject();	
	}
}
