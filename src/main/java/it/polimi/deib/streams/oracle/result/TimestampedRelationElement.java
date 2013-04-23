package it.polimi.deib.streams.oracle.result;

import java.util.HashMap;
import java.util.Map;

public class TimestampedRelationElement{
	private Map<String, Object> outputTuple;
	private long tupleTimestamp, outputRelationTimestamp;
	
	public TimestampedRelationElement(){
		outputTuple = new HashMap<String, Object>();
	}
	
	public void add(String key, Object value) {
		outputTuple.put(key, value);
	}
	
	Map<String,Object> getBinding() {
		return outputTuple;
	}
	
	public long getTimestamp() {
		return tupleTimestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.tupleTimestamp = timestamp;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TimestampedRelationElement){
			Map<String, Object> binding = getBinding();
			if(outputTuple.size()!=binding.size())
				return false;
			for(String key : outputTuple.keySet())
				if(!outputTuple.get(key).equals(binding.get(key)))
					return false;
			return true;
		}
		return false;
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
	
	@Override
	public int hashCode() {
		return outputTuple.hashCode();
	}
}
