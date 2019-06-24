package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Tonality {

	private static Model tonality_model = ModelFactory.createDefaultModel();
	
	public static final String NS_Base = "http://purl.org/ontology/tonality/";
	public static final String NS_Mode = "http://purl.org/ontology/tonality/mode/";
		
	public static final Resource NAMESPACE_BASE = tonality_model.createResource(NS_Base);
	public static final Resource NAMESPACE_MODE = tonality_model.createResource(NS_Mode);
    
    public static String getBaseURI() {return NS_Base;}
    public static String getModeURI() {return NS_Mode;}

    public static final Property tonic  = tonality_model.createProperty(NS_Base+"tonic");
    public static final Property mode  = tonality_model.createProperty(NS_Base+"mode");
    
    public static final Resource major  = tonality_model.createProperty(NS_Mode+"major");
    public static final Resource minor  = tonality_model.createProperty(NS_Mode+"minor");
    public static final Resource Key  = tonality_model.createProperty(NS_Base+"Key");
}
