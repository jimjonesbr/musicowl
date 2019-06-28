package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

public class RDF {
	
	private static Model prov_model = ModelFactory.createDefaultModel();

    public static final Property label  = prov_model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#label");
    public static final Property type  	= prov_model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

}
