package it.polimi.deib.streams.oracle.s2r;

import org.openrdf.model.Literal;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import it.polimi.deib.streams.oracle.Config;
import it.polimi.deib.streams.oracle.repository.BenchmarkVocab;


public class Windower {
	private Window window;
	private ReportPolicy policy;
	private final long t0;
	private long actualTime;
	
	public Windower(Window window, ReportPolicy policy, long t0) {
		super();
		this.window = window;
		this.policy = policy;
		this.t0=t0; //useful for windowClose=false
		actualTime=t0;
	}
	
	public Window getWindow() {
		return window;
	}
	public void setWindow(Window window) {
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
				System.out.println(a+ " " + actualTime+ " " + nextDataTimestamp+ " " + t0);
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
			System.out.println("SELECT ?timestamp FROM <"+BenchmarkVocab.graphList+"> WHERE {?g <"+BenchmarkVocab.hasTimestamp+"> ?timestamp. FILTER(?timestamp > \""+actualTime+"\"^^<http://www.w3.org/2001/XMLSchema#long>)} ORDER BY ?timestamp LIMIT 1");
			
			if(tqr.hasNext()){
				ret = ((Literal)tqr.next().getBinding("timestamp").getValue()).longValue();
			}
			else{
				System.out.println("empty!");
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
	
//	public static void main(String[] args) {
//		int a = 0;
//		int slide = 5000;
//		int width = 10000;
//		int t0=0;
//		long nextDataTimestamp = 15000;
//		long actualTime=nextDataTimestamp;
//		while((a+1)*slide+t0-width  <= actualTime){
//			a++;
//		}
//		System.out.println("["+(a*slide+t0-width) + ", " + (actualTime+Config.getInstance().getTimeUnit()) + ")");
//	}
	
}
