package it.polimi.deib.streams.oracle.result.json.serializer;

import it.polimi.deib.streams.oracle.result.TimestampedRelation;
import it.polimi.deib.streams.oracle.result.TimestampedRelationElement;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

public class TimestampedRelationSerializer extends SerializerBase<TimestampedRelation> {

	public TimestampedRelationSerializer() {
		super(TimestampedRelation.class, true);
	}

	@Override
	public void serialize(TimestampedRelation tr, JsonGenerator jGen, SerializerProvider sPro)
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
		Set<TimestampedRelationElement> bindings = tr.getBindings();
		Iterator<TimestampedRelationElement> it = bindings.iterator();
		if(it.hasNext()){
			TimestampedRelationElement tre = it.next();
			jGen.writeObjectFieldStart("head");
			jGen.writeObjectField("vars", tre.getBinding().keySet());
			jGen.writeEndObject();
		}
		jGen.writeNumberField("timestamp", tr.getComputationTimestamp());
		jGen.writeObjectFieldStart("results");
		jGen.writeObjectField("bindings", bindings);
		jGen.writeEndObject();
		jGen.writeEndObject();	
	}
}
