package de.wwu.music2rdf.core;

public class Instrument {

	String performanceMediumId;
	String performanceMediumTypeId;
	String performanceMediumDescription;
	
	public Instrument() {
		super();
	}

	public String getPerformanceMediumId() {
		return performanceMediumId;
	}

	public void setPerformanceMediumId(String performanceMediumId) {
		this.performanceMediumId = performanceMediumId;
	}

	public String getPerformanceMediumTypeId() {
		return performanceMediumTypeId;
	}

	public void setPerformanceMediumTypeId(String performanceMediumTypeId) {
		this.performanceMediumTypeId = performanceMediumTypeId;
	}

	public String getPerformanceMediumDescription() {
		return performanceMediumDescription;
	}

	public void setPerformanceMediumDescription(String performanceMediumTypeDescription) {
		this.performanceMediumDescription = performanceMediumTypeDescription;
	}
	
	
	
}
