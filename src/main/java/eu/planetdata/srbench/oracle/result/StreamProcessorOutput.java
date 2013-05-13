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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamProcessorOutput {
	private static final Logger logger = LoggerFactory.getLogger(StreamProcessorOutput.class);
	
	private List<TimestampedRelation> results;

	public StreamProcessorOutput() {
		results = new ArrayList<TimestampedRelation>();
	}

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

	public boolean contains(StreamProcessorOutput outputStream){
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
		StreamProcessorOutput other = (StreamProcessorOutput) obj;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		return true;
	}
	
	
}
