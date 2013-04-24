package it.polimi.deib.streams.oracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.deib.streams.oracle.result.OutputStreamResult;
import it.polimi.deib.streams.oracle.s2r.ReportPolicy;

public class SRBenchOracle extends Oracle{
	private final static Logger logger = LoggerFactory.getLogger(SRBenchOracle.class);

	static String prefixes=
			"PREFIX om-owl: <http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#> "+
     		"PREFIX weather: <http://knoesis.wright.edu/ssw/ont/weather.owl#> ";
	
	static String q1=prefixes+"SELECT ?sensor " +
			"WHERE { " +
			//"?value ?p ?s."+
			" ?obs om-owl:observedProperty weather:_AirTemperature ; "+
		    "      om-owl:procedure ?sensor ; "+
		    "      om-owl:result ?res . "+
            " ?res om-owl:floatValue ?value."+
		    " FILTER(?value > 78)"+
			"}  ";
	ReportPolicy policy = Config.getInstance().getPolicy();
	long actualT0 = Config.getInstance().getFirstT0();

	OutputStreamResult executeQuery(String q,long lastTimestamp){
		return executeStreamQuery(q,Config.getInstance().getWindow(),actualT0, policy, lastTimestamp);
	}
	
	public static void main(String[] args) {
		SRBenchOracle oracle = new SRBenchOracle();		 				
	    logger.debug("****** Window with t0={} *********",oracle.actualT0);
			OutputStreamResult sr = oracle.executeQuery(q1,20000);
			logger.debug("Returned result: {}\n", sr);
	}
}
