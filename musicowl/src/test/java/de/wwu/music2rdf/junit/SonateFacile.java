package de.wwu.music2rdf.junit;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URL;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;

public class SonateFacile {

	//private static Logger logger = Logger.getLogger("SonateFacile-Test");
	private static Logger logger = LogManager.getLogger(SonateFacile.class);
	
	@Test
	public void melodyChangingKeysWithNatural() {
		URL url = this.getClass().getResource("/rdf/sonate_facile_piano-forte.ttl");
		File file = new File(url.getFile());

		Model model = ModelFactory.createDefaultModel();
		model.read(file.getAbsolutePath(),"N-TRIPLES");
		String sparql = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
				"PREFIX chord: <http://purl.org/ontology/chord/>\n" + 
				"PREFIX note: <http://purl.org/ontology/chord/note/>\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/property/> \n"+
				"SELECT DISTINCT ?scoreTitle ?movement ?partName ?voiceID ?measure ?staffID ?note3\n" + 
				"WHERE {\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5109291> dc:title ?scoreTitle.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5109291> mo:movement ?movementNode.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5109291> dc:creator ?creatorNode.\n" + 
				"    ?movementNode dc:title ?movemenTitle.\n" + 
				"	?creatorNode foaf:name ?creator.\n" + 
				"	?movementNode mso:hasScorePart ?part.\n" + 
				"	?movementNode dc:title ?movement.\n" + 
				"	?part mso:hasMeasure ?measureNode.\n" + 
				"	?part rdfs:label ?partID.\n" + 
				"	?part dc:description ?partName.\n" + 
				"	?part mso:hasStaff ?staff.    \n" + 
				"	?measureNode dbo:order ?measure.\n" + 
				"	?voice a mso:Voice.\n" + 
				"	?voice rdfs:label ?voiceID.\n" + 
				"	?measureNode mso:hasNoteSet ?noteset0.\n" + 
				"	?staff mso:hasVoice ?voice.\n" + 
				"	?staff rdfs:label ?staffID.\n" + 
				"\n" + 
				"	?noteset0 mso:hasNote ?note0.       \n" + 
				"	?voice mso:hasNoteSet ?noteset0.\n" + 
				"	?note0 chord:natural note:C.\n" + 
				"    ?note0 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset0 mso:hasDuration ?duration0.\n" + 
				"	?duration0 a mso:Eighth.    \n" + 
				"	?noteset0 mso:nextNoteSet ?noteset1.\n" + 
				"\n" + 
				"	?noteset1 mso:hasNote ?note1.\n" + 
				"	?voice mso:hasNoteSet ?noteset1.\n" + 
				"	?note1 chord:natural note:B.\n" + 
				"	?note1 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset1 mso:hasDuration ?duration1.\n" + 
				"	?duration1 a mso:Eighth.    \n" + 
				"	?noteset1 mso:nextNoteSet ?noteset2.\n" + 
				"\n" + 
				"	?noteset2 mso:hasNote ?note2.\n" + 
				"	?voice mso:hasNoteSet ?noteset2.\n" + 
				"	?note2 chord:natural note:C.\n" + 
				"    ?note2 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset2 mso:hasDuration ?duration2.\n" + 
				"	?duration2 a mso:Eighth.\n" + 
				"	?noteset2 mso:nextNoteSet ?noteset3.\n" + 
				"\n" + 
				"	?noteset3 mso:hasNote ?note3.\n" + 
				"    ?note3 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset3.\n" + 
				"	?note3 chord:natural note:B.   	\n" + 
				"	?noteset3 mso:hasDuration ?duration3.\n" + 
				"	?duration3 a mso:Eighth.\n" + 
				"	?noteset3 mso:nextNoteSet ?noteset4.\n" + 
				"\n" + 
				"    ?noteset4 mso:hasNote ?note4.\n" + 
				"    ?note4 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset4.\n" + 
				"	?note4 chord:natural note:C.   	\n" + 
				"	?noteset4 mso:hasDuration ?duration4.\n" + 
				"	?duration4 a mso:Eighth.\n" + 
				"	?noteset4 mso:nextNoteSet ?noteset5.\n" + 
				"    \n" + 
				"    ?noteset5 mso:hasNote ?note5.\n" + 
				"    ?note5 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset5.\n" + 
				"	?note5 chord:natural note:B.   	\n" + 
				"	?noteset5 mso:hasDuration ?duration5.\n" + 
				"	?duration5 a mso:Eighth.\n" + 
				"	?noteset5 mso:nextNoteSet ?noteset6.\n" + 
				"    \n" + 
				"    ?noteset6 mso:hasNote ?note6.\n" + 
				"    ?note6 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset6.\n" + 
				"	?note6 chord:natural note:C.   	\n" + 
				"	?noteset6 mso:hasDuration ?duration6.\n" + 
				"	?duration6 a mso:Eighth.\n" + 
				"	?noteset6 mso:nextNoteSet ?noteset7.\n" + 
				"    \n" + 
				"    ?noteset7 mso:hasNote ?note7.\n" + 
				"    ?note7 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset7.\n" + 
				"	?note7 chord:natural note:B.   	\n" + 
				"    ?noteset7 mso:hasDuration ?duration7.\n" + 
				"	?duration7 a mso:Eighth.\n" + 
				"    \n" + 
				"    FILTER ( NOT EXISTS {?note0 chord:modifier ?m0} )\n" + 
				"    FILTER ( NOT EXISTS {?note1 chord:modifier ?m1} )\n" + 
				"    FILTER ( NOT EXISTS {?note2 chord:modifier ?m2} )\n" + 
				"    FILTER ( NOT EXISTS {?note3 chord:modifier ?m3} )\n" + 
				"    FILTER ( NOT EXISTS {?note4 chord:modifier ?m4} )\n" + 
				"    FILTER ( NOT EXISTS {?note5 chord:modifier ?m5} )\n" + 
				"    FILTER ( NOT EXISTS {?note6 chord:modifier ?m6} )\n" + 
				"    FILTER ( NOT EXISTS {?note7 chord:modifier ?m7} )\n" + 
				"}"; 
		
				
		boolean result = false;
		String measure = "";
		String movement = "";
		String staff = "";
		String part = "";
		String title = "";
		String voice = "";
		
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, model)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{											
				QuerySolution soln = results.nextSolution() ;
				measure = soln.get("?measure").toString();
				movement = soln.get("?movement").toString();
				staff = soln.get("?staffID").toString();
				part= soln.get("?partName").toString();
				title = soln.get("?scoreTitle").toString();
				voice = soln.get("?voiceID").toString();
				
				logger.info("Melody 64th duration notes and double dots: \n"
						+ "  Measure: "+soln.get("?measure") + "\n"
						+ "  Movement: "+soln.get("?movement") + "\n"
						+ "  Staff: "+soln.get("?staffID") + "\n"
						+ "  Part: "+soln.get("?partName") + "\n"
						+ "  Title: "+soln.get("?scoreTitle") + "\n"
						+ "  Voice: "+soln.get("?voiceID") + "\n"
						);
				
				if(measure.equals("67")&&
						movement.equals("Allegro non tanto.")&&
						staff.equals("2")&&
						part.equals("Piano")&&
						title.equals("Sonate facile pour Piano-Forte")&&
						voice.equals("3")) {
					result = true;
				}				

			}
		}

		assertEquals(result, true);			
	}

}
