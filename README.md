[![](https://www.uni-muenster.de/imperia/md/images/allgemein/farbunabhaengig/wwu.svg)](https://www.uni-muenster.de/de/)

[![Build Status](https://travis-ci.com/jimjonesbr/musicowl.svg?branch=master)](https://travis-ci.com/jimjonesbr/musicowl)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

# MusicXML to RDF Converter

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

If you don't feel like building the Musci2RDF converter from soruce, visit the [releases area](https://github.com/jimjonesbr/musicowl/releases) to get a prebuilt jar. You can execute it from your terminal using the following syntax:

```shell
$ java -jar music2rdf-[VERSION].jar 
	file=/home/jones/git/musicowl/musicowl/musicxml/elgar_cello_concerto_op.85.xml 
	output=/home/jones/git/musicowl/musicowl/rdf/elgar_cello_concerto_op.85 
	uri="http://dbpedia.org/resource/Cello_Concerto_(Elgar)" 
	collectionURI=https://url.collection.de 
	collectionName="Great Composers" 
	person="http://dbpedia.org/resource/Edward_Elgar","Sir Edward Elgar","Composer" 
	person="http://jimjones.de","Jim Jones","Encoder" 
	thumbnail="https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg" 
	dateIssued=1919 
	outputformat=TURTLE 
	verbose=false


File 	         : /home/jones/git/musicowl/musicowl/musicxml/elgar_cello_concerto_op.85.xml
Output File      : /home/jones/git/musicowl/musicowl/rdf/elgar_cello_concerto_op.85
URI 	         : http://dbpedia.org/resource/Cello_Concerto_(Elgar)
Collection URI   : https://url.collection.de
Collection Name  : Great Composers
Person URI       : http://dbpedia.org/resource/Edward_Elgar
Person Name      : Sir Edward Elgar
Person Role      : Composer
Person URI       : http://jimjones.de
Person Name      : Jim Jones
Person Role      : Encoder
Thumbnail        : https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg
Date Issued      : 1919
OutputFormat     : TURTLE
Verbose      	 : false



[2019-07-02 16:23:52,397] INFO  [Converter] - Processing elgar_cello_concerto_op.85.xml ...
[2019-07-02 16:23:52,528] INFO  [Converter] - Loading XML file: 131 ms
[2019-07-02 16:23:52,684] INFO  [Converter] - Creating MusicScore object: 156 ms
[2019-07-02 16:23:53,347] INFO  [Converter] - Score serialization: 662 ms

```
