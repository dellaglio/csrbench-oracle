package it.polimi.deib.streams.oracle.repository;

import java.io.File;

import it.polimi.deib.streams.oracle.repository.RepoUtility;
import it.polimi.deib.streams.oracle.repository.StreamImporter;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class StreamImporterTest {
	private Logger logger = LoggerFactory.getLogger(StreamImporterTest.class); 

	private Repository repo;
	private StreamImporter si ;

	@Before public void setup(){
		si = new StreamImporter(new File("test-repository"));
		repo = si.getRepository();
		try {
			if(RepoUtility.countTriples(logger, repo.getConnection())>0)
				si.clearRepository();
		} catch (RepositoryException e) {
			logger.error("Error while initializing the tests", e);
			fail();
		}
	}

	@Test public void shouldImportTriples(){
		try {
			assertEquals(0, RepoUtility.countTriples(logger, repo.getConnection()));
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
			RepoUtility.printTriples(logger, repo.getConnection());
			assertEquals(8, RepoUtility.countTriples(logger, repo.getConnection()));
		} catch (RepositoryException e) {
			logger.error("Error while adding triples to repository");
			fail();
		}
	}

}
