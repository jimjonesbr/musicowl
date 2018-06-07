package de.wwu.music2rdf.core;

import java.util.ArrayList;

public class MusicScore {

	private ArrayList<ScorePart> parts;
	private String uri;
	private String fileName;
	private String fileContent;
	private String title;
	private String encoder;
	private String encodedAt;
	
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

	public void setOutputFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

	public String getEncodingStartTime() {
		return encodedAt;
	}

	public void setEncodingStartTime(String encodedAt) {
		this.encodedAt = encodedAt;
	}


	
}
