package de.wwu.music2rdf.example;

import java.io.File;

import de.wwu.music2rdf.converter.MusicXML2RDF;

public class Example {

	public static void main(String[] args) {

		MusicXML2RDF music2rdf = new MusicXML2RDF();
		
		music2rdf.setInputFile(new File("musicxml/web-samples/elgar_cello_concerto_op.85.xml"));
		music2rdf.setDocumentURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.setOutputFile("ntriples/elgar_cello_concerto_op.85.nt");
		
		music2rdf.parseMusicXML();
		
		
		music2rdf.setInputFile(new File("/home/jones/git/wmss/wmss/data/musicxml/wwu/3098742@25stuckefürorgel.xml"));
		music2rdf.setDocumentURI("https://sammlungen.ulb.uni-muenster.de/id/3098742");
		music2rdf.setOutputFile("ntriples/3098742@25stuckefürorgel.nt");
		
		music2rdf.parseMusicXML();
		
			
	}

}
