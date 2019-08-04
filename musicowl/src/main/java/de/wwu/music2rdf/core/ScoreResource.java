package de.wwu.music2rdf.core;

public class ScoreResource {

	private String url;
	private String description;
	private String type;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ScoreResource() {
		super();
	}
	public ScoreResource(String url, String description) {
		super();
		this.url = url;
		this.description = description;
		this.type = "";
	}

	public ScoreResource(String url) {
		super();
		this.url = url;
		this.description = "";
		this.type = "";
	}
	public ScoreResource(String url, String description, String type) {
		super();
		this.url = url;
		this.description = description;
		this.type = type;
	}
	
	
}
