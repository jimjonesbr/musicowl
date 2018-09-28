package de.wwu.music2rdf.example;

import java.io.File;
import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;

public class Example {

	public static void main(String[] args) {
				
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		
		music2rdf.setInputFile(new File("musicxml/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("ntriples/elgar_cello_concerto_op.85.nt");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://url.collection.de","My Collection"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		
		music2rdf.parseMusicXML();
		

	}

}
