package edu.ncsu.csc216.backlog.scrum_backlog;

import static org.junit.Assert.*;



import org.junit.Test;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
import edu.ncsu.csc216.backlog.task.TaskItem;

/**
 * Test class for ScrumBacklogModel
 * 
 * @author Steven
 */
public class ScrumBacklogModelTest {

	/** Instance for scrumbacklog model */
	ScrumBacklogModel instance = ScrumBacklogModel.getInstance();
	
	
	
	/**
	 * Tests for loadTasksFromFile() for ScrumBacklogModel
	 */
	@Test
	public void testLoadTasksFromFile() {
		instance.loadTasksFromFile("test_files/tasks_valid.xml");
		assertTrue(6 == instance.getTaskItemListAsArray().length);	
		assertTrue(instance.getTaskItemListAsArray()[0][0].equals(1));
		assertTrue(instance.getTaskItemListAsArray()[0][1].equals(TaskItem.BACKLOG_NAME));
		assertTrue(instance.getTaskItemListAsArray()[0][2].equals("Express Carts"));
	}
	
	
	/**
	 * Tests for saveTasksToFile() for ScrumBacklogModel
	 */
	@Test
	public void testSaveTasksFromFile() {
		instance.loadTasksFromFile("test_files/tasks_valid.xml");
		instance.saveTasksToFile("test_files/act_tasks_valid.xml");
		instance.createNewTaskItemList();
		assertEquals(0, instance.getTaskItemListAsArray().length);
		//compareFiles(expFileNames, actFileNames);
	}
	
	/**
	 * Tests for getTaskItemListByOwnerAsArray()
	 */
	@Test
	public void testGetTaskItemListByCreatorAsArray() {
		instance.createNewTaskItemList();
		instance.addTaskItemToList("task1", TaskItem.Type.BUG, "Jesse", "All the notes imaginable");
		instance.addTaskItemToList("task2", TaskItem.Type.FEATURE, "Jesse", "another note");
		instance.addTaskItemToList("task3", TaskItem.Type.FEATURE, "Jesse", "another note");		
		assertEquals("size should be 3", 3, instance.getTaskItemListByCreatorAsArray("Jesse").length);
	
		//TEST GETTASKITEMLISTBYOWNERASARRAY
		instance.getTaskItemById(1).update(new Command(CommandValue.CLAIM, "Steven", "note")); //CLAIM TASK
		instance.getTaskItemById(2).update(new Command(CommandValue.CLAIM, "Steven", "note")); //CLAIM TASK
		instance.getTaskItemById(3).update(new Command(CommandValue.CLAIM, "Jesse", "note")); //CLAIM TASK
		assertEquals("size should be 2", 2, instance.getTaskItemListByOwnerAsArray("Steven").length);
	
		//TEST EXECUTE COMMAND
		instance.executeCommand(1, new Command(CommandValue.PROCESS, "steven", "note"));
		assertTrue(instance.getTaskItemListAsArray()[0][1] == TaskItem.PROCESSING_NAME);
		instance.deleteTaskItemById(2);
		
		//TEST DELETETASKITEMBYID
		assertTrue("Task item should have been deleted", 2 == instance.getTaskItemListAsArray().length);
		
	
	}
}
