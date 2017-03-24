package edu.ncsu.csc216.backlog.scrum_backlog;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.task.TaskItem;
import edu.ncsu.csc216.backlog.task.TaskItem.Type;
import edu.ncsu.csc216.task.xml.*;

/** Concrete class that implements the Singleton design pattern.
 * This class both maintains the TaskItemList and handles commands
 * from the GUI, such as loading, saving, and creating new
 * TaskItemLists.
 * 
 * @author Steven Mayo
 */
public class ScrumBacklogModel {
	
	private static ScrumBacklogModel singleton;
	private TaskItemList taskItemList;
	
	/**
	 * Constructor used for creating the ScrumBacklogModel
	 * instance.
	 */
	private ScrumBacklogModel() {
		singleton = new ScrumBacklogModel();
		taskItemList = new TaskItemList();
	}
	
	/**
	 * Static method used to retrieve the one and only instance
	 * of ScrumBacklogModel.
	 * @return ScrumBacklogModel Instance of ScrumBacklogModel
	 */
	public static ScrumBacklogModel getInstance() {
		return singleton;
	}
	
	/**
	 * Saves the current TaskItemList as an XML file with the
	 * specified filename parameter.
	 * @param filename Name of the XML file to save the current 
	 * TaskItemList to.
	 * @throws IllegalArgumentException If the TaskItemList is null.
	 */
	public void saveTasksToFile(String filename) {
		try {
			TaskWriter writer = new TaskWriter(filename);
			writer.marshal();
		} catch(TaskIOException e) {
			 throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Attempts to load a TaskItemList from the specified XML filename.
	 * @param filename Filename used to read from and populate the list of
	 * TaskItems.
	 * @throws IllegalArgumentException Thrown if ScrumBacklogModel encounters
	 * a TaskIOException when attempting to read from the specified file.
	 * 
	 */
	public void loadTasksFromFile(String filename) {
		try {
			TaskReader reader = new TaskReader(filename);
			reader.getTasks();
		} catch(TaskIOException e) {
			 throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Creates a new TaskItemList.
	 */
	public void createNewTaskItemList() {
		taskItemList = new TaskItemList(); //will call constructor, which calls emptyList().
		
	}
	
	
	/**
	 * Provides information to the GUI by returning a 2D 
	 * Object array that is used to populate the TaskListTable
	 * with information.
	 * @return 2D Object array containing 1 row  and 3 columns 
	 * for every TaskItem returned.
	 */
	public Object[][] getTaskItemListAsArray() {
		return new Object[0][3];
	}
	
	
	/**
	 * Provides information to the GUI by returning a 2D 
	 * Object array used to populate the TaskListTable
	 * with TaskItems filtered by the owner parameter.
	 * @param owner Name used to return information on TaskItems 
	 * with only this specific value.
	 * with 
	 * @return 2D Object array containing a row for each TaskItem
	 * owned by the specified parameter There is a column entry for each 
	 * TaskItem's id number, state name, and title.
	 * @throws IllegalArgumentException if the specified parameter is null.
	 */
	public Object[][] getTaskItemListByOwnerAsArray(String owner) {
		if(owner == null) throw new IllegalArgumentException();
		return new Object[0][0];
	}
	
	
	/**
	 * Provides information to the GUI by returning a 2D 
	 * Object array used to populate the TaskListTable
	 * with TaskItems filtered by the owner parameter.
	 * @param creator Name used to return information on TaskItems 
	 * with only this specific creator name.
	 * the 2D Object value. 
	 * the TaskItem
	 * @return 2D Object array containing a row for each TaskItem
	 * created by the specified parameter. There is a column entry for each 
	 * TaskItem's id number, state name, and title.
	 * @throws IllegalArgumentException if the specified parameter is null.
	 */
	public Object[][] getTaskItemListByCreatorAsArray(String creator) {
		if(creator == null) throw new IllegalArgumentException();
		return new Object[0][3];
	}
	
	/**
	 * Returns the TaskItem associated with the specified unique ID number.
	 * @param id Unique ID number of the TaskItem to return.
	 * @return TaskItem associated with the provided unique ID number.
	 */
	public TaskItem getTaskItemById(int id) {
		return null;
	}
	
	/**
	 * Executes the specified Command on the TaskItem Object corresponding
	 * to the unique ID number parameter.
	 * @param id Unique ID number of the TaskItem.
	 * @param command Command to perform on the associated TaskItem.
	 */
	public void executeCommand(int id, Command command) {
		
	}
	
	/**
	 * Deletes the TaskItem corresponding to the provided unique ID number.
	 * @param id Unique ID number of the TaskItem to delete.
	 */
	public void deleteTaskItemById(int id) {
		
	}
	
	/**
	 * Adds a TaskItem to the TaskItemList using the specified parameters for 
	 * TaskItem title, Type, creator, and note value to 
	 * @param title Title of the TaskItem.
	 * @param type Type of the TaskItem.
	 * @param creator Creator of the TaskItem.
	 * @param note Note designated for the TaskItem.
	 */
	public void addTaskItemToList(String title, Type type, String creator, String note) {
		taskItemList.addTaskItem(title, type, creator, note);
		
	}
}
