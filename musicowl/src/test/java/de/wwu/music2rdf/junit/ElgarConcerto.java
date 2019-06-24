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

public class ElgarConcerto {

	private static Logger logger = Logger.getLogger("ElgarConcerto-Test");
	
	@Test
	public void melodyWithRests() {

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
				"SELECT DISTINCT ?scoreTitle ?measure ?noteset_5\n" + 
				"WHERE {\n" + 
				"\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:title ?scoreTitle.\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> mo:movement ?movementNode.\n" + 
				"    ?movementNode mso:hasScorePart ?part.\n" + 
				"    ?part mso:hasMeasure ?measureNode.\n" + 
				"    ?part rdfs:ID ?partID.\n" + 
				"    ?part dc:description ?partName.\n" + 
				"    ?part mso:hasStaff ?staff.\n" + 
				"    ?measureNode rdfs:ID ?measure.\n" + 
				"    ?voice a mso:Voice.\n" + 
				"    ?voice rdfs:ID ?voiceID.\n" + 
				"    ?measureNode mso:hasNoteSet ?noteset_1.\n" + 
				"    ?staff mso:hasVoice ?voice.\n" + 
				"    ?staff rdfs:ID ?staffID.\n" + 
				"\n" + 
				"    ?voice mso:hasNoteSet ?noteset_1.\n" + 
				"    ?noteset_1 mso:hasNote ?note_E.\n" + 
				"    ?note_E chord:natural note:E.\n" + 
				"    ?note_E mso:hasOctave \"6\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"    ?noteset_1 mso:hasDuration ?duration_1.\n" + 
				"    ?duration_1 a mso:Quarter.\n" + 
				"\n" + 
				"	?noteset_1 mso:nextNoteSet ?noteset_2.\n" + 
				"\n" + 
				"    ?voice mso:hasNoteSet ?noteset_2.\n" + 
				"    ?noteset_2 mso:hasNote ?rest_8th.\n" + 
				"    ?rest_8th chord:natural note:Rest.\n" + 
				"    ?noteset_2 mso:hasDuration ?duration_2.\n" + 
				"    ?duration_2 a mso:Eighth.\n" + 
				"    \n" + 
				"	?noteset_2 mso:nextNoteSet ?noteset_3.\n" + 
				"\n" + 
				"	?voice mso:hasNoteSet ?noteset_3.\n" + 
				"	?noteset_3 mso:hasNote ?rest_4th.\n" + 
				"	?rest_4th chord:natural note:Rest.    \n" + 
				"	?noteset_3 mso:hasDuration ?duration_3.\n" + 
				"	?duration_3 a mso:Quarter.    \n" + 
				"	?duration_3 mso:hasDurationAttribute ?attribute_rest_4th .\n" + 
				"	?attribute_rest_4th a mso:Dot.\n" + 
				"\n" + 
				"    ?noteset_3 mso:nextNoteSet ?noteset_4.\n" + 
				"\n" + 
				"	?voice mso:hasNoteSet ?noteset_4.\n" + 
				"	?noteset_4 mso:hasNote ?rest_4th_2.\n" + 
				"	?rest_4th_2 chord:natural note:Rest.    \n" + 
				"	?noteset_4 mso:hasDuration ?duration_4.\n" + 
				"	?duration_4 a mso:Quarter.    \n" + 
				"	?duration_4 mso:hasDurationAttribute ?attribute_rest_4th_2 .\n" + 
				"	?attribute_rest_4th_2 a mso:Dot.\n" + 
				"    \n" + 
				"    ?noteset_4 mso:nextNoteSet ?noteset_5.\n" + 
				"\n" + 
				"	?voice mso:hasNoteSet ?noteset_5.\n" + 
				"	?noteset_5 mso:hasNote ?rest_whole.\n" + 
				"	?rest_whole chord:natural note:Rest.    \n" + 
				"	?noteset_5 mso:hasDuration ?duration_5.\n" + 
				"	?duration_5 a mso:Whole.    \n" + 
				"    \n" + 
				"    ?noteset_5 mso:nextNoteSet ?noteset_6.\n" + 
				"\n" + 
				"	?voice mso:hasNoteSet ?noteset_6.\n" + 
				"	?noteset_6 mso:hasNote ?rest_whole_2.\n" + 
				"	?rest_whole_2 chord:natural note:Rest.    \n" + 
				"	?noteset_6 mso:hasDuration ?duration_6.\n" + 
				"	?duration_6 a mso:Whole.    \n" + 
				"\n" + 
				"    ?noteset_6 mso:nextNoteSet ?noteset_7.\n" + 
				"    \n" + 
				"   	?voice mso:hasNoteSet ?noteset_7.\n" + 
				"	?noteset_7 mso:hasNote ?rest_whole_3.\n" + 
				"	?rest_whole_3 chord:natural note:Rest.    \n" + 
				"	?noteset_7 mso:hasDuration ?duration_7.\n" + 
				"	?duration_7 a mso:Whole.    \n" + 
				"        \n" + 
				"    ?noteset_7 mso:nextNoteSet ?noteset_8.\n" + 
				"    \n" + 
				"   	?voice mso:hasNoteSet ?noteset_8.\n" + 
				"	?noteset_8 mso:hasNote ?rest_whole_4.\n" + 
				"	?rest_whole_4 chord:natural note:Rest.    \n" + 
				"	?noteset_8 mso:hasDuration ?duration_8.\n" + 
				"	?duration_8 a mso:Whole.    \n" + 
				"\n" + 
				"    ?noteset_8 mso:nextNoteSet ?noteset_9.\n" + 
				"    \n" + 
				"   	?voice mso:hasNoteSet ?noteset_9.\n" + 
				"	?noteset_9 mso:hasNote ?rest_whole_5.\n" + 
				"	?rest_whole_5 chord:natural note:Rest.    \n" + 
				"	?noteset_9 mso:hasDuration ?duration_9.\n" + 
				"	?duration_9 a mso:Whole.    \n" + 
				" \n" + 
				"    ?noteset_9 mso:nextNoteSet ?noteset_10.\n" + 
				"    \n" + 
				"    ?voice mso:hasNoteSet ?noteset_10.\n" + 
				"    ?noteset_10 mso:hasNote ?note_F.\n" + 
				"    ?note_F chord:natural note:F.\n"+
				"	 ?note_F chord:modifier chord:sharp." + 
				"    ?note_F mso:hasOctave \"4\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"    ?noteset_10 mso:hasDuration ?duration_10.\n" + 
				"    ?duration_10 a mso:Quarter.\n" + 
				"    \n" + 
				"    FILTER ( NOT EXISTS {?noteset_1 chord:modifier ?m1} )\n" +  
				"}";

		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.get("?measure").toString();
				logger.info("Melody containing rests in multiple durations: Measure " + result);
			}
		}

		assertEquals(result, "33");
	}
	
	@Test
	public void checkMetadata() {
		boolean result = true;		
		URL url = this.getClass().getResource("/rdf/elgar_cello_concerto_op.85.nt");
		File file = new File(url.getFile());

		Model modelElgar = ModelFactory.createDefaultModel();
		modelElgar.read(file.getAbsolutePath(),"N-TRIPLES");
		String sparql = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
				"PREFIX dct: <http://purl.org/dc/terms/>\n" + 			 
				"PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+				
				"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
				"PREFIX prov: <http://www.w3.org/ns/prov#>\n" + 
				"PREFIX gnd: <http://d-nb.info/standards/elementset/gnd#>\n"+
				"PREFIX dbp: <http://dbpedia.org/ontology/> \n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"\n" + 
				"SELECT ?collection ?collectionName ?scoreTitle ?issued ?movementTitle ?partID ?partName ?thumbnail ?creator ?creatorName ?creatorRole ?creatorRoleName ?creatorRoleDbp ?creatorRoleDbpName ?generatedBy ?encoder ?encoderName ?encoderRole ?encoderRoleName ?encoderRoleDbpName ?encoderRoleDbp\n" + 
				"WHERE {\n" + 
				"	?collection prov:hadMember <http://dbpedia.org/resource/Cello_Concerto_(Elgar)>.\n" + 
				"    ?collection rdfs:label ?collectionName.\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:title ?scoreTitle.\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dct:issued ?issued.\n" + 
				"	 <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> mo:movement ?movementNode.\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> foaf:thumbnail ?thumbnail.\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> prov:wasGeneratedBy ?generatedBy.\n" + 
				"    <http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:creator ?creator.\n" + 
				"    ?creator a foaf:Person.\n" + 
				"    ?creator foaf:name ?creatorName.\n" + 
				"    ?creator gnd:professionOrOccupation ?creatorRole.\n" + 
				"    ?creatorRole gnd:preferredNameForTheSubjectHeading ?creatorRoleName.\n" + 
				"    ?creator dbp:occupation ?creatorRoleDbp.\n" +
				"    ?creatorRoleDbp rdfs:label ?creatorRoleDbpName.\n" +
				"    ?generatedBy prov:wasAssociatedWith ?encoder. \n" + 
				"    ?encoder a foaf:Person.\n" +
				"    ?encoder dbp:occupation ?encoderRoleDbp .\n" + 
				"    ?encoderRoleDbp rdfs:label ?encoderRoleDbpName.\n" + 
				"    ?generatedBy a prov:Activity.\n" + 
				"    ?encoder foaf:name ?encoderName.    \n" + 
				"    ?encoder a foaf:Person.\n" + 
				"    ?encoder prov:hadRole ?encoderRole.\n" + 
				"    ?encoderRole gnd:preferredNameForTheSubjectHeading ?encoderRoleName.\n" + 
				"    ?movementNode dc:title ?movementTitle.\n" + 
				"	?movementNode mso:hasScorePart ?part.\n" + 
				"	?part rdf:ID ?partID.\n" + 
				"	?part dc:description ?partName.		  \n" + 
				"}";

		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;

			if(results.getResultVars().size()==0) {
				result=false;
			}

			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;

				if(!soln.getLiteral("?scoreTitle").toString().equals("Cellokonzert e-Moll op. 85")) result = false;
				if(!soln.get("?thumbnail").toString().equals("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg")) result = false;
				if(!soln.get("?collection").toString().equals("https://url.collection.de")) result = false;
				if(!soln.getLiteral("?collectionName").toString().equals("Great Composers")) result = false;
				if(!soln.getLiteral("?issued").toString().equals("1919")) result = false;
				if(!soln.getLiteral("?movementTitle").toString().equals("Adagio")) result = false;
				if(!soln.get("?creator").toString().equals("http://dbpedia.org/resource/Edward_Elgar")) result = false;
				if(!soln.getLiteral("?creatorName").toString().equals("Sir Edward William Elgar")) result = false;

				if(!soln.get("?creatorRole").toString().equals("http://d-nb.info/gnd/4032009-1")) result = false;
				if(!soln.getLiteral("?creatorRoleName").toString().equals("Composer")) result = false;
				if(!soln.get("?creatorRoleDbp").toString().equals("http://dbpedia.org/resource/Composer")) result = false;
				if(!soln.getLiteral("?creatorRoleDbpName").toString().trim().equals("Composer")) result = false;

				if(!soln.get("?encoder").toString().equals("http://jimjones.de")) result = false;
				if(!soln.getLiteral("?encoderName").toString().equals("Jim Jones")) result = false;
				if(!soln.get("?encoderRole").toString().equals("http://d-nb.info/gnd/4139395-8")) result = false;
				if(!soln.getLiteral("?encoderRoleName").toString().equals("Encoder")) result = false;
				if(!soln.get("?encoderRoleDbp").toString().equals("http://dbpedia.org/resource/Encoder")) result = false;
				if(!soln.getLiteral("?encoderRoleDbpName").toString().trim().equals("Encoder")) result = false;

				logger.info("Meta data:\n"+
						"  Score Title: " + soln.get("?scoreTitle") + 
						"\n  Thumbnail: " + soln.get("?thumbnail")+
						"\n  Collection URI: " + soln.get("?collection")+
						"\n  Collection Name: " + soln.get("?collectionName")+
						"\n  Date Issued: " + soln.get("?issued")+
						"\n  Movement: " + soln.get("?movementTitle")+
						"\n  Composer URI: " + soln.get("?creator")+
						"\n  Composer Name: " + soln.get("?creatorName")+
						"\n  Composer Role URI: " + soln.get("?creatorRole")+
						"\n  Composer Role Name: " + soln.get("?creatorRoleName")+
						"\n  Composer Role URI (DBpedia): " + soln.get("?creatorRoleDbp")+
						"\n  Composer Role Name (DBpedia): " + soln.get("?creatorRoleDbpName")+
						"\n  Encoder URI: " + soln.get("?encoder")+
						"\n  Encoder Name: " + soln.get("?encoderName")+
						"\n  Encoder Role URI: " + soln.get("?encoderRole")+
						"\n  Encoder Role Name: " + soln.get("?encoderRoleName")
						);
			}
		}

		assertEquals(result,true);

	}
	
	@Test
	public void totalMeasures() {
		int result = 0;		
		URL url = this.getClass().getResource("/rdf/elgar_cello_concerto_op.85.nt");
		File file = new File(url.getFile());

		Model modelElgar = ModelFactory.createDefaultModel();
		modelElgar.read(file.getAbsolutePath(),"N-TRIPLES");
		String sparql = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
				"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
				"\n" + 
				"SELECT (COUNT (?measure_1) AS ?measure)\n" + 
				"WHERE {\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:title ?scoreTitle.\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> mo:movement ?movementNode.\n" + 
				"?movementNode mso:hasScorePart ?part.\n" + 
				"?part mso:hasMeasure ?measure_1. \n" + 
				"}";
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.getLiteral("?measure").getInt();
				logger.info("Total measures: " + result);
			}
		}

		assertEquals(result, 106);

	}

	@Test
	public void changingKeys() {
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
				"PREFIX ton: <http://purl.org/ontology/tonality/>\n" + 
				"PREFIX mode: <http://purl.org/ontology/tonality/mode/>\n" + 
				"SELECT *\n" + 
				"WHERE {\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:title ?scoreTitle.\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> mo:movement ?movementNode.\n" + 
				"?movementNode mso:hasScorePart ?part.\n" + 
				"?part mso:hasMeasure ?measure_1.\n" + 
				"?measure_1 rdfs:ID ?measure.\n" + 
				"?measure_1 mso:hasKey ?key_1.\n" + 
				"?key_1 ton:tonic note:E  .\n" + 
				"?key_1 ton:mode mode:major .\n" + 
				"    \n" + 
				"?measure_1 mso:nextMeasure ?measure_2.\n" + 
				"?measure_2 mso:hasKey ?key_2.\n" + 
				"?key_2 ton:tonic note:G  .\n" + 
				"?key_2 ton:mode mode:major .\n" + 
				"}"; 		

		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.get("?measure").toString();
				logger.info("Key signature change EMajor -> GMajor: Measure " + result);
			}
		}

		assertEquals(result, "74");
	}

	@Test
	public void changingTimeSignatures() {

		int result = 0;		
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
				"SELECT (COUNT (?measure_1) AS ?qt)\n" + 
				"WHERE {\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> dc:title ?scoreTitle.\n" + 
				"<http://dbpedia.org/resource/Cello_Concerto_(Elgar)> mo:movement ?movementNode.\n" + 
				"?movementNode mso:hasScorePart ?part.\n" + 
				"?part mso:hasMeasure ?measure_1.\n" + 
				"?part rdfs:ID ?partID.\n" + 
				"?part dc:description ?partName.\n" + 
				"?part mso:hasStaff ?staff.\n" + 
				"?measure_1 rdfs:ID ?measure.\n" + 
				"?measure_1 mso:hasTime ?time1.\n" + 
				"?time1 mso:hasBeats \"6\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?time1 mso:hasBeatType \"8\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?measure_1 mso:nextMeasure ?measure_2.\n" + 
				"?measure_2 mso:hasTime ?time_2.\n" + 
				"?time_2 mso:hasBeats \"12\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?time_2 mso:hasBeatType \"8\"^^<http://www.w3.org/2001/XMLSchema#int> .    \n" + 
				"}"; 

		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.getLiteral("?qt").getInt();
				logger.info("Time signature change 8/6 -> 12/8: "+soln.getLiteral("?qt").getInt() + " times");
			}
		}

		assertEquals(result, 2);
	}

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
				logger.info("Chord sequence: Measure "+soln.get("?measure"));
			}
		}

		assertEquals(result, "1");

	}

	@Test
	public void elgarMelodyChangingKeys() {

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
				"SELECT DISTINCT ?scoreTitle ?measure ?note_F ?chord_1\n" + 
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
				"?measureNode mso:hasNoteSet ?noteset_1.\n" + 
				"?staff mso:hasVoice ?voice.\n" + 
				"?staff rdfs:ID ?staffID.\n" + 
				"\n" + 
				"?voice mso:hasNoteSet ?noteset_1.\n" + 
				"?noteset_1 mso:hasNote ?note_F.\n" + 
				"?note_F chord:natural note:F.\n" + 
				"?note_F mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?note_F chord:modifier chord:sharp .\n" + 
				"?noteset_1 mso:hasDuration ?duration_1.\n" + 
				"?duration_1 mso:hasDurationAttribute ?attribute_note_F .\n" + 
				"?attribute_note_F a mso:Dot.\n" + 
				"?duration_1 a mso:Quarter.\n" + 
				"\n" + 
				"?noteset_1 mso:nextNoteSet ?noteset_2.\n" + 
				"\n" + 
				"?voice mso:hasNoteSet ?noteset_2.\n" + 
				"?noteset_2 mso:hasNote ?note_2.\n" + 
				"?note_2 chord:natural note:F.\n" + 
				"?note_2 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?note_2 chord:modifier chord:doublesharp .\n" + 
				"?noteset_2 mso:hasDuration ?duration_2.\n" + 
				"?duration_2 mso:hasDurationAttribute ?attribute_2 .\n" + 
				"?attribute_2 a mso:Dot.\n" + 
				"?duration_2 a mso:Quarter.\n" + 
				"    \n" + 
				"?noteset_2 mso:nextNoteSet ?noteset_3.\n" + 
				"\n" + 
				"?voice mso:hasNoteSet ?noteset_3.\n" + 
				"?noteset_3 mso:hasNote ?note_3.\n" + 
				"?note_3 chord:natural note:G.\n" + 
				"?note_3 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?note_3 chord:modifier chord:sharp .\n" + 
				"?noteset_3 mso:hasDynamic ?dynamic_3.\n" + 
				"?dynamic_3 a mso:mf.\n" + 
				"?noteset_3 mso:hasDuration ?duration_3.\n" + 
				"?duration_3 mso:hasDurationAttribute ?attribute_3 .\n" + 
				"?attribute_3 a mso:Dot.\n" + 
				"?duration_3 a mso:Eighth.    \n" + 
				"\n" + 
				"?noteset_3 mso:nextNoteSet ?noteset_4.   \n" + 
				"    \n" + 
				"?voice mso:hasNoteSet ?noteset_4.\n" + 
				"?noteset_4 mso:hasNote ?note_4.\n" + 
				"?note_4 chord:natural note:A.\n" + 
				"?note_4 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?noteset_4 mso:hasDuration ?duration_4.\n" + 
				"?duration_4 a mso:16th.     \n" + 
				"    \n" + 
				"?noteset_4 mso:nextNoteSet ?noteset_5.\n" + 
				"    \n" + 
				"?voice mso:hasNoteSet ?noteset_5.\n" + 
				"?noteset_5 mso:hasNote ?note_5.\n" + 
				"?note_5 chord:natural note:G.\n" + 
				"?note_5 chord:modifier chord:sharp .\n" + 
				"?note_5 mso:hasOctave \"3\"^^<http://www.w3.org/2001/XMLSchema#int> .\n" + 
				"?noteset_5 mso:hasDuration ?duration_5.\n" + 
				"?duration_5 a mso:Eighth.   \n" + 
				"\n" + 
				"FILTER ( NOT EXISTS {?noteset_4 chord:modifier ?m1} )\n" + 
				"}";

		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.get("?measure").toString();
				logger.info("Melody containing multiple keys, dynamics and duration attributes: Measure "+soln.get("?measure"));
			}
		}

		assertEquals(result, "54");
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
				"FILTER ( NOT EXISTS {?note2 chord:modifier ?modifier2} )\n" + 
				"FILTER ( NOT EXISTS {?note3 chord:modifier ?modifier3} )\n" + 
				"FILTER ( NOT EXISTS {?note5 chord:modifier ?modifier5} )\n" + 
				"}";
		
		try (QueryExecution qexec = QueryExecutionFactory.create(sparql, modelElgar)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				result = soln.get("?measure").toString();
				logger.info("Simple melody: Measure "+soln.get("?measure"));
			}
		}

		assertEquals(result, "8");		

	}

}
