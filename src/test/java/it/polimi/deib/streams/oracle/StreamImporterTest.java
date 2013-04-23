package it.polimi.deib.streams.oracle;

import it.polimi.deib.streams.oracle.repository.StreamImporter;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class StreamImporterTest {
	private Logger logger = LoggerFactory.getLogger(StreamImporterTest.class); 

	private Repository repo;
	private StreamImporter si ;

	@Before public void setup(){
		si = new StreamImporter();
		repo = si.getRepository();
		if(countTriples()>0)
			try {
				si.clearRepository();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Test public void shouldImportTriples(){
		assertEquals(0, countTriples());
		try {
			ValueFactory vf = repo.getValueFactory();
			URI m1 = vf.createURI("http://ex.org/instances#m1");
			URI m2 = vf.createURI("http://ex.org/instances#m2");
			URI r1 = vf.createURI("http://ex.org/instances#m1");
			URI r2 = vf.createURI("http://ex.org/instances#m2");
			URI detectedAt = vf.createURI("http://ex.org/detectedAt");

			si.addTimestampedStatement(m1, detectedAt, r1, 1000);
			si.addTimestampedStatement(m2, detectedAt, r1, 3000);
			si.addTimestampedStatement(m1, detectedAt, r2, 12000);
			si.addTimestampedStatement(m2, detectedAt, r2, 15000);
		} catch (RepositoryException e) {
			logger.error("Error while adding triples to repository");
		}
		printTriples();
		assertEquals(8, countTriples());
	}

	private int countTriples(){
		int ret=0;
		try{
			RepositoryResult<Statement> result = repo.getConnection().getStatements((Resource)null, (URI)null, (Value)null, false);
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

	private void printTriples(){
		try{
			RepositoryResult<Statement> result = repo.getConnection().getStatements((Resource)null, (URI)null, (Value)null, false);
			while(result.hasNext()){
				Statement s = result.next();
				logger.debug("{}", s);
			}
		} catch(RepositoryException e){
			logger.error("Error while reading the triples in the repository");
		}

	}
}
