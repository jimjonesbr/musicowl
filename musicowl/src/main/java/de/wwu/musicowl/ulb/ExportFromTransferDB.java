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
			
			Connection con = DriverManager.getConnection("jdbc:postgresql://postgis.uni-muenster.de/disco2", "", "");
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(" SELECT " + 
					"	musik.vlid, " +
					"	(XPATH('//mods:title/text()', vlxml.rawxml, ARRAY[ARRAY['mods', 'http://www.loc.gov/mods/v3']]))[1]::TEXT AS title, " +
					"	musik.rawxml " +
					"FROM musik, vlxml " +
					"WHERE musik.vlid = vlxml.vlid");

			int i = 0;
			
			while ( rs.next() )
			{
				System.out.println("Processing movement of "+rs.getString("title") + " ... ");
				i++;
				MusicScore score = music.parseMusicXML(rs.getString("rawxml"));
				score.setFileName(rs.getInt("vlid") + "_" + rs.getString("title") + "-"+i);
				music.createRDF(score);
				
			}

			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}





	}
}
