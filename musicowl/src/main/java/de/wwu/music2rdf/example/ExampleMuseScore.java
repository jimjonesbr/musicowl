package de.wwu.music2rdf.example;

import java.io.File;

import com.google.common.net.MediaType;

import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;
import de.wwu.music2rdf.core.ScoreResource;

public class ExampleMuseScore {

	public static void main(String[] args) {
				
		MusicXML2RDF music2rdf = new MusicXML2RDF();
			
		music2rdf.setInputFile(new File("musicxml/musescore/BACHIANAS_BRASILEIRAS__Ndeg_5_VILLA_LOBOS_.mxl"));
		music2rdf.setOutputFile("rdf/bachianas-brasileiras_villa-lobos");
		music2rdf.setThumbnail("http://www.jobim.org/paulomoura/bitstream/handle/2010.6/166/PartAr%20b%2012%201-2.jpg");
		music2rdf.setScoreURI("https://musescore.com/user/10811666/scores/2369571");
		music2rdf.addCollection(new Collection("https://musescore.com","MuseScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Heitor_Villa-Lobos","Heitor Villa-Lobos",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/user/10811666","Herivaldo Alves Pereira",Role.ENCODER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setOnlineResource(new ScoreResource("https://musescore.com/score/2369571/download/pdf", "Print File",MediaType.PDF.toString()));
		music2rdf.setDocumentTitle("Bachianas Brasileiras N°5 - Cantilena");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1930"); 
		music2rdf.parseMusicXML();
		
		music2rdf = new MusicXML2RDF();			
		music2rdf.setInputFile(new File("musicxml/musescore/Chopin_Prelude_E_minor_Op_28_No_4_w_chord_symbols.mxl"));
		music2rdf.setOutputFile("rdf/chopin-prelude-e_minor-op_28");
		music2rdf.setThumbnail("https://www.omifacsimiles.com/brochures/images/chop_pre28.jpg");
		music2rdf.setScoreURI("https://musescore.com/ericfontainejazz/chopin-prelude-e-min-op28-no4");
		music2rdf.addCollection(new Collection("https://musescore.com","MuseScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Frederic_Chopin","Frédéric Chopin",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/ericfontainejazz","Eric Fontaine",Role.ENCODER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.setDocumentTitle("Chopin Prelude E minor, Op 28, No 4");
		music2rdf.setOnlineResource(new ScoreResource("https://musescore.com/score/1191021/download/pdf", "Print File",MediaType.PDF.toString()));		
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("json-ld");
		music2rdf.setDateIssued("1848"); 
		music2rdf.parseMusicXML();	
				
		music2rdf = new MusicXML2RDF();			
		music2rdf.setInputFile(new File("musicxml/musescore/J._S._Bach_Air_from_Suite_in_D_BWV_1068_2_organ_Hauptwerk_Doesburg_and_violin.mxl"));
		music2rdf.setOutputFile("rdf/bach-air-g-string");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/BWV1006_preludio_autograph_manuscript_1720.jpeg/300px-BWV1006_preludio_autograph_manuscript_1720.jpeg");
		music2rdf.setScoreURI("https://musescore.com/bsg/scores/3649631");
		music2rdf.addCollection(new Collection("https://musescore.com","MuseScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Johann_Sebastian_Bach","Johann Sebastian Bach",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/bsg","BSG",Role.ENCODER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.setOnlineResource(new ScoreResource("https://musescore.com/score/3649631/download/pdf", "Print File",MediaType.PDF.toString()));
		music2rdf.setDocumentTitle("Air from Suite in D, BWV 1068");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("n-triples");
		music2rdf.setDateIssued("1717"); 
		music2rdf.parseMusicXML();	
		
		
		music2rdf = new MusicXML2RDF();			
		music2rdf.setInputFile(new File("musicxml/musescore/Toccata_and_Fugue_in_D_Minor.mxl"));
		music2rdf.setOutputFile("rdf/toccata-fugue-d_minor");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e9/Rincgk_01.jpg/800px-Rincgk_01.jpg");
		music2rdf.setScoreURI("https://musescore.com/classicman/scores/55680");
		music2rdf.addCollection(new Collection("https://musescore.com","MuseScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Johann_Sebastian_Bach","Johann Sebastian Bach",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/classicman","ClassicMan",Role.ENCODER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.setOnlineResource(new ScoreResource("https://musescore.com/score/55680/download/pdf", "Print File",MediaType.PDF.toString()));
		music2rdf.setDocumentTitle("Toccata and Fugue in D Minor");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1703"); 
		music2rdf.parseMusicXML();
		

		music2rdf = new MusicXML2RDF();	
		music2rdf.setInputFile(new File("musicxml/musescore/Vocalise_by_Rachmaninoff_for_Violin_and_Piano.mxl"));
		music2rdf.setOutputFile("rdf/vocalise-rachmaninoff-piano_violin");
		music2rdf.setThumbnail("https://www.cmuse.org/wp-content/uploads/2015/12/silent-night-manuscript-1.jpg");
		music2rdf.setScoreURI("https://musescore.com/user/30892962/scores/5398515");
		music2rdf.addCollection(new Collection("https://musescore.com","MuseScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Sergei_Wassiljewitsch_Rachmaninow","Sergei Wassiljewitsch Rachmaninow",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/user/30892962","ViolinWorker",Role.ENCODER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setOnlineResource(new ScoreResource("https://musescore.com/score/5398515/download/pdf", "Print File",MediaType.PDF.toString()));
		music2rdf.setDocumentTitle("Vocalise");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1912"); 
		music2rdf.parseMusicXML();	

				
	}

}
