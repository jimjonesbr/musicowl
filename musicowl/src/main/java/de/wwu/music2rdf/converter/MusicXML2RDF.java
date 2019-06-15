package de.wwu.music2rdf.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private Collection collection;
	private static Logger logger = Logger.getLogger("Converter");
	private String dateIssued = "";
	
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
			
	private String createMetadata(MusicScore score) {
		
		StringBuffer metadata = new StringBuffer();
		
		metadata.append("<" + score.getURI() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/ontology/mo/Score> .\n");
		
		if(collection.getCollectionURI()==null) {
			logger.warn("No collection provided for ["+score.getURI()+"]");
			metadata.append("<http://unknown.collection.wmss> <http://www.w3.org/ns/prov#hadMember> <" + score.getURI() + "> .\n");
			metadata.append("<http://unknown.collection.wmss> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Collection> .\n");			
			metadata.append("<http://unknown.collection.wmss> <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> \"Unknown Collection\" .\n");
		} else {
			metadata.append("<"+collection.getCollectionURI()+"> <http://www.w3.org/ns/prov#hadMember> <" + score.getURI() + "> .\n");	
			metadata.append("<"+collection.getCollectionURI()+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Collection> .\n");			
			metadata.append("<"+collection.getCollectionURI()+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> \""+collection.getCollectionName()+"\" .\n");

		}
		
		if(!this.getDateIssued().equals("")) {
			metadata.append("<" + score.getURI() + "> <http://purl.org/dc/terms/issued> \""+this.getDateIssued().replaceAll("[^\\d]", "")+"\" .\n");
		}
		
		if(this.thumbnail.equals("")) {
			logger.warn("No thumbnail provided for ["+score.getURI()+"]");
			 metadata.append("<" + score.getURI() + "> <http://xmlns.com/foaf/0.1/thumbnail> <https://www.ulb.uni-muenster.de/imperia/md/images/ulb2/bilder/medien/telemann-noten_730x365.jpg> .\n");
		} else {
			metadata.append("<" + score.getURI() + "> <http://xmlns.com/foaf/0.1/thumbnail> <" + this.thumbnail+ "> .\n");	
		}
		
		metadata.append("<" + score.getURI() + "> <http://www.w3.org/ns/prov#wasGeneratedBy> <https://github.com/jimjonesbr/musicowl> . \n");
		metadata.append("<https://github.com/jimjonesbr/musicowl> <http://www.w3.org/1999/02/22-rdf-syntax-ns#label> \"MusicXML to RDF Converter.\" . \n");

		String activity = "<"+score.getURI()+"_musicxml2rdf>";

		if(persons.size()==0) {
			logger.warn("No person provided (composer, encoder, etc.) for [" +score.getURI() + "].");
			persons.add(new Person("http://unknown.person.wmss","Unknown",Role.UNKNOWN));			
		}
			
		boolean hasEncoder = false;
		
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
	
			if(persons.get(i).getRole().equals("Encoder")) {			
				metadata.append(activity + " <http://www.w3.org/ns/prov#wasAssociatedWith> <"+persons.get(i).getUri()+"> . \n");
				metadata.append(activity + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Activity> . \n");
				metadata.append(activity + " <http://www.w3.org/ns/prov#generated> <" + score.getURI() + "> " + " . \n");
				metadata.append("<"+persons.get(i).getUri()+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> " + " . \n");
				metadata.append("<"+persons.get(i).getUri()+"> <http://xmlns.com/foaf/0.1/name> \""+persons.get(i).getName().replace("\"", "\\\"")+"\" . \n");
				metadata.append("<"+persons.get(i).getUri()+"> <http://www.w3.org/ns/prov#hadRole> <http://d-nb.info/gnd/4139395-8> " + " . \n");
				metadata.append("<http://d-nb.info/gnd/4139395-8> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4139395-8> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Encoder\" . \n");
				hasEncoder = true;
			}
			
			if(persons.get(i).getRole().equals("Composer")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4032009-1> .\n");
				metadata.append("<http://d-nb.info/gnd/4032009-1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4032009-1> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Composer\" .\n");
			}
			
			if(persons.get(i).getRole().equals("Lyricist")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4246394-4> .\n");
				metadata.append("<http://d-nb.info/gnd/4246394-4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4246394-4> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Lyricist\" .\n");
			}
			
			if(persons.get(i).getRole().equals("Arranger")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4604119-9> .\n");
				metadata.append("<http://d-nb.info/gnd/4604119-9> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4604119-9> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Arranger\" .\n");
			}			
			
			if(persons.get(i).getRole().equals("Librettist")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4294338-3> .\n");
				metadata.append("<http://d-nb.info/gnd/4294338-3> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4294338-3> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Librettist\" .\n");
			}			
			
			if(persons.get(i).getRole().equals("Editor")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4159575-0> .\n");
				metadata.append("<http://d-nb.info/gnd/4159575-0> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4159575-0> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Editor\" .\n");
			}	
			
			if(persons.get(i).getRole().equals("Performer")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName() + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4170790-4> .\n");
				metadata.append("<http://d-nb.info/gnd/4170790-4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4170790-4> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Performer\" .\n");
			}
			
			if(persons.get(i).getRole().equals("Translator")) {
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> <http://d-nb.info/gnd/4061414-1> .\n");
				metadata.append("<http://d-nb.info/gnd/4061414-1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append("<http://d-nb.info/gnd/4061414-1> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Translator\" .\n");
			}
			
			if(persons.get(i).getRole().equals("Dedicatee")) {
				String dedicatee = "<"+score.getURI()+"_dedicatee>";
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> "+dedicatee+" .\n");
				metadata.append(dedicatee + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append(dedicatee + " <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Dedicatee\" .\n");
			}
			
			if(persons.get(i).getRole().equals("Unknown")) {
				String unknown = "<http://unknown.role.wmss>";
				metadata.append("<" + score.getURI() + "> <http://purl.org/dc/elements/1.1/creator> <" + persons.get(i).getUri() + "> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://xmlns.com/foaf/0.1/name> \"" + persons.get(i).getName().replace("\"", "\\\"") + "\" .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .\n");
				metadata.append("<" + persons.get(i).getUri() + "> <http://d-nb.info/standards/elementset/gnd#professionOrOccupation> "+unknown+" .\n");
				metadata.append(unknown + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
				metadata.append(unknown + " <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Unknown\" .\n");
			}
		}
		
		
		if(!hasEncoder) {
			
			metadata.append(activity + " <http://www.w3.org/ns/prov#wasAssociatedWith> <http://wmss.unknown.encoder> . \n");
			metadata.append(activity + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Activity> . \n");
			metadata.append(activity + " <http://www.w3.org/ns/prov#generated> <" + score.getURI() + "> " + " . \n");
			metadata.append("<http://wmss.unknown.encoder> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> " + " . \n");
			metadata.append("<http://wmss.unknown.encoder> <http://xmlns.com/foaf/0.1/name> \"Unknown Encoder\" . \n");
			metadata.append("<http://wmss.unknown.encoder> <http://www.w3.org/ns/prov#hadRole> <http://d-nb.info/gnd/4139395-8> " + " . \n");
			metadata.append("<http://d-nb.info/gnd/4139395-8> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/prov#Role> . \n");
			metadata.append("<http://d-nb.info/gnd/4139395-8> <http://d-nb.info/standards/elementset/gnd#preferredNameForTheSubjectHeading> \"Encoder\" . \n");
			
		}
				
		return metadata.toString();
	}
	

	private void createRDF(MusicScore score){
				
		StringBuffer ttl = new StringBuffer();
		
		if(score.getURI().equals("")) {
			score.setURI("http://wmss.undefined.score/"+UUID.randomUUID().toString());
			logger.warn("No URI provided for the current score: " + score.getTitle());
		}

		if(score.getTitle().equals("")) {
			score.setTitle("Unknown Music Score Tile");
			logger.warn("No title provided for the current score: " + score.getURI());
		}		
		
		ttl.append(createMetadata(score));
		
		String uid = UUID.randomUUID().toString();

		String rdfTypeURI = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String rdfIdURI = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#ID> ";		
		String musicOWL = " <http://linkeddata.uni-muenster.de/ontology/musicscore#OBJECT> ";		
		String chordNoteOWL = " <http://purl.org/ontology/chord/note/OBJECT> ";
		String keyModeOWL = " <http://purl.org/ontology/tonality/mode/OBJECT> ";
		String chordOWL = " <http://purl.org/ontology/chord/OBJECT> ";
		String scoreURI = " <"+score.getURI()+"> ";
		String nodeURI = " <http://linkeddata.uni-muenster.de/node/"+uid+"/OBJECT> ";
		String musicOntology = " <http://purl.org/ontology/mo/OBJECT> "; 
		String dcOntology = " <http://purl.org/dc/elements/1.1/OBJECT> ";
		String provOntology = " <http://www.w3.org/ns/prov#OBJECT> ";
		String skosOntology = " <http://www.w3.org/2004/02/skos/core#OBJECT> ";
		String instrumentTaxonomy = " <http://purl.org/ontology/mo/mit#OBJECT> ";

		
		ArrayList<Staff> staves = new ArrayList<Staff>();
		ArrayList<Voice> voices = new ArrayList<Voice>();
		ArrayList<String> notesets = new ArrayList<String>();
		ArrayList<Movement> movements = new ArrayList<Movement>();
		
		if(!score.getTitle().equals("") && (score.getTitle()!=null)) {
			
			ttl.append(scoreURI + dcOntology.replace("OBJECT", "title") + "\"" + score.getTitle().trim().replace("\"", "\\\"") + "\" .\n");
			
		}	
		
		String activity = "<"+score.getURI()+"_musicxml2rdf>";
		
		ttl.append("<https://github.com/jimjonesbr/musicowl>" + rdfTypeURI + provOntology.replace("OBJECT", "SoftwareAgent") + " .\n");
		ttl.append("<https://github.com/jimjonesbr/musicowl> <http://xmlns.com/foaf/0.1/name> \"MusicXML2RDF Converter\" .\n");
		ttl.append(activity + rdfTypeURI + provOntology.replace("OBJECT", "Activity") + " .\n");
		ttl.append(activity + " <http://www.w3.org/2000/01/rdf-schema#comment> \"File convertion from MusicXML to RDF based on the MusicOWL Ontology. \" .\n"); 
		ttl.append(activity + provOntology.replace("OBJECT", "startedAtTime") + "\"" + score.getEncodingStartTime().trim() + "\" .\n");
		ttl.append(activity + provOntology.replace("OBJECT", "wasAssociatedWith") + " <https://github.com/jimjonesbr/musicowl> .\n");
		ttl.append(scoreURI + provOntology.replace("OBJECT", "wasGeneratedBy") + activity + " . \n");
		
		for (int i = 0; i < score.getParts().size(); i++) {		
			
			String partID = score.getParts().get(i).getId();
			String partObject = "";
			int notesetCounter = 0;
			int movementCounter = 0;

			for (int j = 0; j < score.getParts().get(i).getMeasures().size(); j++) {			

				if(score.getParts().get(i).getMeasures().get(j).getId().equals("1") ||
				   score.getParts().get(i).getMeasures().get(j).getId().equals("0")) {

					movementCounter++;

					partObject = nodeURI.replace("OBJECT","MOV" + movementCounter + "_" + "PART_" + partID);
					ttl.append(partObject + rdfIdURI + "\"" + partID  + "\" . \n");

					String movementObject = nodeURI.replace("OBJECT", "MOV" + movementCounter);
					ttl.append(scoreURI + musicOntology.replace("OBJECT", "movement") + movementObject + " .\n");
					ttl.append(movementObject + rdfTypeURI + musicOntology.replace("OBJECT", "Movement") + " .\n");								
					
					if(!score.getParts().get(i).getMeasures().get(j).getBeatUnit().equals("")){												
						String beatUnit = this.getCapital(score.getParts().get(i).getMeasures().get(j).getBeatUnit());
						ttl.append(movementObject + musicOWL.replace("OBJECT", "hasBeatUnit") + musicOWL.replace("OBJECT", beatUnit) +" .\n");						
					} 
					
					if(score.getParts().get(i).getMeasures().get(j).getBeatsPerMinute()!=0) {
						ttl.append(movementObject + musicOWL.replace("OBJECT", "hasBeatsPerMinute") + "\""+score.getParts().get(i).getMeasures().get(j).getBeatsPerMinute() +"\"^^<http://www.w3.org/2001/XMLSchema#int> .\n");
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
							ttl.append(movementObject + dcOntology.replace("OBJECT", "title") + "\"" + movementCounter + " (no title)\" .\n");
						} else {
							ttl.append(movementObject + dcOntology.replace("OBJECT", "title") + "\"" + movement.getTitle().trim().replace("\"", "\\\"") + "\" .\n");		
						}
						movements.add(movement);
					}			

					ttl.append(movementObject + musicOWL.replace("OBJECT", "hasScorePart") + partObject + " . \n");		
					ttl.append(partObject + rdfTypeURI + musicOWL.replace("OBJECT", "ScorePart") + " .\n");
					ttl.append(partObject + rdfTypeURI + musicOntology.replace("OBJECT", "Instrument") + " .\n");
					ttl.append(partObject + rdfTypeURI + skosOntology.replace("OBJECT", "Concept") + " .\n");
					ttl.append(partObject + dcOntology.replace("OBJECT", "description") + "\"" + score.getParts().get(i).getName().trim().replace("\"", "\\\"") + "\" .\n");
										
					if(score.getParts().get(i).isSolo()) {
						ttl.append(partObject + musicOWL.replace("OBJECT", "isSolo") + "\"true\" .\n");
					} else {
						ttl.append(partObject + musicOWL.replace("OBJECT", "isSolo") + "\"false\" .\n");						
					}
					
					if(score.getParts().get(i).isEnsemble()) {
						ttl.append(partObject + musicOWL.replace("OBJECT", "isEnsemble") + "\"true\" .\n");
					} else {
						ttl.append(partObject + musicOWL.replace("OBJECT", "isEnsemble") + "\"false\" .\n");						
					}

					/**
					 * @see https://github.com/w3c/musicxml/blob/v3.1/schema/sounds.xml
					 */
										
					if(!score.getParts().get(i).getInstrument().toLowerCase().equals("unknown")) {

						String[] arrayInstrument = score.getParts().get(i).getInstrument().split(Pattern.quote("."));
						String instrumentLabel = arrayInstrument[arrayInstrument.length-1];
						instrumentLabel = instrumentLabel.substring(0, 1).toUpperCase() + instrumentLabel.substring(1);
						Instrument instrument = getInstrument(score.getParts().get(i).getInstrument());
						
						ttl.append(partObject + skosOntology.replace("OBJECT", "prefLabel") + "\""+instrument.getPerformanceMediumDescription()+"\" .\n");					
						ttl.append(partObject + " <http://www.w3.org/2000/01/rdf-schema#label> \""+instrument.getPerformanceMediumId()+"\" .\n");						
						ttl.append(partObject + skosOntology.replace("OBJECT", "broader") + instrumentTaxonomy.replace("OBJECT", instrument.getPerformanceMediumTypeId()) + " .\n");
						ttl.append(instrumentTaxonomy.replace("OBJECT", instrument.getPerformanceMediumTypeId()) + instrumentTaxonomy.replace("OBJECT", "inScheme") +  " <http://purl.org/ontology/mo/instruments#Musical_instruments> .\n");
						ttl.append(instrumentTaxonomy.replace("OBJECT", instrument.getPerformanceMediumTypeId()) + skosOntology.replace("OBJECT", "prefLabel") +  "\""+instrument.getPerformanceMediumTypeId()+"\" .\n");

						
					} else {
						ttl.append(partObject + skosOntology.replace("OBJECT", "prefLabel") + "\"Unspecified\" .\n");
						ttl.append(partObject + " <http://www.w3.org/2000/01/rdf-schema#label> \"unspecidied.unspecified\" .\n");
						ttl.append(partObject + skosOntology.replace("OBJECT", "broader") + instrumentTaxonomy.replace("OBJECT", "unspecified") + " .\n");
						ttl.append(instrumentTaxonomy.replace("OBJECT", "unspecified") + instrumentTaxonomy.replace("OBJECT", "inScheme") +  " <http://purl.org/ontology/mo/instruments#Musical_instruments> .\n");
						ttl.append(instrumentTaxonomy.replace("OBJECT", "unspecified") + skosOntology.replace("OBJECT", "prefLabel") +  "\"Unspecified\" .\n");

					}

				}

				
				String measureID = score.getParts().get(i).getMeasures().get(j).getId();
				String measureObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID);
				String keyObject = "";

				Key key = new Key();

				String instantObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + "INSTANT_" + measureID); 

				ttl.append(instantObject + musicOWL.replace("OBJECT", "hasMeasure") + measureObject+ ".\n");
				ttl.append(instantObject + rdfTypeURI + musicOWL.replace("OBJECT", "Instant")+ ".\n");


				if(j>0){
					ttl.append(nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + score.getParts().get(i).getMeasures().get(j-1).getId()) + musicOWL.replace("OBJECT", "nextMeasure") + measureObject + ".\n");
					ttl.append(nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + "INSTANT_"+ score.getParts().get(i).getMeasures().get(j-1).getId()) + musicOWL.replace("OBJECT", "nextInstant") + instantObject + ".\n");
				}

				ttl.append(partObject + musicOWL.replace("OBJECT", "hasMeasure") + measureObject  + " . \n");
				ttl.append(measureObject + rdfTypeURI +  musicOWL.replace("OBJECT", "Measure")+ " . \n");
				ttl.append(measureObject + rdfIdURI + "\""+ score.getParts().get(i).getMeasures().get(j).getId() +"\" . \n");
				
				keyObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_KEY");

				if(score.getParts().get(i).getMeasures().get(j).getKey().getMode()!=null){

					ttl.append(measureObject + musicOWL.replace("OBJECT", "hasKey") + keyObject + ". \n");
					ttl.append(keyObject + rdfTypeURI + " <http://purl.org/ontology/tonality/Key> . \n");

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
					
					ttl.append(keyObject + " <http://purl.org/ontology/tonality/tonic> " + chordNoteOWL.replace("OBJECT", key.getTonic()) + " . \n");
					ttl.append(keyObject + " <http://purl.org/ontology/tonality/mode> " + keyModeOWL.replace("OBJECT", key.getMode()) + " . \n");

				} else {

					ttl.append(keyObject + rdfTypeURI + " <http://purl.org/ontology/tonality/Key> . \n");
				}


				String timeObject = nodeURI.replace("OBJECT","MOV" + movementCounter + "_" + partID + "_M" +measureID + "_TIME");

				ttl.append(measureObject + musicOWL.replace("OBJECT", "hasTime") + timeObject + ". \n");
				ttl.append(timeObject + rdfTypeURI + musicOWL.replace("OBJECT", "TimeSignature") + " . \n");
				
				if(score.getParts().get(i).getMeasures().get(j).getTime().getBeats().equals("")) {
					score.getParts().get(i).getMeasures().get(j).getTime().setBeats("0");
				}
				if(score.getParts().get(i).getMeasures().get(j).getTime().getBeatType().equals("")) {
					score.getParts().get(i).getMeasures().get(j).getTime().setBeatType("0");
				}

				ttl.append(timeObject + musicOWL.replace("OBJECT", "hasBeats") + "\"" + score.getParts().get(i).getMeasures().get(j).getTime().getBeats()+ "\"^^<http://www.w3.org/2001/XMLSchema#int> . \n");
				ttl.append(timeObject + musicOWL.replace("OBJECT", "hasBeatType") + "\"" + score.getParts().get(i).getMeasures().get(j).getTime().getBeatType()+ "\"^^<http://www.w3.org/2001/XMLSchema#int> . \n");

				if(score.getParts().get(i).getMeasures().get(j).getBarline()!=null){

					String barlineObject = nodeURI.replace("OBJECT","MOV" + movementCounter + "_" + partID + "_M" +measureID + "_REPEAT");
					ttl.append(measureObject + musicOWL.replace("OBJECT", "hasBarline") + barlineObject + ". \n" );

					if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("backward")) ttl.append(barlineObject + rdfTypeURI + musicOWL.replace("OBJECT", "EndRepeat") + " . \n" );
					if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("forward")) ttl.append(barlineObject + rdfTypeURI + musicOWL.replace("OBJECT", "BeginRepeat") + " . \n" );


				}

				String notesetObject = "";
				String tmpVoice = "";
				//ArrayList<Note> arrayNotesSignature = new ArrayList<Note>();

				for (int k = 0; k < score.getParts().get(i).getMeasures().get(j).getNotes().size(); k++) {

					if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isChord()){

						//arrayNotesSignature = new ArrayList<Note>();
						notesetCounter++;

					}
					
					//arrayNotesSignature.add(score.getParts().get(i).getMeasures().get(j).getNotes().get(k));

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff()==null){

						score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setStaff("1");

					}

					notesetObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_ST" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff() + "_V" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId()  + "_NOTESET_" + notesetCounter) ;					
					score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setSignature(notesetObject);										

					if(this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature()!=null){

						if(!this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature().equals(notesetObject)){

							ttl.append(this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature() + musicOWL.replace("OBJECT", "nextNoteSet") + notesetObject + " .\n");
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


					String staffObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_STAFF_" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff());
				
					
					if(addStaff) {

						staves.add(staff);
						ttl.append(staffObject + rdfTypeURI + musicOWL.replace("OBJECT", "Staff") + " . \n");
						ttl.append(partObject + musicOWL.replace("OBJECT", "hasStaff") + staffObject +" . \n");
						ttl.append(staffObject + rdfIdURI + "\""+ score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff() + "\" . \n");
						
					}


					/**
					 * Voice
					 */

					String clefObject ="";
					String voiceObject = "";
					String durationObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS" + notesetCounter + "_DURATION");
					Voice voice = new Voice();
					voice.getPart().setId(partID);
					voice.getPart().setMovement(movementCounter);

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice()==null){

						//Change regarding voices of notes inside of a chord
						
						if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isChord()) {
							voiceObject = nodeURI.replace("OBJECT","MOV" + movementCounter + "_" + partID + "_VOICE_" + tmpVoice);
							voice.setId(tmpVoice);
						} else {
							voiceObject = nodeURI.replace("OBJECT","MOV" + movementCounter + "_" + partID + "_VOICE_1");
							voice.setId("1");							
						}

						score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setVoice(voice);

					} else {

						voiceObject = nodeURI.replace("OBJECT","MOV" + movementCounter + "_" + partID + "_VOICE_" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId());
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
						ttl.append(voiceObject + rdfIdURI + "\"" + voice.getId() +"\" . \n");
						ttl.append(voiceObject + rdfTypeURI + musicOWL.replace("OBJECT", "Voice") + " . \n");										
						ttl.append(staffObject + musicOWL.replace("OBJECT", "hasVoice") + voiceObject + " . \n");

					}


					boolean addNoteSet = true;

					for (int l = 0; l < notesets.size(); l++) {

						if(notesets.get(l).equals(notesetObject)) {
							addNoteSet = false;
						}

					}

					if(addNoteSet) {

						notesets.add(notesetObject);
						ttl.append(voiceObject + musicOWL.replace("OBJECT", "hasNoteSet") + notesetObject + ". \n");
						ttl.append(notesetObject + rdfTypeURI + musicOWL.replace("OBJECT", "NoteSet") + ". \n");

						clefObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS_" + notesetCounter + "_CLEF");	
						ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasClef") + clefObject + ". \n");

						ttl.append(measureObject + musicOWL.replace("OBJECT", "hasNoteSet") + notesetObject + ". \n");


						if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef()!=null){

							for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size(); l++) {

								String articulationObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS" + notesetCounter + "_ARTICULATION_" + l);

								ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasArticulation") + articulationObject + ".\n");
								ttl.append(articulationObject + rdfTypeURI + musicOWL.replace("OBJECT", this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().get(l))) + ".\n");													

							}

							if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("percussion")){
								
								ttl.append(clefObject + musicOWL.replace("OBJECT", "sign")+ chordNoteOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign()) + ". \n");
								ttl.append(clefObject + musicOWL.replace("OBJECT", "line")+ "\""+ score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine() + "\"^^<http://www.w3.org/2001/XMLSchema#int> .\n");


								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("3") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Alto")+ ".\n");

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("5") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Baritone")+ ".\n");						

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("3") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("F")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Baritone")+ ".\n");					

								}


								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("4") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("F")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Bass") + ".\n");					

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("1") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("G")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "FrenchViolin") + ".\n");				

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("2") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "MezzoSoprano") + ".\n");		

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("1") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Soprano") + ".\n");		

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("5") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("F")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "SubBass") + ".\n");	

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("4") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("C")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Tenor") + ".\n");

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine().equals("2") && score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("G")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Trebble") + ".\n");

								}

								if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("percussion")){

									ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Percussion") + ".\n");
								}

							} else {

								ttl.append(clefObject + rdfTypeURI + musicOWL.replace("OBJECT", "Percussion")+ ".\n");

							}

						}

						ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasDuration") + durationObject + ".\n");

						if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType()==null){

							ttl.append(durationObject + rdfTypeURI + musicOWL.replace("OBJECT", "Duration") + ".\n");

						} else {

							ttl.append(durationObject + rdfTypeURI + musicOWL.replace("OBJECT", this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType()))+ ".\n");

						}

					}


					/**
					 * Note
					 */


					String noteObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS_" + notesetCounter + "_NOTE_" + k);

					ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasNote") + noteObject + ".\n");

					ttl.append(noteObject + rdfTypeURI + chordOWL.replace("OBJECT", "Note") + ".\n");

					if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()==null) {

						ttl.append(noteObject + chordOWL.replace("OBJECT", "natural") + chordOWL.replace("OBJECT", "note/Rest") + ".\n");

					} else { 

						ttl.append(noteObject + chordOWL.replace("OBJECT", "natural") + chordOWL.replace("OBJECT", "note/" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()) + ".\n");
						ttl.append(chordOWL.replace("OBJECT", "note/" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()) + rdfTypeURI + chordOWL.replace("OBJECT", "Natural")+ ".\n");

					}


					if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental().equals("") &&
							!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental().equals("natural")){

						ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental()) + ".\n");

					} 


					/**
					 * Add sharps and flats to notes depending on the key. MuaicXML relies on the reader to identify the accidentals depending on the keys. 
					 */

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental().equals("") &&
							score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch() != null){						

						if((key.getTonic().equals("A") && key.getMode().equals("minor")) || (key.getTonic().equals("C") && key.getMode().equals("major"))){
							//ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental()) + ".\n");
						}

						if((key.getTonic().equals("G") && key.getMode().equals("major")) || (key.getTonic().equals("E") && key.getMode().equals("minor"))){

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							}

						}

						if((key.getTonic().equals("D") && key.getMode().equals("major")) || (key.getTonic().equals("B") && key.getMode().equals("minor"))){

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							}

						}

						if((key.getTonic().equals("A") && key.getMode().equals("major")) || (key.getTonic().equals("F") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							} 

						}

						if((key.getTonic().equals("E") && key.getMode().equals("major")) || (key.getTonic().equals("C") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							}

						}

						if((key.getTonic().equals("B") && key.getMode().equals("major")) || (key.getTonic().equals("G") && key.getMode().equals("minor"))){							
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							}

						}

						if((key.getTonic().equals("Fs") && key.getMode().equals("major")) || (key.getTonic().equals("Ds") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							}

						}

						if((key.getTonic().equals("Cs") && key.getMode().equals("major")) || (key.getTonic().equals("As") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");

							}

						}

						if((key.getTonic().equals("F") && key.getMode().equals("major")) || (key.getTonic().equals("D") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

						if((key.getTonic().equals("Bb") && key.getMode().equals("major")) || (key.getTonic().equals("G") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E")){

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

						if((key.getTonic().equals("Eb") && key.getMode().equals("major")) || (key.getTonic().equals("C") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A")) {

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

						if((key.getTonic().equals("Ab") && key.getMode().equals("major")) || (key.getTonic().equals("F") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D")) {

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

						if((key.getTonic().equals("Db") && key.getMode().equals("major")) || (key.getTonic().equals("Bb") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G")) {

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

						if((key.getTonic().equals("Gb") && key.getMode().equals("major")) || (key.getTonic().equals("Eb") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C")) {

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

						if((key.getTonic().equals("Cb") && key.getMode().equals("major")) || (key.getTonic().equals("Ab") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") ||
									score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F")) {

								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");

							}

						}

					}










					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave()!=null){

						ttl.append(noteObject + musicOWL.replace("OBJECT", "hasOctave") + "\""+ score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave()+ "\"^^<http://www.w3.org/2001/XMLSchema#int> . \n");	

					}




					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isDot()){
						String dotObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" +partID + "_M" + measureID + "_NS" + notesetCounter + "_N" + k + "_D_DOT");
						ttl.append(durationObject + musicOWL.replace("OBJECT", "hasDurationAttribute") + dotObject + ".\n" );
						ttl.append(dotObject + rdfTypeURI + musicOWL.replace("OBJECT", "Dot") + ".\n");
					}



					for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size(); l++) {

						String dynamicObject = nodeURI.replace("OBJECT", "MOV" + movementCounter + "_" + partID + "_M" + measureID + "_NS" + notesetCounter + "_DYNAMIC_" + l);
						ttl.append(dynamicObject + rdfTypeURI + musicOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().get(l)) + ".\n");
						ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasDynamic") + dynamicObject + ".\n");

					}


				}

			}

		}


		try {

			String xml = new String(score.getFileContent().getBytes(StandardCharsets.UTF_8));
			xml = xml.replace("\\", "\\\\");
			ttl.append(scoreURI + musicOWL.replace("OBJECT", "asMusicXML")+ "\"" + xml.replace("\"", "'") + "\" . \n" );
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			ttl.append("<http://musicxml2rdf.musicowl.de/"+uid+">" + provOntology.replace("OBJECT", "endedAtTime") + "\"" + sdf.format(new Date()) + "\" .\n");
			
			FileOutputStream fileStream = new FileOutputStream(new File(this.getOutputFile()),false);
			OutputStreamWriter writer = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);

			writer.append(ttl.toString());
			writer.close();		

			
			staves = null;
			voices = null;
			notesets = null;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		
		File file = this.getInputFile();

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
								logger.info("["+ score.getParts().get(i).getName() + "] Parsing movement: " + movementCount + " ("+measure.getTitle() +") ... ");
							} else {
								logger.info("["+ score.getParts().get(i).getName() + "] Parsing movement: " + movementCount + " ... ");
							}

							

						}

						score.getParts().get(i).getMeasures().add(measure);

						/**
						 * Key
						 */

/**						
						NodeList nodeMeasureKeyMode = (NodeList) xpath.evaluate("//attributes/key/mode", nodeMeasures.item(j),XPathConstants.NODESET);

						if(nodeMeasureKeyMode.getLength()!=0){

//							score.getParts().get(i).getMeasures().get(j).getKey().setMode(nodeMeasureKeyMode.item(0).getTextContent());
//							currentMode = nodeMeasureKeyMode.item(0).getTextContent();
//
//							logger.info("["+ score.getParts().get(i).getName() + "] mode > " + currentMode +" ... ");
							
						} else {

//							score.getParts().get(i).getMeasures().get(j).getKey().setMode(currentMode);
						}

						NodeList nodeMeasureKeyFifth = (NodeList) xpath.evaluate("//attributes/key/fifths", nodeMeasures.item(j),XPathConstants.NODESET);

						if(nodeMeasureKeyFifth.getLength()!=0){

//							score.getParts().get(i).getMeasures().get(j).getKey().setFifths(nodeMeasureKeyFifth.item(0).getTextContent());
//							currentFifth = nodeMeasureKeyFifth.item(0).getTextContent();
//							
//							logger.info("["+ score.getParts().get(i).getName() + "] fifth > " + currentFifth +" ... ");

						} else {

//							score.getParts().get(i).getMeasures().get(j).getKey().setFifths(currentFifth);

						}

*/
						

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
														perMinute = Integer.parseInt(listMetronome.item(n).getTextContent().toString());
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

										logger.info("	[Clef] Measure "+measure.getId()+" Sign: "+ clef.getSign()+ " | Line: " + clef.getLine());
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

										logger.info("	[Time] Measure "+measure.getId()+" Beats: "+ currentBeat+ " | Beat-type: " + currentBeatType);

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

										logger.info("	[Key] Measure "+measure.getId()+" Mode: "+ currentMode+ " | Fifth: " + currentFifth);

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
								if(elementNotes.getElementsByTagName("dot").item(0)!=null) note.setDot(true);
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







			if(verbose){
				
				System.out.println("Score: "+score.getTitle()+"\n");

				for (int i = 0; i < score.getParts().size(); i++) {

					System.out.println("Part: " + score.getParts().get(i).getId());		

					for (int j = 0; j < score.getParts().get(i).getMeasures().size(); j++) {

						System.out.println("|-- Measure ("+score.getParts().get(i).getId()+"): " + score.getParts().get(i).getMeasures().get(j).getId());
						System.out.println("	|-- Key mode: " + score.getParts().get(i).getMeasures().get(j).getKey().getMode());
						System.out.println("	|-- Key fifth: " + score.getParts().get(i).getMeasures().get(j).getKey().getFifths());
						System.out.println("	|-- Time beats: " + score.getParts().get(i).getMeasures().get(j).getTime().getBeats());
						System.out.println("	|-- Time beat-type: " + score.getParts().get(i).getMeasures().get(j).getTime().getBeatType());
						System.out.println("	|-- Bar Line: " + score.getParts().get(i).getMeasures().get(j).getBarline());

						for (int k = 0; k < score.getParts().get(i).getMeasures().get(j).getNotes().size(); k++) {

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isDot()) System.out.println("		|-- Dot");
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isChord()) System.out.println("		|-- Chord");
							System.out.println("		|-- Note-Step   	: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch());
							System.out.println("		|-- Note-Octave 	: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave());
							System.out.println("		|-- Staff  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff());
							System.out.println("		|-- Voice  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId() + " " + score.getParts().get(i).getId() +  " M" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getMeasure());
							System.out.println("		|-- Type  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType());
							System.out.println("		|-- Clef-Sign  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign());
							System.out.println("		|-- Clef-Line  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getLine());

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getSlur() != null)
								System.out.println("		|-- Slur  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getSlur());

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size()!=0){

								for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size(); l++) {

									System.out.println("		|-- Dynamic  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().get(l));

								}

							}

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size()!=0){

								for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size(); l++) {

									System.out.println("		|-- Articulations  	: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().get(l));

								}

							}

							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental()!=null) {
								System.out.println("		|-- Accidental  	: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental());}
								System.out.println("		|-----------------------|");
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

		return this.outputFile;

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

	
	
}
