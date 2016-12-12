package de.wwu.musicowl.ulb;

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

			MusicXML2RDF music = new MusicXML2RDF();
			MusicXML2RDF.setOutputFolder("/home/jones/mm/");
			MusicXML2RDF.setVerbose(false);
			
			Connection con = DriverManager.getConnection("jdbc:postgresql://postgis.uni-muenster.de/disco2", "disco2", "Feierabend");
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(" SELECT " + 
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
				score.setURI("http://sammlungen.ulb.uni-muenster.de/id/"+rs.getInt("vlid"));
				music.createRDF(score);
			}

			
			/**
			 * Generating metadata for music scores
			 */
			
			 
			rs = st.executeQuery("	SELECT " + 
								"	'https://sammlungen.ulb.uni-muenster.de/id/' || musik.vlid AS score, " +
								"	(XPATH('//mods:title/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT AS title, " +
								"	'https://sammlungen.ulb.uni-muenster.de/urn/' || (XPATH('//mods:identifier[@type=\"urn\"]/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT    AS identifier, " +
								"	(XPATH('//mets:fileGrp[@USE=\"FRONTIMAGE\"]/mets:file/mets:FLocat/@xlink:href', vlxml.rawxml, ARRAY[ARRAY['mets', 'http://www.loc.gov/METS/'],ARRAY['xlink', 'http://www.w3.org/1999/xlink']]))[1]::TEXT, " +
								"	XPATH('//mods:name/@valueURI', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]) AS composerURI, " +
								"	XPATH('//mods:name/mods:namePart/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]) AS composersNames " +
								" FROM musik, vlxml " +
								" WHERE musik.vlid = vlxml.vlid");
			
			

			while (rs.next()){
				
				
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}





	}
	
	private void GenerateScoreMetadata(ResultSet rs){
		
		
		
	}
}
