package de.wwu.music2rdf.converter;

import java.io.File;

import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;

public class Converter {


	public static void main(String[] args) {

		String collectionURI = "";
		String collectionName = "";

		MusicXML2RDF music2rdf = new MusicXML2RDF();

		System.out.println("\n");
		for(int i = 0; i < args.length; i++) {

			String[] parameter = args[i].split("=");

			if(parameter[0].toLowerCase().equals("file")){
				System.out.println("File 	         : " + parameter[1]);
				music2rdf.setInputFile(new File(parameter[1]));
			}

			if(parameter[0].toLowerCase().equals("output")){
				music2rdf.setOutputFile(parameter[1]);
				System.out.println("Output File      : " + parameter[1]);
			}

			if(parameter[0].toLowerCase().equals("uri")){
				System.out.println("URI 	         : " + parameter[1]);				
				music2rdf.setScoreURI(parameter[1]);
			}

			if(parameter[0].toLowerCase().equals("thumbnail")){
				System.out.println("Thumbnail        : " + parameter[1]);				
				music2rdf.setThumbnail(parameter[1]);
			}

			if(parameter[0].toLowerCase().equals("collectionuri")){
				System.out.println("Collection URI   : " + parameter[1]);				
				collectionURI = parameter[1];
			}

			if(parameter[0].toLowerCase().equals("collectionname")){
				System.out.println("Collection Name  : " + parameter[1]);				
				collectionName = parameter[1];
			}

			if(parameter[0].toLowerCase().equals("title")){
				System.out.println("Score Title : " + parameter[1]);				
				music2rdf.setDocumentTitle(parameter[1]);				
			}
			
			if(parameter[0].toLowerCase().equals("dateissued")){
				System.out.println("Date Issued      : " + parameter[1]);				
				music2rdf.setDateIssued(parameter[1]);				
			}

			if(parameter[0].toLowerCase().equals("person")){

				String[] tokens = parameter[1].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				System.out.println("Person URI       : "+tokens[0]);
				System.out.println("Person Name      : "+tokens[1]);
				System.out.println("Person Role      : "+tokens[2]);

				music2rdf.addPerson(new Person(tokens[0].replaceAll("[\"]", ""),tokens[1].replaceAll("[\"]", ""),tokens[2].replaceAll("[\"]", "")));	

			}

		}

		System.out.println("\n");

		music2rdf.addCollection(new Collection(collectionURI,collectionName));

		music2rdf.parseMusicXML();


	}


}