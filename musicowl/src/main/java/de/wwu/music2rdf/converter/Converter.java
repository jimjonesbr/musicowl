package de.wwu.music2rdf.converter;

import java.io.File;

public class Converter {

	private boolean verbose = false;
	private String outputFolder = "";
	private String inputFolder = "";
	private String inputFile = "";
	private String mode = "";


	public static void main(String[] args) {

		Converter converter = new Converter();
		MusicXML2RDF music2rdf = new MusicXML2RDF();


		for(int i = 0; i < args.length; i++) {

			String[] parameter = args[i].split("=");

			if(parameter[0].toLowerCase().equals("file")){

				converter.setInputFile(parameter[1]);
				System.out.println("File: " + converter.getInputFile());

			}

			if(parameter[0].toLowerCase().equals("folder")){

				converter.setInputFolder(parameter[1]);
				System.out.println("Input Directory: " + converter.getInputFolder());

			}

			if(parameter[0].toLowerCase().equals("output")){

				converter.setOutputFolder(parameter[1]);
				System.out.println("Output Directory: " + converter.getOutputFolder());

			}

			if(parameter[0].toLowerCase().equals("mode")){

				if(parameter[1].equals("verbose")){

					converter.setVerbose(true);
					System.out.println("Mode: Verbose");

				} else {

					System.out.println("Mode: silent");
				}

			}

		}

		System.out.println("\n");





		if(converter.getOutputFolder().equals("")){

			System.err.println("[ERR001] Output folder not provided.");
			System.exit(1);

		} else if (converter.getInputFile().equals("") && converter.getInputFolder().equals("")) {

			System.err.println("[ERR002] Input file/folder not provided.");
			System.exit(1);

		} else if (!converter.getInputFile().equals("") && !converter.getInputFolder().equals("")){

			System.err.println("[ERR003] Chose either a file or a directory as input.");
			System.exit(1);

		} else if (!converter.getInputFile().equals("")){

			music2rdf.setOutputFile(converter.getOutputFilename());
			music2rdf.parseMusicXML(new File(converter.getInputFile()));


		} else if (!converter.getInputFolder().equals("")){

			File[] files = new File(converter.getInputFolder()).listFiles();

			for (File file : files) {

				if(file.getName().endsWith(".xml")){

					System.out.println(file.getAbsolutePath()+".nt");
					music2rdf.setOutputFile(file.getAbsolutePath()+".nt");
					music2rdf.parseMusicXML(file);					

				}

			}

		}
	}



	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {

		if(!outputFolder.substring(outputFolder.length()-1, outputFolder.length()).equals(File.separator)){

			this.outputFolder = outputFolder + File.separator;

		} else {

			this.outputFolder = outputFolder;

		}

	}

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {

		if(!inputFolder.substring(inputFolder.length()-1, inputFolder.length()).equals(File.separator)){

			this.inputFolder = inputFolder + File.separator;

		} else {
			
			this.inputFolder = inputFolder;
			
		}
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	private String getOutputFilename(){

		File file = new File(this.getInputFile());
		return this.getOutputFolder() + file.getName() + ".nt";
	}

}