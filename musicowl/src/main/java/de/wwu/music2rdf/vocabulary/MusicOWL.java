package de.wwu.music2rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class MusicOWL {

    public static final String NS = "http://linkeddata.uni-muenster.de/ontology/musicscore#";

	private static Model musicowl = ModelFactory.createDefaultModel();
	//public static final Resource NAMESPACE = musicowl_model.createResource(NS);
    //public static String getURI() {return NS;}

	public static final Property hasBeatUnit = musicowl.createProperty(NS + "hasBeatUnit");
	public static final Property hasBeatsPerMinute = musicowl.createProperty(NS + "hasBeatsPerMinute");
	public static final Property hasScorePart = musicowl.createProperty(NS + "hasScorePart");
	public static final Property isSolo = musicowl.createProperty(NS + "isSolo");
	public static final Property isEnsemble = musicowl.createProperty(NS + "isEnsemble");
	public static final Property hasMeasure = musicowl.createProperty(NS + "hasMeasure");
	public static final Property nextMeasure = musicowl.createProperty(NS + "nextMeasure");
	public static final Property nextInstant = musicowl.createProperty(NS + "nextInstant");
	public static final Property hasKey = musicowl.createProperty(NS + "hasKey");
	public static final Property hasTime = musicowl.createProperty(NS + "hasTime");
	public static final Property hasBarline = musicowl.createProperty(NS + "hasBarline");
	public static final Property nextNoteSet = musicowl.createProperty(NS + "nextNoteSet");
	public static final Property hasVoice = musicowl.createProperty(NS + "hasVoice");
	public static final Property hasNoteSet = musicowl.createProperty(NS + "hasNoteSet"); 
	public static final Property hasClef = musicowl.createProperty(NS + "hasClef");
	public static final Property hasArticulation = musicowl.createProperty(NS + "hasArticulation");
	public static final Property hasDuration = musicowl.createProperty(NS + "hasDuration");
	public static final Property sign = musicowl.createProperty(NS + "sign");
	public static final Property line = musicowl.createProperty(NS + "line");
	public static final Property hasNote = musicowl.createProperty(NS + "hasNote");
	public static final Property hasOctave = musicowl.createProperty(NS + "hasOctave");
	public static final Property hasDurationAttribute = musicowl.createProperty(NS + "hasDurationAttribute");
	public static final Property hasDynamic = musicowl.createProperty(NS + "hasDynamic");
	public static final Property asMusicXML = musicowl.createProperty(NS + "asMusicXML");
	public static final Property asMEI = musicowl.createProperty(NS + "asMEI");
	public static final Property hasBeats = musicowl.createProperty(NS + "hasBeats");
	public static final Property hasBeatType = musicowl.createProperty(NS + "hasBeatType");
	public static final Property hasStaff = musicowl.createProperty(NS + "hasStaff");
	public static final Property hasNoteAttribute = musicowl.createProperty(NS + "hasNoteAttribute");

    public static final Resource Instant  = musicowl.createProperty(NS + "Instant");
    public static final Resource Measure  = musicowl.createProperty(NS + "Measure");
    public static final Resource TimeSignature  = musicowl.createProperty(NS + "TimeSignature");
    public static final Resource EndRepeat  = musicowl.createProperty(NS + "EndRepeat");
    public static final Resource BeginRepeat  = musicowl.createProperty(NS + "BeginRepeat");
    public static final Resource Staff  = musicowl.createProperty(NS + "Staff");
    public static final Resource Voice  = musicowl.createProperty(NS + "Voice");
    public static final Resource NoteSet  = musicowl.createProperty(NS + "NoteSet");
    public static final Resource Alto  = musicowl.createProperty(NS + "Alto");
    public static final Resource Baritone  = musicowl.createProperty(NS + "Baritone");
    public static final Resource Bass  = musicowl.createProperty(NS + "Bass");
    public static final Resource FrenchViolin  = musicowl.createProperty(NS + "FrenchViolin");
    public static final Resource MezzoSoprano  = musicowl.createProperty(NS + "MezzoSoprano");
    public static final Resource Soprano  = musicowl.createProperty(NS + "Soprano");
    public static final Resource SubBass  = musicowl.createProperty(NS + "SubBass");
    public static final Resource Tenor  = musicowl.createProperty(NS + "Tenor");
    public static final Resource Trebble  = musicowl.createProperty(NS + "Trebble");
    public static final Resource Percussion  = musicowl.createProperty(NS + "Percussion");
    public static final Resource Whole  = musicowl.createProperty(NS + "Whole");
    public static final Resource Quarter  = musicowl.createProperty(NS + "Quarter");
    public static final Resource QuadrupleWhole  = musicowl.createProperty(NS + "QuadrupleWhole");
    public static final Resource OctupleWhole  = musicowl.createProperty(NS + "OctupleWhole");
    public static final Resource Half  = musicowl.createProperty(NS + "Half");
    public static final Resource Eighth  = musicowl.createProperty(NS + "Eighth");
    public static final Resource DoubleWhole  = musicowl.createProperty(NS + "DoubleWhole");
    public static final Resource D64th = musicowl.createProperty(NS + "64th");
    public static final Resource D32nd  = musicowl.createProperty(NS + "32nd");
    public static final Resource D16th  = musicowl.createProperty(NS + "16th");
    public static final Resource D128th  = musicowl.createProperty(NS + "128th");
    public static final Resource TempoMarking  = musicowl.createProperty(NS + "TempoMarking");
    public static final Resource Duration  = musicowl.createProperty(NS + "Duration");
    public static final Resource Note  = musicowl.createProperty(NS + "Note");
    public static final Resource ScorePart  = musicowl.createProperty(NS + "ScorePart");
    public static final Resource Dot  = musicowl.createProperty(NS + "Dot");
    public static final Resource DoubleDot  = musicowl.createProperty(NS + "DoubleDot");
    public static final Resource TripleDot  = musicowl.createProperty(NS + "TripleDot");
    public static final Resource NoteAttribute  = musicowl.createProperty(NS + "NoteAttribute");
    public static final Resource GraceNote = musicowl.createProperty(NS + "GraceNote");
    
    
  		

}
