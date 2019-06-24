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
	public void convertElgarMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		File file = new File("src/test/resources/rdf/elgar_cello_concerto_op.85.nt");
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/elgar_cello_concerto_op.85.nt");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://url.collection.de","Great Composers"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		music2rdf.setDateIssued("1919"); 
		music2rdf.isVerbose(false);
		music2rdf.parseMusicXML();
		
		assertEquals(true, file.exists());

	}

	@Test
	public void convertAchillesGrandOperaMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		File file = new File("src/test/resources/rdf/achilles_grand-opera.nt");
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/achilles_grand-opera.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/achilles_grand-opera.nt");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5731637");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5731633");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/100001718","Paër, Ferdinando",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Achilles: Grand Opera");
		music2rdf.setDateIssued("1802"); 
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		assertEquals(true, file.exists());

	}
	
	@Test
	public void convertAriettesGrandOperaMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		File file = new File("src/test/resources/rdf/ariettes_paroles_italienes_et_allemandes.nt");
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/ariettes_paroles_italienes_et_allemandes.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/ariettes_paroles_italienes_et_allemandes.nt");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5109624");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5109620");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/11672594X","Crescentini, Girolamo",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("VI Ariettes: Paroles italienes et Allemandesaccompagnées du Piano Forte ou de la Guitarre");
		music2rdf.setDateIssued("1802"); 
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		assertEquals(true, file.exists());

	}	
}
