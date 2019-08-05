package de.wwu.music2rdf.example;

import java.io.File;

import com.google.common.net.MediaType;

import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;
import de.wwu.music2rdf.core.ScoreResource;

public class ExampleOpenScore {

	public static void main(String[] args) {
				
		MusicXML2RDF music2rdf = new MusicXML2RDF();
			
		music2rdf.setInputFile(new File("musicxml/openscore/bach-brandenburg_concerto-BWV1041.mxl"));
		music2rdf.setOutputFile("rdf/bach_brandenburg_concerto");
		music2rdf.setThumbnail("https://www.c82.net/images/offthestaff/large/brandenburg-concerto-no-3-in-g-major-color.jpg");
		music2rdf.setScoreURI("https://musescore.com/openscore/scores/5090058");
		music2rdf.addCollection(new Collection("https://musescore.com/openscore","OpenScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Johann_Sebastian_Bach","Johann Sebastian Bach",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/user/1231121","Roberto Frigato",Role.ENCODER));
		music2rdf.addPerson(new Person("https://www.c82.net/offthestaff","Nicholas Rougeux",Role.LIBRETTIST));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.addResource(new ScoreResource("https://musescore.com/score/5090058/download/pdf", "Print File",MediaType.PDF.toString()));	
		music2rdf.setDocumentTitle("Brandenburg Concerto No.3-BWV 1048");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("n-triples");
		music2rdf.setDateIssued("1721"); 
				
		music2rdf.parseMusicXML();
		
		music2rdf = new MusicXML2RDF();			
		music2rdf.setInputFile(new File("musicxml/openscore/mozart-symphony_no.41-jupiter.mxl"));
		music2rdf.setOutputFile("rdf/mozart-symphony_no.41-jupiter");
		music2rdf.setThumbnail("https://www.c82.net/images/offthestaff/large/symphony-no-41-in-c-major-jupiter-color.jpg");
		music2rdf.setScoreURI("https://musescore.com/openscore/scores/4861738");
		music2rdf.addCollection(new Collection("https://musescore.com/openscore","OpenScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Frederic_Chopin","Frédéric Chopin",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/user/23821766","Naomi Barrettara",Role.LIBRETTIST));
		music2rdf.addPerson(new Person("https://www.c82.net/offthestaff","Nicholas Rougeux",Role.LIBRETTIST));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.addPerson(new Person("https://musescore.com/user/13502736","Musemeister",Role.ENCODER));
		music2rdf.addResource(new ScoreResource("https://musescore.com/score/4861738/download/pdf", "Print File",MediaType.PDF.toString()));
		music2rdf.setDocumentTitle("Symphony No.41");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1788"); 		
		music2rdf.parseMusicXML();
				
		music2rdf = new MusicXML2RDF();			
		music2rdf.setInputFile(new File("musicxml/openscore/tchaikovsky-1812_overture.mxl"));
		music2rdf.setOutputFile("rdf/tchaikovsky-1812_overture");
		music2rdf.setThumbnail("https://www.c82.net/images/offthestaff/large/1812-overture-color.jpg");
		music2rdf.setScoreURI("https://musescore.com/openscore/scores/4145221");
		music2rdf.addCollection(new Collection("https://musescore.com/openscore","OpenScore"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Pjotr_Iljitsch_Tschaikowski","Pyotr Ilyich Tchaikovsky",Role.COMPOSER));
		music2rdf.addPerson(new Person("https://musescore.com/user/6105546","Mike Nelson",Role.ENCODER));
		music2rdf.addPerson(new Person("https://www.c82.net/offthestaff","Nicholas Rougeux",Role.LIBRETTIST));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));			
		music2rdf.addResource(new ScoreResource("https://musescore.com/score/4145221/download/pdf", "Print File",MediaType.PDF.toString()));
		music2rdf.setDocumentTitle("The Year 1812 Op.49");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1822"); 
		
		music2rdf.parseMusicXML();
		
	}

}
