package eu.planetdata.srbench.oracle.result;

import java.util.HashSet;
import java.util.Set;

public class TimestampedRelation {
	Set<TimestampedRelationElement> results;
	long computationTimestamp;

	public TimestampedRelation() {
		super();
		results = new HashSet<TimestampedRelationElement>();
	}

	public TimestampedRelation(Set<TimestampedRelationElement> results,
			long computationTimestamp) {
		super();
		this.results = results;
		this.computationTimestamp = computationTimestamp;
	}

	public Set<TimestampedRelationElement> getBindings() {
		return results;
	}

	public void setResults(Set<TimestampedRelationElement> results) {
		this.results = results;
	}

	public long getComputationTimestamp() {
		return computationTimestamp;
	}

	public void setComputationTimestamp(long computationTimestamp) {
		this.computationTimestamp = computationTimestamp;
	}
	
	public void addElement(TimestampedRelationElement tre){
		results.add(tre);
	}

	public Set<TimestampedRelationElement> minus(TimestampedRelation subtrahendRelation){
		Set<TimestampedRelationElement> ret = new HashSet<TimestampedRelationElement>();
		Set<TimestampedRelationElement> subtrahend = subtrahendRelation.getBindings();
		for(TimestampedRelationElement elem : results){
			if(!subtrahend.contains(elem))
				ret.add(elem);
		}
		return ret;
	}
	
	public static TimestampedRelation createEmptyRelation(long timestamp){
		TimestampedRelation rel = new TimestampedRelation();
		rel.setComputationTimestamp(timestamp);
		return rel;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for(TimestampedRelationElement srr : results)
			ret.append(srr.toString());
		return ret.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((results == null) ? 0 : results.hashCode());
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
		TimestampedRelation other = (TimestampedRelation) obj;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		return true;
	}
	
	
	
}
