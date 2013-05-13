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
