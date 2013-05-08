package eu.planetdata.srbench.oracle.io.serializer;


import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

import eu.planetdata.srbench.oracle.result.TimestampedRelationElement;

public class TimestampedRelationElementSerializer extends SerializerBase<TimestampedRelationElement> {
	
	public TimestampedRelationElementSerializer() {
		super(TimestampedRelationElement.class, true);
	}
	
	@Override
	public void serialize(TimestampedRelationElement tre, JsonGenerator jGen, SerializerProvider sPro)
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
		jGen.writeNumberField("timestamp", tre.getTimestamp());
		jGen.writeObjectField("binding", tre.getBinding());
	    jGen.writeEndObject();	}
}
