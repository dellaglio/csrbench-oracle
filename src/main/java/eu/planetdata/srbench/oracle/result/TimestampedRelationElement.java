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
package eu.planetdata.srbench.oracle.result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openrdf.model.Literal;

public class TimestampedRelationElement{
	private Map<String, Object> outputTuple;
	private Set<String> numericKeys;
	private long tupleTimestamp, outputRelationTimestamp;

	public TimestampedRelationElement(){
		outputTuple = new HashMap<String, Object>();
		numericKeys = new HashSet<String>();
	}

	public void add(String key, Object value) {
		outputTuple.put(key, value);
		if(value instanceof Literal){
			try{
				((Literal) value).doubleValue(); 
				numericKeys.add(key);
			}
			catch(Exception e){}
		}
	}

	public Map<String,Object> getBinding() {
		return outputTuple;
	}

	public long getTimestamp() {
		return tupleTimestamp;
	}

	public void setTimestamp(long timestamp) {
		this.tupleTimestamp = timestamp;
	}

	@Override
	public int hashCode() {
		int result = 1;
		final int prime = 31;
		if(numericKeys.size()==0)
			result = prime * result
					+ ((outputTuple == null) ? 0 : outputTuple.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimestampedRelationElement other = (TimestampedRelationElement) obj;
		if (outputTuple == null) {
			if (other.outputTuple != null)
				return false;
		} else if (!outputTuple.equals(other.outputTuple)){
			if(numericKeys.size()>0){
				for(String key : numericKeys){
					Literal l1 = (Literal)outputTuple.get(key);
					Literal l2 = (Literal)other.outputTuple.get(key);
					if(l1.doubleValue()!=l2.doubleValue())
						return false;
				}
				return true;
			}
			return false;
		}
		return true;
	}

	public long getOutputRelationTimestamp() {
		return outputRelationTimestamp;
	}

	public void setComputationTimestamp(long computationTimestamp) {
		this.outputRelationTimestamp = computationTimestamp;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("\n<");
		for(String key : outputTuple.keySet())
			ret.append(key+"="+outputTuple.get(key)+ " ");
		ret.append(">:["+tupleTimestamp+"]");
		return ret.toString();
	}

	public static TimestampedRelationElement createEmptyTimestampedRelationElement(long timestamp){
		TimestampedRelationElement tre = new TimestampedRelationElement();
		tre.setTimestamp(timestamp);
		return tre;
	}
}
