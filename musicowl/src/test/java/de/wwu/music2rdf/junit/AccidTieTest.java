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

public class AccidTieTest {

	private static Logger logger = LogManager.getLogger(AccidTieTest.class);
	
	@Test
	public void accidentalChangingWithTies() {
		URL url = this.getClass().getResource("/rdf/Accid_Tie_Test.ttl");
		File file = new File(url.getFile());

		Model model = ModelFactory.createDefaultModel();
		model.read(file.getAbsolutePath(),"N-TRIPLES");
		String sparql = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n"
				+ "PREFIX chord: <http://purl.org/ontology/chord/>\n"
				+ "PREFIX note: <http://purl.org/ontology/chord/note/>\n"
				+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX mo: <http://purl.org/ontology/mo/>\n"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
				+ "PREFIX dbo: <http://dbpedia.org/property/> \n"
				+ "SELECT DISTINCT ?scoreTitle ?movement ?partName ?voiceID ?measure ?staffID ?note3\n"
				+ "WHERE {\n"
				+ "	<https://test.com/masthom> dc:title ?scoreTitle.\n"
				+ "	<https://test.com/masthom> mo:movement ?movementNode.\n"
				+ "	<https://test.com/masthom> dc:creator ?creatorNode.\n"
				+ "   ?movementNode dc:title ?movemenTitle.\n"
				+ "	?creatorNode foaf:name ?creator.\n"
				+ "	?movementNode mso:hasScorePart ?part.\n"
				+ "	?movementNode dc:title ?movement.\n"
				+ "	?part mso:hasMeasure ?measureNode.\n"
				+ "	?part rdfs:label ?partID.\n"
				+ "	?part dc:description ?partName.\n"
				+ "	?part mso:hasStaff ?staff.    \n"
				+ "	?measureNode dbo:order ?measure.\n"
				+ "	?voice a mso:Voice.\n"
				+ "	?voice rdfs:label ?voiceID.\n"
				+ "	?measureNode mso:hasNoteSet ?noteset0.\n"
				+ "	?staff mso:hasVoice ?voice.\n"
				+ "	?staff rdfs:label ?staffID.\n"
				+ "\n"
				+ "	?noteset0 mso:hasNote ?note0.       \n"
				+ "	?voice mso:hasNoteSet ?noteset0.\n"
				+ "	?note0 chord:natural note:F.\n"
				+ "	?note0 chord:modifier chord:sharp. \n"
				+ "   ?note0 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "	?noteset0 mso:hasDuration ?duration0.\n"
				+ "	?duration0 a mso:Half.    \n"
				+ "	?noteset0 mso:nextNoteSet ?noteset1.\n"
				+ "\n"
				+ "	?noteset1 mso:hasNote ?note1.\n"
				+ "	?voice mso:hasNoteSet ?noteset1.\n"
				+ "	?note1 chord:natural note:F.\n"
				+ "	?note1 chord:modifier chord:sharp. \n"
				+ "	?note1 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "	?noteset1 mso:hasDuration ?duration1.\n"
				+ "	?duration1 a mso:Quarter.    \n"
				+ "	?noteset1 mso:nextNoteSet ?noteset2.\n"
				+ "\n"
				+ "	?noteset2 mso:hasNote ?note2.\n"
				+ "	?voice mso:hasNoteSet ?noteset2.\n"
				+ "	?note2 chord:natural note:F.\n"
				+ "	?note2 chord:modifier chord:sharp.\n"
				+ "   ?note2 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n"
				+ "	?noteset2 mso:hasDuration ?duration2.\n"
				+ "	?duration2 a mso:Quarter.\n"
				+ "	?noteset2 mso:nextNoteSet ?noteset3.\n"
				+ "\n"
				+ "	?noteset3 mso:hasNote ?note3.\n"
				+ "   ?note3 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "	?voice mso:hasNoteSet ?noteset3.\n"
				+ "	?note3 chord:natural note:F.   	\n"
				+ "	?note3 chord:modifier chord:sharp. \n"
				+ "	?noteset3 mso:hasDuration ?duration3.\n"
				+ "	?duration3 a mso:Quarter.\n"
				+ "	?noteset3 mso:nextNoteSet ?noteset4.\n"
				+ "\n"
				+ "    ?noteset4 mso:hasNote ?note4.\n"
				+ "   ?note4 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "	?voice mso:hasNoteSet ?noteset4.\n"
				+ "	?note4 chord:natural note:F.   	\n"
				+ "	?noteset4 mso:hasDuration ?duration4.\n"
				+ "	?duration4 a mso:Quarter.\n"
				+ "	?noteset4 mso:nextNoteSet ?noteset5.\n"
				+ "    \n"
				+ "   ?noteset5 mso:hasNote ?note5.\n"
				+ "   ?note5 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset5.\n"
				+ "   ?note5 chord:natural note:F.   	\n"
				+ "   ?noteset5 mso:hasDuration ?duration5.\n"
				+ "   ?duration5 a mso:Quarter.\n"
				+ "   ?noteset5 mso:nextNoteSet ?noteset6.\n"
				+ "    \n"
				+ "   ?noteset6 mso:hasNote ?note6.\n"
				+ "   ?note6 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset6.\n"
				+ "   ?note6 chord:natural note:F.   	\n"
				+ "   ?noteset6 mso:hasDuration ?duration6.\n"
				+ "   ?duration6 a mso:Quarter.\n"
				+ "   ?noteset6 mso:nextNoteSet ?noteset7.\n"
				+ "\n"
				+ "   ?noteset7 mso:hasNote ?note7.\n"
				+ "   ?note7 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset7.\n"
				+ "   ?note7 chord:natural note:F.   	\n"
				+ "   ?noteset7 mso:hasDuration ?duration7.\n"
				+ "   ?duration7 a mso:Quarter.\n"
				+ "   ?noteset7 mso:nextNoteSet ?noteset8.    \n"
				+ "\n"
				+ "   ?noteset8 mso:hasNote ?note8.\n"
				+ "   ?note8 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset8.\n"
				+ "   ?note8 chord:natural note:F.   	\n"
				+ "   ?note8 chord:modifier chord:flat.\n"
				+ "   ?noteset8 mso:hasDuration ?duration8.\n"
				+ "   ?duration8 a mso:Quarter.\n"
				+ "   ?noteset8 mso:nextNoteSet ?noteset9.   \n"
				+ "\n"
				+ "   ?noteset9 mso:hasNote ?note9.\n"
				+ "   ?note9 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset9.\n"
				+ "   ?note9 chord:natural note:F.   	\n"
				+ "   ?note9 chord:modifier chord:flat.\n"
				+ "   ?noteset9 mso:hasDuration ?duration9.\n"
				+ "   ?duration9 a mso:Quarter.\n"
				+ "   ?noteset9 mso:nextNoteSet ?noteset10.   \n"
				+ "\n"
				+ "   ?noteset10 mso:hasNote ?note10.\n"
				+ "   ?note10 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset10.\n"
				+ "   ?note10 chord:natural note:F.   	\n"
				+ "   ?note10 chord:modifier chord:flat.\n"
				+ "   ?noteset10 mso:hasDuration ?duration10.\n"
				+ "   ?duration10 a mso:Quarter.\n"
				+ "   ?noteset10 mso:nextNoteSet ?noteset11.   \n"
				+ "    \n"
				+ "   ?noteset11 mso:hasNote ?note11.\n"
				+ "   ?note11 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset11.\n"
				+ "   ?note11 chord:natural note:F.   	\n"
				+ "   ?note11 chord:modifier chord:flat.\n"
				+ "   ?noteset11 mso:hasDuration ?duration11.\n"
				+ "   ?duration11 a mso:Quarter.\n"
				+ "   ?noteset11 mso:nextNoteSet ?noteset12.       \n"
				+ "    \n"
				+ "   ?noteset12 mso:hasNote ?note12.\n"
				+ "   ?note12 mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n"
				+ "   ?voice mso:hasNoteSet ?noteset12.\n"
				+ "   ?note12 chord:natural note:F.   	   \n"
				+ "   ?noteset12 mso:hasDuration ?duration12.\n"
				+ "   ?duration12 a mso:Quarter.\n"
				+ "   ?noteset12 mso:nextNoteSet ?noteset13.   \n"
				+ "    \n"
				+ "    \n"
				+ "   FILTER ( NOT EXISTS {?note4 chord:modifier ?m4} )\n"
				+ "   FILTER ( NOT EXISTS {?note5 chord:modifier ?m5} )\n"
				+ "   FILTER ( NOT EXISTS {?note6 chord:modifier ?m6} )\n"
				+ "   FILTER ( NOT EXISTS {?note7 chord:modifier ?m7} )\n"
				+ "   FILTER ( NOT EXISTS {?note12 chord:modifier ?m12} )\n"
				+ "}"; 
		
				logger.debug(sparql);
				
		boolean result = false;
		String measure = "";
		
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, model)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{											
				QuerySolution soln = results.nextSolution() ;
				measure = soln.get("?measure").toString();
				
				logger.info("Accidental Change in multiple measures (Reported by masthon): \n"
						+ "  Measure: "+soln.get("?measure") + "\n"
						+ "  Movement: "+soln.get("?movement") + "\n"
						+ "  Staff: "+soln.get("?staffID") + "\n"
						+ "  Part: "+soln.get("?partName") + "\n"
						+ "  Title: "+soln.get("?scoreTitle") + "\n"
						+ "  Voice: "+soln.get("?voiceID") + "\n"
						);
				
				if(measure.equals("1")) {
					result = true;
				}				

			}
		}

		assertEquals(result, true);			
	}

}
