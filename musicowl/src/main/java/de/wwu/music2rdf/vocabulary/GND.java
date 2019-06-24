package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class GND {
		
	private static Model gnd_model = ModelFactory.createDefaultModel();

	public static final String NS = "http://d-nb.info/standards/elementset/gnd#";
	public static final Resource NAMESPACE = gnd_model.createResource(NS);
    public static String getURI() {return NS;}

    public static final Property preferredNameForTheSubjectHeading  = gnd_model.createProperty(NS+"preferredNameForTheSubjectHeading");
    public static final Property professionOrOccupation  = gnd_model.createProperty(NS+"professionOrOccupation");
    public static final Resource Encoder  = gnd_model.createProperty("http://d-nb.info/gnd/4139395-8");
    public static final Resource Composer  = gnd_model.createProperty("http://d-nb.info/gnd/4032009-1");
    public static final Resource Lyricist  = gnd_model.createProperty("http://d-nb.info/gnd/4246394-4");
    public static final Resource Arranger  = gnd_model.createProperty("http://d-nb.info/gnd/4604119-9");
    public static final Resource Librettist  = gnd_model.createProperty("http://d-nb.info/gnd/4294338-3");
    public static final Resource Editor  = gnd_model.createProperty("http://d-nb.info/gnd/4159575-0");
    public static final Resource Performer  = gnd_model.createProperty("http://d-nb.info/gnd/4170790-4");
    public static final Resource Translator  = gnd_model.createProperty("http://d-nb.info/gnd/4061414-1");

}
