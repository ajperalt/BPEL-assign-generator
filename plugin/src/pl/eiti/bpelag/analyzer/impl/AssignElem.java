package pl.eiti.bpelag.analyzer.impl;

public class AssignElem {
	private String fromElem = null;
	private String toElem = null;

	public AssignElem() {

	}

	public AssignElem(final String from, final String to) {
		fromElem = from;
		toElem = to;
	}

	public String getFromElem() {
		return fromElem;
	}

	public void setFromElem(String fromElem) {
		this.fromElem = fromElem;
	}

	public String getToElem() {
		return toElem;
	}

	public void setToElem(String toElem) {
		this.toElem = toElem;
	}
}
