package it.polimi.deib.streams.oracle.result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openrdf.model.Literal;
import org.openrdf.model.vocabulary.XMLSchema;

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

//	@Override
//	public boolean equals2(Object obj) {
//		System.out.println("aaa");
//		if(obj instanceof TimestampedRelationElement){
//			Map<String, Object> binding = getBinding();
//			System.out.println(outputTuple.size() + " " + binding.size());
//			if(outputTuple.size()!=binding.size())
//				return false;
//			for(String key : outputTuple.keySet()){
//				System.out.println(outputTuple.get(key).toString() + " " + binding.get(key).toString());
//				if(!outputTuple.get(key).toString().equals(binding.get(key).toString()))
//					return false;
//			}
//			return true;
//		}
//		return false;
//	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
			for(String key : numericKeys){
				Literal l1 = (Literal)outputTuple.get(key);
				Literal l2 = (Literal)other.outputTuple.get(key);
				if(l1.doubleValue()!=l2.doubleValue())
					return false;
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
