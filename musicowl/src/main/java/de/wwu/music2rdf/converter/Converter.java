package de.wwu.music2rdf.converter;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.ScoreResource;

public class Converter {

	public static void main(String[] args) {

//		String collectionURI = "";
//		String collectionName = "";		
		MusicXML2RDF music2rdf = new MusicXML2RDF();

		System.out.println("\n");
		for(int i = 0; i < args.length; i++) {
			
			if(args[i].toLowerCase().equals("version")){
								
				System.out.println("Music2RDF Converter ");
				System.out.println("Version: "+music2rdf.getVersion());
				System.exit(0);
				
			}
			
			String[] parameter = args[i].split("=");
			
			if(parameter[0].toLowerCase().equals("file")){
				System.out.println("File 	        : " + parameter[1]);
				music2rdf.setInputFile(new File(parameter[1]));
			}

			if(parameter[0].toLowerCase().equals("output")){
				music2rdf.setOutputFile(parameter[1]);
				System.out.println("Output File     : " + parameter[1]);
			}

//			if(parameter[0].toLowerCase().equals("uri")){
//				System.out.println("URI 	         : " + parameter[1]);				
//				music2rdf.setScoreURI(parameter[1]);
//			}
//
//			if(parameter[0].toLowerCase().equals("thumbnail")){
//				System.out.println("Thumbnail        : " + parameter[1]);				
//				music2rdf.setThumbnail(parameter[1]);
//			}
//
//			if(parameter[0].toLowerCase().equals("collectionuri")){
//				System.out.println("Collection URI   : " + parameter[1]);				
//				collectionURI = parameter[1];
//			}
//
//			if(parameter[0].toLowerCase().equals("collectionname")){
//				System.out.println("Collection Name  : " + parameter[1]);				
//				collectionName = parameter[1];
//			}
//
//			if(parameter[0].toLowerCase().equals("title")){
//				System.out.println("Score Title : " + parameter[1]);				
//				music2rdf.setDocumentTitle(parameter[1]);				
//			}
//			
//			if(parameter[0].toLowerCase().equals("dateissued")){
//				System.out.println("Date Issued      : " + parameter[1]);				
//				music2rdf.setDateIssued(parameter[1]);				
//			}

			if(parameter[0].toLowerCase().equals("verbose")){
				System.out.println("Verbose      	: " + parameter[1]);				
				music2rdf.isVerbose(Boolean.valueOf(parameter[1]));				
			}
			
			if(parameter[0].toLowerCase().equals("outputformat")){
				System.out.println("OutputFormat    : " + parameter[1]);				
				music2rdf.setOutputFormat(parameter[1]);				
			}
			
//			if(parameter[0].toLowerCase().equals("person")){
//
//				String[] tokens = parameter[1].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//				System.out.println("Person URI       : "+tokens[0]);
//				System.out.println("Person Name      : "+tokens[1]);
//				System.out.println("Person Role      : "+tokens[2]);
//
//				music2rdf.addPerson(new Person(tokens[0].replaceAll("[\"]", ""),tokens[1].replaceAll("[\"]", ""),tokens[2].replaceAll("[\"]", "")));	
//
//			}
			
			if(parameter[0].toLowerCase().equals("metadata")){
				
				try {
					
					File fXmlFile = new File(parameter[1]);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();
					
					NodeList nList = doc.getElementsByTagName("score");
					
					for (int j = 0; j < nList.getLength(); j++) {
						
						Node nNode = nList.item(j);
						
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							
							Element scoreElement = (Element) nNode;
							
							System.out.println("Score URI       : " + scoreElement.getElementsByTagName("scoreIdentifier").item(0).getTextContent());
							System.out.println("Title           : " + scoreElement.getElementsByTagName("title").item(0).getTextContent());
							System.out.println("Date Issued     : " + scoreElement.getElementsByTagName("issued").item(0).getTextContent());
							System.out.println("Thumbnail       : " + scoreElement.getElementsByTagName("thumbnail").item(0).getTextContent());
							
							music2rdf.setScoreURI(scoreElement.getElementsByTagName("scoreIdentifier").item(0).getTextContent());
							music2rdf.setDocumentTitle(scoreElement.getElementsByTagName("title").item(0).getTextContent());
							music2rdf.setDateIssued(scoreElement.getElementsByTagName("issued").item(0).getTextContent());
							music2rdf.setThumbnail(scoreElement.getElementsByTagName("thumbnail").item(0).getTextContent());
													
							Element personsElement = (Element) scoreElement.getElementsByTagName("persons").item(0);
							
							NodeList personList = personsElement.getElementsByTagName("person");
							
							for (int k = 0; k < personList.getLength(); k++) {
								
								Node personNode = personList.item(k);
								
								if (personNode.getNodeType() == Node.ELEMENT_NODE) {
									
									Element personElement = (Element) personNode;
									
									System.out.println("Person URI      : " + personElement.getElementsByTagName("personIdentifier").item(0).getTextContent());
									System.out.println("Person Name     : " + personElement.getElementsByTagName("personName").item(0).getTextContent());
									System.out.println("Person Role     : " + personElement.getElementsByTagName("personRole").item(0).getTextContent());
									
									String personURI = personElement.getElementsByTagName("personIdentifier").item(0).getTextContent();
									String personName = personElement.getElementsByTagName("personName").item(0).getTextContent();
									String personRole = personElement.getElementsByTagName("personRole").item(0).getTextContent();
									
									music2rdf.addPerson(new Person(personURI,personName,personRole));
									
								}
							}
							
							
							Element resourcesElement = (Element) scoreElement.getElementsByTagName("resources").item(0);
							
							NodeList resourceList = resourcesElement.getElementsByTagName("resource");
							
							for (int k = 0; k < resourceList.getLength(); k++) {
								
								Node resourceNode = resourceList.item(k);
								
								if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
									
									Element personElement = (Element) resourceNode;
									
									System.out.println("Resource URI    : " + personElement.getElementsByTagName("resourceURL").item(0).getTextContent());
									System.out.println("Resource Desc.  : " + personElement.getElementsByTagName("resourceDescription").item(0).getTextContent());
									System.out.println("Resource Type   : " + personElement.getElementsByTagName("resourceType").item(0).getTextContent());
									
									String resourceURI = personElement.getElementsByTagName("resourceURL").item(0).getTextContent();
									String resourceDescription = personElement.getElementsByTagName("resourceDescription").item(0).getTextContent();
									String resourceType = personElement.getElementsByTagName("resourceType").item(0).getTextContent();
									
									music2rdf.addResource(new ScoreResource(resourceURI, resourceDescription,resourceType));
								}
							}
							
							
							Element collectionsElement = (Element) scoreElement.getElementsByTagName("collections").item(0);
							
							NodeList collectionList = collectionsElement.getElementsByTagName("collection");
							
							for (int k = 0; k < collectionList.getLength(); k++) {
								
								Node collectionNode = collectionList.item(k);
								
								if (collectionNode.getNodeType() == Node.ELEMENT_NODE) {
									
									Element collectionElement = (Element) collectionNode;
									
									System.out.println("Collection URI  : " + collectionElement.getElementsByTagName("collectionURL").item(0).getTextContent());
									System.out.println("Collection Name : " + collectionElement.getElementsByTagName("collectionName").item(0).getTextContent());
									
									
									String collectionURL = collectionElement.getElementsByTagName("collectionURL").item(0).getTextContent();
									String collectionName = collectionElement.getElementsByTagName("collectionName").item(0).getTextContent();
																		
									music2rdf.addCollection(new Collection(collectionURL,collectionName));
								}
							}
							
						}
						
						
					}
					
					
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				

				
				
			}

		}

		System.out.println("\n");

//		music2rdf.addCollection(new Collection(collectionURI,collectionName));

		music2rdf.parseMusicXML();


	}


}