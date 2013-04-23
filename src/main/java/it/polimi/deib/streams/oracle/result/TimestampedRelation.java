package it.polimi.deib.streams.oracle.result;

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

	public Set<TimestampedRelationElement> getElements() {
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
		Set<TimestampedRelationElement> subtrahend = subtrahendRelation.getElements();
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
	
}
