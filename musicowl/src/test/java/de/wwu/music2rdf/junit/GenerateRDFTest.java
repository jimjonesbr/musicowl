package de.wwu.music2rdf.junit;

import org.junit.*;

import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;

import static org.junit.Assert.*;

import java.io.File;

public class GenerateRDFTest {

	@Test
	public void parseMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		File file = new File("src/test/resources/rdf/elgar_cello_concerto_op.85.nt");
		file.delete();
		
		music2rdf.setInputFile(new File("musicxml/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/elgar_cello_concerto_op.85.nt");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://url.collection.de","My Collection"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		music2rdf.setDateIssued("1919"); 
		
		music2rdf.parseMusicXML();
		
		assertEquals(true, file.exists());
		

	}
	
}