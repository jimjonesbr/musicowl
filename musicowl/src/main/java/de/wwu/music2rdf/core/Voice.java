package de.wwu.music2rdf.core;

public class Voice {

	private String measure;
	private String id;
	private ScorePart part;
	
	public Voice() {
		super();
		this.part = new ScorePart();
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ScorePart getPart() {
		return part;
	}
	
}
