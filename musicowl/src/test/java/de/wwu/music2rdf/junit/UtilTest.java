package de.wwu.music2rdf.junit;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import de.wwu.music2rdf.util.Util;

public class UtilTest {
	@Test
	public void testTimeElapsed() {
		System.out.println("Executing tests ...");
		Util x = new Util();
		assertEquals(x.timeElapsed(new Date(), new Date()), "0 ms");
		
	}
	@Test
	public void testSum() {
		System.out.println("Executing tests (Sum)...");
		assertEquals(2+2, 4);		
	}
	
}