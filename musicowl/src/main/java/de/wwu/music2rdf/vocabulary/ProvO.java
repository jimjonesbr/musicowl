package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class ProvO {
	
	private static Model prov_model = ModelFactory.createDefaultModel();
	
	public static final String NS = "http://www.w3.org/ns/prov#";
	public static final Resource NAMESPACE = prov_model.createResource(NS);
    public static String getURI() {return NS;}

    public static final Property wasGeneratedBy  = prov_model.createProperty(NS+"wasGeneratedBy");
    public static final Property wasAssociatedWith  = prov_model.createProperty(NS+"wasAssociatedWith");
    public static final Property generated  = prov_model.createProperty(NS+"generated");
    public static final Property hadRole  = prov_model.createProperty(NS+"hadRole");        
    public static final Resource Activity  = prov_model.createProperty(NS+"Activity");

}
