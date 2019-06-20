[![](https://www.uni-muenster.de/imperia/md/images/allgemein/farbunabhaengig/wwu.svg)](https://www.uni-muenster.de/de/)

# MusicXML to RDF Converter (BETA)

This converter parses MusicXML files and converts them to RDF, based on the [MusicOWL Ontology](http://linkeddata.uni-muenster.de/ontology/musicscore/mso.owl). It currently supports the following concepts:

 
 * Articulations
 * Clefs
 * Durations
 * Dynamics
 * Measures (Bars)
 * Note Sets
 * Notes (Pitches)
 * Staves
 * Tonalities 
 * Voices

## Using the Java API

```java
package de.wwu.music2rdf.example;

import java.io.File;
import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.music2rdf.core.Collection;
import de.wwu.music2rdf.core.Person;
import de.wwu.music2rdf.core.Role;

public class Example {

	public static void main(String[] args) {
				
		MusicXML2RDF music2rdf = new MusicXML2RDF();
			
		music2rdf.setInputFile(new File("musicxml/elgar_cello_concerto_op.85.xml"));
		music2rdf.setOutputFile("ntriples/elgar_cello_concerto_op.85.nt");
		music2rdf.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg");
		music2rdf.setScoreURI("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		music2rdf.addCollection(new Collection("https://url.collection.de","My Collection"));
		music2rdf.addPerson(new Person("http://dbpedia.org/resource/Edward_Elgar","Sir Edward William Elgar",Role.COMPOSER));
		music2rdf.addPerson(new Person("http://jimjones.de","Jim Jones",Role.ENCODER));		
		music2rdf.setDocumentTitle("Cellokonzert e-Moll op. 85");
		music2rdf.setDateIssued("1919"); //Formats accepted: yyyy, yyyyMM, yyyyMMdd.
		
		music2rdf.parseMusicXML();
		
	}

}

```

## Using the Java API via Terminal
Converting a MusicXML file:

```shell
$ java -jar musicowl-converter.jar 
file=/home/jones/git/musicowl/musicowl/musicxml/elgar_cello_concerto_op.85.xml 
output=/home/jones/git/musicowl/musicowl/ntriples/elgar_cello_concerto_op.85.nt 
uri="http://dbpedia.org/resource/Cello_Concerto_(Elgar)" 
collectionURI=https://url.collection.de collectionName="My Collection" 
person="http://dbpedia.org/resource/Edward_Elgar","Sir Edward Elgar","Composer" 
person="http://jimjones.de","Jim Jones","Encoder" 
thumbnail="https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg" 
dateIssued=1919

File 	         : /home/jones/git/musicowl/musicowl/musicxml/elgar_cello_concerto_op.85.xml
Output File      : /home/jones/git/musicowl/musicowl/ntriples/elgar_cello_concerto_op.85_console.nt
URI 	         : http://dbpedia.org/resource/Cello_Concerto_(Elgar)
Collection URI   : https://url.collection.de
Collection Name  : My Collection
Person URI       : http://dbpedia.org/resource/Edward_Elgar
Person Name      : Sir Edward Elgar
Person Role      : Composer
Person URI       : http://jimjones.de
Person Name      : Jim Jones
Person Role      : Encoder
Thumbnail        : https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Elgar-cello-concerto-manuscript.jpg/220px-Elgar-cello-concerto-manuscript.jpg
Date Issued      : 1919

[2019-06-15 11:53:30,864] INFO  [Converter] - Processing elgar_cello_concerto_op.85.xml ...
[2019-06-15 11:53:31,301] INFO  [Converter] - Loading XML file: 433 ms
[2019-06-15 11:53:31,550] INFO  [Converter] - [Violoncello] Parsing movement: 1 (Adagio) ... 
[2019-06-15 11:53:31,551] INFO  [Converter] - 	[Key] Measure 1 Mode: major | Fifth: 1
[2019-06-15 11:53:31,551] INFO  [Converter] - 	[Time] Measure 1 Beats: 4 | Beat-type: 4
[2019-06-15 11:53:31,552] INFO  [Converter] - 	[Clef] Measure 1 Sign: F | Line: 4
[2019-06-15 11:53:31,557] INFO  [Converter] - 	[Clef] Measure 7 Sign: C | Line: 4
[2019-06-15 11:53:31,557] INFO  [Converter] - 	[Time] Measure 9 Beats: 9 | Beat-type: 8
[2019-06-15 11:53:31,559] INFO  [Converter] - 	[Clef] Measure 18 Sign: F | Line: 4
[2019-06-15 11:53:31,561] INFO  [Converter] - 	[Clef] Measure 25 Sign: C | Line: 4
[2019-06-15 11:53:31,562] INFO  [Converter] - 	[Clef] Measure 28 Sign: G | Line: 2
[2019-06-15 11:53:31,564] INFO  [Converter] - 	[Clef] Measure 38 Sign: C | Line: 4
[2019-06-15 11:53:31,565] INFO  [Converter] - 	[Clef] Measure 42 Sign: F | Line: 4
[2019-06-15 11:53:31,566] INFO  [Converter] - 	[Clef] Measure 47 Sign: C | Line: 4
[2019-06-15 11:53:31,567] INFO  [Converter] - 	[Time] Measure 48 Beats: 12 | Beat-type: 8
[2019-06-15 11:53:31,567] INFO  [Converter] - 	[Clef] Measure 50 Sign: G | Line: 2
[2019-06-15 11:53:31,568] INFO  [Converter] - 	[Clef] Measure 52 Sign: F | Line: 4
[2019-06-15 11:53:31,569] INFO  [Converter] - 	[Key] Measure 55 Mode: major | Fifth: 4
[2019-06-15 11:53:31,569] INFO  [Converter] - 	[Clef] Measure 55 Sign: C | Line: 4
[2019-06-15 11:53:31,570] INFO  [Converter] - 	[Clef] Measure 61 Sign: G | Line: 2
[2019-06-15 11:53:31,572] INFO  [Converter] - 	[Time] Measure 66 Beats: 6 | Beat-type: 8
[2019-06-15 11:53:31,572] INFO  [Converter] - 	[Time] Measure 67 Beats: 12 | Beat-type: 8
[2019-06-15 11:53:31,575] INFO  [Converter] - 	[Time] Measure 74 Beats: 6 | Beat-type: 8
[2019-06-15 11:53:31,575] INFO  [Converter] - 	[Key] Measure 75 Mode: major | Fifth: 1
[2019-06-15 11:53:31,576] INFO  [Converter] - 	[Time] Measure 75 Beats: 12 | Beat-type: 8
[2019-06-15 11:53:31,577] INFO  [Converter] - 	[Time] Measure 80 Beats: 9 | Beat-type: 8
[2019-06-15 11:53:31,578] INFO  [Converter] - 	[Clef] Measure 81 Sign: C | Line: 4
[2019-06-15 11:53:31,578] INFO  [Converter] - 	[Clef] Measure 82 Sign: C | Line: 4
[2019-06-15 11:53:31,579] INFO  [Converter] - 	[Clef] Measure 83 Sign: F | Line: 4
[2019-06-15 11:53:31,580] INFO  [Converter] - 	[Clef] Measure 88 Sign: G | Line: 2
[2019-06-15 11:53:31,582] INFO  [Converter] - 	[Clef] Measure 99 Sign: C | Line: 4
[2019-06-15 11:53:31,582] INFO  [Converter] - 	[Clef] Measure 101 Sign: F | Line: 4
[2019-06-15 11:53:31,583] WARN  [Converter] - The title "Cellokonzert e-Moll op. 85" was provided and will therefore overwrite the title provided in the MusicXML document.
[2019-06-15 11:53:31,583] INFO  [Converter] - Creating MusicScore object: 281 ms
[2019-06-15 11:53:31,760] INFO  [Converter] - Score serialization: 177 ms

```
