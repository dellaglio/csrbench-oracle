package eu.planetdata.srbench.oracle.query;

public class WindowDefinition {
	private long size, slide;
	
	public WindowDefinition() {
		super();
	}

	public WindowDefinition(long size, long slide) {
		super();
		this.size = size;
		this.slide = slide;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSlide() {
		return slide;
	}

	public void setSlide(long slide) {
		this.slide = slide;
	}
}
