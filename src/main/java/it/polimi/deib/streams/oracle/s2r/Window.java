package it.polimi.deib.streams.oracle.s2r;

public class Window {
	private long size, slide;
	
	public Window() {
		super();
	}

	public Window(long size, long slide) {
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
