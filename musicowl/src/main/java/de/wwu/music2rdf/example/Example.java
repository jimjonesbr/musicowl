package de.wwu.music2rdf.example;

import static org.junit.Assert.assertEquals;

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
		music2rdf.addCollection(new Collection("https://url.collection.de","Great Composers"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.setDateIssued("1919"); //Formats accepted: yyyy, yyyyMM, yyyyMMdd.
		
		music2rdf.parseMusicXML();
	
		
		
				
		music2rdf.setInputFile(new File("musicxml/sonate_facile_piano-forte.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/sonate_facile_piano-forte");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5109295");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5109291");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/129246638","Bečvařovský, Antonín František",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Sonate facile pour Piano-Forte");
		music2rdf.setDateIssued("1800"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/sonate_facile_piano-forte.ttl");
		assertEquals(true, file.exists());
	}

}
