package de.wwu.musicowl.core;

import java.util.ArrayList;

public class MusicScore {

	private ArrayList<ScorePart> parts;
	private String id;
	private String fileName;
	
	public MusicScore() {
	
		super();
		this.parts = new ArrayList<ScorePart>();
		
	}

	public ArrayList<ScorePart> getParts() {
		return parts;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
}
