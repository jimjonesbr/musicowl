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
		
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/elgar_cello_concerto_op.85");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://url.collection.de","Great Composers"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		music2rdf.setDateIssued("1919"); 
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/elgar_cello_concerto_op.85.ttl");
		assertEquals(true, file.exists());

	}

	@Test
	public void convertAchillesGrandOperaMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/achilles_grand-opera.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/achilles_grand-opera");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5731637");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5731633");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/100001718","Paër, Ferdinando",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Achilles: Grand Opera");
		music2rdf.setDateIssued("1802"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/achilles_grand-opera.ttl");
		assertEquals(true, file.exists());

	}
	
	@Test
	public void convertAriettesGrandOperaMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/ariettes_paroles_italienes_et_allemandes.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/ariettes_paroles_italienes_et_allemandes");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5109624");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5109620");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/11672594X","Crescentini, Girolamo",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("VI Ariettes: Paroles italienes et Allemandesaccompagnées du Piano Forte ou de la Guitarre");
		music2rdf.setDateIssued("1802"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/ariettes_paroles_italienes_et_allemandes.ttl");
		assertEquals(true, file.exists());

	}
	
	@Test
	public void convertAchtzhenNeueLiederMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/achtzehn_neue_lieder_gemischten-chor.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/achtzehn_neue_lieder_gemischten-chor");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/2398469");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/2398460");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/117735647","Bisping, Max",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Achtzehn neue Lieder für Gemischten Chor");
		music2rdf.setDateIssued("1871"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/achtzehn_neue_lieder_gemischten-chor.ttl");
		assertEquals(true, file.exists());
	}
	
	
	@Test
	public void convertATroisAirsMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		//file.delete();
		
		music2rdf.setInputFile(new File("musicxml/trois_airs_violon.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/trois_airs_violon");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/4287180");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/4287173");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/117735647","Romberg, Andreas",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Trois airs Variés pour le violon");
		//music2rdf.setDateIssued("1871"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/trois_airs_violon.ttl");
		assertEquals(true, file.exists());

	}

	
	@Test
	public void convertLeichtesRondoMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		
		music2rdf.setInputFile(new File("musicxml/leichtes_rondo_piano-forte.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/leichtes_rondo_piano-forte");
		music2rdf.setThumbnail("https://sammlungen.ulb.uni-muenster.de/hd/image/largethumb/5464454");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5464450");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/129246638","Kelz, Johann Friedrich",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Leichtes Rondo für das Piano-Forte");
		music2rdf.setDateIssued("1861"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/leichtes_rondo_piano-forte.ttl");
		assertEquals(true, file.exists());
	}
	
	@Test
	public void convertSiegesMaerscheMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		
		music2rdf.setInputFile(new File("musicxml/sieges_maersche_piano-forte.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/sieges_maersche_piano-forte");
		music2rdf.setThumbnail("https://sammlungen.ulb.uni-muenster.de/hd/image/largethumb/5393369");
		music2rdf.setScoreURI("https://sammlungen.ulb.uni-muenster.de/id/5393365");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/129246638","Eppinger, L.J.",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Sieges Mærsche für's Piano-Forte : gewidmet den Witwen und Waisen der Landwehr-mäner des k.k. Hoch und Deutschmeister Regiments");
		music2rdf.setDateIssued("1850"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/sieges_maersche_piano-forte.ttl");
		assertEquals(true, file.exists());

	}

}
