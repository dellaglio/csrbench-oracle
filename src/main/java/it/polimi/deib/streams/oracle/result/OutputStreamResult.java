package it.polimi.deib.streams.oracle.result;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputStreamResult {
	private static final Logger logger = LoggerFactory.getLogger(OutputStreamResult.class);
	
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

	public List<TimestampedRelationElement> getResultBindingLists() {
		List<TimestampedRelationElement> ret = new ArrayList<TimestampedRelationElement>();
		for(TimestampedRelation rel : results){
			ret.addAll(rel.getElements());
		}
		return ret;
	}

	public List<TimestampedRelation> getResultRelations() {
		return results;
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

	public boolean contains(OutputStreamResult outputStream){
		List<TimestampedRelation> subSeq = outputStream.getResultRelations();
		if(results.size()<subSeq.size()){
			logger.debug("the subset size is greater than the set size!");
			return false;
		}
		if(subSeq.size()==0)
			return true;
		TimestampedRelation firstResult = subSeq.get(0);
		for(int i=0; i<results.size(); i++){
			logger.debug("Comparing {} with {}", results.get(i), firstResult);
			if(results.get(i).equals(firstResult)){
				boolean exit=false;
				int j=i+1;
				for(int k=1; k<subSeq.size() && exit==false;){
					if((j>=results.size() && k<subSeq.size()) || !results.get(j++).equals(subSeq.get(k++)))
						exit=true;
				}
				if(!exit)
					return true;
			}
		}
		return false;
	}
}
