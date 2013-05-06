package eu.planetdata.srbench.oracle.result;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputStreamResult {
	private static final Logger logger = LoggerFactory.getLogger(OutputStreamResult.class);
	
	private List<TimestampedRelation> results;

	public OutputStreamResult() {
		results = new ArrayList<TimestampedRelation>();
	}

//	public List<TimestampedRelationElement> getResultBindingLists() {
//		List<TimestampedRelationElement> ret = new ArrayList<TimestampedRelationElement>();
//		for(TimestampedRelation rel : results){
//			ret.addAll(rel.getBindings());
//		}
//		return ret;
//	}

	public List<TimestampedRelation> getResultRelations() {
		return results;
	}

	public void addRelation(TimestampedRelation relation){
		results.add(relation);
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for(TimestampedRelation rel: results)
			for(TimestampedRelationElement srr : rel.getBindings())
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
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
		OutputStreamResult other = (OutputStreamResult) obj;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		return true;
	}
	
	
}
