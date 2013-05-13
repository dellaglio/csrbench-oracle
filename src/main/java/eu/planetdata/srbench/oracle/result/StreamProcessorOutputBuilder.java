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

public class StreamProcessorOutputBuilder {
	public enum R2SOperator {Istream, Rstream, Dstream};

	private R2SOperator operator;
	private boolean outputEmptyRelation;
	private TimestampedRelation previousRelation;

	private StreamProcessorOutput output;

	public StreamProcessorOutputBuilder(R2SOperator operator, boolean outputEmptyRelation) {
		super();
		this.operator = operator;
		this.outputEmptyRelation = outputEmptyRelation;
		output = new StreamProcessorOutput();
	}

	public void addRelation(TimestampedRelation relation){
		if(operator.equals(R2SOperator.Rstream)){
			if(relation.getBindings().size()==0){
				if(outputEmptyRelation){
					relation.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
					output.addRelation(relation);
				}
			} else
				output.addRelation(relation);
		} else if(operator.equals(R2SOperator.Istream)){
			if(previousRelation==null){
				if(relation.getBindings().size()==0){
					if(outputEmptyRelation){
						relation.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
						output.addRelation(relation);
					}
				} else
					output.addRelation(relation);
			}
			else{
				TimestampedRelation diff = new TimestampedRelation(relation.minus(previousRelation), relation.getComputationTimestamp());
				if(diff.getBindings().size()==0){
					if(outputEmptyRelation){
						diff.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
						output.addRelation(diff);
					}
				} else
					output.addRelation(diff);
			}
		} else { //Dstream
			if(previousRelation!=null){
				TimestampedRelation diff = new TimestampedRelation(previousRelation.minus(relation), relation.getComputationTimestamp());
				if(diff.getBindings().size()==0){
					if(outputEmptyRelation){
						diff.addElement(TimestampedRelationElement.createEmptyTimestampedRelationElement(relation.getComputationTimestamp()));
						output.addRelation(diff);
					}
				} else
					output.addRelation(diff);
			}
		}
		previousRelation=relation;
	}

	public StreamProcessorOutput getOutputStreamResult(){
		return output;
	}


}
