package edu.ncsu.csc216.backlog.task;


import static org.junit.Assert.*;
import org.junit.Test;
import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
import edu.ncsu.csc216.task.xml.NoteItem;
import edu.ncsu.csc216.task.xml.NoteList;
import edu.ncsu.csc216.task.xml.Task;

/**
 * Test class for the TaskItem class 
 * 
 * @author Steven
 */
public class TaskItemTest {
	
	/**TaskItem object */
	TaskItem item;
	
	/**
	 * Tests for TaskItem constructors
	 */
	@Test
	public void testTaskItem() {
		//test invalid construction
		try {
			item = new TaskItem(null, TaskItem.Type.BUG, "creator", "note");
			fail("should not be able to construct null title TaskItem");
		} catch (IllegalArgumentException e){
			assertNull(item);
		}
		
		try {
			item = new TaskItem("", TaskItem.Type.BUG, "creator", "note");
			fail("should not be able to construct \"\" title TaskItem");
		} catch (IllegalArgumentException e){
			assertNull(item);
		}
		
		try {
			item = new TaskItem("title", TaskItem.Type.FEATURE, null, "note");
			fail("should not be able to construct null creator TaskItem");
		} catch (IllegalArgumentException e){
			assertNull(item);
		}
		
		try {
			item = new TaskItem("", TaskItem.Type.BUG, "", "note");
			fail("should not be able to construct \"\" creator TaskItem");
		} catch (IllegalArgumentException e){
			assertNull(item);
		}
		
		try {
			item = new TaskItem(null, TaskItem.Type.BUG, "creator", null);
			fail("should not be able to construct null noteTaskItem");
		} catch (IllegalArgumentException e){
			assertNull(item);
		}
		
		try {
			item = new TaskItem("", TaskItem.Type.BUG, "creator", "");
			fail("should not be able to construct \"\" note TaskItem");
		} catch (IllegalArgumentException e){
			assertNull(item);
		}
		
		//test valid TaskItemConstruction
		
		try{
			item = new TaskItem("title", TaskItem.Type.BUG, "creator", "note");
			assertEquals(item.getTaskItemId(), 1);
			assertTrue(item.getTitle().equals("title"));
			assertTrue(TaskItem.Type.BUG == item.getType());
			assertTrue(item.getCreator().equals("creator"));
			assertTrue(item.getNotesArray()[0][1].equals("note"));
			assertTrue(item.getNotesArray()[0][0].equals("creator"));
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
	 
		// Test TaskItem Constructor with task parameters
		//construct the task
		Task task = new Task();
		task.setId(1);
		task.setTitle("task1");
		task.setCreator("steven");
		task.setOwner("steven");
		task.setId(1);
		task.setState(TaskItem.BACKLOG_NAME);
		task.setType("B");
		task.setVerified(false);
		//construct the noteitem
		NoteItem note = new NoteItem();
		note.setNoteAuthor("Jesse");
		note.setNoteText("very note");
		//construct the notelist
		NoteList noteList = new NoteList();
		noteList.getNotes().add(note); 
		task.setNoteList(noteList); 
		item = new TaskItem(task);
		assertEquals("task1", item.getTitle());
		assertEquals("steven", item.getCreator());
		assertEquals("B", item.getTypeString());
		assertEquals(1, item.getTaskItemId());
		assertEquals("Bug", item.getTypeFullString());
		assertEquals("Jesse", item.getNotesArray()[0][0]);
		assertEquals("very note", item.getNotesArray()[0][1]);
		assertEquals("Backlog", item.getStateName());
		assertEquals(1, item.getNotes().size());
		task.setState(TaskItem.OWNED_NAME);
		assertEquals("steven", item.getOwner());
	}
	
	/**
	 * Test getXMLTask()
	 */
	@Test
	public void testGetXMLTask() {
		Task task = new Task();
		task.setId(1);
		task.setTitle("task1");
		task.setCreator("steven");
		task.setOwner("steven");
		task.setState(TaskItem.BACKLOG_NAME);
		task.setType("B");
		task.setVerified(false);
		//construct the noteitem
		NoteItem note = new NoteItem();
		note.setNoteAuthor("Jesse");
		note.setNoteText("very note");
		//construct the notelist
		NoteList noteList = new NoteList();
		noteList.getNotes().add(note); 
		task.setNoteList(noteList); 
		item = new TaskItem(task);
		assertEquals("task1", item.getXMLTask().getTitle());
	}
	
	/**
	 * Test FSM 
	 */
	@Test
	public void testTaskItemState() {
		//Commands for quick access
		Command backlogCommand = new Command(CommandValue.BACKLOG, "owner", "backlogged");
		Command ownCommand = new Command(CommandValue.CLAIM, "owner", "claimed");
		Command processCommand = new Command(CommandValue.PROCESS, "owner", "process");
		Command verifyCommand = new Command(CommandValue.VERIFY, "owner", "verified");
		Command doneCommand = new Command(CommandValue.COMPLETE, "owner", "completed");
		Command rejectCommand = new Command(CommandValue.REJECT, "owner", "rejected");
		
		Command command = new Command(CommandValue.CLAIM, "jsm", "note text1");
		item = new TaskItem("title", TaskItem.Type.BUG, "steven", "note text");
		assertEquals(TaskItem.BACKLOG_NAME, item.getStateName());
		item.update(command);
		assertEquals("jsm", item.getOwner());
		assertTrue(TaskItem.OWNED_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 3); //not sure about the 3
		
		//transition to backlog test
		command = new Command(CommandValue.BACKLOG, "steven", "note 2");
		item.update(command);
		assertEquals("jsm", item.getOwner());
		assertTrue(TaskItem.BACKLOG_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 4); 
		try {
			item.update(command);
			fail();
		} catch (UnsupportedOperationException e){
			assertEquals("jsm", item.getOwner());
			assertTrue(TaskItem.BACKLOG_NAME == item.getStateName());
			assertEquals(item.getNotesArray().length, 4); 
		}
		
		item.update(ownCommand); //owned state
		item.update(processCommand); //owned to process
		assertEquals("owner", item.getOwner());
		assertTrue("should change to processing", TaskItem.PROCESSING_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 6); // should have 5 notes by now
		
		item.update(verifyCommand); //process to verified state
		assertEquals("owner", item.getOwner());
		assertTrue("should change to veryifing", TaskItem.VERIFYING_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 7); // should have 7 notes by now
		
		item.update(doneCommand); //verified to done state
		assertEquals("owner", item.getOwner());
		assertTrue("should change to done", TaskItem.DONE_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 8); // should have 8 notes by now
		
		item.update(backlogCommand); //send back to backlog state
		assertEquals("owner", item.getOwner());
		assertTrue("should change to backlog", TaskItem.BACKLOG_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 9);
		
		item.update(ownCommand); //owned state
		item.update(rejectCommand); //reject
		assertEquals("owner", item.getOwner());
		assertTrue("should change to rejected", TaskItem.REJECTED_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 11);
		
		item.update(backlogCommand); //owned to backlog
		assertEquals("owner", item.getOwner());
		assertTrue("should change to backlog", TaskItem.BACKLOG_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 12);
		
		item.update(ownCommand); //owned
		try {
			item.update(doneCommand); //try to send to complete state
			fail();
		} catch (UnsupportedOperationException e){
			assertEquals("owner", item.getOwner());
			assertTrue(TaskItem.OWNED_NAME == item.getStateName());
			assertEquals(item.getNotesArray().length, 13); 
		}
		
		item.update(rejectCommand); //own to reject state
		assertEquals("owner", item.getOwner());
		assertTrue("should change to rejected", TaskItem.REJECTED_NAME == item.getStateName());
		assertEquals(item.getNotesArray().length, 14);
		
		item.update(backlogCommand);
		item.update(ownCommand);
		item.update(processCommand);
		item.update(backlogCommand); //process to backlog
		item.update(ownCommand); 
		item.update(processCommand);
		item.update(verifyCommand);
		item.update(processCommand); //verify back to process
		item.update(verifyCommand); //back to verify
		try {
			item.update(rejectCommand); //try to send to reject state
			fail();
		} catch (UnsupportedOperationException e){
			assertEquals("owner", item.getOwner());
			assertTrue(TaskItem.VERIFYING_NAME == item.getStateName());
			assertEquals(item.getNotesArray().length, 23); //NOTE LENGTH = CORRECT TRANSITION INDICATION!!! :)
		}
		
		//test KA type transition
		item = new TaskItem("title", TaskItem.Type.KNOWLEDGE_ACQUISITION, "owner", "note text");
		item.update(ownCommand);
		item.update(processCommand);
		try {
			item.update(verifyCommand);
			fail("Should not send to verify for KA type task items.");
		} catch (UnsupportedOperationException e) {
			assertTrue(TaskItem.PROCESSING_NAME == item.getStateName());
		}
		//item.update(verifyCommand);
		item.update(doneCommand);
		item.update(processCommand);
		
		
		
	}
}
	