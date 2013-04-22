package it.polimi.deib.streams.oracle.s2r;

public class ReportPolicy {
	private boolean windowClose;
	private boolean nonEmptyContent;
	private boolean contentChange;

	public ReportPolicy() {
		super();
	}

	public boolean isWindowClose() {
		return windowClose;
	}

	public void setWindowClose(boolean windowClose) {
		this.windowClose = windowClose;
	}

	public boolean isNonEmptyContent() {
		return nonEmptyContent;
	}

	public void setNonEmptyContent(boolean nonEmptyContent) {
		this.nonEmptyContent = nonEmptyContent;
	}

	public boolean isContentChange() {
		return contentChange;
	}

	public void setContentChange(boolean contentChange) {
		this.contentChange = contentChange;
	}
	
	
}
