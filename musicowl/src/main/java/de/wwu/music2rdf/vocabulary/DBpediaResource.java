package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

public class DBpediaResource {

	private static Model dbpedia_model = ModelFactory.createDefaultModel();

    public static final Resource Encoder  = dbpedia_model.createResource("http://dbpedia.org/resource/Encoder");
    public static final Resource Lyricist  = dbpedia_model.createResource("http://dbpedia.org/resource/Lyricist");
    public static final Resource Arranger  = dbpedia_model.createResource("http://dbpedia.org/resource/Arranger");
    public static final Resource Librettist  = dbpedia_model.createResource("http://dbpedia.org/resource/Librettist");
    public static final Resource Editor  = dbpedia_model.createResource("http://dbpedia.org/resource/Editor");
    public static final Resource Performer  = dbpedia_model.createResource("http://dbpedia.org/resource/Performer");
    public static final Resource Translator  = dbpedia_model.createResource("http://dbpedia.org/resource/Translator");
    public static final Resource Dedicatee  = dbpedia_model.createResource("http://dbpedia.org/resource/Dedicatee");
    public static final Resource Composer  = dbpedia_model.createResource("http://dbpedia.org/resource/Composer");
    public static final Resource Unknown  = dbpedia_model.createResource("http://dbpedia.org/resource/Unknown");
    public static final Resource Document  = dbpedia_model.createResource("http://dbpedia.org/resource/Document");
	
}
