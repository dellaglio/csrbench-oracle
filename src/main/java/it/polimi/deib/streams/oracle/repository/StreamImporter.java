package it.polimi.deib.streams.oracle.repository;

import java.io.File;
import java.util.ArrayList;

import it.polimi.deib.streams.oracle.Config;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.NumericLiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamImporter {
	private final static Logger logger = LoggerFactory.getLogger(StreamImporter.class);
	protected Repository repo; 
    ArrayList<String> graphs=new ArrayList<String>();
	
	public StreamImporter(){
		this(Config.getInstance().getRepoDir());
	}
	
	public StreamImporter(File repositoryFolder){
		repo = new SailRepository(new NativeStore(repositoryFolder, "cspo,cops"));
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			logger.error("Error while initializing the repository", e);
		}
	}

	public static void main(String[] args) {
		StreamImporter si = new StreamImporter();
		try {
			si.addTimestampedStatement(new URIImpl("http://ex.org/instances#m1"), new URIImpl("http://ex.org/detectedAt"), new URIImpl("http://ex.org/instances#r1"), 1000);
			si.addTimestampedStatement(new URIImpl("http://ex.org/instances#m2"), new URIImpl("http://ex.org/detectedAt"), new URIImpl("http://ex.org/instances#r1"), 3000);
			si.addTimestampedStatement(new URIImpl("http://ex.org/instances#m1"), new URIImpl("http://ex.org/detectedAt"), new URIImpl("http://ex.org/instances#r2"), 12000);
			si.addTimestampedStatement(new URIImpl("http://ex.org/instances#m2"), new URIImpl("http://ex.org/detectedAt"), new URIImpl("http://ex.org/instances#r2"), 15000);
		} catch (RepositoryException e) {
			logger.error("Error while adding triples to repository");
		}
	}
	
	public void addTimestampedStatement(Resource subject, URI predicate, Value object, long timestamp) throws RepositoryException{
		URI graph = BenchmarkVocab.getGraphURI(timestamp);
		repo.getConnection().begin();
		if(!existsGraph(graph)){
			repo.getConnection().add(graph, BenchmarkVocab.hasTimestamp, new NumericLiteralImpl(timestamp), BenchmarkVocab.graphList);
		}
		repo.getConnection().add(subject, predicate, object, graph);
		repo.getConnection().commit();
	}
	
	public void clearRepository() throws RepositoryException{
		repo.getConnection().clear();
		repo.getConnection().commit();
	}
	
	public Repository getRepository(){
		return repo;
	}
	
	protected boolean existsGraph(URI graphName){
		try {
			return repo.getConnection().getStatements(graphName, null, null, false, BenchmarkVocab.graphList).hasNext();
		} catch (RepositoryException e) {
			logger.error("Error while checking if "+graphName +" exists", e);
		}
		return false;
	}
}
