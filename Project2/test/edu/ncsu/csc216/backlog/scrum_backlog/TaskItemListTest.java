package edu.ncsu.csc216.backlog.scrum_backlog;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
import edu.ncsu.csc216.backlog.task.TaskItem;
import edu.ncsu.csc216.task.xml.NoteItem;
import edu.ncsu.csc216.task.xml.NoteList;
import edu.ncsu.csc216.task.xml.Task;

public class TaskItemListTest {
	
	/** TaskItemList to hold TaskItem Objects */
	TaskItemList list; 
	
	@Test
	public void testTaskItemList() {
		list = new TaskItemList();
		assertEquals("List should be empty after creation", list.getTaskItems().size(), 0);
	}
	
	/**
	 * Tests for addTaskItem() of TaskItemList
	 */
	@Test
	public void testAddTaskItem() {
		list = new TaskItemList();
		list.addTaskItem("task1", TaskItem.Type.BUG, "Steven", "All the notes imaginable");
		assertEquals(1, list.getTaskItems().size());
	}
	
	/**
	 * Tests for addXMLTasks() of TaskItemList
	 */
	@Test
	public void testAddXMLTasks() {
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
		List<Task> tasklist = new ArrayList<Task>();
		tasklist.add(task);
		
		list = new TaskItemList();
		list.addXMLTasks(tasklist);
		assertEquals("should be 1 item from XML Task", 1, list.getTaskItems().size());
	}
	
	/**
	 * Tests for getTaskItemsByOwner() of TaskItemList
	 */
	@Test
	public void testGetTasksByCreator() {
		list = new TaskItemList();
		list.addTaskItem("task1", TaskItem.Type.BUG, "Steven", "All the notes imaginable");
		list.getTaskItemById(1).update(new Command(CommandValue.CLAIM, "Steven", "note")); //CLAIM TASK
		list.addTaskItem("task2", TaskItem.Type.FEATURE, "Steven", "another note");
		list.getTaskItemById(2).update(new Command(CommandValue.CLAIM, "Steven", "note")); //CLAIM TASK
		assertEquals(3, list.addTaskItem("task3", TaskItem.Type.FEATURE, "Jesse", "another note"));
		list.getTaskItemById(3).update(new Command(CommandValue.CLAIM, "Jesse", "note")); //CLAIM TASK
		assertEquals(3, list.getTaskItems().size());
		assertTrue(list.getTaskItemById(1).getTitle().equals("task1"));
		assertTrue(list.getTaskItemById(2).getCreator().equals("Steven"));
		assertEquals("size should be 2", 2, list.getTaskItemsByOwner("Steven").size());
	}
}
