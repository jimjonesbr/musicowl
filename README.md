[![](https://www.uni-muenster.de/imperia/md/images/allgemein/farbunabhaengig/wwu.svg)](https://www.uni-muenster.de/de/)

[![Build Status](https://travis-ci.com/jimjonesbr/musicowl.svg?branch=master)](https://travis-ci.com/jimjonesbr/musicowl)
[![Maven Central](https://img.shields.io/maven-central/v/de.uni-muenster/music2rdf.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22de.uni-muenster%22%20AND%20a:%22music2rdf%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

# Music Score to RDF Converter

This converter parses MusicXML 3.0 files and converts them to RDF, based on the [MusicOWL Ontology](http://linkeddata.uni-muenster.de/ontology/musicscore/mso.owl) [ˈmjuːzɪkəl]. It fully supports the following concepts:
 
 * Articulations
 * Clefs
 * Durations
 * Dynamics
 * Measures (Bars)
 * Note Sets
 * Notes
 * Staves
 * Tonalities 
 * Voices

## Parameters

`setInputFile`&nbsp;   MusicXML 3.0 file to be converted. Supported are `.xml` and `.mxl` files.

`setOutputFile`&nbsp;   Path and name for RDF output file.

`setThumbnail`&nbsp;   Thumbnail for the converted music score (optional).

`setScoreURI`&nbsp;   URI for the converted score (optional) .

`setDocumentTitle`&nbsp;   Music score title.

`issued`&nbsp;   Date issued. Formats accepted: `yyyy`, `yyyyMM`, `yyyyMMdd` (optional).

`setCollection`&nbsp;   Collections can be used for classifying music scores into certaing groups, e.g. "18th Century Composers", "Advanced Music Scores", etc.

`isVerbose`&nbsp;   Setting it to `true` logs in the console the converter progress.

`setOutputFormat`&nbsp;   Sets the RDF notation for the output file. Accepted notations are: `TURLTE`, `NTRIPLES`, `JSON-LD`, `RDF/XML`, `RDF/JSON`.

`addPerson`&nbsp;   This method can be used to add persons related to the music score. It consists of an URI, a name and a role, which can have one of the following attributes:

```java
Role.ARRANGER;
Role.COMPOSER;
Role.EDITOR;
Role.ENCODER;
Role.LIBRETTIST;
Role.LYRICIST;
Role.PERFORMER;
Role.TRANSLATOR;
Role.UNKNOWN;
```

`addResource`&nbsp;   Links existing resources to the converted music score, such as pdf or mp3 files. This method relies on three parameters:

- **Link**: Link to the resource
- **Description**: Text describing the resource
- **Type**: MIME Type

## Using the Java API

The above mentioned parameters can be used as follows:

```java
package de.wwu.music2rdf.example;

import java.io.File;
import com.google.common.net.MediaType;
import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;
import de.wwu.music2rdf.core.ScoreResource;

public class Example {

	public static void main(String[] args) {
				
		MusicXML2RDF music2rdf = new MusicXML2RDF();
			
		music2rdf.setInputFile(new File("musicxml/ulb-muenster/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("rdf/elgar_cello_concerto_op.85");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://wwu.greatcomposers.de","Great Composers"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));
		music2rdf.addResource(new ScoreResource("https://musescore.com/score/152011/download/pdf", "Print",MediaType.PDF.toString()));
		music2rdf.addResource(new ScoreResource("https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)", "Wikipedia Article",MediaType.HTML_UTF_8.toString()));
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		music2rdf.isVerbose(false);
		music2rdf.setOutputFormat("turtle");
		music2rdf.setDateIssued("1919"); //Formats accepted: yyyy, yyyyMM, yyyyMMdd.
		
		music2rdf.parseMusicXML();
		
	}

}

```

## Using the converter in the console

If you don't feel like building the Musci2RDF converter from source, visit the [releases area](https://github.com/jimjonesbr/musicowl/releases) to get a prebuilt jar. You can use this jar with following parameters:

`metadata`&nbsp; In order to keep the full metadata support via console, you have to pass the metadata in separated xml file. The XML file has to be encoded in the following format:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<score>
	<scoreIdentifier>http://dbpedia.org/resource/Cello_Concerto_(Elgar)</scoreIdentifier>
	<title>Cellokonzert e-Moll op. 85</title>
	<thumbnail>https://www.rcm.ac.uk/media/Elgar%20Cello%20Concerto%20maunscript%206x4.jpg</thumbnail>
	<issued>1919</issued>
	<collections>
		<collection>
			<collectionName>Great Composers</collectionName>
			<collectionURL>https://wwu.greatcomposers.de</collectionURL>
		</collection>
	</collections>
	<persons>
		<person>
			<personIdentifier>http://dbpedia.org/resource/Edward_Elgar</personIdentifier>
			<personName>Sir Edward William Elgar</personName>
			<personRole>Composer</personRole>
		</person>
		<person>
			<personIdentifier>http://jimjones.de</personIdentifier>
			<personName>Jim Jones</personName>
			<personRole>Encoder</personRole>
		</person>
	</persons>
	<resources>
		<resource>
			<resourceURL>https://musescore.com/score/152011/download/pdf</resourceURL>
			<resourceDescription>Print</resourceDescription>
			<resourceType>application/pdf</resourceType>
		</resource>
		<resource>
			<resourceURL>https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)</resourceURL>
			<resourceDescription>Wikipedia Article</resourceDescription>
			<resourceType>text/html</resourceType>
		</resource>
	</resources>
</score>

```

`file` &nbsp; Music score file.

`output`&nbsp;  Name for the output file.

`outputFormat`&nbsp; RDF notation for the output file

Example (using the above described metadata file):

```shell
$ java -jar music2rdf-[VERSION].jar file=musicxml/metadata-file/elgar_cello_concerto_op.85.xml
metadata=musicxml/elgar_cello_concerto_op.85-metadata.xml 
output=rdf/elgar_cello_concerto_op.85 
outputFormat=turtle

File  	        : musicxml/elgar_cello_concerto_op.85.xml
Score URI       : http://dbpedia.org/resource/Cello_Concerto_(Elgar)
Title           : Cellokonzert e-Moll op. 85
Date Issued     : 1919
Thumbnail       : https://www.rcm.ac.uk/media/Elgar%20Cello%20Concerto%20maunscript%206x4.jpg
Person URI      : http://dbpedia.org/resource/Edward_Elgar
Person Name     : Sir Edward William Elgar
Person Role     : Composer
Person URI      : http://jimjones.de
Person Name     : Jim Jones
Person Role     : Encoder
Resource URI    : https://musescore.com/score/152011/download/pdf
Resource Desc.  : Print
Resource Type   : application/pdf
Resource URI    : https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)
Resource Desc.  : Wikipedia Article
Resource Type   : text/html
Collection URI  : https://wwu.greatcomposers.de
Collection Name : Great Composers
Output File     : rdf/elgar_cello_concerto_op.85
OutputFormat    : turtle

[2019-08-11 19:33:14,788] INFO  [Converter] - Processing elgar_cello_concerto_op.85.xml ...
[2019-08-11 19:33:15,011] INFO  [Converter] - Loading XML file: 222 ms
[2019-08-11 19:33:15,341] WARN  [Converter] - The title "Cellokonzert e-Moll op. 85" was provided and will therefore overwrite the title provided in the MusicXML document.
[2019-08-11 19:33:15,341] INFO  [Converter] - Creating MusicScore object: 329 ms
[2019-08-11 19:33:16,224] INFO  [Converter] - Score serialization: 883 ms
```
