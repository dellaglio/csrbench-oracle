package it.polimi.deib.streams.oracle.result.json.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.openrdf.model.URI;

public class URISerializer extends SerializerBase<URI> {
	
	public URISerializer() {
		super(URI.class, true);
	}
	
	@Override
	public void serialize(URI uri, JsonGenerator jGen, SerializerProvider sPro)
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
	    jGen.writeStringField("type", "uri");
	    jGen.writeStringField("value", uri.toString());
	    jGen.writeEndObject();	}
}
