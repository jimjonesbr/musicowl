package de.wwu.music2rdf.example;

import java.io.File;

import com.google.common.net.MediaType;

import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;
import de.wwu.music2rdf.core.ScoreResource;

public class Example {

	public static void main(String[] args) {
				
		MusicXML2RDF music2rdf = new MusicXML2RDF();
			
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("rdf/elgar_cello_concerto_op.85");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreIdentifier("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://wwu.greatcomposers.de","Great Composers"));
		music2rdf.addCollection(new Collection("https://sammlungen.ulb.uni-muenster.de","Digitale Sammlung der Universität und Landesbibliothek Münster"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.addResource(new ScoreResource("https://musescore.com/score/152011/download/pdf", "Print",MediaType.PDF.toString()));
		music2rdf.addResource(new ScoreResource("https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)", "Wikipedia Article",MediaType.HTML_UTF_8.toString()));
		music2rdf.setScoreTitle("Cellokonzert e-Moll op. 85");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1919"); //Formats accepted: yyyy, yyyyMM, yyyyMMdd.
		
		music2rdf.parseMusicXML();
		
	}

}
