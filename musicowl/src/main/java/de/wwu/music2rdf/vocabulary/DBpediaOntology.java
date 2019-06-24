package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

public class DBpediaOntology {
	
	private static Model dbpo_model = ModelFactory.createDefaultModel();

    public static final Property occupation  = dbpo_model.createProperty("http://dbpedia.org/ontology/occupation");

}
