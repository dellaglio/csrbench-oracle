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
package eu.planetdata.srbench.oracle.io.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.openrdf.model.Literal;

public class LiteralSerializer extends SerializerBase<Literal> {
	
	public LiteralSerializer() {
		super(Literal.class, true);
	}
	
	@Override
	public void serialize(Literal literal, JsonGenerator jGen, SerializerProvider sPro)
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
    	jGen.writeStringField("type", "literal");
	    if(literal.getDatatype()!=null)
		    jGen.writeStringField("datatype", literal.getDatatype().toString());
	    if(literal.getLanguage()!=null){
	    	jGen.writeStringField("xml:lang", literal.getLanguage());
	    }
	    jGen.writeStringField("value", literal.toString());
	    jGen.writeEndObject();	}
}
