package eu.planetdata.srbench.oracle.query;

import eu.planetdata.srbench.oracle.result.StreamProcessorOutput;

public class StreamQuery {
	private String booleanQuery;
	private WindowDefinition windowDefinition;
	private long firstT0;
	private StreamProcessorOutput answer;

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
	
	
}
