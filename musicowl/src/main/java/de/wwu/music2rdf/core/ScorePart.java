package de.wwu.music2rdf.core;

import java.util.ArrayList;

public class ScorePart {

	private String id;
	private String name;
	private int movement;
	
	private ArrayList<Measure> measures;
	
	public ScorePart() {
		super();
		this.measures = new ArrayList<Measure>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Measure> getMeasures() {
		return measures;
	}

	public int getMovement() {
		return movement;
	}

	public void setMovement(int movement) {
		this.movement = movement;
	}
	
	
}
