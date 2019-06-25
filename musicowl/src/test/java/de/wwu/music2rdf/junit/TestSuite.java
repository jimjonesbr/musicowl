package de.wwu.music2rdf.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	GenerateRDFTest.class, 
	ElgarConcerto.class,
	AchillesGrandOpera.class,
	Ariettes.class,
	AchtzehnLiederChor.class,
	TroisAirs.class
})

public class TestSuite {

}