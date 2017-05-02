package de.wwu.music2rdf.core;

public class Key {

	private String fifths;
	private String mode;
	private String tonic;
	
	public Key() {
		super();
	
	}
	
	public String getFifths() {
		return fifths;
	}
	public void setFifths(String fifths) {
		this.fifths = fifths;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTonic() {
		return tonic;
	}

	public void setTonic(String tonic) {
		this.tonic = tonic;
	}
	
	
}
