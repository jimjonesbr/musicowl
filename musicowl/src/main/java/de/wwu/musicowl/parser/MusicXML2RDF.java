package de.wwu.musicowl.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import de.wwu.musicowl.core.Clef;
import de.wwu.musicowl.core.Key;
import de.wwu.musicowl.core.Measure;
import de.wwu.musicowl.core.MusicScore;
import de.wwu.musicowl.core.Note;
import de.wwu.musicowl.core.ScorePart;

public class MusicXML2RDF {

	private static ArrayList<Clef> clefList;
	private static ArrayList<Note> currentNotes;
	
	private static boolean verbose = false;

	public static void main(String[] args) {

		//loadMusicXML(new File("scores/xmlsamples/Dichterliebe01.xml"));		
		//loadMusicXML(new File("scores/paper/bach_fuge.xml"));
		//loadMusicXML(new File("scores/xmlsamples/Elgar_cello_Concerto_Op.85.xml"));
		MusicXML2RDF m = new MusicXML2RDF();

		//m.createRDF(m.loadMusicXML(new File("scores/xmlsamples/ActorPreludeSample.xml")));

		File[] files = new File("scores/sammlung/").listFiles();

		for (File file : files) {

			if(file.getName().endsWith(".xml")){
				m.createRDF(m.loadMusicXML(file));				
			}

		}


	}




	@SuppressWarnings("static-access")
	public MusicXML2RDF() {
		super();
		this.clefList = new ArrayList<Clef>();
		this.currentNotes = new ArrayList<Note>();
				
	}



	public void createRDF(MusicScore score){


		StringBuffer ttl = new StringBuffer();
		String uid = UUID.randomUUID().toString();

		String rdfTypeURI = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String rdfIdURI = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#ID> ";
		String musicOWL = " <http://linkeddata.uni-muenster.de/ontology/musicscore#OBJECT> ";		
		String chordNoteOWL = " <http://purl.org/ontology/chord/note/OBJECT> ";
		String keyModeOWL = " <http://purl.org/ontology/tonality/mode/OBJECT> ";
		String chordOWL = " <http://purl.org/ontology/chord/OBJECT> ";
		String nodeURI = " <http://linkeddata.uni-muenster.de/node/"+uid+"/OBJECT> ";
		String musicOntology = " <http://purl.org/ontology/mo/OBJECT> "; 

		String scoreObject = nodeURI.replace("OBJECT",uid);

		ttl.append(scoreObject + rdfTypeURI + musicOntology.replace("OBJECT", "Movement") + " .\n");
		
		for (int i = 0; i < score.getParts().size(); i++) {

			String partID = score.getParts().get(i).getId();
			String partObject = nodeURI.replace("OBJECT","PART_" + partID);
			int notesetCounter = 0;


			ttl.append(scoreObject + musicOWL.replace("OBJECT", "hasScorePart") + partObject + " . \n");		
			ttl.append(partObject + rdfTypeURI + musicOWL.replace("OBJECT", "ScorePart") + " .\n");

			for (int j = 0; j < score.getParts().get(i).getMeasures().size(); j++) {

				String measureID = score.getParts().get(i).getMeasures().get(j).getId();
				String measureObject = nodeURI.replace("OBJECT", partID + "_M" + measureID);
				String keyObject = "";
//				String keyType = "";
//				String keyMode = "";
//				String keyTonic = "";
				
				Key key = new Key();

				String instantObject = nodeURI.replace("OBJECT", "INSTANT_" + measureID); 

				ttl.append(instantObject + musicOWL.replace("OBJECT", "hasMeasure") + measureObject+ ".\n");
				ttl.append(instantObject + rdfTypeURI + musicOWL.replace("OBJECT", "Instant")+ ".\n");


				if(j>0){
					ttl.append(nodeURI.replace("OBJECT", partID + "_M" + score.getParts().get(i).getMeasures().get(j-1).getId()) + musicOWL.replace("OBJECT", "nextMeasure") + measureObject + ".\n");
					ttl.append(nodeURI.replace("OBJECT", "INSTANT_"+ score.getParts().get(i).getMeasures().get(j-1).getId()) + musicOWL.replace("OBJECT", "nextInstant") + instantObject + ".\n");
				}

				ttl.append(partObject + musicOWL.replace("OBJECT", "hasMeasure") + measureObject  + " . \n");
				ttl.append(partObject + rdfIdURI + "\"" +score.getParts().get(i).getId()  + "\" . \n");
				ttl.append(measureObject + rdfTypeURI +  musicOWL.replace("OBJECT", "Measure")+ " . \n");
				ttl.append(measureObject + rdfIdURI + "\""+ score.getParts().get(i).getMeasures().get(j).getId() +"\" . \n");


				keyObject = nodeURI.replace("OBJECT",partID + "_M" + measureID + "_KEY");
				
				if(score.getParts().get(i).getMeasures().get(j).getKey().getMode()!=null){

					ttl.append(measureObject + musicOWL.replace("OBJECT", "hasKey") + keyObject + ". \n");
					ttl.append(keyObject + rdfTypeURI + " <http://purl.org/ontology/tonality/Key> . \n");
					
					if(score.getParts().get(i).getMeasures().get(j).getKey().getMode().equals("major")){

						key.setMode("major");

						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("0")) {
						//	keyType = "<http://purl.org/NET/c4dm/keys.owl#CMajor>";
							key.setTonic("C");
						}						
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("1")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#GMajor>";
							key.setTonic("G");
							//ttl.append(keyObject + " <http://www.w3.org/2002/07/owl#sameAs> <http://purl.org/ontology/tonality/key/Gmajor> . \n");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("2")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#DMajor>";
							key.setTonic("D");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("3")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#AMajor>";
							key.setTonic("A");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("4")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#EMajor>";
							key.setTonic("E");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("5")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#BMajor>";
							key.setTonic("B");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("6")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#FShparpMajor>";
							key.setTonic("Fs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("7")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#CSharpMajor>";
							key.setTonic("Cs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-1")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#FMajor>";							
							key.setTonic("F");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-2")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#BFlatMajor>";							
							key.setTonic("Bb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-3")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#EFlatMajor>";							
							key.setTonic("Eb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-4")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#AFlatMajor>";
							key.setTonic("Ab");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-5")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#DFlatMajor>";
							key.setTonic("Db");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-6")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#GFlatMajor>";							
							key.setTonic("Gb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-7")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#CFlatMajor>";
							key.setTonic("Cb");
						}

					}

					if(score.getParts().get(i).getMeasures().get(j).getKey().getMode().equals("minor")){

						key.setMode("minor");
						
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("0")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#AMinor>";
							key.setTonic("A");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("1")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#EMinor>";
							key.setTonic("E");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("2")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#BMinor>";
							key.setTonic("B");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("3")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#FSharpMinor>";
							key.setTonic("Fs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("4")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#CSharpMinor>";
							key.setTonic("Cs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("5")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#GSharpMinor>";
							key.setTonic("Gs");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("6")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#DSharpMinor>";
							key.setTonic("Ds");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("7")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#AFlatMinor>";
							key.setTonic("Ab");
						}

						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-1")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#DMinor>";
							key.setTonic("D");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-2")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#GMinor>";
							key.setTonic("G");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-3")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#CMinor>";
							key.setTonic("C");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-4")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#FMinor>";
							key.setTonic("F");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-5")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#BFlatMinor>";
							key.setTonic("Bb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-6")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#EFlatMinor>";
							key.setTonic("Eb");
						}
						if (score.getParts().get(i).getMeasures().get(j).getKey().getFifths().equals("-7")) {
							//keyType = "<http://purl.org/NET/c4dm/keys.owl#GSharpMinor>";
							key.setTonic("Gs");
						}

					}
			
					ttl.append(keyObject + " <http://purl.org/ontology/tonality/tonic> " + chordNoteOWL.replace("OBJECT", key.getTonic()) + " . \n");
					ttl.append(keyObject + " <http://purl.org/ontology/tonality/mode> " + keyModeOWL.replace("OBJECT", key.getMode()) + " . \n");

				} else {

					//ttl.append(keyObject + rdfTypeURI + keyType + " . \n");
					ttl.append(keyObject + rdfTypeURI + " <http://purl.org/ontology/tonality/Key> . \n");
				}


				String timeObject = nodeURI.replace("OBJECT",partID + "_M" +measureID + "_TIME");

				ttl.append(measureObject + musicOWL.replace("OBJECT", "hasTime") + timeObject + ". \n");
				ttl.append(timeObject + rdfTypeURI + musicOWL.replace("OBJECT", "TimeSignatur") + " . \n");
				ttl.append(timeObject + musicOWL.replace("OBJECT", "hasBeats") + "\"" + score.getParts().get(i).getMeasures().get(j).getTime().getBeats()+ "\"^^<http://www.w3.org/2001/XMLSchema#int> . \n");
				ttl.append(timeObject + musicOWL.replace("OBJECT", "hasBeatType") + "\"" + score.getParts().get(i).getMeasures().get(j).getTime().getBeatType()+ "\"^^<http://www.w3.org/2001/XMLSchema#int> . \n");

				if(score.getParts().get(i).getMeasures().get(j).getBarline()!=null){

					String barlineObject = nodeURI.replace("OBJECT",partID + "_M" +measureID + "_REPEAT");
					ttl.append(measureObject + musicOWL.replace("OBJECT", "hasBarline") + barlineObject + ". \n" );

					if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("backward")) ttl.append(barlineObject + rdfTypeURI + musicOWL.replace("OBJECT", "EndRepeat") + " . \n" );
					if(score.getParts().get(i).getMeasures().get(j).getBarline().equals("forward")) ttl.append(barlineObject + rdfTypeURI + musicOWL.replace("OBJECT", "BeginRepeat") + " . \n" );


				}

				String notesetObject = "";


				for (int k = 0; k < score.getParts().get(i).getMeasures().get(j).getNotes().size(); k++) {

					if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isChord()){

						notesetCounter++;

					}

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff()==null){

						score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setStaff("1");

					}

					notesetObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_ST" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff() + "_V" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId()  + "_NOTESET_" + notesetCounter) ;
					score.getParts().get(i).getMeasures().get(j).getNotes().get(k).setSignature(notesetObject);

					if(this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature()!=null){

						ttl.append(this.getPreviousNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k)).getSignature() + musicOWL.replace("OBJECT", "nextNoteSet") + notesetObject + " .\n");
						this.setCurrentNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k));

					} else {

						this.setCurrentNoteSet(score.getParts().get(i).getMeasures().get(j).getNotes().get(k));

					}

					ttl.append(notesetObject + rdfTypeURI + musicOWL.replace("OBJECT", "NoteSet") + ". \n");
					ttl.append(measureObject + musicOWL.replace("OBJECT", "hasNoteSet") + notesetObject + ". \n");

					String voiceObject = "";

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice()==null){

						voiceObject = nodeURI.replace("OBJECT",partID + "_VOICE_1");

					} else {

						voiceObject = nodeURI.replace("OBJECT",partID + "_VOICE_" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getVoice().getId());

					}

					ttl.append(voiceObject + rdfTypeURI + musicOWL.replace("OBJECT", "Voice") + " . \n");					
					//ttl.append(measureObject + musicOWL.replace("OBJECT", "hasVoice") + voiceObject + ". \n");
					ttl.append(voiceObject + musicOWL.replace("OBJECT", "hasNoteSet") + notesetObject + ". \n");
					//ttl.append(voiceObject + musicOWL.replace("OBJECT", "hasNoteSet") + notesetObject + ". \n");

					String staffObject = "";


					staffObject = nodeURI.replace("OBJECT", partID + "_STAFF_" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getStaff());

					ttl.append(staffObject + rdfTypeURI + musicOWL.replace("OBJECT", "Staff") + " . \n");
					ttl.append(staffObject + musicOWL.replace("OBJECT", "hasNoteSet") + notesetObject + ". \n");

					String noteObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_NS_" + notesetCounter + "_NOTE_" + k);

					ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasNote") + noteObject + ".\n");
					ttl.append(noteObject + rdfTypeURI + chordOWL.replace("OBJECT", "Note") + ".\n");
					
					if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()==null) {

						ttl.append(noteObject + chordOWL.replace("OBJECT", "natural") + chordOWL.replace("OBJECT", "note/Rest") + ".\n");

					} else { 
					
						ttl.append(noteObject + chordOWL.replace("OBJECT", "natural") + chordOWL.replace("OBJECT", "note/" + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()) + ".\n");
					
					}
					
					
										
					if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental().equals("") &&
					   !score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental().equals("natural")){
						
						ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental()) + ".\n");
					}
					

					/*
					 * Add sharps and flats to notes depending on the key. MuaicXML relies on the reader to identify the accidentals depending on the keys. 
					 */

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental().equals("") &&
					   score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch() != null){						
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#AMinor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#CMajor>")){
						if((key.getTonic().equals("A") && key.getMode().equals("minor")) || (key.getTonic().equals("C") && key.getMode().equals("major"))){
							ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental()) + ".\n");
							
						}
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#GMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#EMinor>")){
						if((key.getTonic().equals("G") && key.getMode().equals("major")) || (key.getTonic().equals("E") && key.getMode().equals("minor"))){
							
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");
								
							}
							
						}
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#DMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#BMinor>")){
						if((key.getTonic().equals("D") && key.getMode().equals("major")) || (key.getTonic().equals("B") && key.getMode().equals("minor"))){
							
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");
								
							}
							
						}

						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#AMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#FMinor>")){
						if((key.getTonic().equals("A") && key.getMode().equals("major")) || (key.getTonic().equals("F") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
							   score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
							   score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");
								
							}
							
						}

						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#EMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#CMinor>")){
						if((key.getTonic().equals("E") && key.getMode().equals("major")) || (key.getTonic().equals("C") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");
								
							}
							
						}
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#BMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#GMinor>")){
						if((key.getTonic().equals("B") && key.getMode().equals("major")) || (key.getTonic().equals("G") && key.getMode().equals("minor"))){							
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("F") || 
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("C") || 
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "sharp") + ".\n");
								
							}
							
						}

						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#FSharpMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#DSharpMinor>")){
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
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#CSharpMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#ASharpMinor>")){
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
						
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#FMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#DMinor>")){
						if((key.getTonic().equals("F") && key.getMode().equals("major")) || (key.getTonic().equals("D") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");
								
							}
							
						}
						
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#BFlatMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#GMinor>")){
						if((key.getTonic().equals("Bb") && key.getMode().equals("major")) || (key.getTonic().equals("G") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E")){
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");
								
							}
							
						}
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#EFlatMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#CMinor>")){
						if((key.getTonic().equals("Eb") && key.getMode().equals("major")) || (key.getTonic().equals("C") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A")) {
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");
								
							}
							
						}

						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#AFlatMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#FMinor>")){
						if((key.getTonic().equals("Ab") && key.getMode().equals("major")) || (key.getTonic().equals("F") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D")) {
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");
								
							}
							
						}

						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#DFlatMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#BFlatMinor>")){
						if((key.getTonic().equals("Db") && key.getMode().equals("major")) || (key.getTonic().equals("Bb") && key.getMode().equals("minor"))){
							if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("B") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("E") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("A") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("D") ||
								score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch().equals("G")) {
								
								ttl.append(noteObject + chordOWL.replace("OBJECT", "modifier") + chordOWL.replace("OBJECT", "flat") + ".\n");
								
							}
							
						}

						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#GFlatMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#EFlatMinor>")){
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
						
						//if(keyType.equals("<http://purl.org/NET/c4dm/keys.owl#CFlatMajor>") || keyType.equals("<http://purl.org/NET/c4dm/keys.owl#AFlatMinor>")){
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
						//System.out.println(keyType);
					}
					//					if (score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch()==null) {
					//
					//						ttl.append(noteObject + rdfTypeURI + musicOWL.replace("OBJECT", "Rest") + ".\n");
					//
					//					} else {
					//
					//						ttl.append(noteObject + rdfTypeURI + keyOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getPitch() + this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental())) + ".\n");
					//					}



					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave()!=null){

						ttl.append(noteObject + musicOWL.replace("OBJECT", "hasOctave") + "\""+ score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getOctave()+ "\"^^<http://www.w3.org/2001/XMLSchema#int> . \n");	

					}

					String durationObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_NS" + notesetCounter + "_N" + k + "_DURATION");
					ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasDuration") + durationObject + ".\n");

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).isDot()){
						String dotObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_NS" + notesetCounter + "_N" + k + "_D_DOT");
						ttl.append(durationObject + musicOWL.replace("OBJECT", "hasDurationAttribute") + dotObject + ".\n" );
						ttl.append(dotObject + rdfTypeURI + musicOWL.replace("OBJECT", "Dot") + ".\n");
					}

					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType()!=null){

						ttl.append(durationObject + rdfTypeURI + musicOWL.replace("OBJECT", this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getType()))+ ".\n");

					} else {

						ttl.append(durationObject + rdfIdURI + musicOWL.replace("OBJECT", "Rest")+ ".\n");

					}


					for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size(); l++) {

						String dynamicObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_NS" + notesetCounter + "_DYNAMIC_" + l);
						ttl.append(dynamicObject + rdfTypeURI + musicOWL.replace("OBJECT", score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().get(l)) + ".\n");
						ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasDynamic") + dynamicObject + ".\n");

					}




					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef()!=null){

						String clefObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_NS_" + notesetCounter + "_CLEF");	
						ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasClef") + clefObject + ". \n");


						for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size(); l++) {

							String articulationObject = nodeURI.replace("OBJECT", partID + "_M" + measureID + "_NS" + notesetCounter + "_ARTICULATION_" + l);

							ttl.append(notesetObject + musicOWL.replace("OBJECT", "hasArticulation") + articulationObject + ".\n");
							ttl.append(articulationObject + rdfTypeURI + musicOWL.replace("OBJECT", this.getCapital(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().get(l))) + ".\n");													

						}

						if(!score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getClef().getSign().equals("percussion")){


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














					//if score.getParts().get(i).getMeasures().get(j).getNotes()

					//					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getSlur() != null)
					//						System.out.println("		|-- Slur  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getSlur());
					//
					//					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size()!=0){
					//
					//						for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().size(); l++) {
					//
					//							System.out.println("		|-- Dynamic  		: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getDynamics().get(l));
					//
					//						}
					//
					//					}
					//
					//					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size()!=0){
					//
					//						for (int l = 0; l < score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().size(); l++) {
					//
					//							System.out.println("		|-- Articulations  	: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getArticulations().get(l));
					//
					//						}
					//
					//					}
					//
					//					if(score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental()!=null) {
					//						System.out.println("		|-- Accidental  	: " + score.getParts().get(i).getMeasures().get(j).getNotes().get(k).getAccidental());}
					//					System.out.println("		|-----------------------|");
				}

			}
		}


		try {

			FileOutputStream fileStream = new FileOutputStream(new File("ntriples/" + score.getFileName() + ".nt"),false);
			OutputStreamWriter writer = new OutputStreamWriter(fileStream, "UTF8");

			writer.append(ttl.toString());
			writer.close();		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}





	}


	public MusicScore loadMusicXML(File file){

		System.out.println("Processing " + file.getName() + ", please wait ...");
		MusicScore score = new MusicScore();

		score.setFileName(file.getName().replaceAll(".xml", ""));

		String currentMode = "";
		String currentFifth = ""; 
		String currentBeatType = "";
		String currentBeat = "";
		int slurCount = 0;
		boolean slurFlag = false;
		//Clef currentClef = new Clef();

		//String scoreOntologySequence = " <" + scoreOntologyURI + "#hasSequence> ";
		//String scoreOntologyNextNoteNet = " <" + scoreOntologyURI + "#nextNoteSet> ";

		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(false);
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

			builder.setEntityResolver(new EntityResolver() {

				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

					return new InputSource(new StringReader(""));

				}
			});


			Document document = builder.parse(file);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			NodeList subfields = (NodeList) xpath.evaluate("//score-partwise/movement-title", document,XPathConstants.NODESET);

			String scoreSubject = "_:debussy";

			score.setId(scoreSubject);

			subfields = (NodeList) xpath.evaluate("//score-partwise/part-list/score-part/@id", document,XPathConstants.NODESET);

			for (int i = 0; i < subfields.getLength(); i++) {

				ScorePart part = new ScorePart();
				part.setId(subfields.item(i).getTextContent());
				score.getParts().add(part);					

			}


			for (int i = 0; i < score.getParts().size(); i++) {

				this.clefList = new ArrayList<Clef>();

				NodeList nodeMeasures = (NodeList) xpath.evaluate("//score-partwise/part[@id='"+score.getParts().get(i).getId()+"']/measure", document,XPathConstants.NODESET);


				if (nodeMeasures.getLength() != 0) {


					for (int j = 0; j < nodeMeasures.getLength(); j++) {

						Measure measure = new Measure();







						//NodeList nodeMeasureNumbers = (NodeList) xpath.evaluate("//score-partwise/part[@id='"+score.getParts().get(i).getId()+"']/measure/@number", document,XPathConstants.NODESET);

						measure.setId(nodeMeasures.item(j).getAttributes().getNamedItem("number").getNodeValue());					

						score.getParts().get(i).getMeasures().add(measure);

						/**
						 * Key
						 */

						NodeList nodeMeasureKeyMode = (NodeList) xpath.evaluate("//score-partwise/part[@id='" + score.getParts().get(i).getId() + "']/measure[@number='"+measure.getId()+"']/attributes/key/mode", document,XPathConstants.NODESET);

						if(nodeMeasureKeyMode.getLength()!=0){

							score.getParts().get(i).getMeasures().get(j).getKey().setMode(nodeMeasureKeyMode.item(0).getTextContent());
							currentMode = nodeMeasureKeyMode.item(0).getTextContent();

						} else {

							score.getParts().get(i).getMeasures().get(j).getKey().setMode(currentMode);
						}

						NodeList nodeMeasureKeyFifth = (NodeList) xpath.evaluate("//score-partwise/part[@id='"+score.getParts().get(i).getId()+"']/measure[@number='"+measure.getId()+"']/attributes/key/fifths", document,XPathConstants.NODESET);

						if(nodeMeasureKeyFifth.getLength()!=0){

							score.getParts().get(i).getMeasures().get(j).getKey().setFifths(nodeMeasureKeyFifth.item(0).getTextContent());
							currentFifth = nodeMeasureKeyFifth.item(0).getTextContent();

						} else {

							score.getParts().get(i).getMeasures().get(j).getKey().setFifths(currentFifth);
						}





						/**
						 * one xpath 4 all
						 */

						NodeList nodeMeasureElementsList = (NodeList) xpath.evaluate("//score-partwise/part[@id='"+score.getParts().get(i).getId()+"']/measure[@number='"+measure.getId()+"']", document,XPathConstants.NODESET);


						ArrayList<String> dynamics = new ArrayList<String>();


						for (int k = 0; k < nodeMeasureElementsList.item(0).getChildNodes().getLength(); k++) {

							Note note = new Note();

							if(nodeMeasureElementsList.item(0).getChildNodes().item(k).getNodeName().equals("direction")){

								Element elementDirection = (Element) nodeMeasureElementsList.item(0).getChildNodes().item(k);

								for (int l = 0; l < elementDirection.getChildNodes().getLength(); l++) {

									if(elementDirection.getElementsByTagName("dynamics").item(l)!=null){

										NodeList listDynamics = elementDirection.getElementsByTagName("dynamics").item(0).getChildNodes();

										for (int m = 0; m < listDynamics.getLength(); m++) {

											if(!listDynamics.item(m).getNodeName().matches("#text")) {

												dynamics.add(listDynamics.item(m).getNodeName());

											}
										}

									}

								}


							}



							if(nodeMeasureElementsList.item(0).getChildNodes().item(k).getNodeName().equals("attributes")){

								NodeList listAttributes = nodeMeasureElementsList.item(0).getChildNodes().item(k).getChildNodes();

								for (int l = 0; l < listAttributes.getLength(); l++) {


									if (listAttributes.item(l).getNodeName().equals("clef")) {

										NodeList listClef = listAttributes.item(l).getChildNodes();

										Clef clef = new Clef();

										for (int m = 0; m < listClef.getLength(); m++) {


											if(listAttributes.item(l).getAttributes().getNamedItem("number")!=null) {

												clef.setStaffNumber(listAttributes.item(l).getAttributes().getNamedItem("number").getNodeValue());


											} else {

												clef.setStaffNumber("1");

											}


											if(listClef.item(m).getNodeName().equals("line")){

												clef.setLine(listClef.item(m).getTextContent());

											}

											if(listClef.item(m).getNodeName().equals("sign")){

												clef.setSign(listClef.item(m).getTextContent());

											}


										}

										this.addClef(clef);										

									}



									if(listAttributes.item(l).getNodeName().equals("time")){

										NodeList listTime = listAttributes.item(l).getChildNodes();

										for (int m = 0; m < listTime.getLength(); m++) {

											if (listTime.item(m).getNodeName().equals("beats")){

												currentBeat=listTime.item(m).getTextContent();

											}

											if (listTime.item(m).getNodeName().equals("beat-type")){

												currentBeatType = listTime.item(m).getTextContent();


											}

										}


									}


								}

							}


							measure.getTime().setBeats(currentBeat);
							measure.getTime().setBeatType(currentBeatType);


							if(nodeMeasureElementsList.item(0).getChildNodes().item(k).getNodeName().equals("barline")){

								NodeList listBarline = nodeMeasureElementsList.item(0).getChildNodes().item(k).getChildNodes();

								for (int l = 0; l < listBarline.getLength(); l++) {

									if (listBarline.item(l).getNodeName().equals("repeat")){

										measure.setBarline(listBarline.item(l).getAttributes().getNamedItem("direction").getNodeValue());

									}

								}

							}


							if(nodeMeasureElementsList.item(0).getChildNodes().item(k).getNodeName().equals("note")){


								NodeList listNoteElements = nodeMeasureElementsList.item(0).getChildNodes().item(k).getChildNodes();



								for (int l = 0; l < listNoteElements.getLength(); l++) {


									if (listNoteElements.item(l).getNodeName().equals("staff")) {

										note.setStaff(listNoteElements.item(l).getTextContent());

									}

									if (listNoteElements.item(l).getNodeName().equals("notations")) {

										NodeList listNotationElements = listNoteElements.item(l).getChildNodes();

										for (int m = 0; m < listNotationElements.getLength(); m++) {

											if(listNotationElements.item(m).getNodeName().equals("slur")){

												if(listNotationElements.item(m).getAttributes().getNamedItem("type").getNodeValue().equals("start")){

													slurCount = slurCount + 1; 
													slurFlag = true;													

												}	

												if(listNotationElements.item(m).getAttributes().getNamedItem("type").getNodeValue().equals("stop")){


													slurFlag = false;
												}

												note.setSlur(Integer.toString(slurCount));

											}



											if(listNotationElements.item(m).getNodeName().equals("articulations")){

												NodeList listAriculationElements = listNotationElements.item(m).getChildNodes();	

												for (int n = 0; n < listAriculationElements.getLength(); n++) {

													if(!listAriculationElements.item(n).getNodeName().matches("#text")){

														note.getArticulations().add(listAriculationElements.item(n).getNodeName());

													}

												}


											}


										}


									}


								}


								Element elementNotes = (Element) nodeMeasureElementsList.item(0).getChildNodes().item(k);


								if(elementNotes.getElementsByTagName("chord").item(0)!=null) note.setChord(true);
								if(elementNotes.getElementsByTagName("dot").item(0)!=null) note.setDot(true);
								if(elementNotes.getElementsByTagName("octave").item(0)!=null) note.setOctave(elementNotes.getElementsByTagName("octave").item(0).getTextContent());
								if(elementNotes.getElementsByTagName("step").item(0)!=null) note.setPitch(elementNotes.getElementsByTagName("step").item(0).getTextContent());								
								if(elementNotes.getElementsByTagName("type").item(0)!=null) note.setType(elementNotes.getElementsByTagName("type").item(0).getTextContent());
								
								if(elementNotes.getElementsByTagName("voice").item(0)!=null) {
									
									//Voice voice = new Voice();
									note.getVoice().setId(elementNotes.getElementsByTagName("voice").item(0).getTextContent());
									//note.setVoice(elementNotes.getElementsByTagName("voice").item(0).getTextContent());
									
								} else {
									
									note.getVoice().setId("1");
									
								}
								
								note.getVoice().setPart(score.getParts().get(i).getId());
								note.getVoice().setMeasure(measure.getId());

								
								if(elementNotes.getElementsByTagName("accidental").item(0)!=null){

//									if(elementNotes.getElementsByTagName("accidental").item(0).equals("flat")) note.setAccidental("b");
//									if(elementNotes.getElementsByTagName("accidental").item(0).equals("sharp")) note.setAccidental("s");
//									if(elementNotes.getElementsByTagName("accidental").item(0).equals("natural")) note.setAccidental("n");
									note.setAccidental(elementNotes.getElementsByTagName("accidental").item(0).getTextContent());
								}


								if(dynamics.size()!=0) {

									for (int l = 0; l < dynamics.size(); l++) {

										note.getDynamics().add(dynamics.get(l));
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

			Date date= new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			System.out.println("Finished at: "+ts + "\n");


		} catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
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

/*
		for (int i = 0; i < currentNotes.size(); i++) {

			if(currentNotes.get(i).getStaff().equals(note.getStaff())){

				currentNotes.get(i).setSignature(note.getSignature());
				System.out.println(">>>>>>>>>>>>>" + note.getVoice().getId());
				System.out.println(">>>>>>>>>>>>>" + note.getVoice().getMeasure());
				System.out.println(">>>>>>>>>>>>>" + note.getVoice().getPart());
				exists = true;
			}
		}

		*/
		
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
}
