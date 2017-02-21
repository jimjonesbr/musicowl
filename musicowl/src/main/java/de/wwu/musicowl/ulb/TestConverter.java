package de.wwu.musicowl.ulb;

import java.io.File;

import de.wwu.musicowl.core.MusicScore;
import de.wwu.musicowl.parser.MusicXML2RDF;

public class TestConverter {

	public static void main(String[] args) {

		String inputFolder = "/home/jones/Dropbox/Musik/MusicXML/MIAMI/3e724fa4-f67e-4dff-94d6-a01b78d73049/";
		
		MusicXML2RDF m = new MusicXML2RDF();
		m.setOutputFolder("/home/jones/Schreibtisch/");
		
		File[] files = new File(inputFolder).listFiles();

		for (File file : files) {

			if(file.getName().endsWith(".xml")){
				MusicScore score = new MusicScore();
				score = m.parseMusicXML(file);
				score.setFileName(file.getName());
				
				m.createRDF(score);
				
			}

		}

	}

}
