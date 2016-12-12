package de.wwu.musicowl.core;

import java.util.ArrayList;

public class MusicScore {

	private ArrayList<ScorePart> parts;
	private String uri;
	private String fileName;
	private String title;
	
	public MusicScore() {
	
		super();
		this.parts = new ArrayList<ScorePart>();
		
	}

	public ArrayList<ScorePart> getParts() {
		return parts;
	}


	public String getURI() {
		return uri;
	}

	public void setURI(String id) {
		this.uri = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	
}
