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

	public static RepositoryResult<Statement> getStatementsAt(RepositoryConnection conn, int timestamp) throws RepositoryException{
		return conn.getStatements((Resource)null, (URI)null, (Value)null, false, BenchmarkVocab.getGraphURI(timestamp));
	}

}
