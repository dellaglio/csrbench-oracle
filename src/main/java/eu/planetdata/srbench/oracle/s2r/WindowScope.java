package eu.planetdata.srbench.oracle.s2r;

public class WindowScope {
	private long from, to;

	public WindowScope(long from, long to) {
		super();
		this.from = from;
		this.to = to;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}
	
	 
}
