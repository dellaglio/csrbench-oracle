package it.polimi.deib.streams.oracle;

import it.polimi.deib.streams.oracle.query.StreamQuery;
import it.polimi.deib.streams.oracle.repository.BenchmarkVocab;
import it.polimi.deib.streams.oracle.result.OutputStreamResult;
import it.polimi.deib.streams.oracle.result.TimestampedRelation;
import it.polimi.deib.streams.oracle.result.TimestampedRelationElement;
import it.polimi.deib.streams.oracle.s2r.ReportPolicy;
import it.polimi.deib.streams.oracle.s2r.WindowScope;
import it.polimi.deib.streams.oracle.s2r.Windower;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.impl.DatasetImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Oracle {
	private final static Logger logger = LoggerFactory.getLogger(Oracle.class);
	private Repository repo; 

	public Oracle(){
		repo = new SailRepository(new NativeStore(Config.getInstance().getRepoDir()));
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			logger.error("Error while initializing the repository", e);
		}
	}

	protected OutputStreamResult executeStreamQuery(StreamQuery query, long t0, ReportPolicy policy, long lastTimestamp){
		OutputStreamResult ret = new OutputStreamResult();
		Windower windower = new Windower(query.getWindowDefinition(), policy, t0);

		try{
		RepositoryConnection conn = repo.getConnection();
		
		WindowScope tr = windower.getNextWindowScope(conn);
		while(tr!=null && tr.getFrom()<=lastTimestamp){
			logger.debug("Block: [{},{})", tr.getFrom(), tr.getTo());
			List<URI> graphs = getGraphsContent(tr);
			if(graphs.size()>0){
				TimestampedRelation rel = executeStreamQueryOverABlock(query.getBooleanQuery(), graphs, tr.getTo());
				ret.addRelation(rel);
			} else {
				logger.debug("The content is empty and the non-empty content report policy is {}", policy.isNonEmptyContent());
				if(!policy.isNonEmptyContent()){
					ret.addRelation(TimestampedRelation.createEmptyRelation(tr.getTo()));
				}
			}
			tr=windower.getNextWindowScope(conn);
		}
		}catch(RepositoryException e){logger.error("Error while retrieving the connection to the RDF store", e);}
		return ret;
	}
	
	private TimestampedRelation executeStreamQueryOverABlock(String query, List<URI> graphsList, long computationTimestamp){
		try {
			TimestampedRelation ret = new TimestampedRelation();
			TupleQuery tq;
			tq = repo.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, query);
			DatasetImpl dataset = new DatasetImpl();
			for(URI graph : graphsList){
				dataset.addDefaultGraph(graph);
			}
			tq.setDataset(dataset);
			TupleQueryResult tqr = tq.evaluate();
			
			if(tqr.hasNext()){
				while(tqr.hasNext()){
					BindingSet bs = tqr.next();
					TimestampedRelationElement srr = new TimestampedRelationElement();
					srr.setTimestamp(computationTimestamp);
					for(String key : tqr.getBindingNames())
						srr.add(key, bs.getBinding(key).getValue());
					ret.addElement(srr);
				}
				return ret;
			}
			else
				return TimestampedRelation.createEmptyRelation(computationTimestamp);

		} catch (RepositoryException e) {
			logger.error("Error while connecting to the repository", e);
		} catch (MalformedQueryException e) {
			logger.error("Malformed query", e);
		} catch (QueryEvaluationException e) {
			logger.error("Error while evaluating the query", e);
		}
		return null;
	}


	private List<URI> getGraphsContent(WindowScope range){
		List<URI> graphsList = new ArrayList<URI>();
		String query = prepareQuery(range);
		logger.trace("Query to retrieve the graphs: {}", query);
		try {
			TupleQuery tq = repo.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, query);
			TupleQueryResult result = tq.evaluate();
			while(result.hasNext()){
				graphsList.add((URI)result.next().getBinding("g").getValue());
				logger.debug("Retrieved graph: {}", graphsList.get(graphsList.size()-1));
			}
			return graphsList;
		} catch (RepositoryException e) {
			logger.error("Error while connecting to the repository", e);
		} catch (MalformedQueryException e) {
			logger.error("Malformed query", e);
		} catch (QueryEvaluationException e) {
			logger.error("Error while evaluating the query", e);
		}
		return null;
	}

	private String prepareQuery(WindowScope range){
		return "SELECT ?g " +
				"FROM <"+BenchmarkVocab.graphList+"> " +
				"WHERE{" +
				"?g <" + BenchmarkVocab.hasTimestamp + "> ?timestamp. " +
				"FILTER(?timestamp >= "+range.getFrom() + " && ?timestamp < "+range.getTo()+") " +
				"}";
	}

	public static void main(String[] args) throws IOException {
		Oracle oracle = new Oracle();
	
		boolean detailedResults=false;
		
//		Writer out = new BufferedWriter(new FileWriter("output-"+System.currentTimeMillis()+".html"));
		Writer out = new BufferedWriter(new FileWriter("output.html"));
		out.write("<html><head><title>Oracle Results</title></head><body>");
		
		ReportPolicy policy = Config.getInstance().getPolicy();
		for(String queryKey : Config.getInstance().getQuerySet()){
			logger.info("Testing query {}", queryKey);
			out.write("<h2>Query "+queryKey+"</h2>");
			StreamQuery query = Config.getInstance().getQuery(queryKey);

			if(detailedResults && query.getAnswer()!=null){
				out.write("<h3>Result of the system</h3>");
				out.write(query.getAnswer().toString().replaceAll("<", "&lt;").replaceAll("]", "]<br/>"));
			}


			out.write("<h3>Oracle results</h3><table><thead><tr><td>Execution number</td><td>t0</td>"+(detailedResults?"<td>Result</td>":"")+"<td>Matches</td></tr></thead><tbody>");
			long actualT0 = query.getFirstT0();
			long lastT0 = query.getFirstT0()+query.getWindowDefinition().getSize();
			long lastTimestamp = Config.getInstance().getInputStreamMaxTime()*Config.getInstance().getInputStreamInterval()+query.getWindowDefinition().getSize();
			logger.info("t0 will vary between {} and {}; the last timestamp will be: {}", new Object[]{actualT0, lastT0, lastTimestamp});
			for(int i=1; actualT0<=lastT0;actualT0+=Config.getInstance().getTimeUnit()){
				logger.info("Execution {}: Window with t0={}", i, actualT0);
				OutputStreamResult sr = oracle.executeStreamQuery(query, actualT0, policy, lastTimestamp);
				logger.info("Returned result: {}\n", sr);
				out.write("<tr><td>"+i+"</td><td>"+
						actualT0+"</td><td>"+
						(detailedResults?(sr.toString().replaceAll("<", "&lt;").replaceAll("]", "]<br/>")):"")+
						"</td>");
				if(query.getAnswer()!=null){
					boolean match = sr.contains(query.getAnswer());
					logger.info("The system answer matches: {}", sr.contains(query.getAnswer()));
					out.write("<td>"+match+"</td></tr>");
				} else
					out.write("<td>N/A</td></tr>");
				i++;
			}
			out.write("</tbody></table>");
		}
		out.write("</body></html>");
		out.close();
		
	}
	
	
	
}
