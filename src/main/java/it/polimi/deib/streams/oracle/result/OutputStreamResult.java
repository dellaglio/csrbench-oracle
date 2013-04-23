package it.polimi.deib.streams.oracle.result;

import java.util.ArrayList;
import java.util.List;

public class OutputStreamResult {
	public enum S2ROperator {Istream, Rstream, Dstream};

	List<TimestampedRelation> results;
	S2ROperator operator;
	TimestampedRelation previousRelation;

	public OutputStreamResult() {
		results = new ArrayList<TimestampedRelation>();
		this.operator=S2ROperator.Rstream;
	}

	public OutputStreamResult(S2ROperator operator) {
		results = new ArrayList<TimestampedRelation>();
		this.operator=operator;
	}

	public List<TimestampedRelationElement> getResults() {
		List<TimestampedRelationElement> ret = new ArrayList<TimestampedRelationElement>();
		for(TimestampedRelation rel : results){
			ret.addAll(rel.getElements());
		}
		return ret;
	}

	public void addRelation(TimestampedRelation relation){
		if(operator.equals(S2ROperator.Rstream)){
			if(relation.getElements().size()==0)
				relation.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
			results.add(relation);
		} else if(operator.equals(S2ROperator.Istream)){
			if(previousRelation==null){
				if(relation.getElements().size()==0)
					relation.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
				results.add(relation);
			}
			else{
				TimestampedRelation diff = new TimestampedRelation(relation.minus(previousRelation), relation.getComputationTimestamp());
				if(diff.getElements().size()==0)
					diff.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
				results.add(diff);
			}
		} else { //Dstream
			if(previousRelation!=null){
				TimestampedRelation diff = new TimestampedRelation(previousRelation.minus(relation), relation.getComputationTimestamp());
				if(diff.getElements().size()==0)
					diff.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
				results.add(diff);
			}
		}
		previousRelation=relation;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for(TimestampedRelation rel: results)
			for(TimestampedRelationElement srr : rel.getElements())
				ret.append(srr.toString());
		return ret.toString();
	}

	public boolean contains(OutputStreamResult rel){
		for(TimestampedRelationElement tre : rel.getResults())
			if(!results.contains(tre)) 
				return false;
		return false;
	}
}
