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

public class Ariettes {


	//private static Logger logger = Logger.getLogger("Ariettes-Test");
	private static Logger logger = LogManager.getLogger(Ariettes.class);
	@Test
	public void melodyWithRests() {

		boolean result = true;
		
		//URL url = this.getClass().getResource("/rdf/ariettes_paroles_italienes_et_allemandes.nt");
		URL url = this.getClass().getResource("/rdf/ariettes_paroles_italienes_et_allemandes.ttl");
		
		
		File file = new File(url.getFile());

		Model modelElgar = ModelFactory.createDefaultModel();
		modelElgar.read(file.getAbsolutePath(),"N-TRIPLES");
		String sparql = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
				"PREFIX chord: <http://purl.org/ontology/chord/>\n" + 
				"PREFIX note: <http://purl.org/ontology/chord/note/>\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
				"PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
				"SELECT DISTINCT ?scoreNode ?scoreTitle ?movement ?partName ?voice ?measure ?staffID\n" + 
				"WHERE {\n" + 
				"\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5109620> dc:title ?scoreTitle.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5109620> mo:movement ?movementNode.\n" + 
				"	?movementNode dc:title ?movemenTitle.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/5109620> dc:creator ?creatorNode.\n" + 
				"	?creatorNode foaf:name ?creator.\n" + 
				"	?movementNode mso:hasScorePart ?part.\n" + 
				"	?movementNode dc:title ?movement.\n" + 
				"	?part mso:hasMeasure ?measureNode.\n" + 
				"	?part rdfs:label ?partID.\n" + 
				"	?part dc:description ?partName.\n" + 
				"	?part mso:hasStaff ?staff.    \n" + 
				"	?measureNode rdfs:ID ?measure.\n" + 
				"	?voice a mso:Voice.\n" + 
				"	?voice rdfs:ID ?voiceID.\n" + 
				"	?measureNode mso:hasNoteSet ?noteset0.\n" + 
				"	?staff mso:hasVoice ?voice.\n" + 
				"	?staff rdfs:ID ?staffID.\n" + 
				"\n" + 
				"	?noteset0 mso:hasNote ?note0.       \n" + 
				"	?voice mso:hasNoteSet ?noteset0.\n" + 
				"	?note0 chord:natural note:G.\n" + 
				"    ?note0 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset0 mso:hasDuration ?duration0.\n" + 
				"	?duration0 a mso:Quarter.    \n" + 
				"	?noteset0 mso:nextNoteSet ?noteset1.\n" + 
				"\n" + 
				"	?noteset1 mso:hasNote ?note1.\n" + 
				"	?voice mso:hasNoteSet ?noteset1.\n" + 
				"	?note1 chord:natural note:B.\n" + 
				"	?note1 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset1 mso:hasDuration ?duration1.\n" + 
				"	?duration1 a mso:Quarter.    \n" + 
				"	?noteset1 mso:nextNoteSet ?noteset2.\n" + 
				"\n" + 
				"	?noteset2 mso:hasNote ?note2.\n" + 
				"	?voice mso:hasNoteSet ?noteset2.\n" + 
				"	?note2 chord:natural note:C.\n" + 
				"    ?note2 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset2 mso:hasDuration ?duration2.\n" + 
				"	?duration2 a mso:Quarter.\n" + 
				"	?noteset2 mso:nextNoteSet ?noteset3.\n" + 
				"\n" + 
				"	?noteset3 mso:hasNote ?note3.\n" + 
				"    ?note3 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset3.\n" + 
				"	?note3 chord:natural note:D.   	\n" + 
				"	?noteset3 mso:hasDuration ?duration3.\n" + 
				"	?duration3 a mso:Quarter.\n" + 
				"	?noteset3 mso:nextNoteSet ?noteset4.\n" + 
				"\n" + 
				"\n" + 
				"	?noteset4 mso:hasNote ?note4.\n" + 
				"	?voice mso:hasNoteSet ?noteset4.\n" + 
				"	?note4 chord:natural note:D.\n" + 
				"    ?note4 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset4 mso:hasDuration ?duration4.\n" + 
				"	?duration4 a mso:Quarter.\n" + 
				"	?noteset4 mso:nextNoteSet ?noteset5.    \n" + 
				"    \n" + 
				"    ?noteset5 mso:hasNote ?note5.\n" + 
				"	?voice mso:hasNoteSet ?noteset5.\n" + 
				"	?note5 chord:natural note:Rest.    \n" + 
				"	?noteset5 mso:hasDuration ?duration5.\n" + 
				"	?duration5 a mso:Quarter.\n" + 
				"	?noteset5 mso:nextNoteSet ?noteset6.   \n" + 
				"    \n" + 
				"    ?noteset6 mso:hasNote ?note6.\n" + 
				"	?voice mso:hasNoteSet ?noteset6.\n" + 
				"	?note6 chord:natural note:G.\n" + 
				"    ?note6 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset6 mso:hasDuration ?duration6.\n" + 
				"	?duration6 a mso:Quarter.\n" + 
				"	?noteset6 mso:nextNoteSet ?noteset7.  \n" + 
				"    \n" + 
				"    ?noteset7 mso:hasNote ?note7.\n" + 
				"	?voice mso:hasNoteSet ?noteset7.\n" + 
				"	?note7 chord:natural note:G.\n" + 
				"    ?note7 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset7 mso:hasDuration ?duration7.\n" + 
				"	?duration7 a mso:Quarter.\n" + 
				"	?noteset7 mso:nextNoteSet ?noteset8.  \n" + 
				"    \n" + 
				"    ?noteset8 mso:hasNote ?note8.\n" + 
				"	?voice mso:hasNoteSet ?noteset8.\n" + 
				"	?note8 chord:natural note:G.\n" + 
				"    ?note8 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset8 mso:hasDuration ?duration8.\n" + 
				"	?duration8 a mso:Quarter.\n" + 
				"	?noteset8 mso:nextNoteSet ?noteset9.  \n" + 
				"    \n" + 
				"    ?noteset9 mso:hasNote ?note9.\n" + 
				"	?voice mso:hasNoteSet ?noteset9.\n" + 
				"	?note9 chord:natural note:A.\n" + 
				"    ?note9 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset9 mso:hasDuration ?duration9.\n" + 
				"	?duration9 a mso:Quarter.\n" + 
				"	?noteset9 mso:nextNoteSet ?noteset10.  \n" + 
				"    \n" + 
				"    ?noteset10 mso:hasNote ?note10.\n" + 
				"	?voice mso:hasNoteSet ?noteset10.\n" + 
				"	?note10 chord:natural note:B.\n" + 
				"    ?note10 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset10 mso:hasDuration ?duration10.\n" + 
				"	?duration10 a mso:Quarter.\n" + 
				"	?noteset10 mso:nextNoteSet ?noteset11.  \n" + 
				"    \n" + 
				"    ?noteset11 mso:hasNote ?note11.\n" + 
				"	?voice mso:hasNoteSet ?noteset11.\n" + 
				"	?note11 chord:natural note:Rest.    \n" + 
				"	?noteset11 mso:hasDuration ?duration11.\n" + 
				"	?duration11 a mso:Quarter.\n" + 
				"	?noteset11 mso:nextNoteSet ?noteset12.\n" + 
				"    \n" + 
				"	 FILTER ( NOT EXISTS {?note0 chord:modifier ?m0} )\n" + 
				"    FILTER ( NOT EXISTS {?note1 chord:modifier ?m1} )\n" + 
				"    FILTER ( NOT EXISTS {?note2 chord:modifier ?m2} )\n" + 
				"    FILTER ( NOT EXISTS {?note3 chord:modifier ?m3} )\n" + 
				"    FILTER ( NOT EXISTS {?note4 chord:modifier ?m4} )\n" + 
				"	 FILTER ( NOT EXISTS {?note6 chord:modifier ?m6} )\n" + 
				"    FILTER ( NOT EXISTS {?note7 chord:modifier ?m7} )\n" + 
				"    FILTER ( NOT EXISTS {?note8 chord:modifier ?m8} )\n" + 
				"    FILTER ( NOT EXISTS {?note9 chord:modifier ?m9} )\n" + 
				"    FILTER ( NOT EXISTS {?note10 chord:modifier ?m10} )\n" + 
				"}"; 
			
			try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
				ResultSet results = qexec.execSelect() ;
				for ( ; results.hasNext() ; )
				{
					QuerySolution soln = results.nextSolution() ;
					if(!soln.get("?measure").toString().equals("3") && !soln.get("?measure").toString().equals("29")) {
						result = false;
					} else {
						logger.info("Melody containing rests in multiple durations: Measure " + soln.get("?measure"));
					}				

				}
			}
			
		assertEquals(result, true);			
	}


}
