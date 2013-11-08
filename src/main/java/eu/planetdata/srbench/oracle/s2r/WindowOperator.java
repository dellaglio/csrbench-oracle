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

import org.openrdf.model.Literal;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.planetdata.srbench.oracle.configuration.Config;
import eu.planetdata.srbench.oracle.query.WindowDefinition;
import eu.planetdata.srbench.oracle.repository.BenchmarkVocab;



public class WindowOperator {
	private WindowDefinition window;
	private ReportPolicy policy;
	private final long t0;
	private long actualTime;
	
	private static final Logger logger = LoggerFactory.getLogger(WindowOperator.class);
	
	public WindowOperator(WindowDefinition window, ReportPolicy policy, long t0) {
		super();
		this.window = window;
		this.policy = policy;
		this.t0=t0; //useful for windowClose=false
		actualTime=t0;
	}
	
	public WindowDefinition getWindow() {
		return window;
	}
	public void setWindow(WindowDefinition window) {
		this.window = window;
	}
	
	public ReportPolicy getPolicy() {
		return policy;
	}
	public void setPolicy(ReportPolicy policy) {
		this.policy = policy;
	}
	
	public WindowScope getNextWindowScope(RepositoryConnection conn){
		if(policy.isWindowClose() && !policy.isContentChange()){
			if(actualTime<window.getSize())
				actualTime+=window.getSize();
			else
				actualTime+=window.getSlide();
			return new WindowScope(Math.max(0, actualTime-window.getSize()), actualTime);
		} else if(!policy.isWindowClose() && policy.isContentChange()){
			int a = 0;
			Long nextDataTimestamp = getNextDataTimestamp(conn);
			if(nextDataTimestamp!=null){
				actualTime=nextDataTimestamp;
				while((a*window.getSlide())+t0+window.getSize() <= actualTime){a++;};
				return new WindowScope(Math.max(t0, t0+a*window.getSlide()), actualTime+Config.getInstance().getTimeUnit());
			}else{
				return null;
			}
		}
		else
			throw new RuntimeException("The report policy is not supported yet!");
	}
	
	protected Long getNextDataTimestamp(RepositoryConnection conn){
		Long ret = null;
		try {
			TupleQuery tq = conn.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT ?timestamp FROM <"+BenchmarkVocab.graphList+"> WHERE {?g <"+BenchmarkVocab.hasTimestamp+"> ?timestamp. FILTER(?timestamp > \""+actualTime+"\"^^<http://www.w3.org/2001/XMLSchema#long>)} ORDER BY ?timestamp LIMIT 1");
			TupleQueryResult tqr = tq.evaluate();
			logger.debug("SELECT ?timestamp FROM <"+BenchmarkVocab.graphList+"> WHERE {?g <"+BenchmarkVocab.hasTimestamp+"> ?timestamp. FILTER(?timestamp > \""+actualTime+"\"^^<http://www.w3.org/2001/XMLSchema#long>)} ORDER BY ?timestamp LIMIT 1");
			
			if(tqr.hasNext()){
				ret = ((Literal)tqr.next().getBinding("timestamp").getValue()).longValue();
			}
			else{
				logger.debug("the query returned an empty result!");
			}
			tqr.close();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
}
