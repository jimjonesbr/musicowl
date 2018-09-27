package de.wwu.music2rdf.core;

public class Person {

	private String uri;
	private String name;
	private String role;
	
	public Person() {
		super();
	}
		
	public Person(String uri, String name, String role) {
		super();
		this.uri = uri;
		this.name = name;
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
		
}
