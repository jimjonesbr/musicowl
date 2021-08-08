package de.wwu.music2rdf.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	GenerateRDFTest.class,
	AccidTieTest.class,
	ElgarConcerto.class,
	Ariettes.class,
	AchtzehnLiederChor.class,
	TroisAirs.class,
	AchillesGrandOpera.class,
	LeichtesRondoPianoForte.class,
	SiegesMaerschePianoForte.class,
	SonateFacile.class
})

public class TestSuiteMusic2RDF {

}