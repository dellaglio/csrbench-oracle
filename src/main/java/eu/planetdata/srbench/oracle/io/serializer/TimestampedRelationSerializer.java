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
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

import eu.planetdata.srbench.oracle.result.TimestampedRelation;
import eu.planetdata.srbench.oracle.result.TimestampedRelationElement;

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
