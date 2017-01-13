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

import de.wwu.musicowl.core.MusicScore;
import de.wwu.musicowl.parser.MusicXML2RDF;

public class ExportFromTransferDB {

	public static void main(String[] args) {

		try {

			String outputFolder = "ntriples/";
			
			MusicXML2RDF music = new MusicXML2RDF();
			MusicXML2RDF.setOutputFolder(outputFolder);
			MusicXML2RDF.setVerbose(false);
			
			Connection con = DriverManager.getConnection("jdbc:postgresql://postgis.uni-muenster.de/disco2", "disco2", "Feierabend");
			Statement st = con.createStatement();

			
			
			ResultSet rs = st.executeQuery("	SELECT " + 
					"	'https://sammlungen.ulb.uni-muenster.de/id/' || musik.vlid AS score, " +
					"	(XPATH('//mods:title/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT AS title, " +
					"	'https://sammlungen.ulb.uni-muenster.de/urn/' || (XPATH('//mods:identifier[@type=\"urn\"]/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT    AS identifier, " +
					"	(XPATH('//mets:fileGrp[@USE=\"FRONTIMAGE\"]/mets:file/mets:FLocat/@xlink:href', vlxml.rawxml, ARRAY[ARRAY['mets', 'http://www.loc.gov/METS/'],ARRAY['xlink', 'http://www.w3.org/1999/xlink']]))[1]::TEXT AS thumbnail, " +
					"	(XPATH('//mods:name/@valueURI', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))::TEXT[] AS composerURI," +
					"	(XPATH('//mods:name/mods:namePart/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))::TEXT[] AS composerName " +
					" FROM musik, vlxml " +
					" WHERE musik.vlid = vlxml.vlid");
						
			StringBuffer metadata = new StringBuffer();
			
			System.out.println("1. Generating scores metadata ...");
			
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
			
			FileOutputStream fileStream = new FileOutputStream(new File(outputFolder + "metadata.nt"),false);
			OutputStreamWriter writer = new OutputStreamWriter(fileStream, "UTF8");

			writer.append(metadata.toString());
			writer.close();	
			
			System.out.println("DONE!\n");
			
			System.out.println("2. Converting MusicXML to RDF ... \n");
			
			
			rs = st.executeQuery(" SELECT " + 
					"	musik.vlid, " +
					"	(XPATH('//mods:title/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT AS title, " +
					"	musik.rawxml " +
					"FROM musik, vlxml " +
					"WHERE musik.vlid = vlxml.vlid");

			int i = 0;
			
			while (rs.next()){
				System.out.println("Processing movement of "+rs.getString("title") + " ... ");
				i++;
				
				MusicScore score = music.parseMusicXML(rs.getString("rawxml"));
				score.setFileName(rs.getInt("vlid") + "_" + rs.getString("title") + "-" + i);
				score.setURI("https://sammlungen.ulb.uni-muenster.de/id/"+rs.getInt("vlid"));
				music.createRDF(score);
				
			}
					
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}





	}
	
}
