package edu.ncsu.csc216.backlog.scrum_backlog;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.task.TaskItem;
import edu.ncsu.csc216.backlog.task.TaskItem.Type;

public class ScrumBacklogModel {
	
	public ScrumBacklogModel() {
		
	}
	
	public static ScrumBacklogModel getInstance() {
		return new ScrumBacklogModel();
	}
	
	public void saveTasksToFile(String filename) {
		
	}
	
	public void loadTasksFromFile(String filename) {
		
	}
	
	public void createNewTaskItemList() {
	
	}
	
	public Object[][] getTaskItemListAsArray() {
		return new Object[0][0];
	}
	
	public Object[][] getTaskItemListByOwnerAsArray(String owner) {
		return new Object[0][0];
	}
	
	public Object[][] getTaskItemListByCreatorAsArray(String creator) {
		return new Object[0][0];
	}
	
	public TaskItem getTaskItemById(int id) {
		return null;
	}
	
	public void executeCommand(int id, Command command) {
		
	}
	
	public void deleteTaskItemById(int id) {
		
	}
	
	public void addTaskItemToList(String a, Type b, String c, String d) {
		
	}
}
