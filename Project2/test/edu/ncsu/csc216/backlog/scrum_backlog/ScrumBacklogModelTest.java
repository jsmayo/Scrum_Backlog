package edu.ncsu.csc216.backlog.scrum_backlog;

import static org.junit.Assert.*;



import org.junit.Test;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
import edu.ncsu.csc216.backlog.task.TaskItem;

public class ScrumBacklogModelTest {

	/** Instance for scrumbacklog model */
	ScrumBacklogModel instance = ScrumBacklogModel.getInstance();
	
	
	

//	/** Contains files that will be created by the tests */
//	private String actFileNames = "test_files/act_tasks_valid.xml";
//	/** Contains expected results for the files */
//	private String expFileNames = "test_files/tasks_valid.xml";
//	
//	/**
//	 * Compares the contents of the two files.  Returns true if the contents 
//	 * are exactly the same. Returns false if the contents are not
//	 * the same or if there are any errors while processing.
//	 * 
//	 * @param expectedFile the file with expected results
//	 * @param actualFile the file with actual results
//	 */
//	private void compareFiles(String expectedFile, String actualFile) {
//		try {
//			Scanner expectedIn = new Scanner(new File (expectedFile));
//			Scanner actualIn = new Scanner(new File (actualFile));
//			
//			while (expectedIn.hasNextLine()) {
//				assertEquals("Expected File: " + expectedFile, expectedIn.nextLine(), actualIn.nextLine());
//			}
//			if (actualIn.hasNextLine()) {
//				fail("Expected File: " + expectedFile + " Actual File has more lines than expected file.");
//			}
//			
//			expectedIn.close();
//			actualIn.close();
//		} catch (FileNotFoundException e) {
//			fail("Expected File: " + expectedFile + " FileNotFound for expected or actual file.");
//		} catch (NoSuchElementException e) {
//			fail("Expected File: " + expectedFile + " One of the files didn't have an expected line.");
//		}
//	}
	
	
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
