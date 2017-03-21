package edu.ncsu.csc216.backlog.task;

import java.util.ArrayList;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.task.xml.Task;

public class TaskItem {

	private int taskId;
	private TaskItemState state;
	private String title;
	private String creator;
	private String owner;
	private boolean isVerified;
	private final TaskItemState backlogState = new BacklogState();
	private final TaskItemState ownedState = new OwnedState();
	private final TaskItemState processingState = new ProcessingState();
	private final TaskItemState verifyingState = new VerifyingState();
	private final TaskItemState doneState = new DoneState();
	private final TaskItemState rejectedState = new RejectedState();
	public static final String BACKLOG_NAME = "Backlog";
	public static final String OWNED_NAME = "Owned";
	public static final String PROCESSING_NAME = "Processing";
	public static final String VERIFYING_NAME = "Verifying";
	public static final String DONE_NAME = "Done";
	public static final String REJECTED_NAME = "Rejected";
	static final String T_FEATURE = "F";
	static final String T_BUG = "B";
	static final String T_TECHNICAL_WORK = "TW";
	static final String T_KNOWLEDGE_ACQUISITION = "KA";
	static int counter = 1;
	public static enum Type { FEATURE, BUG, TECHNICAL_WORK, KNOWLEDGE_ACQUISITION }
	
	public TaskItem(String title, Type type, String creator, String note) {
	
	}
	
	public TaskItem(Task task) {
		
	}
	
	public static void incrementCounter() {
		counter++;
	}
	
	public int getTaskItemId() {
		return this.taskId;
	}

	public String getStateName() {
		return "state name";
	}

	private void setState(String state) {
		if(state == null ) throw new IllegalArgumentException();
	}
	
	private void setType(String type) {
		
	}
	
	public Type getType() {
		return Type.BUG;
	}
	
	public String getTypeString() {
		return "type";
	}
	
	public String getTypeFullString() {
		return "type";
	}
	
	public String getOwner() { 
		return "owner";
	}
	
	public String getTitle() {
		return "title";
	}
	
	public String getCreator() {
		return "creator";
	}
	
	public ArrayList<Note> getNotes() {
		return new ArrayList<Note>();
	}
	
	public void update(Command command) {
		
	}
	
	public Task getXMLTask() {
		return null;
		
	}

	public static void setCounter(int counter) {
		TaskItem.counter = counter;
	}
	
	public String[][] getNotesArray() {
		return new String[0][0];
	}
	
	
	/**
	 * Interface for states in the Task State Pattern.  All 
	 * concrete task states must implement the TaskState interface.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu) 
	 */
	private interface TaskItemState {
		
		/**
		 * Update the {@link TaskItem} based on the given {@link Command}.
		 * An {@link UnsupportedOperationException} is throw if the {@link CommandValue}
		 * is not a valid action for the given state.  
		 * @param c {@link Command} describing the action that will update the {@link TaskItem}'s
		 * state.
		 * @throws UnsupportedOperationException if the {@link CommandValue} is not a valid action
		 * for the given state.
		 */
		void updateState(Command c);
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();
	
	}
	
	private class BacklogState implements TaskItemState {
		
		private BacklogState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
	private class OwnedState implements TaskItemState {

		private OwnedState() {

		}

		public void updateState(Command command) {

		}

		public String getStateName() {
			return "state";
		}
	}
	
	private class ProcessingState implements TaskItemState {

		private ProcessingState() {

		}

		public void updateState(Command command) {

		}

		public String getStateName() {
			return "state";
		}
	}
	
	private class VerifyingState implements TaskItemState {
		
		private VerifyingState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
	private class DoneState implements TaskItemState {
		
		private DoneState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
	private class RejectedState implements TaskItemState {
		
		private RejectedState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
}
