package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class InstrumentTaxonomy {

	private static Model instrument_model = ModelFactory.createDefaultModel();
	public static final String NS = "http://purl.org/ontology/mo/mit#";

	public static final Property inScheme  = instrument_model.createProperty(NS+"inScheme");
	public static final Resource Musical_instruments  = instrument_model.createProperty(NS+"Musical_instrument");
}
