package eu.planetdata.srbench.oracle.query;

import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;
import eu.planetdata.srbench.oracle.result.StreamProcessorOutputBuilder.R2SOperator;

public class StreamQuery {
	private String booleanQuery;
	private WindowDefinition windowDefinition;
	private long firstT0;
	private StreamProcessorOutput answer;
	private R2SOperator r2SOperator;

	public StreamQuery() {
		super();
	}

	public String getBooleanQuery() {
		return booleanQuery;
	}

	public void setBooleanQuery(String booleanQuery) {
		this.booleanQuery = booleanQuery;
	}

	public WindowDefinition getWindowDefinition() {
		return windowDefinition;
	}

	public void setWindowDefinition(WindowDefinition windowDefinition) {
		this.windowDefinition = windowDefinition;
	}

	public long getFirstT0() {
		return firstT0;
	}

	public void setFirstT0(long firstT0) {
		this.firstT0 = firstT0;
	}

	public StreamProcessorOutput getAnswer() {
		return answer;
	}

	public void setAnswer(StreamProcessorOutput systemAnswer) {
		this.answer = systemAnswer;
	}

	public R2SOperator getS2ROperator() {
		return r2SOperator;
	}

	public void setS2ROperator(R2SOperator r2sOperator) {
		this.r2SOperator = r2sOperator;
	}
	
	
}
