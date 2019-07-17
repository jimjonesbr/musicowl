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

public class AchtzehnLiederChor {

	private static Logger logger = Logger.getLogger("Achtzehn_Lieder_Gemischten_Chor-Test");

			
	@Test
	public void melody() {

		URL url = this.getClass().getResource("/rdf/achtzehn_neue_lieder_gemischten-chor.ttl");
		File file = new File(url.getFile());

		Model modelElgar = ModelFactory.createDefaultModel();
		modelElgar.read(file.getAbsolutePath(),"N-TRIPLES");
		String sparql = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
				"PREFIX chord: <http://purl.org/ontology/chord/>\n" + 
				"PREFIX note: <http://purl.org/ontology/chord/note/>\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
				"PREFIX dbo: <http://dbpedia.org/property/> \n"+
				"SELECT DISTINCT ?scoreNode ?scoreTitle ?movement ?partName ?voice ?measure ?staffID ?voiceID\n" + 
				"WHERE {\n" + 
				"\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/2398460> dc:title ?scoreTitle.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/2398460> mo:movement ?movementNode.\n" + 
				"	?movementNode dc:title ?movemenTitle.\n" + 
				"	<https://sammlungen.ulb.uni-muenster.de/id/2398460> dc:creator ?creatorNode.\n" + 
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
				"	?note0 chord:natural note:G.\n" + 
				"    ?note0 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset0 mso:hasDuration ?duration0.\n" + 
				"	?duration0 a mso:16th.    \n" + 
				"	?noteset0 mso:nextNoteSet ?noteset1.\n" + 
				"\n" + 
				"	?noteset1 mso:hasNote ?note1.\n" + 
				"	?voice mso:hasNoteSet ?noteset1.\n" + 
				"	?note1 chord:natural note:F.\n" + 
				"    ?note1 chord:modifier chord:sharp.\n" + 
				"	?note1 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?noteset1 mso:hasDuration ?duration1.\n" + 
				"	?duration1 a mso:16th.    \n" + 
				"	?noteset1 mso:nextNoteSet ?noteset2.\n" + 
				"\n" + 
				"	?noteset2 mso:hasNote ?note2.\n" + 
				"	?voice mso:hasNoteSet ?noteset2.\n" + 
				"	?note2 chord:natural note:E.\n" + 
				"    ?note2 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .	\n" + 
				"	?noteset2 mso:hasDuration ?duration2.\n" + 
				"	?duration2 a mso:Eighth.\n" + 
				"	?noteset2 mso:nextNoteSet ?noteset3.\n" + 
				"\n" + 
				"	?noteset3 mso:hasNote ?note3.\n" + 
				"    ?note3 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"	?voice mso:hasNoteSet ?noteset3.\n" + 
				"	?note3 chord:natural note:D.   	\n" + 
				"	?noteset3 mso:hasDuration ?duration3.\n" + 
				"	?duration3 a mso:Eighth.\n" + 
				"	?noteset3 mso:nextNoteSet ?noteset4.\n" + 
				"\n" + 
				"\n" + 
				"    FILTER ( NOT EXISTS {?note0 chord:modifier ?m0} )\n" + 
				"    FILTER ( NOT EXISTS {?note2 chord:modifier ?m2} )\n" + 
				"    FILTER ( NOT EXISTS {?note3 chord:modifier ?m3} )\n" + 
				"}"; 
					
		boolean result = false;
		String measure = "";
		String movement = "";
		String staff = "";
		String part = "";
		String title = "";
		String voice = "";
		
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
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
				
				logger.info("Simple melody: \n"
						+ "  Measure: "+soln.get("?measure") + "\n"
						+ "  Movement: "+soln.get("?movement") + "\n"
						+ "  Staff: "+soln.get("?staffID") + "\n"
						+ "  Part: "+soln.get("?partName") + "\n"
						+ "  Title: "+soln.get("?scoreTitle") + "\n"
						+ "  Voice: "+soln.get("?voiceID") + "\n"
						);
			}
		}

		if(measure.equals("6")&&
		   movement.equals("17. Herbstlied.")&&
		   staff.equals("1")&&
		   part.equals("Bass")&&
		   title.equals("Achtzehn neue Lieder für Gemischten Chor")&&
		   voice.equals("2")) {
			result = true;
		}
		
		assertEquals(result, true);			
	}

}
