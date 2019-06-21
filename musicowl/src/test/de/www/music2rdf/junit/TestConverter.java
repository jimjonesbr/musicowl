package de.www.music2rdf.junit;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import de.wwu.music2rdf.util.Util;

public class TestConverter {
	@Test
	public void testTimeElapsed() {
		System.out.println("Executing tests ...");
		assertEquals(Util.timeElapsed(new Date(), new Date()), "0 ms");
		
	}
	@Test
	public void testSum() {
		System.out.println("Executing tests (Sum)...");
		assertEquals(2+2, 4);		
	}
	
}