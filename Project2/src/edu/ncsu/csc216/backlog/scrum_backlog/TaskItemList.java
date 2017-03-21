package edu.ncsu.csc216.backlog.scrum_backlog;
import edu.ncsu.csc216.task.xml.*;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.task.TaskItem;
import edu.ncsu.csc216.backlog.task.TaskItem.Type;

public class TaskItemList {

	ArrayList<TaskItem> tasks;
	static final int INITIAL_COUNTER_VALUE = 1;
	
	public TaskItemList() {
	}
	
	private void emptyList() {
		
	}
	
	public int addTaskItem(String a, Type b, String c, String d) {
		return 1;
	}
	
	public void addXMLTasks(List<Task> tasklist) {
		
	}
	
	public List<TaskItem> getTaskItems() {
		return tasks;
	}
	
	public List<TaskItem> getTaskItemsByOwner(String owner) {
		return new ArrayList<TaskItem>();
		
	}
	
	public List<TaskItem> getTaskItemsByCreator(String creator) {
		return new ArrayList<TaskItem>();
	}
	
	public TaskItem getTaskItemById(int id) {
		return tasks.get(id);
	}
	
	public void executeCommand(int id, Command command) {
		
	}
	
	public void deleteTAskItemById(int id) {
		
	}
}
