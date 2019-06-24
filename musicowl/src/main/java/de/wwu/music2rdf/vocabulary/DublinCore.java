package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

public class DublinCore {

	private static Model dc_model = ModelFactory.createDefaultModel();
	
    public static final String NS_dcterms = "http://purl.org/dc/terms/";
    public static final String NS_dcelements = "http://purl.org/dc/elements/1.1/";
		
	public static final Property title = dc_model.createProperty(NS_dcelements+"title");
	public static final Property creator = dc_model.createProperty(NS_dcelements+"creator");
	public static final Property issued = dc_model.createProperty(NS_dcterms+"issued");
	
}
