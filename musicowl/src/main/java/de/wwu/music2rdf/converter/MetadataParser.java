package de.wwu.music2rdf.converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.ScoreResource;

public class MetadataParser {

	private static Logger logger = Logger.getLogger("Parser-Util");


	public static MusicXML2RDF parseMetadataFile(File metadataFile) {

		MusicXML2RDF music2rdf = new MusicXML2RDF();

		if(isXMLValid(metadataFile)) {

			try {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(metadataFile);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("score");

				for (int j = 0; j < nList.getLength(); j++) {

					Node nNode = nList.item(j);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element scoreElement = (Element) nNode;

						System.out.println("Score URI       : " + scoreElement.getElementsByTagName("scoreIdentifier").item(0).getTextContent());
						System.out.println("Score Title     : " + scoreElement.getElementsByTagName("scoreTitle").item(0).getTextContent());
						System.out.println("Date Issued     : " + scoreElement.getElementsByTagName("issued").item(0).getTextContent());
						System.out.println("Thumbnail       : " + scoreElement.getElementsByTagName("thumbnail").item(0).getTextContent());

						music2rdf.setScoreIdentifier(scoreElement.getElementsByTagName("scoreIdentifier").item(0).getTextContent());
						music2rdf.setScoreTitle(scoreElement.getElementsByTagName("scoreTitle").item(0).getTextContent());
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
								System.out.println("Resource Label  : " + personElement.getElementsByTagName("resourceLabel").item(0).getTextContent());
								System.out.println("Resource Type   : " + personElement.getElementsByTagName("resourceType").item(0).getTextContent());

								String resourceURI = personElement.getElementsByTagName("resourceURL").item(0).getTextContent();
								String resourceDescription = personElement.getElementsByTagName("resourceLabel").item(0).getTextContent();
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

								System.out.println("Collection Identifier  : " + collectionElement.getElementsByTagName("collectionIdentifier").item(0).getTextContent());
								System.out.println("Collection Label : " + collectionElement.getElementsByTagName("collectionLabel").item(0).getTextContent());


								String collectionURL = collectionElement.getElementsByTagName("collectionIdentifier").item(0).getTextContent();
								String collectionName = collectionElement.getElementsByTagName("collectionLabel").item(0).getTextContent();

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

		} else if (isJSONValid(metadataFile)) {

			logger.info("Parsing JSON metadata file: " + metadataFile.getAbsolutePath() );

			try {

				JSONParser parser = new JSONParser();	
				String str = FileUtils.readFileToString(metadataFile, StandardCharsets.UTF_8);						
				Object obj = parser.parse(str);
				JSONObject jsonObject = (JSONObject) obj;

				System.out.println("\nScore URI       : " + jsonObject.get("scoreIdentifier").toString());
				System.out.println("Title           : " + jsonObject.get("scoreTitle").toString());
				System.out.println("Date Issued     : " + jsonObject.get("issued").toString());
				System.out.println("Thumbnail       : " + jsonObject.get("thumbnail").toString());

				music2rdf.setScoreIdentifier(jsonObject.get("scoreIdentifier").toString());
				music2rdf.setScoreTitle(jsonObject.get("scoreTitle").toString());
				music2rdf.setDateIssued(jsonObject.get("issued").toString());
				music2rdf.setThumbnail(jsonObject.get("thumbnail").toString());

				JSONArray collections = (JSONArray) jsonObject.get("collections");

				for (int j = 0; j < collections.size(); j++) {

					JSONObject collection = (JSONObject) collections.get(j);

					music2rdf.addCollection(new Collection(collection.get("collectionIdentifier").toString(),collection.get("collectionLabel").toString()));

					System.out.println("Collection Identifier: " + collection.get("collectionIdentifier").toString());
					System.out.println("Collection Label 	 : " + collection.get("collectionLabel").toString());

				}

				JSONArray persons = (JSONArray) jsonObject.get("persons");

				for (int j = 0; j < persons.size(); j++) {

					JSONObject person = (JSONObject) persons.get(j);

					System.out.println("Person URI      : " + person.get("personIdentifier").toString());
					System.out.println("Person Name     : " + person.get("personName").toString());
					System.out.println("Person Role     : " + person.get("personRole").toString());

					music2rdf.addPerson(new Person(person.get("personIdentifier").toString(),
							person.get("personName").toString(),
							person.get("personRole").toString()));

				}

				JSONArray resources = (JSONArray) jsonObject.get("resources");

				for (int j = 0; j < resources.size(); j++) {

					JSONObject resource = (JSONObject) resources.get(j);

					System.out.println("Resource URI    : " + resource.get("resourceURL").toString());
					System.out.println("Resource Label  : " + resource.get("resourceLabel").toString());
					System.out.println("Resource Type   : " + resource.get("resourceType").toString());

					music2rdf.addResource(new ScoreResource(resource.get("resourceURL").toString(), 
							resource.get("resourceLabel").toString(), 
							resource.get("resourceType").toString()));
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}



		} else {

			logger.fatal("Invalid fetadata file: " + metadataFile.getAbsolutePath());
		}

		return music2rdf;
	}

	public static boolean isXMLValid(File xml) {
		boolean result = false;				
		try {
			if(FileUtils.readFileToString(xml, StandardCharsets.UTF_8).startsWith("<")) {
				result = true;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isJSONValid(File json) {

		boolean result = false;				
		try {
			if(FileUtils.readFileToString(json, StandardCharsets.UTF_8).startsWith("{")) {
				result = true;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
