package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MusicOWL {

    public static final String NS = "http://linkeddata.uni-muenster.de/ontology/musicscore#";

	private static Model musicowl_model = ModelFactory.createDefaultModel();
	//public static final Resource NAMESPACE = musicowl_model.createResource(NS);
    //public static String getURI() {return NS;}

	public static final Property hasBeatUnit = musicowl_model.createProperty(NS + "hasBeatUnit");
	public static final Property hasBeatsPerMinute = musicowl_model.createProperty(NS + "hasBeatsPerMinute");
	public static final Property hasScorePart = musicowl_model.createProperty(NS + "hasScorePart");
	public static final Property isSolo = musicowl_model.createProperty(NS + "isSolo");
	public static final Property isEnsemble = musicowl_model.createProperty(NS + "isEnsemble");
	public static final Property hasMeasure = musicowl_model.createProperty(NS + "hasMeasure");
	public static final Property nextMeasure = musicowl_model.createProperty(NS + "nextMeasure");
	public static final Property nextInstant = musicowl_model.createProperty(NS + "nextInstant");
	public static final Property hasKey = musicowl_model.createProperty(NS + "hasKey");
	public static final Property hasTime = musicowl_model.createProperty(NS + "hasTime");
	public static final Property hasBarline = musicowl_model.createProperty(NS + "hasBarline");
	public static final Property nextNoteSet = musicowl_model.createProperty(NS + "nextNoteSet");
	public static final Property hasVoice = musicowl_model.createProperty(NS + "hasVoice");
	public static final Property hasNoteSet = musicowl_model.createProperty(NS + "hasNoteSet"); 
	public static final Property hasClef = musicowl_model.createProperty(NS + "hasClef");
	public static final Property hasArticulation = musicowl_model.createProperty(NS + "hasArticulation");
	public static final Property hasDuration = musicowl_model.createProperty(NS + "hasDuration");
	public static final Property sign = musicowl_model.createProperty(NS + "sign");
	public static final Property line = musicowl_model.createProperty(NS + "line");
	public static final Property hasNote = musicowl_model.createProperty(NS + "hasNote");
	public static final Property hasOctave = musicowl_model.createProperty(NS + "hasOctave");
	public static final Property hasDurationAttribute = musicowl_model.createProperty(NS + "hasDurationAttribute");
	public static final Property hasDynamic = musicowl_model.createProperty(NS + "hasDynamic");
	public static final Property asMusicXML = musicowl_model.createProperty(NS + "asMusicXML");
	public static final Property asMEI = musicowl_model.createProperty(NS + "asMEI");
	public static final Property hasBeats = musicowl_model.createProperty(NS + "hasBeats");
	public static final Property hasBeatType = musicowl_model.createProperty(NS + "hasBeatType");
	public static final Property hasStaff = musicowl_model.createProperty(NS + "hasStaff");

    public static final Resource Instant  = musicowl_model.createProperty(NS + "Instant");
    public static final Resource Measure  = musicowl_model.createProperty(NS + "Measure");
    public static final Resource TimeSignature  = musicowl_model.createProperty(NS + "TimeSignature");
    public static final Resource EndRepeat  = musicowl_model.createProperty(NS + "EndRepeat");
    public static final Resource BeginRepeat  = musicowl_model.createProperty(NS + "BeginRepeat");
    public static final Resource Staff  = musicowl_model.createProperty(NS + "Staff");
    public static final Resource Voice  = musicowl_model.createProperty(NS + "Voice");
    public static final Resource NoteSet  = musicowl_model.createProperty(NS + "NoteSet");
    public static final Resource Alto  = musicowl_model.createProperty(NS + "Alto");
    public static final Resource Baritone  = musicowl_model.createProperty(NS + "Baritone");
    public static final Resource Bass  = musicowl_model.createProperty(NS + "Bass");
    public static final Resource FrenchViolin  = musicowl_model.createProperty(NS + "FrenchViolin");
    public static final Resource MezzoSoprano  = musicowl_model.createProperty(NS + "MezzoSoprano");
    public static final Resource Soprano  = musicowl_model.createProperty(NS + "Soprano");
    public static final Resource SubBass  = musicowl_model.createProperty(NS + "SubBass");
    public static final Resource Tenor  = musicowl_model.createProperty(NS + "Tenor");
    public static final Resource Trebble  = musicowl_model.createProperty(NS + "Trebble");
    public static final Resource Percussion  = musicowl_model.createProperty(NS + "Percussion");
    public static final Resource Whole  = musicowl_model.createProperty(NS + "Whole");
    public static final Resource Quarter  = musicowl_model.createProperty(NS + "Quarter");
    public static final Resource QuadrupleWhole  = musicowl_model.createProperty(NS + "QuadrupleWhole");
    public static final Resource OctupleWhole  = musicowl_model.createProperty(NS + "OctupleWhole");
    public static final Resource Half  = musicowl_model.createProperty(NS + "Half");
    public static final Resource Eighth  = musicowl_model.createProperty(NS + "Eighth");
    public static final Resource DoubleWhole  = musicowl_model.createProperty(NS + "DoubleWhole");
    public static final Resource D64th = musicowl_model.createProperty(NS + "64th");
    public static final Resource D32nd  = musicowl_model.createProperty(NS + "32nd");
    public static final Resource D16th  = musicowl_model.createProperty(NS + "16th");
    public static final Resource D128th  = musicowl_model.createProperty(NS + "128th");
    public static final Resource TempoMarking  = musicowl_model.createProperty(NS + "TempoMarking");
    public static final Resource Duration  = musicowl_model.createProperty(NS + "Duration");
    public static final Resource Note  = musicowl_model.createProperty(NS + "Note");
    public static final Resource ScorePart  = musicowl_model.createProperty(NS + "ScorePart");
    public static final Resource Dot  = musicowl_model.createProperty(NS + "Dot");
    public static final Resource DoubleDot  = musicowl_model.createProperty(NS + "DoubleDot");
    public static final Resource TripleDot  = musicowl_model.createProperty(NS + "TripleDot");
    
    
  		

}
