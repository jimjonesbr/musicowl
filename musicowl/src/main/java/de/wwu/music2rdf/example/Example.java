package de.wwu.music2rdf.example;

import java.io.File;

import de.wwu.music2rdf.converter.MusicXML2RDF;

public class Example {

	public static void main(String[] args) {

		/**
		 * Converting the file 'web/xml/1118465.xml' into RDF
		 */
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		
		music2rdf.setInputFile(new File("musicxml/web-samples/elgar_cello_concerto_op.85.xml"));
		music2rdf.setDocumentURI("http://elgar.co.uk/cello-concerto");
		music2rdf.setOutputFile("ntriples/elgar_cello_concerto_op.85.nt");
		
		music2rdf.parseMusicXML();
			
	}

}
