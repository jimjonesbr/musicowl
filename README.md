[![](https://www.nebenbei-studieren.de/wp-content/uploads/2015/11/uni_muenster_logo.jpg ) ](https://www.uni-muenster.de/de/)

# MusicXML to RDF Converter (BETA)

This converter parses MusicXML files and converts them to RDF based on the [MusicOWL](http://linkeddata.uni-muenster.de/ontology/musicscore/mso.owl). It currently supports the following concepts:

 
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
import java.io.File;
import de.wwu.music2rdf.converter.MusicXML2RDF;

public class Example {
	public static void main(String[] args) {
		MusicXML2RDF music2rdf = new MusicXML2RDF();		
		music2rdf.setOutputFile("/home/user/elgar_cello_concerto.nt");
		music2rdf.parseMusicXML(new File("/home/user/elgar_cello_concerto.xml"));			
	}
}
```

## Using the Java API via Terminal
Converting a single MusicXML file:

```shell

$ java -jar file=/home/user/musicxml/file.xml output=/home/user/rdf/

```

Converting all MusicXML files in a directory:

```shell

$ java -jar folder=/home/user/musicxml/ output=/home/user/rdf/

```

Activating verbose processing:

```shell

$ java -jar folder=/home/user/musicxml/ output=/home/user/rdf/ mode=verbose

```

