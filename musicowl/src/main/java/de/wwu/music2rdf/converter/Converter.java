package de.wwu.music2rdf.converter;

import java.io.File;

public class Converter {
	
	public static void main(String[] args) {

		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		File scoreFile = null;
		String outputFile = "";
		String outputFormat = "";
		boolean verbose = false;
				
		System.out.println("\n");
		for(int i = 0; i < args.length; i++) {

			if(args[i].toLowerCase().equals("version")){

				System.out.println("Music2RDF Converter ");
				System.out.println("Version: "+music2rdf.getVersion());
				System.exit(0);

			}

			String[] parameter = args[i].split("=");

			if(parameter[0].toLowerCase().equals("file")){
				System.out.println("File  	        : " + parameter[1]);
				scoreFile = new File(parameter[1]);
			}

			if(parameter[0].toLowerCase().equals("output")){
				outputFile = parameter[1];
				System.out.println("Output File     : " + parameter[1]);
			}

			if(parameter[0].toLowerCase().equals("verbose")){
				System.out.println("Verbose      	: " + parameter[1]);				
				verbose = Boolean.valueOf(parameter[1]);
			}

			if(parameter[0].toLowerCase().equals("outputformat")){
				System.out.println("OutputFormat    : " + parameter[1]);				
				outputFormat = parameter[1];
			}

			if(parameter[0].toLowerCase().equals("metadata")){

				music2rdf = MetadataParser.parseMetadataFile(new File(parameter[1]));
			}

		}

		System.out.println("\n");

		music2rdf.setInputFile(scoreFile);
		music2rdf.setOutputFile(outputFile);
		music2rdf.isVerbose(verbose);
		music2rdf.setOutputFormat(outputFormat);		
		music2rdf.parseMusicXML();

	}


}