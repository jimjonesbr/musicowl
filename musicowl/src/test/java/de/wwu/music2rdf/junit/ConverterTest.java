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
import org.junit.Test;

public class ConverterTest {
	
	@Test
	public void elgarChordSequence() {

		String result = "";		
        URL url = this.getClass().getResource("/rdf/elgar_cello_concerto_op.85.nt");
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
				"SELECT DISTINCT ?scoreTitle ?measure\n" + 
				"WHERE {\n" + 
				"\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:title ?scoreTitle.\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> mo:movement ?movementNode.\n" + 
				"?movementNode mso:hasScorePart ?part.\n" + 
				"?part mso:hasMeasure ?measureNode.\n" + 
				"?part rdfs:ID ?partID.\n" + 
				"?part dc:description ?partName.\n" + 
				"?part mso:hasStaff ?staff.\n" + 
				"?measureNode rdfs:ID ?measure.\n" + 
				"?voice a mso:Voice.\n" + 
				"?voice rdfs:ID ?voiceID.\n" + 
				"?measureNode mso:hasNoteSet ?chord_1.\n" + 
				"?staff mso:hasVoice ?voice.\n" + 
				"?staff rdfs:ID ?staffID.\n" + 
				"\n" + 
				"?voice mso:hasNoteSet ?chord_1.\n" + 
				"?chord_1 mso:hasNote ?note_chord1_B.\n" + 
				"?note_chord1_B chord:natural note:B.\n" + 
				"?note_chord1_B mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?chord_1 mso:hasNote ?note_chord1_E.\n" + 
				"?note_chord1_E chord:natural note:E.\n" + 
				"?note_chord1_E mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?chord_1 mso:hasNote ?note_chord1_G.\n" + 
				"?note_chord1_G chord:natural note:G.\n" + 
				"?note_chord1_G mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .    \n" + 
				"?chord_1 mso:hasDynamic ?dynamic .\n" + 
				"?dynamic a mso:ff   .\n" + 
				"?chord_1 mso:hasDuration ?duration_chord1.\n" + 
				"?duration_chord1 a mso:Half.\n" + 
				"        \n" + 
				"?chord_1 mso:nextNoteSet ?chord_2.\n" + 
				"    \n" + 
				"?chord_2 mso:hasNote ?note_chord2_E.\n" + 
				"?note_chord2_E chord:natural note:E.\n" + 
				"?note_chord2_E mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int>  .  \n" + 
				"?chord_2 mso:hasNote ?note_chord2_G .\n" + 
				"?note_chord2_G chord:natural note:G .\n" + 
				"?note_chord2_G mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?chord_2 mso:hasNote ?note_chord2_B .\n" + 
				"?note_chord2_B chord:natural note:B .\n" + 
				"?note_chord2_B mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?chord_2 mso:hasNote ?note_chord2_E2 .\n" + 
				"?note_chord2_E2 chord:natural note:E .\n" + 
				"?note_chord2_E2 mso:hasOctave \"2\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"    \n" + 
				"?chord_2 mso:hasDuration ?duration_chord2.\n" + 
				"?duration_chord2 a mso:Half.\n" + 
				"    \n" + 
				"FILTER ( NOT EXISTS {?note_chord1_B chord:modifier ?m1} )\n" + 
				"FILTER ( NOT EXISTS {?note_chord1_E chord:modifier ?m2} )\n" + 
				"FILTER ( NOT EXISTS {?note_chord1_G chord:modifier ?m3} )\n" + 
				"FILTER ( NOT EXISTS {?note_chord2_E chord:modifier ?m4} )\n" + 
				"FILTER ( NOT EXISTS {?note_chord2_G chord:modifier ?m5} )\n" + 
				"FILTER ( NOT EXISTS {?note_chord2_B chord:modifier ?m6} )\n" + 
				"FILTER ( NOT EXISTS {?note_chord2_E2 chord:modifier ?m7} )\n" + 
				"}";
		
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.get("?measure").toString();
				System.out.println("Elgar Chord Sequence: Measure "+soln.get("?measure"));
			}
		}
		
		assertEquals(result, "1");

	}
	
	
	@Test
	public void elgarSimpleMelody() {

		String result = "";		
        URL url = this.getClass().getResource("/rdf/elgar_cello_concerto_op.85.nt");
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
				"SELECT DISTINCT ?scoreNode ?scoreTitle ?movement ?partName ?voice ?measure\n" + 
				"WHERE {\n" + 
				"\n" + 
				"?scoreNode dc:title ?scoreTitle.\n" + 
				"?scoreNode mo:movement ?movementNode.\n" + 
				"?movementNode dc:title ?movemenTitle.\n" + 
				"?scoreNode dc:creator ?creatorNode.\n" + 
				"?creatorNode foaf:name ?creator.\n" + 
				"?movementNode mso:hasScorePart ?part.\n" + 
				"?movementNode dc:title ?movement.\n" + 
				"?part mso:hasMeasure ?measureNode.\n" + 
				"?part rdfs:ID ?partID.\n" + 
				"?part dc:description ?partName.\n" + 
				"?part mso:hasStaff ?staff.\n" + 
				"?measureNode rdfs:ID ?measure.\n" + 
				"?voice a mso:Voice.\n" + 
				"?voice rdfs:ID ?voiceID.\n" + 
				"?measureNode mso:hasNoteSet ?noteset0.\n" + 
				"?staff mso:hasVoice ?voice.\n" + 
				"?staff rdfs:ID ?staffID.\n" + 
				"\n" + 
				"?noteset0 mso:hasNote ?note0.\n" + 
				"?voice mso:hasNoteSet ?noteset0.\n" + 
				"?note0 chord:natural note:A.\n" + 
				"?noteset0 mso:hasDuration ?duration0.\n" + 
				"?duration0 a mso:Eighth.\n" + 
				"?noteset0 mso:nextNoteSet ?noteset1.\n" + 
				"\n" + 
				"?noteset1 mso:hasNote ?note1.\n" + 
				"?voice mso:hasNoteSet ?noteset1.\n" + 
				"?note1 chord:natural note:B.\n" + 
				"?noteset1 mso:hasDuration ?duration1.\n" + 
				"?duration1 a mso:Eighth.\n" + 
				"?noteset1 mso:nextNoteSet ?noteset2.\n" + 
				"\n" + 
				"?noteset2 mso:hasNote ?note2.\n" + 
				"?voice mso:hasNoteSet ?noteset2.\n" + 
				"?note2 chord:natural note:C.\n" + 
				"?noteset2 mso:hasDuration ?duration2.\n" + 
				"?duration2 a mso:Eighth.\n" + 
				"?noteset2 mso:nextNoteSet ?noteset3.\n" + 
				"\n" + 
				"?noteset3 mso:hasNote ?note3.\n" + 
				"?voice mso:hasNoteSet ?noteset3.\n" + 
				"?note3 chord:natural note:D.\n" + 
				"?noteset3 mso:hasDuration ?duration3.\n" + 
				"?duration3 a mso:Eighth.\n" + 
				"?noteset3 mso:nextNoteSet ?noteset4.\n" + 
				"\n" + 
				"?noteset4 mso:hasNote ?note4.\n" + 
				"?voice mso:hasNoteSet ?noteset4.\n" + 
				"?note4 chord:natural note:D.\n" + 
				"?note4 chord:modifier chord:sharp.\n" + 
				"?noteset4 mso:hasDuration ?duration4.\n" + 
				"?duration4 a mso:Eighth.\n" + 
				"?noteset4 mso:nextNoteSet ?noteset5.\n" + 
				"   \n" + 
				"?noteset5 mso:hasNote ?note5.\n" + 
				"?voice mso:hasNoteSet ?noteset5.\n" + 
				"?note5 chord:natural note:E.\n" + 
				"?noteset5 mso:hasDuration ?duration5.\n" + 
				"?duration5 a mso:Eighth.   \n" + 
				"\n" + 
				"FILTER ( NOT EXISTS {?note0 chord:modifier ?modifier0} )\n" + 
				"FILTER ( NOT EXISTS {?note1 chord:modifier ?modifier1} )\n" + 
				"FILTER ( NOT EXISTS {?note2 chord:modifier ?modifier2} )   \n" + 
				"FILTER ( NOT EXISTS {?note3 chord:modifier ?modifier3} ) \n" + 
				"FILTER ( NOT EXISTS {?note5 chord:modifier ?modifier5} )     \n" + 
				"}";
		
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.get("?measure").toString();
				System.out.println("Elgar Simple Melody: Measure "+soln.get("?measure"));
			}
		}
		
		assertEquals(result, "8");
		
		
	}



}
