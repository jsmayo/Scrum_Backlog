package edu.ncsu.csc216.backlog.command;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.backlog.command.Command.CommandValue;

/**
 * Test Class for the Command class
 * 
 * @author Steven Mayo
 */
public class CommandTest {
	/**Command object */
	private Command command;

	/**
	 * Tests for Command Constructor
	 */
	@Test
	public void testCommand() {
		//test valid construction
		command = new Command (CommandValue.BACKLOG, "steven", "this is my note");
		assertTrue(CommandValue.BACKLOG == command.getCommand());
		assertTrue("this is my note".equals(command.getNoteText()));
		assertTrue(command.getNoteAuthor().equals("steven"));
		
		//test null command value
		try {
			command = new Command (null, "this is my note", "Steven");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue("Command should not have been overwritten", command.getCommand() == CommandValue.BACKLOG);
		}
		
		//test null note text
		try {
			command = new Command (CommandValue.BACKLOG, null, "Steven");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue("Command should not have been overwritten", command.getCommand() == CommandValue.BACKLOG);
		}

		//test empty note text
		try {
			command = new Command (CommandValue.BACKLOG, "", "Steven");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue("Command should not have been overwritten", command.getCommand() == CommandValue.BACKLOG);
		}

		//test null note author
		try {
			command = new Command (CommandValue.BACKLOG, "this is my note", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue("Command should not have been overwritten", command.getCommand() == CommandValue.BACKLOG);
		}

		//test empty note author
		try {
			command = new Command (CommandValue.BACKLOG, "this is my note", "");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue("Command should not have been overwritten", command.getCommand() == CommandValue.BACKLOG);
		}
		
		assertTrue(CommandValue.BACKLOG != null);
		assertTrue(CommandValue.CLAIM != null);
		assertTrue(CommandValue.PROCESS != null);
		assertTrue(CommandValue.REJECT != null);
		assertTrue(CommandValue.COMPLETE != null);
		assertEquals("BACKLOG", CommandValue.BACKLOG.name());
		assertEquals("CLAIM", CommandValue.CLAIM.name());
		assertEquals("PROCESS", CommandValue.PROCESS.name());
		assertEquals("REJECT", CommandValue.REJECT.name());
		assertEquals("COMPLETE", CommandValue.COMPLETE.name());
	}

}
