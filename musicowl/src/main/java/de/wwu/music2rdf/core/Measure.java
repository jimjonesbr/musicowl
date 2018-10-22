package de.wwu.music2rdf.core;

import java.util.ArrayList;

public class Measure {

	private String id;
	private Key key;
	private TimeSignature time;
	private ArrayList<Note> notes;
	private String barline;
	private String title;
	private int beatsPerMinute = 0;
	private String beatUnit = "";
	
	public Measure() {
		super();
		this.key = new Key();
		this.time = new TimeSignature();
		this.notes = new ArrayList<Note>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Key getKey() {
		return key;
	}

	public TimeSignature getTime() {
		return time;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}

	public String getBarline() {
		return barline;
	}

	public void setBarline(String barline) {
		this.barline = barline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getBeatsPerMinute() {
		return beatsPerMinute;
	}

	public void setBeatsPerMinute(int beatsPerMinute) {
		this.beatsPerMinute = beatsPerMinute;
	}

	public String getBeatUnit() {
		return beatUnit;
	}

	public void setBeatUnit(String beatUnit) {
		this.beatUnit = beatUnit;
	}

	
	
	
}
