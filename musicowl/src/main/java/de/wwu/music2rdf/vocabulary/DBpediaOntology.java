package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

public class DBpediaOntology {
	
	private static Model model = ModelFactory.createDefaultModel();

    public static final Property occupation  = model.createProperty("http://dbpedia.org/property/occupation");
    public static final Property version  = model.createProperty("http://dbpedia.org/property/version");
    public static final Property order = model.createProperty("http://dbpedia.org/property/order");
    public static final Property document = model.createProperty("http://dbpedia.org/property/document");
    public static final Property mime = model.createProperty("http://dbpedia.org/property/mime");
    
}
