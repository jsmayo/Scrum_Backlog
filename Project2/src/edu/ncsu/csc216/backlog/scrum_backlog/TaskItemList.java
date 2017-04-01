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

	List<TaskItem> tasks = new ArrayList<TaskItem>();
	static final int INITIAL_COUNTER_VALUE = 1;
	
	
	/**
	 * Constructor for the TaskItemList object.
	 */
	public TaskItemList() {
		this.tasks = new ArrayList<TaskItem>();
		
		//emptyList();
		TaskItem.setCounter(TaskItemList.INITIAL_COUNTER_VALUE);
		//TaskItem task = new TaskItem();
		this.tasks = emptyList();
		
	}
	
	/**
	 * Removes the contents of the current TaskItemList.
	 */
	private ArrayList<TaskItem> emptyList() {
		//ScrumBacklogModel.getInstance().createNewTaskItemList();
		//List<TaskItem> emptyList = new ArrayList<TaskItem>();
		//this.tasks = emptyList;
		TaskItem.setCounter(TaskItemList.INITIAL_COUNTER_VALUE);
		return new ArrayList<TaskItem>();
		
		
	}
	
	/**
	 * Adds a TaskItem to the TaskItemList using the specified 
	 * parameters for TaskItem: title, Type, creator, and note.
	 * @param title Title of the TaskItem.
	 * @param type Type of the TaskItem.
	 * @param creator Creator of the TaskItem.
	 * @param note Note designated for the TaskItem.
	 * @return The unique ID number of the newly created TaskItem.
	 */
	public int addTaskItem(String title, Type type, String creator, String note) {
		TaskItem taskItem = new TaskItem(title, type, creator, note);
		tasks.add(taskItem);
		return taskItem.getTaskItemId();
	}
	
	/**
	 * Add's a list of Tasks to the current TaskItemList.
	 * @param tasklist List of Task's to add to the TaskItemList.
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
		if(this.tasks == null) return new ArrayList<TaskItem>();
		return this.tasks;
	}
	
	/**
	 * Getter method that returns a filtered list of TaskItems. The
	 * owner is used as means for filtering the current TaskItemList.
	 * @param owner Owner of the TaskItem.
	 * @return List of TaskItem objects that are filtered by the TaskItem
	 * owner name.
	 */
	public List<TaskItem> getTaskItemsByOwner(String owner) {
		if(owner == null || owner.isEmpty()) throw new IllegalArgumentException();
		List<TaskItem> taskItemList = new ArrayList<TaskItem>();
		taskItemList = getTaskItems();
		List<TaskItem> byOwner = new ArrayList<TaskItem>();
		for(int i = 0; i < taskItemList.size(); i++) {
			if(taskItemList.get(i).getOwner() != null && taskItemList.get(i).getOwner().equals(owner)) { //search for any matching Owners
				TaskItem task = taskItemList.get(i); //create a new taskitem 
				byOwner.add(task); //add new taskitem to list
			}
		}
		return byOwner;
		
	}
	
	/**
	 * Getter method that returns a filtered list of TaskItems. The
	 * Task creator is used as means for filtering the current TaskItemList.
	 * @param creator Creator of the TaskItem.
	 * @return List of TaskItem objects that are filtered by the TaskItem
	 * creator name.
	 */
	public List<TaskItem> getTasksByCreator(String creator) {
		if(creator == null || creator.isEmpty()) throw new IllegalArgumentException();
		List<TaskItem> taskItemList = new ArrayList<TaskItem>();
		List<TaskItem> byCreator = new ArrayList<TaskItem>();
		taskItemList = getTaskItems();
		for(int i = 0; i < taskItemList.size(); i++) {
			//if(taskItemList.get(i).getCreator() == null) continue;
			if(taskItemList.get(i).getCreator().equals(creator)) {
				TaskItem task = taskItemList.get(i);
				byCreator.add(task);
			}
			else continue;
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
		List<TaskItem> taskItemList = this.getTaskItems();
		for(int i = 0; i < taskItemList.size(); i++) {
			if(taskItemList.get(i).getTaskItemId() == id) {
				taskItemList.remove(taskItemList.get(i));
				this.tasks = taskItemList;
			}
		}
		//taskItemList.remove(byId.get(0)); //should be only one match.
	}
}
