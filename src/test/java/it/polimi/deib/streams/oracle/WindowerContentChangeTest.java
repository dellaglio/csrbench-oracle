package it.polimi.deib.streams.oracle;

import java.io.File;

import it.polimi.deib.streams.oracle.repository.StreamImporter;
import it.polimi.deib.streams.oracle.s2r.ReportPolicy;
import it.polimi.deib.streams.oracle.s2r.WindowScope;
import it.polimi.deib.streams.oracle.s2r.Window;
import it.polimi.deib.streams.oracle.s2r.Windower;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			if(countTriples(si.getRepository().getConnection())>0)
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
			printTriples(conn);
		} catch (RepositoryException e) {
			logger.error("Error while initializing the test store", e);
		}
	}
	
	@Test public void timeSlidingWindow1ShouldSlideWithContentChange(){
		try {
			ReportPolicy policy = new ReportPolicy();
			policy.setWindowClose(false);
			policy.setContentChange(true);

			RepositoryConnection conn = si.getRepository().getConnection();

			Windower windower = new Windower(new Window(10000,3000), policy, 0);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test public void timeSlidingWindow2ShouldSlideWithContentChange(){
		try {
			ReportPolicy policy = new ReportPolicy();
			policy.setWindowClose(false);
			policy.setContentChange(true);

			RepositoryConnection conn = si.getRepository().getConnection();

			Windower windower = new Windower(new Window(10000,3000), policy, 2000);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static int countTriples(RepositoryConnection conn){
		int ret=0;
		try{
			RepositoryResult<Statement> result = conn.getStatements((Resource)null, (URI)null, (Value)null, false);
			while(result.hasNext()){
				ret++;
				result.next();
			}
			return ret;
		} catch(RepositoryException e){
			logger.error("Error while reading the triples in the repository");
			return -1;
		}
	}

	private static void printTriples(RepositoryConnection conn){
		try{
			RepositoryResult<Statement> result = conn.getStatements((Resource)null, (URI)null, (Value)null, false);
			while(result.hasNext()){
				Statement s = result.next();
				logger.debug("{}", s);
			}
		} catch(RepositoryException e){
			logger.error("Error while reading the triples in the repository");
		}

	}

}
