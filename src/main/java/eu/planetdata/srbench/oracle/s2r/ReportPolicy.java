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
package eu.planetdata.srbench.oracle.s2r;

public class ReportPolicy {
	private boolean windowClose;
	private boolean nonEmptyContent;
	private boolean contentChange;

	public ReportPolicy() {
		super();
	}

	public boolean isWindowClose() {
		return windowClose;
	}

	public void setWindowClose(boolean windowClose) {
		this.windowClose = windowClose;
	}

	public boolean isNonEmptyContent() {
		return nonEmptyContent;
	}

	public void setNonEmptyContent(boolean nonEmptyContent) {
		this.nonEmptyContent = nonEmptyContent;
	}

	public boolean isContentChange() {
		return contentChange;
	}

	public void setContentChange(boolean contentChange) {
		this.contentChange = contentChange;
	}
	
	
}
