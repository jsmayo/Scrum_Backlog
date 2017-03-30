package edu.ncsu.csc216.backlog.task;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoteTest {
	
	private Note note;

	@Test
	public void testNote() {
		//test invalid Note creation 
		try {
			note = new Note(null, "steven");
			fail();
		} catch (IllegalArgumentException e) {
		assertNull(note);
		}
		
		try {
			note = new Note("", "steven");
			fail();
		} catch (IllegalArgumentException e){
			assertNull(note);
		}
		
		
		try {
			note = new Note("note text", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(note);
		}
		
		try {
		note = new Note("note text", "");
		fail();
		} catch(IllegalArgumentException e) {
			assertNull(note);
		}
		
		//test valid construction
		try {
			note = new Note("steven", "note text goes here");
			assertTrue("steven".equals(note.getNoteAuthor()));
			assertTrue("note text goes here".equals(note.getNoteText()));
			String[] info = note.getNoteArray();
			assertTrue(info[0].equals("steven"));
			assertTrue(info[1].equals("note text goes here"));
			assertEquals("array length should be 2", 2, info.length);
		} catch (IllegalArgumentException e) {
			fail("should not fail with valid note parameters");
		}
	}

}
