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

import java.io.File;


import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.query.WindowDefinition;
import eu.planetdata.srbench.oracle.repository.RepoUtility;
import eu.planetdata.srbench.oracle.repository.StreamImporter;
import eu.planetdata.srbench.oracle.s2r.ReportPolicy;
import eu.planetdata.srbench.oracle.s2r.WindowScope;
import eu.planetdata.srbench.oracle.s2r.WindowOperator;

import static org.junit.Assert.*;

public class WindowerContentChangeTest {
	private static final Logger logger = LoggerFactory.getLogger(WindowerContentChangeTest.class);
	private static File testStore;
	private static StreamImporter si;

	@BeforeClass 
	public static void initialize(){
		testStore = new File("test-repository");
		si = new StreamImporter(testStore);
		try {
			if(RepoUtility.countTriples(logger, si.getRepository().getConnection())>0)
				si.clearRepository();

			ValueFactory vf = si.getRepository().getValueFactory();
			URI m1 = vf.createURI("http://ex.org/instances#m1");
			URI m2 = vf.createURI("http://ex.org/instances#m2");
			URI r1 = vf.createURI("http://ex.org/instances#m1");
			URI r2 = vf.createURI("http://ex.org/instances#m2");
			URI detectedAt = vf.createURI("http://ex.org/detectedAt");

			si.addTimestampedStatement(m1, detectedAt, r1, 1000);
			si.addTimestampedStatement(m2, detectedAt, r1, 3000);
			si.addTimestampedStatement(m1, detectedAt, r2, 12000);
			si.addTimestampedStatement(m2, detectedAt, r2, 15000);
			RepositoryConnection conn = si.getRepository().getConnection();
			RepoUtility.printTriples(logger, conn);
		} catch (RepositoryException e) {
			logger.error("Error while initializing the test store", e);
		}
	}
	
	//W(10,3) t0=0
	@Test public void timeSlidingWindow1ShouldSlideWithContentChange(){
		try {
			ReportPolicy policy = new ReportPolicy();
			policy.setWindowClose(false);
			policy.setContentChange(true);

			RepositoryConnection conn = si.getRepository().getConnection();

			WindowOperator windower = new WindowOperator(new WindowDefinition(10000,3000), policy, 0);
			WindowScope range = windower.getNextWindowScope(conn);
			assertEquals(0, range.getFrom());
			assertEquals(2000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(0, range.getFrom());
			assertEquals(4000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(3000, range.getFrom());
			assertEquals(13000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(6000, range.getFrom());
			assertEquals(16000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertNull(range);
		} catch (RepositoryException e) {
			logger.error("Error while executing a test", e);
			fail();
		}
	}

	//W(10,3) t0=2
	@Test public void timeSlidingWindow2ShouldSlideWithContentChange(){
		try {
			ReportPolicy policy = new ReportPolicy();
			policy.setWindowClose(false);
			policy.setContentChange(true);

			RepositoryConnection conn = si.getRepository().getConnection();

			WindowOperator windower = new WindowOperator(new WindowDefinition(10000,3000), policy, 2000);
			WindowScope range = windower.getNextWindowScope(conn);
			assertEquals(2000, range.getFrom());
			assertEquals(4000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(5000, range.getFrom());
			assertEquals(13000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(8000, range.getFrom());
			assertEquals(16000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertNull(range);

		} catch (RepositoryException e) {
			logger.error("Error while executing a test", e);
			fail();
		}

	}

	//W(1,1) t0=0
	@Test public void timeSlidingWindow3ShouldSlideWithContentChange(){
		try {
			ReportPolicy policy = new ReportPolicy();
			policy.setWindowClose(false);
			policy.setContentChange(true);

			RepositoryConnection conn = si.getRepository().getConnection();

			WindowOperator windower = new WindowOperator(new WindowDefinition(1000,1000), policy, 0000);
			WindowScope range = windower.getNextWindowScope(conn);
			assertEquals(1000, range.getFrom());
			assertEquals(2000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(3000, range.getFrom());
			assertEquals(4000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(12000, range.getFrom());
			assertEquals(13000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(15000, range.getFrom());
			assertEquals(16000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertNull(range);

		} catch (RepositoryException e) {
			logger.error("Error while executing a test", e);
			fail();
		}

	}

	//W(1,1) t0=5
	@Test public void timeSlidingWindow4ShouldSlideWithContentChange(){
		try {
			ReportPolicy policy = new ReportPolicy();
			policy.setWindowClose(false);
			policy.setContentChange(true);

			RepositoryConnection conn = si.getRepository().getConnection();

			WindowOperator windower = new WindowOperator(new WindowDefinition(1000,1000), policy, 5000);
			WindowScope range = windower.getNextWindowScope(conn);
			assertEquals(12000, range.getFrom());
			assertEquals(13000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertEquals(15000, range.getFrom());
			assertEquals(16000, range.getTo());

			range = windower.getNextWindowScope(conn);
			assertNull(range);

		} catch (RepositoryException e) {
			logger.error("Error while executing a test", e);
			fail();
		}

	}

}
