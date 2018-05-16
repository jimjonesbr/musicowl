package de.wwu.music2rdf.core;

public class Staff {

	private String id;
	private ScorePart part;
	
	public Staff() {		
		super();
		this.part = new ScorePart();
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

