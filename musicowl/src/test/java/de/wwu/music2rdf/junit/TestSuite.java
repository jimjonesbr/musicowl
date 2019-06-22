package de.wwu.music2rdf.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	GenerateRDFTest.class, 
	ConverterTestElgar.class
})

public class TestSuite {

}