package de.wwu.music2rdf.core;

import java.util.ArrayList;

public class ScorePart {

	private String id;
	private String name;
	private String instrument;
	private int movement;
	private boolean solo;
	
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

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public boolean isSolo() {
		return solo;
	}

	public void setSolo(boolean solo) {
		this.solo = solo;
	}
	
	
}
