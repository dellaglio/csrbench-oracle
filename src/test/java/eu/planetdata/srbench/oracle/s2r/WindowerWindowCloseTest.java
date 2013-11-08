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


import org.junit.Test;

import eu.planetdata.srbench.oracle.query.WindowDefinition;
import eu.planetdata.srbench.oracle.s2r.ReportPolicy;
import eu.planetdata.srbench.oracle.s2r.WindowScope;
import eu.planetdata.srbench.oracle.s2r.WindowOperator;

import static org.junit.Assert.*;

public class WindowerWindowCloseTest {

	//W(10,3), t0=0
	@Test public void timeSlidingWindow1ShouldSlideWithWindowClose(){
		ReportPolicy policy = new ReportPolicy();
		policy.setWindowClose(true);
		policy.setContentChange(false);

		WindowOperator windower = new WindowOperator(new WindowDefinition(10000,3000), policy, 0);
		WindowScope range = windower.getNextWindowScope(null);
		assertEquals(0, range.getFrom());
		assertEquals(10000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(3000, range.getFrom());
		assertEquals(13000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(6000, range.getFrom());
		assertEquals(16000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(9000, range.getFrom());
		assertEquals(19000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(12000, range.getFrom());
		assertEquals(22000, range.getTo());
	}

	//W(10,3), t0=2
	@Test public void timeSlidingWindow2ShouldSlideWithWindowClose(){
		ReportPolicy policy = new ReportPolicy();
		policy.setWindowClose(true);
		policy.setContentChange(false);

		WindowOperator windower = new WindowOperator(new WindowDefinition(10000,3000), policy, 2000);
		WindowScope range = windower.getNextWindowScope(null); 
		assertEquals(2000, range.getFrom());
		assertEquals(12000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(5000, range.getFrom());
		assertEquals(15000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(8000, range.getFrom());
		assertEquals(18000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(11000, range.getFrom());
		assertEquals(21000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(14000, range.getFrom());
		assertEquals(24000, range.getTo());
	}

	//W(10,1), t0=0
	@Test public void timeSlidingWindow3ShouldSlideWithWindowClose(){
		ReportPolicy policy = new ReportPolicy();
		policy.setWindowClose(true);
		policy.setContentChange(false);

		WindowOperator windower = new WindowOperator(new WindowDefinition(10000,1000), policy, 0);
		WindowScope range = windower.getNextWindowScope(null);
		assertEquals(0, range.getFrom());
		assertEquals(10000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(1000, range.getFrom());
		assertEquals(11000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(2000, range.getFrom());
		assertEquals(12000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(3000, range.getFrom());
		assertEquals(13000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(4000, range.getFrom());
		assertEquals(14000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(5000, range.getFrom());
		assertEquals(15000, range.getTo());
	}

	//W(10,1), t0=3
	@Test public void timeSlidingWindow4ShouldSlideWithWindowClose(){
		ReportPolicy policy = new ReportPolicy();
		policy.setWindowClose(true);
		policy.setContentChange(false);

		WindowOperator windower = new WindowOperator(new WindowDefinition(10000,1000), policy, 3000);
		WindowScope range = windower.getNextWindowScope(null);
		assertEquals(3000, range.getFrom());
		assertEquals(13000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(4000, range.getFrom());
		assertEquals(14000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(5000, range.getFrom());
		assertEquals(15000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(6000, range.getFrom());
		assertEquals(16000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(7000, range.getFrom());
		assertEquals(17000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(8000, range.getFrom());
		assertEquals(18000, range.getTo());
	}

	//W(1,1), t0=0
	@Test public void timeSlidingWindow5ShouldSlideWithWindowClose(){
		ReportPolicy policy = new ReportPolicy();
		policy.setWindowClose(true);
		policy.setContentChange(false);

		WindowOperator windower = new WindowOperator(new WindowDefinition(1000,1000), policy, 0);
		WindowScope range = windower.getNextWindowScope(null);
		assertEquals(0, range.getFrom());
		assertEquals(1000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(1000, range.getFrom());
		assertEquals(2000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(2000, range.getFrom());
		assertEquals(3000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(3000, range.getFrom());
		assertEquals(4000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(4000, range.getFrom());
		assertEquals(5000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(5000, range.getFrom());
		assertEquals(6000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(6000, range.getFrom());
		assertEquals(7000, range.getTo());
	}

	//W(1,1), t0=2
	@Test public void timeSlidingWindow6ShouldSlideWithWindowClose(){
		ReportPolicy policy = new ReportPolicy();
		policy.setWindowClose(true);
		policy.setContentChange(false);

		WindowOperator windower = new WindowOperator(new WindowDefinition(1000,1000), policy, 2000);
		WindowScope range = windower.getNextWindowScope(null);
		assertEquals(2000, range.getFrom());
		assertEquals(3000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(3000, range.getFrom());
		assertEquals(4000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(4000, range.getFrom());
		assertEquals(5000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(5000, range.getFrom());
		assertEquals(6000, range.getTo());

		range = windower.getNextWindowScope(null);
		assertEquals(6000, range.getFrom());
		assertEquals(7000, range.getTo());
	}
}
