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
package eu.planetdata.srbench.oracle.query;

import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;
import eu.planetdata.srbench.oracle.result.StreamProcessorOutputBuilder.R2SOperator;

public class StreamQuery {
	private String booleanQuery;
	private WindowDefinition windowDefinition;
	private long firstT0;
	private StreamProcessorOutput answer;
	private R2SOperator r2SOperator;

	public StreamQuery() {
		super();
	}

	public String getBooleanQuery() {
		return booleanQuery;
	}

	public void setBooleanQuery(String booleanQuery) {
		this.booleanQuery = booleanQuery;
	}

	public WindowDefinition getWindowDefinition() {
		return windowDefinition;
	}

	public void setWindowDefinition(WindowDefinition windowDefinition) {
		this.windowDefinition = windowDefinition;
	}

	public long getFirstT0() {
		return firstT0;
	}

	public void setFirstT0(long firstT0) {
		this.firstT0 = firstT0;
	}

	public StreamProcessorOutput getAnswer() {
		return answer;
	}

	public void setAnswer(StreamProcessorOutput systemAnswer) {
		this.answer = systemAnswer;
	}

	public R2SOperator getS2ROperator() {
		return r2SOperator;
	}

	public void setS2ROperator(R2SOperator r2sOperator) {
		this.r2SOperator = r2sOperator;
	}
	
	
}
