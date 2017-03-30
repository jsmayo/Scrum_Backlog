package edu.ncsu.csc216.backlog.task;


import static org.junit.Assert.*;
import org.junit.Test;

public class TaskItemTest {
	
	/**TaskItem object */
	TaskItem item;
	

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
			//TODO: fix
			//assertTrue(item.getNotesArray()[0][1].equals("note"));
			//assertTrue(item.getNotesArray()[0][0].equals("creator"));
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
	}

}
