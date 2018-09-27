package de.wwu.music2rdf.core;

public class Collection {

	private String collectionURI;
	private String collectionName;
	
	public Collection() {
		super();
	}
	public Collection(String collectionURI, String collectionName) {
		super();
		this.collectionURI = collectionURI;
		this.collectionName = collectionName;
	}
	public String getCollectionURI() {
		return collectionURI;
	}
	public void setCollectionURI(String collectionURI) {
		this.collectionURI = collectionURI;
	}
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	
	
}
