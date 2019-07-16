package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

public class DBpediaOntology {
	
	private static Model model = ModelFactory.createDefaultModel();

    public static final Property occupation  = model.createProperty("http://dbpedia.org/ontology/occupation");
    public static final Property version  = model.createProperty("http://dbpedia.org/ontology/version");

}
