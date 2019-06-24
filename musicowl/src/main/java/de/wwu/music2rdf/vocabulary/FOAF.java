package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class FOAF {
	
	private static Model foaf_model = ModelFactory.createDefaultModel();
	public static final String NS = "http://xmlns.com/foaf/0.1/";
	public static final Resource NAMESPACE = foaf_model.createResource(NS);
    public static String getURI() {return NS;}
    
    public static final Property thumbnail  = foaf_model.createProperty(NS+"thumbnail");
    public static final Property name  = foaf_model.createProperty(NS+"name");
    public static final Resource Person  = foaf_model.createProperty(NS+"Person");

}
