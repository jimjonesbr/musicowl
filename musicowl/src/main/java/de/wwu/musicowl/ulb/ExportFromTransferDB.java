package de.wwu.musicowl.ulb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import de.wwu.musicowl.core.MusicScore;
import de.wwu.musicowl.parser.MusicXML2RDF;

public class ExportFromTransferDB {

	public static void main(String[] args) {

		try {

			String outputFolder = "ntriples/";
			
			MusicXML2RDF music = new MusicXML2RDF();
			MusicXML2RDF.setOutputFolder(outputFolder);
			MusicXML2RDF.setVerbose(false);
			StringBuffer metadata = new StringBuffer();
			
			Connection con = DriverManager.getConnection("jdbc:postgresql://postgis.uni-muenster.de/disco2", "disco2", "");
			Statement st = con.createStatement();
			ResultSet rs;
			FileOutputStream fileStream;
			OutputStreamWriter writer;
			

			
			/**
			 * Data from Digitale Sammlung (Visual Library)
			 */
			
			rs = st.executeQuery("	SELECT " + 
					"	'https://sammlungen.ulb.uni-muenster.de/id/' || musik.docid AS score, " +
					"	(XPATH('//mods:title/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT AS title, " +
					"	'https://sammlungen.ulb.uni-muenster.de/urn/' || (XPATH('//mods:identifier[@type=\"urn\"]/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT    AS identifier, " +
					"	(XPATH('//mets:fileGrp[@USE=\"FRONTIMAGE\"]/mets:file/mets:FLocat/@xlink:href', vlxml.rawxml, ARRAY[ARRAY['mets', 'http://www.loc.gov/METS/'],ARRAY['xlink', 'http://www.w3.org/1999/xlink']]))[1]::TEXT AS thumbnail, " +
					"	(XPATH('//mods:mods/mods:name/@valueURI', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))::TEXT[] AS composerURI," +
					"	(XPATH('//mods:mods/mods:name/mods:namePart/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))::TEXT[] AS composerName " +
					" FROM musik, vlxml " +
					" WHERE musik.docid = vlxml.vlid::VARCHAR");
						
						
			System.out.println("1. [Visual Library] Generating scores metadata ...");
			
			while (rs.next()){
				
				metadata.append("<" + rs.getString("score") + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/ontology/mo/Score> .\n");
				metadata.append("<" + rs.getString("score") + "> <http://purl.org/dc/elements/1.1/title> \"" + rs.getString("title") + "\" .\n");
				metadata.append("<" + rs.getString("score") + "> <http://xmlns.com/foaf/0.1/thumbnail> <" + rs.getString("thumbnail")+ "> .\n");
				
				Array arrComposersURI = rs.getArray("composerURI");
				String[] composersURI = (String[])arrComposersURI.getArray();

				Array arrComposersName = rs.getArray("composerName");
				String[] composersName = (String[])arrComposersName.getArray();

				
				for (int i = 0; i < composersURI.length; i++) {
				
					metadata.append("<" + rs.getString("score") + "> <http://purl.org/dc/elements/1.1/creator> <" + composersURI[i] + "> .\n");
					metadata.append("<" + composersURI[i] + "> <http://xmlns.com/foaf/0.1/name> \"" + composersName[i] + "\" .\n");
				}
				
				
			}
			
			fileStream = new FileOutputStream(new File(outputFolder + "metadata.nt"),false);
			writer = new OutputStreamWriter(fileStream, "UTF8");

			writer.append(metadata.toString());
			writer.close();	
			
			System.out.println("DONE!\n");
			
			System.out.println("2. [Visual Library] Converting MusicXML to RDF ... \n");
						
			rs = st.executeQuery(" SELECT " + 
					"	musik.docid, " +
					"	(XPATH('//mods:title/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT AS title, " +
					"	musik.rawxml " +
					"FROM musik, vlxml " +
					"WHERE musik.docid = vlxml.vlid::VARCHAR");

			int i = 0;
			
			while (rs.next()){
				System.out.println("Processing movement of "+rs.getString("title") + " ... ");
				i++;
				
				MusicScore score = music.parseMusicXML(rs.getString("rawxml"));
				score.setFileName(rs.getInt("docid") + "_" + rs.getString("title") + "-" + i);
				score.setURI("https://sammlungen.ulb.uni-muenster.de/id/"+rs.getInt("docid"));
				music.createRDF(score);
				
			}
					
		
			
			
			
			
			
			
			
			
			
			


			
			/**
			 * Metadata from MIAMI
			 */
			rs = st.executeQuery("	SELECT " + 
								"	'https://miami.uni-muenster.de/Record/' || musik.docid AS score,  " +
								"	'https://www.ulb.uni-muenster.de/imperia/md/images/ulb2/_v/logo.svg' AS thumbnail,  " +
								"	miami.title AS title,  " +
								"	MD5((XPATH('//marcxml:datafield[@tag=\"700\"]/marcxml:subfield[@code=\"a\"]/text()', miami.rawxml, ARRAY[ARRAY['marcxml', 'http://www.loc.gov/MARC21/slim']]))[1]::VARCHAR) AS composerId,  " +
								"	(XPATH('//marcxml:datafield[@tag=\"700\"]/marcxml:subfield[@code=\"a\"]/text()', miami.rawxml, ARRAY[ARRAY['marcxml', 'http://www.loc.gov/MARC21/slim']]))[1]::VARCHAR AS composerName  " +
								" FROM musik  " +
								" INNER JOIN metsdaten ON musik.docid = metsdaten.uuid  " + 
								" INNER JOIN miami ON metsdaten.urn = miami.urn");
						
			
			
			System.out.println("3. [MIAMI] Generating scores metadata ...");
			
			String uuid = UUID.randomUUID().toString();
			
			while (rs.next()){
					
				
				metadata.append("<" + rs.getString("score") + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/ontology/mo/Score> .\n");
				metadata.append("<" + rs.getString("score") + "> <http://purl.org/dc/elements/1.1/title> \"" + rs.getString("title") + "\" .\n");
				metadata.append("<" + rs.getString("score") + "> <http://xmlns.com/foaf/0.1/thumbnail> <https://www.ulb.uni-muenster.de/imperia/md/images/ulb2/bilder/medien/telemann-noten_730x365.jpg> .\n");
								
				
				metadata.append("<" + rs.getString("score") + "> <http://purl.org/dc/elements/1.1/creator> _:"+rs.getString("composerId")+" .\n");
				metadata.append("_:"+rs.getString("composerId")+"  <http://xmlns.com/foaf/0.1/name> \"" + rs.getString("composerName") + "\" .\n");


//				Array arrComposersName = rs.getArray("composerName");
//				String[] composersName = (String[])arrComposersName.getArray();
//
//				
//				for (int j = 0; j < composersName.length; j++) {
//					
//					//TODO: remove http://d-nb.info/gnd/134827635 as default for all composers. Missing GND ids lead to inconsistency. 
//					
//					metadata.append("<" + rs.getString("score") + "> <http://purl.org/dc/elements/1.1/creator> <http://d-nb.info/gnd/134827635> .\n");
//					metadata.append("_:" + UUID.randomUUID().toString() + " <http://xmlns.com/foaf/0.1/name> \"" + composersName[j] + "\" .\n");
//				}
				
				
			}
			
			fileStream = new FileOutputStream(new File(outputFolder + "metadata_MIAMI.nt"),false);
			writer = new OutputStreamWriter(fileStream, "UTF8");

			writer.append(metadata.toString());
			writer.close();	
			
			System.out.println("DONE!\n");
			
			System.out.println("4. [MIAMI] Converting MusicXML to RDF ... \n");
			
			
			rs = st.executeQuery(" SELECT " +
								" 	musik.docid		AS docid,  " +
								" 	musik.rawxml	AS rawxml, " +
								" 	miami.title 	AS title " +
								" FROM musik " +
								" INNER JOIN metsdaten ON musik.docid = metsdaten.uuid " + 
								" INNER JOIN miami ON metsdaten.urn = miami.urn");

			int j = 0;
			
			while (rs.next()){
				System.out.println("Processing movement of " + rs.getString("title") + " ... ");
				j++;
				
				MusicScore score = music.parseMusicXML(rs.getString("rawxml"));
				score.setFileName(rs.getString("docid") + "_" + rs.getString("title") + "-" + j);
				score.setURI("https://miami.uni-muenster.de/Record/"+rs.getString("docid"));
				music.createRDF(score);
				
			}
			
			
			
			
			
			
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}



		


	}
	
}
