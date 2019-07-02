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
import org.apache.log4j.Logger;
import org.junit.Test;

public class SiegesMaerschePianoForte {
	
	private static Logger logger = Logger.getLogger("Sieges-Märsche-Piano-Forte-Test");

	@Test
	public void melodyWithOverwrittenAccidentals() {

		URL url = this.getClass().getResource("/rdf/sieges_maersche_piano-forte.ttl");
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
				"SELECT DISTINCT ?scoreTitle ?movement ?partName ?voiceID ?measure ?staffID ?note3\n" + 
				"WHERE {\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5393365> dc:title ?scoreTitle.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5393365> mo:movement ?movementNode.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5393365> dc:creator ?creatorNode.\n" + 
				"    ?movementNode dc:title ?movemenTitle.\n" + 
				"	?creatorNode foaf:name ?creator.\n" + 
				"	?movementNode mso:hasScorePart ?part.\n" + 
				"	?movementNode dc:title ?movement.\n" + 
				"	?part mso:hasMeasure ?measureNode.\n" + 
				"	?part rdfs:label ?partID.\n" + 
				"	?part dc:description ?partName.\n" + 
				"	?part mso:hasStaff ?staff.    \n" + 
				"	?measureNode rdfs:label ?measure.\n" + 
				"	?voice a mso:Voice.\n" + 
				"	?voice rdfs:label ?voiceID.\n" + 
				"	?measureNode mso:hasNoteSet ?noteset0.\n" + 
				"	?staff mso:hasVoice ?voice.\n" + 
				"	?staff rdfs:label ?staffID.\n" + 
				"\n" + 
				"	?noteset0 mso:hasNote ?note0.       \n" + 
				"	?voice mso:hasNoteSet ?noteset0.\n" + 
				"	?note0 chord:natural note:F.\n" + 
				"    ?note0 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset0 mso:hasDuration ?duration0.\n" + 
				"	?duration0 a mso:Quarter.    \n" + 
				"	?noteset0 mso:nextNoteSet ?noteset1.\n" + 
				"\n" + 
				"	?noteset1 mso:hasNote ?note1.\n" + 
				"	?voice mso:hasNoteSet ?noteset1.\n" + 
				"	?note1 chord:natural note:A.\n" + 
				"    ?note1 chord:modifier chord:flat.\n" + 
				"	?note1 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset1 mso:hasDuration ?duration1.\n" + 
				"	?duration1 a mso:Quarter.    \n" + 
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
				"    ?note3 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset3.\n" + 
				"	?note3 chord:natural note:A.   	\n" + 
				"    ?note3 chord:modifier chord:flat.\n" + 
				"	?noteset3 mso:hasDuration ?duration3.\n" + 
				"	?duration3 a mso:Eighth.\n" + 
				"	?noteset3 mso:nextNoteSet ?noteset4.\n" + 
				"\n" + 
				"    ?noteset4 mso:hasNote ?note4.\n" + 
				"    ?note4 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset4.\n" + 
				"	?note4 chord:natural note:G.   	\n" + 
				"	?noteset4 mso:hasDuration ?duration4.\n" + 
				"	?duration4 a mso:Eighth.\n" + 
				"	?noteset4 mso:nextNoteSet ?noteset5.\n" + 
				"    \n" + 
				"    ?noteset5 mso:hasNote ?note5.\n" + 
				"    ?note5 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset5.\n" + 
				"	?note5 chord:natural note:F.   	\n" + 
				"	?noteset5 mso:hasDuration ?duration5.\n" + 
				"	?duration5 a mso:Eighth.\n" + 
				"	?noteset5 mso:nextNoteSet ?noteset6.\n" + 
				"    \n" + 
				"    ?noteset6 mso:hasNote ?note6.\n" + 
				"    ?note6 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset6.\n" + 
				"	?note6 chord:natural note:F.   	\n" + 
				"	?noteset6 mso:hasDuration ?duration6.\n" + 
				"	?duration6 a mso:Eighth.\n" + 
				"	?noteset6 mso:nextNoteSet ?noteset7.\n" + 
				"    \n" + 
				"    ?noteset7 mso:hasNote ?note7.\n" + 
				"    ?note7 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset7.\n" + 
				"	?note7 chord:natural note:E.   	\n" + 
				"    ?noteset7 mso:hasDuration ?duration7.\n" + 
				"	?duration7 a mso:Eighth.\n" + 
				"	?noteset7 mso:nextNoteSet ?noteset8.\n" + 
				"    \n" + 
				"    ?noteset8 mso:hasNote ?note8.\n" + 
				"    ?note8 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset8.\n" + 
				"	?note8 chord:natural note:D.   	\n" + 
				"    ?noteset8 mso:hasDuration ?duration8.\n" + 
				"	?duration8 a mso:Eighth.\n" + 
				"    ?noteset8 mso:nextNoteSet ?noteset9.\n" + 
				"    \n" + 
				"    ?noteset9 mso:hasNote ?note9.\n" + 
				"    ?note9 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset9.\n" + 
				"	?note9 chord:natural note:C.   	\n" + 
				"    ?noteset9 mso:hasDuration ?duration9.\n" + 
				"	?duration9 a mso:Eighth.\n" + 
				"    ?noteset9 mso:nextNoteSet ?noteset10.\n" + 
				"    \n" + 
				"    ?noteset10 mso:hasNote ?note10.\n" + 
				"    ?note10 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset10.\n" + 
				"	?note10 chord:natural note:B.   	\n" + 
				"    ?noteset10 mso:hasDuration ?duration10.    \n" + 
				"	?duration10 a mso:Eighth.\n" + 
				"    ?noteset10 mso:nextNoteSet ?noteset11.\n" + 
				"    \n" + 
				"    ?noteset11 mso:hasNote ?note11.\n" + 
				"    ?note11 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset11.\n" + 
				"	?note11 chord:natural note:C.   	\n" + 
				"    ?noteset11 mso:hasDuration ?duration11.    \n" + 
				"	?duration11 a mso:Eighth.\n" + 
				"    ?noteset11 mso:nextNoteSet ?noteset12.\n" + 
				"\n" + 
				"    ?noteset12 mso:hasNote ?note12.\n" + 
				"    ?note11 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset12.\n" + 
				"	?note12 chord:natural note:D.   	\n" + 
				"    ?noteset12 mso:hasDuration ?duration12.    \n" + 
				"	?duration12 a mso:Eighth.\n" + 
				"    ?noteset12 mso:nextNoteSet ?noteset13.\n" + 
				"\n" + 
				"    ?noteset13 mso:hasNote ?note13.\n" + 
				"    ?note11 mso:hasOctave \"5\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset13.\n" + 
				"	?note13 chord:natural note:E.   	\n" + 
				"    ?noteset13 mso:hasDuration ?duration13.    \n" + 
				"	?duration13 a mso:Eighth.\n" + 
				"   \n" + 
				"    FILTER ( NOT EXISTS {?note0 chord:modifier ?m0} )\n" + 
				"    FILTER ( NOT EXISTS {?note2 chord:modifier ?m2} )\n" + 
				"    FILTER ( NOT EXISTS {?note4 chord:modifier ?m4} )\n" + 
				"    FILTER ( NOT EXISTS {?note5 chord:modifier ?m5} )\n" + 
				"    FILTER ( NOT EXISTS {?note6 chord:modifier ?m6} )\n" + 
				"    FILTER ( NOT EXISTS {?note7 chord:modifier ?m7} )\n" + 
				"    FILTER ( NOT EXISTS {?note8 chord:modifier ?m8} )\n" + 
				"    FILTER ( NOT EXISTS {?note9 chord:modifier ?m9} )\n" + 
				"    FILTER ( NOT EXISTS {?note10 chord:modifier ?m10} )\n" + 
				"    FILTER ( NOT EXISTS {?note11 chord:modifier ?m11} )\n" + 
				"    FILTER ( NOT EXISTS {?note12 chord:modifier ?m12} )\n" + 
				"    FILTER ( NOT EXISTS {?note13 chord:modifier ?m13} )\n" +  
				"}\n"; 
			
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
				
				logger.info("Melody F, Ab (Quarter note) C, Ab, G, F, F, E, D, C, B, C, D, E (eighth note) : \n"
						+ "  Measure: "+soln.get("?measure") + "\n"
						+ "  Movement: "+soln.get("?movement") + "\n"
						+ "  Staff: "+soln.get("?staffID") + "\n"
						+ "  Part: "+soln.get("?partName") + "\n"
						+ "  Title: "+soln.get("?scoreTitle") + "\n"
						+ "  Voice: "+soln.get("?voiceID") + "\n"
						);
				
				if(measure.equals("20")&&
						movement.equals("Marsch Nº1.")&&
						staff.equals("1")&&
						part.equals("Piano")&&
						title.equals("Sieges Mærsche für's Piano-Forte : gewidmet den Witwen und Waisen der Landwehr-mäner des k.k. Hoch und Deutschmeister Regiments")&&
						voice.equals("1")) {
					result = true;
				}				

			}
		}

		assertEquals(result, true);			
	}

}
