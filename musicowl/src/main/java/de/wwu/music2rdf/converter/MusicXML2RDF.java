package de.wwu.music2rdf.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import de.wwu.music2rdf.core.Clef;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Instrument;
import de.wwu.music2rdf.core.Key;
import de.wwu.music2rdf.core.Measure;
import de.wwu.music2rdf.core.Movement;
import de.wwu.music2rdf.core.MusicScore;
import de.wwu.music2rdf.core.Note;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;
import de.wwu.music2rdf.core.ScorePart;
import de.wwu.music2rdf.core.Staff;
import de.wwu.music2rdf.core.Voice;
import de.wwu.music2rdf.util.Util;
import de.wwu.music2rdf.vocabulary.Chord;
import de.wwu.music2rdf.vocabulary.DBpediaOntology;
import de.wwu.music2rdf.vocabulary.DBpediaResource;
import de.wwu.music2rdf.vocabulary.GND;
import de.wwu.music2rdf.vocabulary.InstrumentTaxonomy;
import de.wwu.music2rdf.vocabulary.MusicOWL;
import de.wwu.music2rdf.vocabulary.MusicOntology;
import de.wwu.music2rdf.vocabulary.ProvO;
import de.wwu.music2rdf.vocabulary.Tonality;

public class MusicXML2RDF {

	private ArrayList<Clef> clefList;
	private ArrayList<Note> currentNotes;
	private ArrayList<Instrument> instruments = new Util().getInstruments();
	private ArrayList<Person> persons;
	private boolean verbose = false;
	private String outputFile = "";
	private File inputFile = null;
	private String scoreURI = "";
	private String documentTitle = "";
	private String thumbnail = "";
	private String outputFormat = "TURTLE";
	private Collection collection;
	private static Logger logger = Logger.getLogger("Converter");
	private String dateIssued = "";
	private ArrayList<Note> accidentalsOverwrite = new ArrayList<Note>();
	
	public MusicXML2RDF() {
		super();
		this.clefList = new ArrayList<Clef>();
		this.currentNotes = new ArrayList<Note>();
		this.persons = new ArrayList<Person>();
		this.collection = new Collection();
	}

	public void addPerson(Person person) {
		this.persons.add(person);
	}

	public void addCollection(Collection collection) {
		this.collection.setCollectionURI(collection.getCollectionURI());
		this.collection.setCollectionName(collection.getCollectionName());
	}

	public void isVerbose (boolean verbose) {
		this.verbose = verbose;
	}

	
	
	private String getAccidental(Key key, Note note) {
		
		ArrayList<Note> measureAccidentals = new ArrayList<Note>();
				
		if((key.getTonic().equals("G") && key.getMode().equals("major")) || (key.getTonic().equals("E") && key.getMode().equals("minor"))){

				measureAccidentals.add(new Note("F","sharp"));

		}

		if((key.getTonic().equals("D") && key.getMode().equals("major")) || (key.getTonic().equals("B") && key.getMode().equals("minor"))){

				measureAccidentals.add(new Note("F","sharp"));
				measureAccidentals.add(new Note("C","sharp"));

		}

		if((key.getTonic().equals("A") && key.getMode().equals("major")) || (key.getTonic().equals("F") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("F","sharp"));
				measureAccidentals.add(new Note("C","sharp"));
				measureAccidentals.add(new Note("G","sharp"));

		}

		if((key.getTonic().equals("E") && key.getMode().equals("major")) || (key.getTonic().equals("C") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("F","sharp"));
				measureAccidentals.add(new Note("C","sharp"));
				measureAccidentals.add(new Note("G","sharp"));
				measureAccidentals.add(new Note("D","sharp"));

		}

		if((key.getTonic().equals("B") && key.getMode().equals("major")) || (key.getTonic().equals("G") && key.getMode().equals("minor"))){							
				
				measureAccidentals.add(new Note("F","sharp"));
				measureAccidentals.add(new Note("C","sharp"));
				measureAccidentals.add(new Note("G","sharp"));
				measureAccidentals.add(new Note("D","sharp"));
				measureAccidentals.add(new Note("A","sharp"));
	
		}

		if((key.getTonic().equals("Fs") && key.getMode().equals("major")) || (key.getTonic().equals("Ds") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("F","sharp"));
				measureAccidentals.add(new Note("C","sharp"));
				measureAccidentals.add(new Note("G","sharp"));
				measureAccidentals.add(new Note("D","sharp"));
				measureAccidentals.add(new Note("A","sharp"));
				measureAccidentals.add(new Note("E","sharp"));

		}

		if((key.getTonic().equals("Cs") && key.getMode().equals("major")) || (key.getTonic().equals("As") && key.getMode().equals("minor"))){

				measureAccidentals.add(new Note("F","sharp"));
				measureAccidentals.add(new Note("C","sharp"));
				measureAccidentals.add(new Note("G","sharp"));
				measureAccidentals.add(new Note("D","sharp"));
				measureAccidentals.add(new Note("A","sharp"));
				measureAccidentals.add(new Note("E","sharp"));
				measureAccidentals.add(new Note("B","sharp"));

		}
	
		
		if((key.getTonic().equals("F") && key.getMode().equals("major")) || (key.getTonic().equals("D") && key.getMode().equals("minor"))){

				measureAccidentals.add(new Note("B","flat"));

		}

		if((key.getTonic().equals("Bb") && key.getMode().equals("major")) || (key.getTonic().equals("G") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("B","flat"));
				measureAccidentals.add(new Note("E","flat"));


		}

		if((key.getTonic().equals("Eb") && key.getMode().equals("major")) || (key.getTonic().equals("C") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("B","flat"));
				measureAccidentals.add(new Note("E","flat"));
				measureAccidentals.add(new Note("A","flat"));

		}

		if((key.getTonic().equals("Ab") && key.getMode().equals("major")) || (key.getTonic().equals("F") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("B","flat"));
				measureAccidentals.add(new Note("E","flat"));
				measureAccidentals.add(new Note("A","flat"));
				measureAccidentals.add(new Note("D","flat"));


		}

		if((key.getTonic().equals("Db") && key.getMode().equals("major")) || (key.getTonic().equals("Bb") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("B","flat"));
				measureAccidentals.add(new Note("E","flat"));
				measureAccidentals.add(new Note("A","flat"));
				measureAccidentals.add(new Note("D","flat"));
				measureAccidentals.add(new Note("G","flat"));
	
		}

		if((key.getTonic().equals("Gb") && key.getMode().equals("major")) || (key.getTonic().equals("Eb") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("B","flat"));
				measureAccidentals.add(new Note("E","flat"));
				measureAccidentals.add(new Note("A","flat"));
				measureAccidentals.add(new Note("D","flat"));
				measureAccidentals.add(new Note("G","flat"));
				measureAccidentals.add(new Note("C","flat"));		

		}

		if((key.getTonic().equals("Cb") && key.getMode().equals("major")) || (key.getTonic().equals("Ab") && key.getMode().equals("minor"))){
				
				measureAccidentals.add(new Note("B","flat"));
				measureAccidentals.add(new Note("E","flat"));
				measureAccidentals.add(new Note("A","flat"));
				measureAccidentals.add(new Note("D","flat"));
				measureAccidentals.add(new Note("G","flat"));
				measureAccidentals.add(new Note("C","flat"));
				measureAccidentals.add(new Note("F","flat"));
		
		}
		


		/**
		 * Updating accidental list with measure specific accidentals
		 */
		
		for (int i = 0; i < this.accidentalsOverwrite.size(); i++) {
		
			boolean accidentalOverwritten = false;
			
			for (int j = 0; j < measureAccidentals.size(); j++) {
				
				if(measureAccidentals.get(j).getPitch().equals(this.accidentalsOverwrite.get(i).getPitch())) {
					
					measureAccidentals.get(j).setAccidental(this.accidentalsOverwrite.get(i).getAccidental());										
					accidentalOverwritten = true;
				}
				
			}
			
			if(!accidentalOverwritten) {
				measureAccidentals.add(this.accidentalsOverwrite.get(i));
			}
			
		}
		
		
		/**
		 * Removing from accidental overwrite in case of natural
		 */
		
		for (Iterator<Note> it = measureAccidentals.iterator(); it.hasNext(); ){
			Note n = it.next();
			if(n.getAccidental().equals("natural")) {
				it.remove();
			}
		}
		
		
		
		/**
		 * Returning the accidental for a given note, taking into account key related modifiers and possible overwrites inside of a measure.
		 */
		for (int i = 0; i < measureAccidentals.size(); i++) {
			if(measureAccidentals.get(i).getPitch().equals(note.getPitch())) {
				note.setAccidental(measureAccidentals.get(i).getAccidental());
			}
		}
		

		return note.getAccidental();
	}
	

	private void updateAccidentalsList(Note note) {
		
		boolean accidentalExists = false;
		
		for (int i = 0; i < this.accidentalsOverwrite.size(); i++) {
			
			if(this.accidentalsOverwrite.get(i).getPitch().equals(note.getPitch())) {

				this.accidentalsOverwrite.get(i).setAccidental(note.getAccidental()); 
				accidentalExists = true;
				
			}
		}
		
		if(!accidentalExists) {
			this.accidentalsOverwrite.add(note);
		}	
	}
	
	
	private void createRDF(MusicScore score){

		/**
		 * BEGIN METADATA
		 */
			
		Model model = ModelFactory.createDefaultModel();
		Resource resScore = model.createResource(score.getURI());
				
		model.setNsPrefixes(PrefixMapping.Standard);
		model.setNsPrefix("mso", "http://linkeddata.uni-muenster.de/ontology/musicscore#");
		model.setNsPrefix("chord", "http://purl.org/ontology/chord/");
		model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
		model.setNsPrefix("mo", "http://purl.org/ontology/mo/");
		model.setNsPrefix("note", "http://purl.org/ontology/chord/note/");
		model.setNsPrefix("ton", "http://purl.org/ontology/tonality/");
		model.setNsPrefix("mode", "http://purl.org/ontology/tonality/mode/");
		model.setNsPrefix("prov", "http://www.w3.org/ns/prov#");
		model.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
		model.setNsPrefix("dbpedia-ont", "http://dbpedia.org/ontology/");
		model.setNsPrefix("node", "http://linkeddata.uni-muenster.de/node/");
		model.setNsPrefix("dc-terms", "http://purl.org/dc/terms/");
		model.setNsPrefix("gndo", "http://d-nb.info/standards/elementset/gnd#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		model.setNsPrefix("mit", "http://purl.org/ontology/mo/mit#");
		
		
		model.add(model.createStatement(resScore, RDF.type, MusicOntology.Score));
		
		if(collection.getCollectionURI()==null) {
			logger.warn("No collection provided for ["+score.getURI()+"]");
			
			Resource resCollection = model.createResource("http://unknown.collection.wmss");
			model.add(model.createStatement(resCollection, ProvO.hadMember, resScore));
			model.add(model.createStatement(resCollection, RDF.type, ProvO.Collection));
			model.add(model.createLiteralStatement(resCollection, RDFS.label, "Unknown Collection"));
			
		} else {
			
			Resource resCollection = model.createResource(collection.getCollectionURI());
			model.add(model.createStatement(resCollection, ProvO.hadMember, resScore));
			model.add(model.createStatement(resCollection, RDF.type, ProvO.Collection));
			model.add(model.createLiteralStatement(resCollection, RDFS.label, collection.getCollectionName()));
			
		}

		if(!this.getDateIssued().equals("")) {
			model.add(model.createLiteralStatement(resScore, DCTerms.issued, this.getDateIssued().replaceAll("[^\\d]", "")));
		}

		if(this.thumbnail.equals("")) {
			logger.warn("No thumbnail provided for ["+score.getURI()+"]");			
			model.add(model.createStatement(resScore,FOAF.thumbnail,model.createResource("<https://www.ulb.uni-muenster.de/imperia/md/images/ulb2/bilder/medien/telemann-noten_730x365.jpg>")));
		} else {
			model.add(model.createStatement(resScore,FOAF.thumbnail,model.createResource(this.thumbnail)));
		}

		Resource resEconder = model.createResource("https://github.com/jimjonesbr/musicowl");
		model.add(model.createStatement(resScore, ProvO.wasGeneratedBy, resEconder));
		model.add(model.createLiteralStatement(resEconder, RDFS.label, "MusicXML to RDF Converter"));
		
		Resource resActivity = model.createResource(score.getURI()+"_musicxml2rdf");

		if(persons.size()==0) {
			logger.warn("No person provided (composer, encoder, etc.) for [" +score.getURI() + "].");
			persons.add(new Person("http://unknown.person.wmss","Unknown",Role.UNKNOWN));			
		}

		boolean hasEncoder = false;
		boolean hasComposer = false;

		for (int i = 0; i < persons.size(); i++) {

			if(persons.get(i).getUri()==null) {
				logger.warn("No URI provided for [" +score.getURI() + "].");
				persons.get(i).setUri("http://unknown.person.wmss/"+UUID.randomUUID());
			}

			if(persons.get(i).getName()==null) {
				logger.warn("No name provided for [" +score.getURI() + "].");
				persons.get(i).setName("Unknown");
			}

			if(persons.get(i).getRole()==null) {
				logger.warn("No role provided for [" +score.getURI() + "].");
				persons.get(i).setUri(Role.UNKNOWN);
			}

			Resource resPerson = model.createResource(persons.get(i).getUri());

			if(persons.get(i).getRole().equals("Composer")) {

				hasComposer = true;
				
				model.add(model.createStatement(resScore, DC.creator, resPerson));				
				model.add(model.createLiteralStatement(resScore, FOAF.name,persons.get(i).getName().replace("\"", "\\\"")));				
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));		
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Composer));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Composer));
				model.add(model.createStatement(GND.Composer, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(GND.Composer, GND.preferredNameForTheSubjectHeading, "Composer"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Composer));
				model.add(model.createStatement(DBpediaResource.Composer, RDFS.label, "Composer"));
				
			}
			
			if(persons.get(i).getRole().equals("Encoder")) {			
				
				hasEncoder = true;
								
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Encoder));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Encoder));
				model.add(model.createStatement(GND.Encoder, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(GND.Encoder, GND.preferredNameForTheSubjectHeading, "Encoder"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Encoder));
				model.add(model.createStatement(DBpediaResource.Encoder, RDFS.label, "Encoder"));
			}

			if(persons.get(i).getRole().equals("Lyricist")) {
				
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Lyricist));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Lyricist));
				model.add(model.createStatement(GND.Lyricist, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(GND.Lyricist, GND.preferredNameForTheSubjectHeading, "Lyricist"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Lyricist));
				model.add(model.createStatement(DBpediaResource.Lyricist, RDFS.label, "Lyricist"));
			}

			if(persons.get(i).getRole().equals("Arranger")) {

				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Arranger));
				model.add(model.createStatement(GND.Arranger, RDF.type, ProvO.Role));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Arranger));
				model.add(model.createLiteralStatement(GND.Arranger, GND.preferredNameForTheSubjectHeading, "Arranger"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Arranger));
				model.add(model.createStatement(DBpediaResource.Arranger, RDFS.label, "Arranger"));
			}			

			if(persons.get(i).getRole().equals("Librettist")) {
				
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Librettist));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Librettist));
				model.add(model.createStatement(GND.Librettist, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(GND.Librettist, GND.preferredNameForTheSubjectHeading, "Librettist"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Librettist));
				model.add(model.createStatement(DBpediaResource.Librettist, RDFS.label, "Librettist"));
			}			

			if(persons.get(i).getRole().equals("Editor")) {
				
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Editor));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Editor));
				model.add(model.createStatement(GND.Editor, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(GND.Editor, GND.preferredNameForTheSubjectHeading, "Editor"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Editor));
				model.add(model.createStatement(DBpediaResource.Editor, RDFS.label, "Editor"));
			}	

			if(persons.get(i).getRole().equals("Performer")) {
				
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Performer));
				model.add(model.createStatement(GND.Performer, RDF.type, ProvO.Role));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Performer));
				model.add(model.createLiteralStatement(GND.Performer, GND.preferredNameForTheSubjectHeading, "Performer"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Performer));
				model.add(model.createStatement(DBpediaResource.Performer, RDFS.label, "Performer"));

			}

			if(persons.get(i).getRole().equals("Translator")) {
				
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, GND.Translator));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, GND.Translator));
				model.add(model.createStatement(GND.Translator, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(GND.Translator, GND.preferredNameForTheSubjectHeading, "Translator"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Translator));
				model.add(model.createStatement(DBpediaResource.Translator, RDFS.label, "Translator"));
			}

			if(persons.get(i).getRole().equals("Dedicatee")) {

				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, DBpediaResource.Dedicatee));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, DBpediaResource.Dedicatee));
				model.add(model.createStatement(DBpediaResource.Dedicatee, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(DBpediaResource.Dedicatee, GND.preferredNameForTheSubjectHeading, "Dedicatee"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Dedicatee));
				model.add(model.createStatement(DBpediaResource.Encoder, RDFS.label, "Dedicatee"));
			}

			if(persons.get(i).getRole().equals("Unknown")) {
				
				model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resPerson));
				model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
				model.add(model.createStatement(resActivity, ProvO.generated, resScore));
				model.add(model.createStatement(resPerson, RDF.type, FOAF.Person));
				model.add(model.createLiteralStatement(resPerson, FOAF.name, persons.get(i).getName().replace("\"", "\\\"")));
				model.add(model.createStatement(resPerson, ProvO.hadRole, DBpediaResource.Unknown));
				model.add(model.createStatement(resPerson, GND.professionOrOccupation, DBpediaResource.Unknown));
				model.add(model.createStatement(DBpediaResource.Unknown, RDF.type, ProvO.Role));
				model.add(model.createLiteralStatement(DBpediaResource.Unknown, GND.preferredNameForTheSubjectHeading, "Unknown"));
				model.add(model.createStatement(resPerson, DBpediaOntology.occupation, DBpediaResource.Unknown));
				model.add(model.createStatement(DBpediaResource.Encoder, RDFS.label, "Unknown"));

			}
		}


		if(!hasEncoder) {

			Resource resUnknownPerson = model.createResource("http://wmss.unknown.encoder");
			model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resUnknownPerson));
			model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
			model.add(model.createStatement(resActivity, ProvO.generated, resScore));
			model.add(model.createStatement(resUnknownPerson, RDF.type, FOAF.Person));
			model.add(model.createLiteralStatement(resUnknownPerson, FOAF.name, "Unknown Encoder"));
			model.add(model.createStatement(resUnknownPerson, ProvO.hadRole, GND.Composer));
			model.add(model.createStatement(GND.Encoder, RDF.type, ProvO.Role));
			model.add(model.createLiteralStatement(GND.Encoder, GND.preferredNameForTheSubjectHeading, "Encoder"));
			model.add(model.createStatement(resUnknownPerson, DBpediaOntology.occupation, DBpediaResource.Encoder));
			model.add(model.createStatement(DBpediaResource.Encoder, RDFS.label, "Encoder"));
		}
		
		if(!hasComposer) {

			Resource resUnknownPerson = model.createResource("http://wmss.unknown.composer");
			model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resUnknownPerson));
			model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
			model.add(model.createStatement(resActivity, ProvO.generated, resScore));
			model.add(model.createStatement(resUnknownPerson, RDF.type, FOAF.Person));
			model.add(model.createLiteralStatement(resUnknownPerson, FOAF.name, "Unknown Composer"));
			model.add(model.createStatement(resUnknownPerson, ProvO.hadRole, GND.Composer));
			model.add(model.createStatement(GND.Composer, RDF.type, ProvO.Role));
			model.add(model.createLiteralStatement(GND.Composer, GND.preferredNameForTheSubjectHeading, "Composer"));
			model.add(model.createStatement(resUnknownPerson, DBpediaOntology.occupation, DBpediaResource.Composer));
			model.add(model.createStatement(DBpediaResource.Composer, RDFS.label, "Composer"));

		}
				
		/**
		 * END METADATA
		 */
		
		if(score.getURI().equals("")) {
			score.setURI("http://wmss.undefined.score/"+UUID.randomUUID().toString());
			logger.warn("No URI provided for the current score: " + score.getTitle());
		}

		if(score.getTitle().equals("")) {
			score.setTitle("Unknown Music Score Tile");
			logger.warn("No title provided for the current score: " + score.getURI());
		}		

		String uid = UUID.randomUUID().toString();
		String nodeBaseURI = "http://linkeddata.uni-muenster.de/node/"+uid+"_";
		ArrayList<Staff> staves = new ArrayList<Staff>();
		ArrayList<Voice> voices = new ArrayList<Voice>();
		ArrayList<String> notesets = new ArrayList<String>();
		ArrayList<Movement> movements = new ArrayList<Movement>();

		if(!score.getTitle().equals("") && (score.getTitle()!=null)) {
			model.add(model.createLiteralStatement(resScore, DC.title, score.getTitle().trim().replace("\"", "\\\"")));
		}	

		Resource resSoftwareAgent = model.createResource("https://github.com/jimjonesbr/musicowl");
		model.add(model.createStatement(resSoftwareAgent, RDF.type, ProvO.SoftwareAgent));
		model.add(model.createLiteralStatement(resSoftwareAgent, FOAF.name, "MusicXML2RDF Converter"));
		model.add(model.createStatement(resActivity, RDF.type, ProvO.Activity));
		model.add(model.createLiteralStatement(resActivity, RDFS.comment, "File convertion from MusicXML to RDF based on the MusicOWL Ontology."));
		model.add(model.createLiteralStatement(resActivity, ProvO.startedAtTime, score.getEncodingStartTime().trim() ));
		model.add(model.createStatement(resActivity, ProvO.wasAssociatedWith, resSoftwareAgent));
		model.add(model.createStatement(resScore, ProvO.wasGeneratedBy, resActivity));
		
		for (int i = 0; i < score.getParts().size(); i++) {		

			String partID = score.getParts().get(i).getId();
			int notesetCounter = 0;
			int movementCounter = 0;		
			Resource resPart = null;
			
			for (int j = 0; j < score.getParts().get(i).getMeasures().size(); j++) {
				
				if(score.getParts().get(i).getMeasures().get(j).getId().equals("1") || 
				   score.getParts().get(i).getMeasures().get(j).getId().equals("0")) {

					movementCounter++;

					resPart = model.createResource(nodeBaseURI+"MOV"+movementCounter+"_"+"PART_"+partID);
					model.add(model.createLiteralStatement(resPart, RDFS.label, partID));
					Resource resMovement = model.createResource(nodeBaseURI+ "MOV" + movementCounter);
					
					model.add(model.createStatement(resScore, MusicOntology.movement, resMovement));
					model.add(model.createStatement(resMovement, RDF.type, MusicOntology.Movement));
					
					if(!score.getParts().get(i).getMeasures().get(j).getBeatUnit().equals("")){												
						String beatUnit = this.getCapital(score.getParts().get(i).getMeasures().get(j).getBeatUnit());		
						model.add(model.createStatement(resMovement, MusicOWL.hasBeatUnit, model.createResource(MusicOWL.NS+beatUnit)));
					} 

					if(score.getParts().get(i).getMeasures().get(j).getBeatsPerMinute()!=0) {
						model.add(model.createLiteralStatement(resMovement, MusicOWL.hasBeatsPerMinute, score.getParts().get(i).getMeasures().get(j).getBeatsPerMinute()));
					}

					boolean addMovement = true;
					Movement movement = new Movement();
					movement.setId(movementCounter);
					movement.setTitle(score.getParts().get(i).getMeasures().get(j).getTitle());

					for (int k = 0; k < movements.size(); k++) {

						if(movements.get(k).getId()==movementCounter) {
							addMovement=false;							
						}

					}

					if(addMovement) {
						if(movement.getTitle()==null || movement.getTitle().equals("")) {
							model.add(model.createLiteralStatement(resMovement, DC.title, "(no title)" ));
						} else {
							model.add(model.createLiteralStatement(resMovement, DC.title, movement.getTitle().trim().replace("\"", "\\\"")));
						}
						movements.add(movement);
					}			

					model.add(model.createStatement(resMovement, MusicOWL.hasScorePart, resPart));
					model.add(model.createStatement(resPart, RDF.type, MusicOWL.ScorePart));
					model.add(model.createStatement(resPart, RDF.type, MusicOntology.Instrument));
					model.add(model.createStatement(resPart, RDF.type, SKOS.Concept));
					model.add(model.createLiteralStatement(resPart, DC.description,score.getParts().get(i).getName().trim().replace("\"", "\\\"")));
					model.add(model.createLiteralStatement(resPart, MusicOWL.isSolo, score.getParts().get(i).isSolo()));
					model.add(model.createLiteralStatement(resPart, MusicOWL.isEnsemble, score.getParts().get(i).isEnsemble()));
					
					/**
					 * @see https://github.com/w3c/musicxml/blob/v3.1/schema/sounds.xml 
					 */

					if(!score.getParts().get(i).getInstrument().toLowerCase().equals("unknown")) {

						String[] arrayInstrument = score.getParts().get(i).getInstrument().split(Pattern.quote("."));
						String instrumentLabel = arrayInstrument[arrayInstrument.length-1];
						instrumentLabel = instrumentLabel.substring(0, 1).toUpperCase() + instrumentLabel.substring(1);
						Instrument instrument = getInstrument(score.getParts().get(i).getInstrument());

						model.add(model.createLiteralStatement(resPart, SKOS.prefLabel, instrument.getPerformanceMediumDescription()));
						model.add(model.createLiteralStatement(resPart, SKOS.altLabel, instrument.getPerformanceMediumId()));
						Resource resInstrumentBroader = model.createResource(InstrumentTaxonomy.NS + instrument.getPerformanceMediumTypeId());
						model.add(model.createStatement(resPart, SKOS.broader,resInstrumentBroader));
						model.add(model.createStatement(resInstrumentBroader, InstrumentTaxonomy.inScheme, InstrumentTaxonomy.Musical_instruments));
						model.add(model.createLiteralStatement(resInstrumentBroader, SKOS.prefLabel, instrument.getPerformanceMediumTypeId()));

					} else {

						model.add(model.createLiteralStatement(resPart, SKOS.prefLabel, "Unspecified"));
						model.add(model.createLiteralStatement(resPart, SKOS.altLabel, "unspecidied.unspecified"));
						Resource resInstrumentBroader = model.createResource(InstrumentTaxonomy.NS + "unspecified");
						model.add(model.createStatement(resPart, SKOS.broader,resInstrumentBroader));
						model.add(model.createStatement(resInstrumentBroader, InstrumentTaxonomy.inScheme, InstrumentTaxonomy.Musical_instruments));
						model.add(model.createLiteralStatement(resInstrumentBroader, SKOS.prefLabel, "Unspecified"));

					}

				}


				String measureID = score.getParts().get(i).getMeasures().get(j).getId();			
				Resource resMeasure = model.createResource(nodeBaseURI+"MOV"+movementCounter+"_"+partID+"_M"+measureID);
				this.accidentalsOverwrite.clear();
				
				Key key = new Key();

				Resource resInstant = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + "INSTANT_" + measureID);
				model.add(model.createStatement(resInstant, MusicOWL.hasMeasure, resMeasure));
				model.add(model.createStatement(resInstant, RDF.type, MusicOWL.Instant));			

				if(j>0){

					Resource resPreviousMeasure = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + score.getParts().get(i).getMeasures().get(j-1).getId());					
					model.add(model.createStatement(resPreviousMeasure, MusicOWL.nextMeasure, resMeasure));
					Resource resPreviousInstant = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + "INSTANT_"+ score.getParts().get(i).getMeasures().get(j-1).getId());
					model.add(model.createStatement(resPreviousInstant, MusicOWL.nextInstant, resInstant));					
				}

				model.add(model.createStatement(resPart, MusicOWL.hasMeasure, resMeasure));
				model.add(model.createStatement(resMeasure, RDF.type, MusicOWL.Measure));
				model.add(model.createStatement(resMeasure, RDFS.label, score.getParts().get(i).getMeasures().get(j).getId()));
				Resource resKey = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_KEY");

				if(score.getParts().get(i).getMeasures().get(j).getKey().getMode()!=null){
					
					if(score.getParts().get(i).getMeasures().get(j).getKey().getMode().equals("major")){

						key.setMode("major");

						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("0")) {
							key.setTonic("C");
						}						
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("1")) {
							key.setTonic("G");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("2")) {
							key.setTonic("D");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("3")) {
							key.setTonic("A");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("4")) {
							key.setTonic("E");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("5")) {
							key.setTonic("B");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("6")) {
							key.setTonic("Fs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("7")) {
							key.setTonic("Cs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-1")) {						
							key.setTonic("F");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-2")) {		
							key.setTonic("Bb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-3")) {		
							key.setTonic("Eb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-4")) {
							key.setTonic("Ab");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-5")) {
							key.setTonic("Db");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-6")) {		
							key.setTonic("Gb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-7")) {
							key.setTonic("Cb");
						}

					}

					if(score.getParts().get(i).getMeasures().get(j).getKey().getMode().equals("minor")){

						key.setMode("minor");

						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("0")) {
							key.setTonic("A");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("1")) {
							key.setTonic("E");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("2")) {
							key.setTonic("B");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("3")) {
							key.setTonic("Fs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("4")) {
							key.setTonic("Cs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("5")) {
							key.setTonic("Gs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("6")) {
							key.setTonic("Ds");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("7")) {
							key.setTonic("Ab");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-1")) {
							key.setTonic("D");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-2")) {
							key.setTonic("G");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-3")) {
							key.setTonic("C");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-4")) {
							key.setTonic("F");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-5")) {
							key.setTonic("Bb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-6")) {
							key.setTonic("Eb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-7")) {
							key.setTonic("Gs");
						}

					}

					if(key.getTonic()==null) {
						key.setTonic("Unknown");
					}
					if(key.getMode()==null) {
						key.setMode("Unknown");
					}

					model.add(model.createStatement(resKey, Tonality.tonic, model.createResource(Chord.NS_Note+key.getTonic())));
					model.add(model.createStatement(resKey, Tonality.mode, model.createResource(Tonality.NAMESPACE_MODE+key.getMode())));
					model.add(model.createStatement(resKey,RDF.type,Tonality.Key));
					
					model.add(model.createStatement(resMeasure,MusicOWL.hasKey,resKey));
					
				} else {

					model.add(model.createStatement(resKey, RDF.type,Tonality.Key));
				}

				Resource resTime = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" +measureID + "_TIME");
				model.add(model.createStatement(resMeasure, MusicOWL.hasTime,resTime));
				model.add(model.createStatement(resTime, RDF.type,MusicOWL.TimeSignature));

				if(score.getParts().get(i).getMeasures().get(j).getTime().getBeats()==null) {
					score.getParts().get(i).getMeasures().get(j).getTime().setBeats("0");
				} else if(score.getParts().get(i).getMeasures().get(j).getTime().getBeats().equals("")) {
					score.getParts().get(i).getMeasures().get(j).getTime().setBeats("0");
				}
				
				if(score.getParts().get(i).getMeasures().get(j).getTime().getBeatType()==null) {
					score.getParts().get(i).getMeasures().get(j).getTime().setBeatType("0");
				} else if(score.getParts().get(i).getMeasures().get(j).getTime().getBeatType().equals("")) {
					score.getParts().get(i).getMeasures().get(j).getTime().setBeatType("0");
				}

				model.add(model.createLiteralStatement(resTime, MusicOWL.hasBeats, Integer.parseInt(score.getParts().get(i).getMeasures().get(j).getTime().getBeats())));
				model.add(model.createLiteralStatement(resTime, MusicOWL.hasBeatType,Integer.parseInt(score.getParts().get(i).getMeasures().get(j).getTime().getBeatType())));
				
				if(score.getParts().get(i).getMeasures().get(j).getBarline()!=null){

					Resource resBarline = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" +measureID + "_REPEAT");					
					model.add(model.createLiteralStatement(resMeasure, MusicOWL.hasBarline,resBarline));

					if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("backward")) //ttl.append(barlineObject + rdfTypeURI + musicOWL.replace("OBJECT", "EndRepeat") + " . \n" );
						if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("forward")) //ttl.append(barlineObject + rdfTypeURI + musicOWL.replace("OBJECT", "BeginRepeat") + " . \n" );

							if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("backward")){
								model.add(model.createStatement(resBarline,RDF.type, MusicOWL.EndRepeat));
							}
					if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("forward")) {
						model.add(model.createStatement(resBarline,RDF.type, MusicOWL.BeginRepeat));
					}

				}

				String notesetObject = "";
				String tmpVoice = "";
				
				this.accidentalsOverwrite.clear();
				
				for (int k = 0; k < score.getParts().get(i).getMeasures().get(j).getNotes().size(); k++) {

					if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isChord()){

						notesetCounter++;

					}

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff()==null){

						score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setStaff("1");

					}
					
					notesetObject = nodeBaseURI+ "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_ST" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff() + "_V" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId()  + "_NOTESET_" + notesetCounter ;
					Resource resNoteset = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_ST" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff() + "_V" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId()  + "_NOTESET_" + notesetCounter);
					
					score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setSignature(notesetObject);										

					if(this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature()!=null){

						if(!this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature().equals(notesetObject)){

							Resource resPreivousNoteset = model.createResource(this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature().trim().replaceAll("[<>]", ""));							
							model.add(model.createStatement(resPreivousNoteset, MusicOWL.nextNoteSet, resNoteset));
							
							this.setCurrentNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k));

						} 

					} else {

						this.setCurrentNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k));

					}


					/**
					 * Staff 					
					 */

					Staff staff = new Staff();
					staff.setId(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff());
					staff.getPart().setId(score.getParts().get(i).getId());					
					staff.getPart().setMovement(movementCounter);

					boolean addStaff = true;

					for (int l = 0; l < staves.size(); l++) {

						if(staves.get(l).getId().equals(staff.getId()) && 
								staves.get(l).getPart().getId().equals(staff.getPart().getId()) &&
								staves.get(l).getPart().getMovement() == staff.getPart().getMovement()) {
							addStaff = false;

						}

					}

					Resource resStaff = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_STAFF_" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff());

					if(addStaff) {

						staves.add(staff);					
						model.add(model.createStatement(resStaff, RDF.type, MusicOWL.Staff));
						model.add(model.createStatement(resPart, MusicOWL.hasStaff, resStaff));
						model.add(model.createStatement(resStaff,RDFS.label,score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff() ));

					}


					/**
					 * Voice
					 */

					Resource resVoice = null;
					Resource resClef = null;
					Resource resDuration = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS" + notesetCounter + "_DURATION");
					
					Voice voice = new Voice();
					voice.getPart().setId(partID);
					voice.getPart().setMovement(movementCounter);

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice()==null){

						/**
						 * Change regarding voices of notes inside of a chord
						 */

						if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isChord()) {					
							voice.setId(tmpVoice);
						} else {
							voice.setId("1");							
						}

						score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setVoice(voice);

					} else {

						resVoice = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_VOICE_" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId());
						voice.setId(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId());
						tmpVoice=score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId();
					}


					boolean addVoice = true;

					for (int l = 0; l < voices.size(); l++) {

						if(voices.get(l).getId().equals(voice.getId()) && 
								voices.get(l).getPart().getId().equals(voice.getPart().getId()) &&
								voices.get(l).getPart().getMovement()==voice.getPart().getMovement()) {
							addVoice = false;

						}

					}

					if(addVoice) {

						voices.add(voice);
						model.add(model.createLiteralStatement(resVoice, RDFS.label, voice.getId()));
						model.add(model.createStatement(resVoice, RDF.type, MusicOWL.Voice));
						model.add(model.createStatement(resStaff, MusicOWL.hasVoice, resVoice));

					}


					boolean addNoteSet = true;

					for (int l = 0; l < notesets.size(); l++) {

						if(notesets.get(l).equals(notesetObject)) {
							addNoteSet = false;
						}

					}

					if(addNoteSet) {

						notesets.add(notesetObject);
						resClef = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS_" + notesetCounter + "_CLEF");
						model.add(model.createStatement(resVoice, MusicOWL.hasNoteSet, resNoteset));
						model.add(model.createStatement(resNoteset, RDF.type, MusicOWL.NoteSet));
						model.add(model.createStatement(resNoteset, MusicOWL.hasClef,resClef));
						model.add(model.createStatement(resMeasure, MusicOWL.hasNoteSet,resNoteset));

						if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef()!=null){

							for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size(); l++) {

								Resource resArticulation = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS" + notesetCounter + "_ARTICULATION_" + l);
								model.add(model.createStatement(resNoteset, MusicOWL.hasArticulation, resArticulation));
								model.add(model.createStatement(resArticulation, RDF.type,model.createResource(MusicOWL.NS+this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().get(l)))));

							}
							
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign()!=null && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine()!=null){

								if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("percussion")){

									model.add(model.createStatement(resClef, MusicOWL.sign,model.createResource(Chord.NS_Note+score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign())));
									model.add(model.createLiteralStatement(resClef, MusicOWL.line, Integer.parseInt(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine())));

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("3") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Alto));

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("5") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){
						
										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Baritone));
									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("3") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("F")){
		
										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Baritone));			

									}


									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("4") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("F")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Bass));					

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("1") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("G")){
	
										model.add(model.createStatement(resClef, RDF.type, MusicOWL.FrenchViolin));		

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("2") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.MezzoSoprano));		

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("1") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Soprano));		

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("5") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("F")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.SubBass));	

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("4") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Tenor));

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("2") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("G")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Trebble));

									}

									if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("percussion")){

										model.add(model.createStatement(resClef, RDF.type, MusicOWL.Percussion));
									}

								} else {

									model.add(model.createStatement(resClef, RDF.type, MusicOWL.Percussion));

								}


							} else {

								model.add(model.createStatement(resClef, RDF.type, MusicOWL.Percussion));
							}
						}

						model.add(model.createStatement(resNoteset, MusicOWL.hasDuration,resDuration));

						if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType()==null){

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()==null) {	
								model.add(model.createStatement(resDuration, RDF.type, MusicOWL.Whole));
							} else {
								model.add(model.createStatement(resDuration, RDF.type, MusicOWL.Duration));
							}

						} else {

							model.add(model.createStatement(resDuration, RDF.type, model.createResource(MusicOWL.NS + this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType()))));
						}

					}


					/**
					 * Note
					 */

 					Resource resNote = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS_" + notesetCounter + "_NOTE_" + k);
					model.add(model.createStatement(resNoteset, MusicOWL.hasNote, resNote));
					model.add(model.createStatement(resNote, RDF.type, Chord.Note));
										
					if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()==null) {

						model.add(model.createStatement(resNote, Chord.natural, Chord.Rest));

					} else { 

						model.add(model.createStatement(resNote, Chord.natural, model.createResource(Chord.NS_Note+score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch())));
						model.add(model.createStatement(model.createResource(Chord.NS_Note+score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()),RDF.type,Chord.Natural));
						
					}
					

					/**
					 * Add sharps and flats to notes depending on the key. 
					 */
					
					Note note = new Note();
					note.setPitch(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch());
					note.setAccidental(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental());
					note.setOctave(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave());
					note.setGrace(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isGrace());
					
					if(!note.getAccidental().equals("")){
											
						if(!note.getAccidental().toLowerCase().equals("natural")) {
							
							model.add(model.createStatement(resNote, Chord.modifier, model.createResource(Chord.NS_Base+score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental())));
							
						} 
						
						this.updateAccidentalsList(note);
					
					} else {

						if(note.getPitch() != null){							
						
							if(!this.getAccidental(key, note).equals("")) {
								model.add(model.createStatement(resNote, Chord.modifier, model.createResource(Chord.NS_Base+this.getAccidental(key, note))));
							}
							
						}
						
					}

					
					if(note.isGrace()) {
						Resource resGraceNote = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS_" + notesetCounter + "_N" + k + "_NA_GRACE");
						model.add(model.createStatement(resNote, MusicOWL.hasNoteAttribute, resGraceNote));
						model.add(model.createStatement(resGraceNote,RDF.type,MusicOWL.GraceNote));
					}
																		
																		
					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave()!=null){

						model.add(model.createLiteralStatement(resNote, MusicOWL.hasOctave, Integer.parseInt(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave())));
					}

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isDot()){

						Resource resDot = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" +partID + "_M" + measureID + "_NS" + notesetCounter + "_N" + k + "_D_DOT");
						model.add(model.createStatement(resDuration, MusicOWL.hasDurationAttribute, resDot));
						model.add(model.createStatement(resDot, RDF.type, MusicOWL.Dot));
					}

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isDoubleDot()){
					
						Resource resDoubleDot = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" +partID + "_M" + measureID + "_NS" + notesetCounter + "_N" + k + "_D_DOUBLEDOT");
						model.add(model.createStatement(resDuration, MusicOWL.hasDurationAttribute, resDoubleDot));
						model.add(model.createStatement(resDoubleDot, RDF.type, MusicOWL.DoubleDot));
						
					}
					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isTripleDot()){
						
						Resource resTripleDot = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" +partID + "_M" + measureID + "_NS" + notesetCounter + "_N" + k + "_D_TRIPLEDOT");
						model.add(model.createStatement(resDuration, MusicOWL.hasDurationAttribute, resTripleDot));
						model.add(model.createStatement(resTripleDot, RDF.type, MusicOWL.TripleDot));
						
					}


					for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size(); l++) {

						Resource resDynamic = model.createResource(nodeBaseURI+"MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS" + notesetCounter + "_DYNAMIC_" + l);
						model.add(model.createStatement(resDynamic, RDF.type, model.createResource(MusicOWL.NS+score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().get(l))));
						model.add(model.createStatement(resNoteset, MusicOWL.hasDynamic, resDynamic));

					}

					
					
				}

			}

		}


		try {

			String xml = new String(score.getFileContent().getBytes(StandardCharsets.UTF_8));
			xml = xml.replace("\\", "\\\\");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			
			model.add(model.createLiteralStatement(resScore, MusicOWL.asMusicXML, xml.replace("\"", "'")));			
			model.add(model.createLiteralStatement(resActivity, ProvO.endedAtTime, sdf.format(new Date())));

			staves = null;
			voices = null;
			notesets = null;
					
			model.write(new FileOutputStream(new File(this.getOutputFile())), this.getOutputFormat());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();		
		}

	}


	private Instrument getInstrument(String musicxmlInstrument) {

		Instrument result = new Instrument();

		for (int i = 0; i < instruments.size(); i++) {
			if(instruments.get(i).getPerformanceMediumId().equals(musicxmlInstrument)) {
				result.setPerformanceMediumDescription(instruments.get(i).getPerformanceMediumDescription());
				result.setPerformanceMediumId(instruments.get(i).getPerformanceMediumId());
				result.setPerformanceMediumTypeId(instruments.get(i).getPerformanceMediumTypeId());
			}
		}

		return result;

	}
	
	private boolean isMusicXML(File file) {
		
		boolean result = false;
		
		if(FilenameUtils.getExtension(file.getName()).equals("xml") || FilenameUtils.getExtension(file.getName()).equals("mxl")) {
			result = true;
		}
		
		return result;
	}

	public void parseMusicXML(){

		String musicXMLString = "";

		if(this.getOutputFile().equals("")) {
			logger.fatal("No output file provided (N-Triple output).");
			System.exit(1);			
		}

		if(this.getInputFile()==null) {
			logger.fatal("No input file provided (MusicXML).");
			System.exit(1);			
		}
		
		if(!this.getOutputFormat().equals("")&&
			!this.getOutputFormat().toLowerCase().trim().equals("turtle")&&
			!this.getOutputFormat().toLowerCase().trim().equals("json-ld")&&
			!this.getOutputFormat().toLowerCase().trim().equals("n-triples")&&
			!this.getOutputFormat().toLowerCase().trim().equals("n-quads")&&
			!this.getOutputFormat().toLowerCase().trim().equals("trig")&&
			!this.getOutputFormat().toLowerCase().trim().equals("trix")&&
			!this.getOutputFormat().toLowerCase().trim().equals("rdf/xml")&&
			!this.getOutputFormat().toLowerCase().trim().equals("rdf/json")){
			logger.fatal("Invalid output format ["+this.getOutputFormat()+"]. Supported formats are: TURTLE, JSON-LD, N-TRIPLES, N-QUADS, TRIG, TRIX, RDF/XML, RDF/JSON");
			System.exit(1);			

		}

		File file = this.getInputFile();

		if(this.isMusicXML(file)) {

			if(FilenameUtils.getExtension(file.getName()).equals("mxl")) {
				logger.info("Decompressing MusicXML file: " + file.getName());
				file = Util.decompressMXL(file.getAbsolutePath(), "tmp/");			
			}

			try {

				logger.info("Processing " + file.getName() + " ...");

				Date start = new Date();

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setValidating(false);
				DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

				builder.setEntityResolver(new EntityResolver() {

					public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

						return new InputSource(new StringReader(""));

					}
				});

				Document document = builder.parse(file);

				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				StringWriter writer = new StringWriter();
				transformer.transform(new DOMSource(document), new StreamResult(writer));
				musicXMLString = writer.getBuffer().toString().replaceAll("\n|\r", "");

				logger.info("Loading XML file: " + Util.timeElapsed(start, new Date()));

			} catch (Exception e) {

				logger.error("[Error processing MusicXML File]: " + e.getMessage());

			}	


			Date start = new Date();
			MusicScore score = createMusicScoreDocument(musicXMLString); 
			score.setFileContent(musicXMLString);
			score.setOutputFileName(file.getName());
			score.setURI(this.scoreURI);

			if(!documentTitle.equals("")) {
				score.setTitle(this.documentTitle);	
				logger.warn("The title \""+this.documentTitle+"\" was provided and will therefore overwrite the title provided in the MusicXML document.");
			}


			logger.info("Creating MusicScore object: " + Util.timeElapsed(start, new Date()));

			start = new Date();
			this.createRDF(score);
			logger.info("Score serialization: " + Util.timeElapsed(start, new Date())+"\n");

		} else {
			
			logger.error("Invalid file format: " + file.getName());
		}

	}

	private MusicScore createMusicScoreDocument(String musicXML){

		MusicScore score = new MusicScore();

		String currentMode = "";
		String currentFifth = ""; 
		String currentBeatType = "";
		String currentBeat = "";
		int slurCount = 0;


		boolean slurFlag = false;

		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(false);
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

			builder.setEntityResolver(new EntityResolver() {

				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

					return new InputSource(new StringReader(""));

				}
			});

			InputSource is = new InputSource(new StringReader(musicXML));
			Document document = builder.parse(is);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			NodeList subfields = (NodeList) xpath.evaluate("//work/work-title", document,XPathConstants.NODESET);

			if (subfields.getLength() != 0) {

				score.setTitle(subfields.item(0).getTextContent());
				
			} 
			
			
			/**
			 * In case title isn't provided in the 'work-titlke' tag, tries to find it at'movement-title'. 
			 */
			if(score.getTitle().equals("")) {			
				subfields = (NodeList) xpath.evaluate("//movement-title", document,XPathConstants.NODESET);				
				if (subfields.getLength() != 0) {
					score.setTitle(subfields.item(0).getTextContent());
				}
			}
			
			

			subfields = (NodeList) xpath.evaluate("//identification/encoding/encoder", document,XPathConstants.NODESET);

			if (subfields.getLength() != 0) {

				score.setEncoder(subfields.item(0).getTextContent());

			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			score.setEncodingStartTime(sdf.format(new Date()));

			subfields = (NodeList) xpath.evaluate("//score-partwise/part-list/score-part/@id", document,XPathConstants.NODESET);

			for (int i = 0; i < subfields.getLength(); i++) {

				ScorePart part = new ScorePart();
				part.setId(subfields.item(i).getTextContent());
				score.getParts().add(part);					

			}


			for (int i = 0; i < score.getParts().size(); i++) {

				this.clefList = new ArrayList<Clef>();
				int movementCount = 0;

				NodeList nodePartName = (NodeList) xpath.evaluate("//score-partwise/part-list/score-part[@id='"+score.getParts().get(i).getId()+"']/part-name", document,XPathConstants.NODESET);

				if(nodePartName.getLength()!=0){

					if(nodePartName.item(0).getTextContent().trim().equals("")){

						score.getParts().get(i).setName(score.getParts().get(i).getId());

					} else {

						score.getParts().get(i).setName(nodePartName.item(0).getTextContent());

					}

				} else {

					score.getParts().get(i).setName(score.getParts().get(i).getId());

				}


				NodeList nodeInstrumentName = (NodeList) xpath.evaluate("//score-partwise/part-list/score-part[@id='"+score.getParts().get(i).getId()+"']/score-instrument/instrument-sound", document,XPathConstants.NODESET);

				if(nodeInstrumentName.getLength()!=0){

					if(nodeInstrumentName.item(0).getTextContent().trim().equals("")){

						score.getParts().get(i).setInstrument("unknown");

					} else {

						score.getParts().get(i).setInstrument(nodeInstrumentName.item(0).getTextContent());

					}

				} else {

					score.getParts().get(i).setInstrument("unknown");

				}


				NodeList nodeInstrumentSolo = (NodeList) xpath.evaluate("//score-partwise/part-list/score-part[@id='"+score.getParts().get(i).getId()+"']/score-instrument/solo", document,XPathConstants.NODESET);

				if(nodeInstrumentSolo.getLength()!=0)
				{					
					score.getParts().get(i).setSolo(true);
				}else {
					score.getParts().get(i).setSolo(false);
				}


				NodeList nodeInstrumentEnsemble = (NodeList) xpath.evaluate("//score-partwise/part-list/score-part[@id='"+score.getParts().get(i).getId()+"']/score-instrument/ensemble", document,XPathConstants.NODESET);

				if(nodeInstrumentEnsemble.getLength()!=0)
				{					
					score.getParts().get(i).setEnsemble(true);
				}else {
					score.getParts().get(i).setEnsemble(false);
				}



				NodeList nodeMeasures = (NodeList) xpath.evaluate("//score-partwise/part[@id='"+score.getParts().get(i).getId()+"']/measure", document,XPathConstants.NODESET);

				if (nodeMeasures.getLength() != 0) {

					for (int j = 0; j < nodeMeasures.getLength(); j++) {

						Measure measure = new Measure();
						measure.setId(nodeMeasures.item(j).getAttributes().getNamedItem("number").getNodeValue());	

						if(measure.getId().equals("1")) {

							movementCount = movementCount +1;						
							Element eElement = (Element) nodeMeasures.item(j);

							if(eElement.getElementsByTagName("words").getLength() != 0) {
								measure.setTitle(eElement.getElementsByTagName("words").item(0).getTextContent());
								if(verbose) {
									logger.info("["+ score.getParts().get(i).getName() + "] Parsing movement: " + movementCount + " ("+measure.getTitle() +") ... ");
								}
							} else {
								if(verbose) {
									logger.info("["+ score.getParts().get(i).getName() + "] Parsing movement: " + movementCount + " ... ");
								}
							}



						}

						score.getParts().get(i).getMeasures().add(measure);


						/**
						 * one xpath element 4 all
						 */


						ArrayList<String> dynamics = new ArrayList<String>();
						NodeList nodeMeasureElementsList = nodeMeasures.item(j).getChildNodes();

						for (int l = 0; l < nodeMeasureElementsList.getLength(); l++) {




							if(nodeMeasureElementsList.item(l).getNodeName().equals("direction")){

								Element elementDirection = (Element) nodeMeasureElementsList.item(l).getChildNodes();

								for (int m = 0; m < elementDirection.getChildNodes().getLength(); m++) {

									if(elementDirection.getElementsByTagName("dynamics").item(m)!=null){

										NodeList listDynamics = elementDirection.getElementsByTagName("dynamics").item(m).getChildNodes();

										for (int n = 0; n < listDynamics.getLength(); n++) {

											if(!listDynamics.item(n).getNodeName().matches("#text")) {

												dynamics.add(listDynamics.item(n).getNodeName());

											}
										}

									}

									if(elementDirection.getElementsByTagName("direction-type").item(m)!=null){

										if(!(elementDirection.getElementsByTagName("metronome").item(m)==null)) {

											NodeList listMetronome = elementDirection.getElementsByTagName("metronome").item(m).getChildNodes();
											for (int n = 0; n < listMetronome.getLength(); n++) {

												if(listMetronome.item(n).getNodeName().matches("beat-unit")) {
													measure.setBeatUnit(listMetronome.item(n).getTextContent());	
												}
												if(listMetronome.item(n).getNodeName().matches("per-minute")) {
													int perMinute = 0;
													if(!listMetronome.item(n).getTextContent().toString().equals("") || listMetronome.item(n).getTextContent().toString().matches("^[0-9]+$")) {
														perMinute = Integer.parseInt(listMetronome.item(n).getTextContent().toString().replaceAll("[^\\d]", ""));
													}
													measure.setBeatsPerMinute(perMinute);
												}

											}
										}
									}

								}

							}


							if(nodeMeasureElementsList.item(l).getNodeName().equals("attributes")){

								NodeList listAttributes = nodeMeasureElementsList.item(l).getChildNodes();

								for (int m = 0; m < listAttributes.getLength(); m++) {


									if (listAttributes.item(m).getNodeName().equals("clef")) {

										NodeList listClef = listAttributes.item(m).getChildNodes();

										Clef clef = new Clef();

										for (int n = 0; n < listClef.getLength(); n++) {


											if(listAttributes.item(m).getAttributes().getNamedItem("number")!=null) {

												clef.setStaffNumber(listAttributes.item(m).getAttributes().getNamedItem("number").getNodeValue());


											} else {

												clef.setStaffNumber("1");

											}


											if(listClef.item(n).getNodeName().equals("line")){

												clef.setLine(listClef.item(n).getTextContent());

											}

											if(listClef.item(n).getNodeName().equals("sign")){

												clef.setSign(listClef.item(n).getTextContent());

											}


										}

										this.addClef(clef);										

										if(verbose) {
											logger.info("	[Clef] Measure: "+measure.getId()+" Sign: "+ clef.getSign()+ " | Line: " + clef.getLine());
										}
									}


									if(listAttributes.item(m).getNodeName().equals("time")){

										NodeList listTime = listAttributes.item(m).getChildNodes();

										for (int n = 0; n < listTime.getLength(); n++) {

											if (listTime.item(n).getNodeName().equals("beats")){

												currentBeat=listTime.item(n).getTextContent();

											}

											if (listTime.item(n).getNodeName().equals("beat-type")){

												currentBeatType = listTime.item(n).getTextContent();


											}

										}

										if(this.verbose) {
										logger.info("	[Time] Measure: "+measure.getId()+" Beats: "+ currentBeat+ " | Beat-type: " + currentBeatType);
										}

									}

									if(listAttributes.item(m).getNodeName().equals("key")){

										NodeList listKey = listAttributes.item(m).getChildNodes();

										for (int n = 0; n < listKey.getLength(); n++) {

											if (listKey.item(n).getNodeName().equals("fifths")){

												currentFifth=listKey.item(n).getTextContent();

											}

											if (listKey.item(n).getNodeName().equals("mode")){

												currentMode = listKey.item(n).getTextContent();

											}

										}

										if(this.verbose) {
											logger.info("	[Key] Measure: "+measure.getId()+" Mode: "+ currentMode+ " | Fifth: " + currentFifth);
										}
									}



								}

							}

							measure.getKey().setMode(currentMode);
							measure.getKey().setFifths(currentFifth);
							measure.getTime().setBeats(currentBeat);
							measure.getTime().setBeatType(currentBeatType);

							if(nodeMeasureElementsList.item(l).getChildNodes().equals("barline")){

								NodeList listBarline = nodeMeasureElementsList.item(l).getChildNodes();

								for (int m = 0; m < listBarline.getLength(); m++) {

									if (listBarline.item(m).getNodeName().equals("repeat")){

										measure.setBarline(listBarline.item(m).getAttributes().getNamedItem("direction").getNodeValue());

									}

								}

							}


							Note note = new Note();

							if(nodeMeasureElementsList.item(l).getNodeName().equals("note")){


								NodeList listNoteElements = nodeMeasureElementsList.item(l).getChildNodes();

								for (int m = 0; m < listNoteElements.getLength(); m++) {


									if (listNoteElements.item(m).getNodeName().equals("grace")) {

										note.setGrace(true);
										
									}
									
									if (listNoteElements.item(m).getNodeName().equals("staff")) {

										note.setStaff(listNoteElements.item(m).getTextContent());

									}

									if (listNoteElements.item(m).getNodeName().equals("notations")) {

										NodeList listNotationElements = listNoteElements.item(m).getChildNodes();

										for (int n = 0; n < listNotationElements.getLength(); n++) {

											if(listNotationElements.item(n).getNodeName().equals("slur")){

												if(listNotationElements.item(n).getAttributes().getNamedItem("type").getNodeValue().equals("start")){

													slurCount = slurCount + 1; 
													slurFlag = true;													

												}	

												if(listNotationElements.item(n).getAttributes().getNamedItem("type").getNodeValue().equals("stop")){

													slurFlag = false;
												}

												note.setSlur(Integer.toString(slurCount));

											}


											if(listNotationElements.item(n).getNodeName().equals("articulations")){

												NodeList listAriculationElements = listNotationElements.item(n).getChildNodes();	

												for (int o = 0; o < listAriculationElements.getLength(); o++) {

													if(!listAriculationElements.item(o).getNodeName().matches("#text")){

														note.getArticulations().add(listAriculationElements.item(o).getNodeName());

													}

												}


											}


										}


									}


								}


								Element elementNotes = (Element) nodeMeasureElementsList.item(l).getChildNodes();

								if(elementNotes.getElementsByTagName("chord").item(0)!=null) note.setChord(true);

								if(elementNotes.getElementsByTagName("dot").getLength()==1) {
									note.setDot(true);
								}

								if(elementNotes.getElementsByTagName("dot").getLength()==2) {
									note.setDoubleDot(true);
								}
								if(elementNotes.getElementsByTagName("dot").getLength()==3) {
									note.setTripleDot(true);
								}

								if(elementNotes.getElementsByTagName("octave").item(0)!=null) note.setOctave(elementNotes.getElementsByTagName("octave").item(0).getTextContent());
								if(elementNotes.getElementsByTagName("step").item(0)!=null) note.setPitch(elementNotes.getElementsByTagName("step").item(0).getTextContent());								
								if(elementNotes.getElementsByTagName("type").item(0)!=null) note.setType(elementNotes.getElementsByTagName("type").item(0).getTextContent());

								if(elementNotes.getElementsByTagName("voice").item(0)!=null) {

									note.getVoice().setId(elementNotes.getElementsByTagName("voice").item(0).getTextContent());

								} else {

									note.getVoice().setId("1");

								}

								note.getVoice().getPart().setId(score.getParts().get(i).getId());
								note.getVoice().setMeasure(measure.getId());

								if(elementNotes.getElementsByTagName("accidental").item(0)!=null){
									String accidental = elementNotes.getElementsByTagName("accidental").item(0).getTextContent();
									if(accidental.equals("flat-flat")) {
										accidental = "doubleflat";
									} else if(accidental.equals("double-sharp")) {
										accidental = "doublesharp";
									}
									note.setAccidental(accidental);
								}


								if(dynamics.size()!=0) {

									for (int m = 0; m < dynamics.size(); m++) {

										note.getDynamics().add(dynamics.get(m));
									}

									dynamics = new ArrayList<String>();
								}

								if(note.getStaff()==null)note.setStaff("1");


								note.getClef().setLine(this.getCurrentClef(note.getStaff()).getLine());
								note.getClef().setSign(this.getCurrentClef(note.getStaff()).getSign());


								if(slurFlag)note.setSlur(Integer.toString(slurCount));


								score.getParts().get(i).getMeasures().get(j).getNotes().add(note);


							}


						}


					}

				}

			}

		} catch (SAXException  e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return score; 

	}

	private void addClef(Clef clef){

		boolean exists = false;

		for (int i = 0; i < clefList.size(); i++) {

			if(clefList.get(i).getStaffNumber().equals(clef.getStaffNumber())){

				clefList.get(i).setLine(clef.getLine());
				clefList.get(i).setSign(clef.getSign());
				exists = true;

			}

		}


		if(!exists) 
			clefList.add(clef);

	}

	private Clef getCurrentClef(String staff){

		Clef result = new Clef();

		for (int i = 0; i < clefList.size(); i++) {

			if(clefList.get(i).getStaffNumber().equals(staff)){

				result = clefList.get(i);

			}

		}

		return result;

	}

	private void setCurrentNoteSet(Note note){

		boolean exists = false;

		for (int i = 0; i < currentNotes.size(); i++) {

			if(currentNotes.get(i).getVoice().getId().equals(note.getVoice().getId())){

				if(currentNotes.get(i).getStaff().equals(note.getStaff())){

					currentNotes.get(i).setSignature(note.getSignature());
					exists = true;

				}

			}

		}


		if(!exists)	{
			currentNotes.add(note);

		}

	}

	private Note getPreviousNoteSet(Note note){

		Note result = new Note();

		for (int i = 0; i < currentNotes.size(); i++) {

			if(currentNotes.get(i).getStaff()!=null){

				if(currentNotes.get(i).getStaff().equals(note.getStaff()) ){

					if(currentNotes.get(i).getVoice().getId().equals(note.getVoice().getId()) ){

						result = currentNotes.get(i);

					}


				}
			}
		}


		return result;
	}

	private String getCapital(String string){


		if(string != null && string != "" ) string = string.substring(0, 1).toUpperCase() + string.substring(1);

		return string;

	}

	public String getOutputFile(){

		String result = "";
		
		if(this.getOutputFormat().toLowerCase().equals("json-ld")) {
			result = this.outputFile + ".jsonld";
		} if(this.getOutputFormat().toLowerCase().equals("turtle")) {
			result = this.outputFile + ".ttl";
		} if(this.getOutputFormat().toLowerCase().equals("n-triples")) {
			result = this.outputFile + ".nt";
		} if(this.getOutputFormat().toLowerCase().equals("n-quads")) {
			result = this.outputFile + ".nq";
		} if(this.getOutputFormat().toLowerCase().equals("trig")) {
			result = this.outputFile + ".trig";
		} if(this.getOutputFormat().toLowerCase().equals("rdf/json")) {
			result = this.outputFile + ".rj";
		} if(this.getOutputFormat().toLowerCase().equals("trix")) {
			result = this.outputFile + ".trix";
		} if(this.getOutputFormat().toLowerCase().equals("rdf/xml")) {
			result = this.outputFile + ".rdf";
		}
		
		return result;

	}

	public void setOutputFile(String outputfile){

		this.outputFile = outputfile;
	}

	public void setInputFile(File musicxml){

		this.inputFile = musicxml;

	}

	public File getInputFile(){

		return this.inputFile;

	}

	public String getScoreURI(){

		return this.scoreURI;
	}

	public void setScoreURI(String uri){

		this.scoreURI = uri;
	}

	public void setDocumentTitle(String title){

		this.documentTitle = title;

	}

	public String getDocumentTitle(String title){

		return this.documentTitle;

	}

	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}

	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}


}
