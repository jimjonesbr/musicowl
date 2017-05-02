package de.wwu.music2rdf.example;

import java.io.File;

import de.wwu.music2rdf.converter.MusicXML2RDF;

public class Example {

	public static void main(String[] args) {

		/**
		 * Converting the file 'web/xml/1118465.xml' into RDF
		 */
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		
		music2rdf.setOutputFile("ntriples/1118465.nt");
		music2rdf.parseMusicXML(new File("web/xml/1118465.xml"));
			
	}

}
