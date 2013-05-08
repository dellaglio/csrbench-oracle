package eu.planetdata.srbench.oracle.repository;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

public class BenchmarkVocab  {
	private static final String baseURI = "http://www.streamreasoning.org/schema/benchmark#";
	private static final String baseGraphURI = baseURI + "timestampedGraph";
	public static final URI graphList = new URIImpl(baseURI+"graphsList");
	public static final URI hasTimestamp = new URIImpl(baseURI+"hasTimestamp");
	
	public static URI getGraphURI(long timestamp){
		return new URIImpl(baseGraphURI+timestamp);
	}
}
