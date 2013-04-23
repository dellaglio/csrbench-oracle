package it.polimi.deib.streams.oracle.result;

import java.util.ArrayList;
import java.util.List;

public class OutputStreamResult {
	public enum S2ROperator {Istream, Rstream, Dstream};

	List<TimestampedRelationElement> results;
	S2ROperator operator;
	TimestampedRelation previousRelation;

	public OutputStreamResult() {
		results = new ArrayList<TimestampedRelationElement>();
		this.operator=S2ROperator.Rstream;
	}

	public OutputStreamResult(S2ROperator operator) {
		results = new ArrayList<TimestampedRelationElement>();
		this.operator=operator;
	}

	public List<TimestampedRelationElement> getResults() {
		return results;
	}

	public void addRelation(TimestampedRelation relation){
		if(operator.equals(S2ROperator.Rstream)){
			if(relation.getElements().size()==0)
				results.add(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
			else
				for(TimestampedRelationElement row : relation.getElements())
					results.add(row);
		}
		else if(operator.equals(S2ROperator.Istream)){
			if(previousRelation==null)
				for(TimestampedRelationElement row : relation.getElements())
					results.add(row);
			else
				for(TimestampedRelationElement row : relation.minus(previousRelation))
					results.add(row);
		}
		else
			if(previousRelation!=null)
				for(TimestampedRelationElement row : relation.minus(previousRelation))
					results.add(row);
		previousRelation=relation;
	}

	//	public void addResult(TimestampedRelationElement row){
	//		if(operator==S2ROperator.Rstream)
	//			results.add(row);
	//		else if(operator==S2ROperator.Istream){
	//			long previousStepTimestamp=-1;
	//			boolean present = false, exit = false;
	//			for(int i = results.size(); i>=0 && present==false && exit == false; i--){
	//				TimestampedRelationElement srr = results.get(i);
	//				if(previousStepTimestamp==-1 && srr.getTimestamp()<row.getTimestamp()){
	//					previousStepTimestamp=row.getTimestamp();
	//				}
	//				if(row.getTimestamp()==previousStepTimestamp)
	//					if(row.equals(srr)){
	//						present=true;
	//					}
	//				if(row.getTimestamp()<previousStepTimestamp)
	//					exit=true;
	//			}
	//			if(present==false)
	//				results.add(row);
	//		}
	//	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for(TimestampedRelationElement srr : results)
			ret.append(srr.toString());
		return ret.toString();
	}
}
