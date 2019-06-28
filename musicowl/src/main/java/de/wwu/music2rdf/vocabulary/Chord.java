package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Chord {

	private static Model chord_model = ModelFactory.createDefaultModel();
	public static final String NS_Note = "http://purl.org/ontology/chord/note/";
	public static final String NS_Modifier = "http://purl.org/ontology/chord/modifier/";
	public static final String NS_Base = "http://purl.org/ontology/chord/";
	public static final Resource NAMESPACE_NOTE = chord_model.createResource(NS_Note);
	public static final Resource NAMESPACE_BASE = chord_model.createResource(NS_Base);
	public static final Resource NAMESPACE_MODIFIER = chord_model.createResource(NS_Modifier);
    
    public static final Resource C  = chord_model.createResource(NS_Note+"C");
    public static final Resource D  = chord_model.createResource(NS_Note+"D");
    public static final Resource E  = chord_model.createResource(NS_Note+"E");
    public static final Resource F  = chord_model.createResource(NS_Note+"F");
    public static final Resource G  = chord_model.createResource(NS_Note+"G");
    public static final Resource A  = chord_model.createResource(NS_Note+"A");
    public static final Resource B  = chord_model.createResource(NS_Note+"B");
    public static final Resource Natural  = chord_model.createResource(NS_Base+"Natural");
    public static final Resource Sharp  = chord_model.createResource(NS_Base+"sharp");
    public static final Resource Flat  = chord_model.createResource(NS_Base+"flat");
    public static final Resource DoubleSharp  = chord_model.createResource(NS_Base+"doublesharp");
    public static final Resource DoubleFlat  = chord_model.createResource(NS_Base+"doubleflat");
    public static final Resource Demiflat  = chord_model.createResource(NS_Base+"demiflat");
    public static final Resource Demisharp  = chord_model.createResource(NS_Base+"demisharp");
    public static final Resource Sesquisharp  = chord_model.createResource(NS_Base+"sesquisharp");
    public static final Resource Sesquiflat  = chord_model.createResource(NS_Base+"sesquiflat");
    public static final Resource Rest  = chord_model.createResource(NS_Note+"Rest");
    public static final Resource Note  = chord_model.createResource(NS_Base+"Note");

    public static final Property modifier  = chord_model.createProperty(NS_Base+"modifier");
    public static final Property natural  = chord_model.createProperty(NS_Base+"natural");
}
