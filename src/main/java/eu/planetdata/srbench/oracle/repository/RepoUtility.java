package eu.planetdata.srbench.oracle.repository;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;

public class RepoUtility {
	public static int countTriples(Logger logger, RepositoryConnection conn){
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

	public static void printTriples(Logger logger, RepositoryConnection conn){
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
