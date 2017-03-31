package edu.ncsu.csc216.backlog.scrum_backlog;
import edu.ncsu.csc216.task.xml.*;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.task.TaskItem;
import edu.ncsu.csc216.backlog.task.TaskItem.Type;

/**
 * Concrete class used to manage all tasks during Scrum Backlog's
 * execution. The TaskItemList maintains a List of TaskItem objects
 * and has support for adding, removing, searching for, updating, and 
 * filtering TaskItems.
 * 
 * @author Steven Mayo
 */
public class TaskItemList {

	List<TaskItem> tasks;
	static final int INITIAL_COUNTER_VALUE = 1;
	
	/**
	 * Constructor for the TaskItemList object.
	 */
	public TaskItemList() {
		emptyList();
		tasks = new ArrayList<TaskItem>();
		
	}
	
	/**
	 * Removes the contents of the current TaskItemList.
	 */
	private void emptyList() {
		/*if one test adds TaskItems to a list, that those TaskItems will 
		 * be there for the next test. To make each test atomic so that it
		 *  can run in isolation, you can use the ScrumBacklogModel.createNewTaskItemList() 
		 *  method to remove any tasks that exist in the ScrumBacklogModel. Since the taskItemList 
		 *  is the only other state (besides the singleton instance), using that method resets the Singleton 
		 *  to an empty TaskItemList.
		 */
		tasks = new ArrayList<TaskItem>();
		TaskItem.setCounter(1);
	}
	
	/**
	 * Adds a TaskItem to the TaskItemList using the specified 
	 * parameters for TaskItem: title, Type, creator, and note.
	 * @param title Title of the TaskItem.
	 * @param type Type of the TaskItem.
	 * @param creator Creator of the TaskItem.
	 * @param note Note designated for the TaskItem.
	 */
	public int addTaskItem(String title, Type type, String creator, String note) {
		TaskItem taskItem = new TaskItem(title, type, creator, note);
		tasks.add(taskItem);
		return taskItem.getTaskItemId();
	}
	
	/**
	 * Add's a list of Tasks to the current TaskItemList.
	 * @param List of Task's to add to the TaskItemList.
	 */
	public void addXMLTasks(List<Task> tasklist) {
		//use TaskItem(Task) constructor.
		int highestId = -1;
		for(int i = 0; i < tasklist.size(); i++) {
			tasks.add(new TaskItem(tasklist.get(i)));
			if( tasklist.get(i).getId() > highestId) highestId = tasklist.get(i).getId();
		}
		TaskItem.setCounter(highestId + 1);
	}
	
	/**
	 * Getter method that returns the current list of TaskItems.
	 * @return The list of TaskItems.
	 */
	public List<TaskItem> getTaskItems() {
		return tasks;
	}
	
	/**
	 * Getter method that returns a filtered list of TaskItems. The
	 * owner is used as means for filtering the current TaskItemList.
	 * @param owner Owner of the TaskItem.
	 * @return List of TaskItem objects that are filtered by the TaskItem
	 * owner name.
	 */
	public List<TaskItem> getTaskItemsByOwner(String owner) {
		List<TaskItem> byOwner = getTaskItems();
		for(int i = 0; i <= tasks.size(); i++) {
			if(tasks.get(i).getOwner().equals(owner)) byOwner.add(tasks.get(i));
		}
		return byOwner;
		
	}
	
	/**
	 * Getter method that returns a filtered list of TaskItems. The
	 * Task creator is used as means for filtering the current TaskItemList.
	 * @param owner Creator of the TaskItem.
	 * @return List of TaskItem objects that are filtered by the TaskItem
	 * creator name.
	 */
	public List<TaskItem> getTasksByCreator(String creator) {
		List<TaskItem> byCreator = this.getTaskItems();
		for(int i = 0; i <= tasks.size(); i++) {
			if(tasks.get(i).getCreator().equals(creator)) byCreator.add(tasks.get(i));
		}
		return byCreator;
	}
	
	/**
	 * Getter method that searches for, and retrieves, the TaskItem 
	 * associated with the provided unique ID number.
	 * @param id Unique ID number of the TaskItem to retrieve.
	 * @return TaskItem associated with the provided unique ID number.
	 * creator name.
	 */
	public TaskItem getTaskItemById(int id) {
		TaskItem byId = null;
		for(int i = 0; i < tasks.size(); i++) {
			if(tasks.get(i).getTaskItemId() == id) byId = tasks.get(i);
		}
		return byId;

	}
	
	/**
	 * Updates a TaskItem in the list through execution of the 
	 * passed in Command parameter.
	 * @param id The Unique ID number of the TaskItem.
	 * @param command Command to perform on the TaskItem.
	 */
	public void executeCommand(int id, Command command) {
		 getTaskItemById(id).update(command);
	}
	
	/**
	 * Deletes the TaskItem in the TaskItemList using the specified
	 * unique ID parameter.
	 * @param id Unique ID number of the TaskItem to delete.
	 */
	public void deleteTaskItemById(int id) {
		List<TaskItem> byId = getTaskItems();
		for(int i = 0; i <= tasks.size(); i++) {
			if(tasks.get(i).getTaskItemId() == id) byId.add(tasks.get(i));
		}
		tasks.remove(byId.get(0)); //should be only one match.
	}
}
