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
    public static final Property hadMember  = prov_model.createProperty(NS+"hadMember");       
    public static final Resource Activity  = prov_model.createProperty(NS+"Activity");
    public static final Resource Collection  = prov_model.createProperty(NS+"Collection");
    public static final Resource Role  = prov_model.createProperty(NS+"Role");
    public static final Resource SoftwareAgent  = prov_model.createProperty(NS+"SoftwareAgent");
    public static final Property startedAtTime  = prov_model.createProperty(NS+"startedAtTime");
    public static final Property endedAtTime  = prov_model.createProperty(NS+"endedAtTime");
    
    
    
}
