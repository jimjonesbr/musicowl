package de.wwu.music2rdf.junit;

import org.junit.*;

import com.google.common.net.MediaType;

import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;
import de.wwu.music2rdf.core.ScoreResource;

import static org.junit.Assert.*;
import java.io.File;

public class GenerateRDFTest {

	@Test
	public void convertElgarMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/elgar_cello_concerto_op.85");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreIdentifier("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://url.collection.de","Great Composers"));
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlung der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.addResource(new ScoreResource("https://musescore.com/score/152011/download/pdf", "Print",MediaType.PDF.toString()));
		music2rdf.addResource(new ScoreResource("https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)", "Wikipedia Article",MediaType.HTML_UTF_8.toString()));
		music2rdf.setScoreTitle("Cellokonzert e-Moll op. 85");
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
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/achilles_grand-opera.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/achilles_grand-opera");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5731637");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/5731633");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/100001718","Paër, Ferdinando",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Achilles: Grand Opera");
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
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/ariettes_paroles_italienes_et_allemandes.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/ariettes_paroles_italienes_et_allemandes");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5109624");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/5109620");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/11672594X","Crescentini, Girolamo",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("VI Ariettes: Paroles italienes et Allemandesaccompagnées du Piano Forte ou de la Guitarre");
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
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/achtzehn_neue_lieder_gemischten-chor.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/achtzehn_neue_lieder_gemischten-chor");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/2398469");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/2398460");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/117735647","Bisping, Max",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Achtzehn neue Lieder für Gemischten Chor");
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
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/trois_airs_violon.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/trois_airs_violon");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/4287180");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/4287173");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/117735647","Romberg, Andreas",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Trois airs Variés pour le violon");
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
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/leichtes_rondo_piano-forte.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/leichtes_rondo_piano-forte");
		music2rdf.setThumbnail("https://sammlungen.ulb.uni-muenster.de/hd/image/largethumb/5464454");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/5464450");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/129246638","Kelz, Johann Friedrich",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Leichtes Rondo für das Piano-Forte");
		music2rdf.setDateIssued("1861"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/leichtes_rondo_piano-forte.ttl");
		assertEquals(true, file.exists());
	}
	
	@Test
	public void convertSonateFacilePianoForte() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/sonate_facile_piano-forte.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/sonate_facile_piano-forte");
		music2rdf.setThumbnail("http://sammlungen.ulb.uni-muenster.de/download/webcache/304/5109295");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/5109291");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://d-nb.info/gnd/129246638","Bečvařovský, Antonín František",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Sonate facile pour Piano-Forte");
		music2rdf.setDateIssued("1800"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/sonate_facile_piano-forte.ttl");
		assertEquals(true, file.exists());

	}

	@Test
	public void convertSiegesMaerscheMusicXML() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/sieges_maersche_piano-forte.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/sieges_maersche_piano-forte");
		music2rdf.setThumbnail("https://sammlungen.ulb.uni-muenster.de/hd/image/largethumb/5393369");
		music2rdf.setScoreIdentifier("https://sammlungen.ulb.uni-muenster.de/id/5393365");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Eppinger","Eppinger, L.J.",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Sieges Märsche für's Piano-Forte");
		music2rdf.setDateIssued("1850"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/sieges_maersche_piano-forte.ttl");
		assertEquals(true, file.exists());

	}
	
	
	@Test
	public void convertAccidTieTest() {
	
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		
		music2rdf.setInputFile(new File("musicxml/test-score/Accid_Tie_Test.xml"));
		music2rdf.setOutputFile("src/test/resources/rdf/Accid_Tie_Test");
		music2rdf.setThumbnail("https://sammlungen.ulb.uni-muenster.de/hd/image/largethumb/5393369");
		music2rdf.setScoreIdentifier("https://test.com/masthom");
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlungen der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Eppinger","Eppinger, L.J.",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setScoreTitle("Test Score Title");
		music2rdf.setDateIssued("1850"); 
		music2rdf.setOutputFormat("TURTLE");
		music2rdf.isVerbose(false);
		
		music2rdf.parseMusicXML();
		
		File file = new File("src/test/resources/rdf/test-score.ttl");
		assertEquals(true, file.exists());

	}
}
