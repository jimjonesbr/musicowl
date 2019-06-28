package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MusicOntology {

	private static Model mo_model = ModelFactory.createDefaultModel();
	public static final String NS = "http://purl.org/ontology/mo/";
	public static final Resource NAMESPACE = mo_model.createResource(NS);
    public static String getURI() {return NS;}
    
    public static final Resource Score  = mo_model.createResource(NS+"Score");
    public static final Resource Movement  = mo_model.createResource(NS+"Movement");
    public static final Resource Instrument  = mo_model.createResource(NS+"Instrument");
    public static final Property movement  = mo_model.createProperty(NS+"movement");

}
