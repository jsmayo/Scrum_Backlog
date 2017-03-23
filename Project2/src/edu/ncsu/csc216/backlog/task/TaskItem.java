package edu.ncsu.csc216.backlog.task;

import java.util.ArrayList;
import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.task.xml.Task;

/**
 * Interface that describes behaivors of any concrete TaskItem 
 * for the Scrum Backlog FSM. 
 * 
 * @author Steven Mayo
 */
public class TaskItem {

	/** Unique ID number of the TaskItem */
	private int taskId;
	/** State of the TaskItem */
	private TaskItemState state;
	/** Title of the TaskItem */
	private String title;
	/** Creator of the TaskItem */
	private String creator;
	/** Owner of the TaskItem */
	private String owner;
	/** Verification indicator */
	private boolean isVerified;
	/** Backlog State Object for ScrumBacklog FSM */
	private final TaskItemState backlogState = new BacklogState();
	/** Owned State Object for ScrumBacklog FSM */
	private final TaskItemState ownedState = new OwnedState();
	/** Processing State Object for ScrumBacklog FSM */
	private final TaskItemState processingState = new ProcessingState();
	/** Verifying State Object for ScrumBacklog FSM */
	private final TaskItemState verifyingState = new VerifyingState();
	/** Done State Object for ScrumBacklog FSM */
	private final TaskItemState doneState = new DoneState();
	/** Rejected State Object for ScrumBacklog FSM */
	private final TaskItemState rejectedState = new RejectedState();
	/** Name assigned to the Backlog State */
	public static final String BACKLOG_NAME = "Backlog";
	/** Name assigned to the Owned State */
	public static final String OWNED_NAME = "Owned";
	/** Name assigned to the Backlog State */
	public static final String PROCESSING_NAME = "Processing";
	/** Name assigned to the Processing State */
	public static final String VERIFYING_NAME = "Verifying";
	/** Name assigned to the Done State */
	public static final String DONE_NAME = "Done";
	/** Name assigned to the Rejected State */
	public static final String REJECTED_NAME = "Rejected";
	/** String representing the Feature Type of TaskItem */
	static final String T_FEATURE = "F";
	/** String representing the Bug Type of TaskItem */
	static final String T_BUG = "B";
	/** String representing the Technical Work Type of TaskItem */
	static final String T_TECHNICAL_WORK = "TW";
	/** String representing the Knowledge Acquisition Type of TaskItem */
	static final String T_KNOWLEDGE_ACQUISITION = "KA";
	/** Counter for unique ID number assignment */
	static int counter = 1;
	/** Enumerator for the designating the specific Type of the TaskItem Object */
	public static enum Type { FEATURE, BUG, TECHNICAL_WORK, KNOWLEDGE_ACQUISITION }
	
	
	/**
	 * Constructor for a TaskItem.
	 * @param title Title of the TaskItem.
	 * @param type Type of the TaskItem.
	 * @param creator Creator of the TaskItem.
	 * @param note Note contents associated with the TaskItem.
	 */
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
	
	/**
	 * Concrete class that represents the backlog state of the Scrum Backlog FSM.
	 * 
	 * @author Steven
	 */
	private class BacklogState implements TaskItemState {
		
		private BacklogState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
	/**
	 * Concrete class that represents the owned state of the Scrum Backlog FSM.
	 * 
	 * @author Steven Mayo
	 */
	private class OwnedState implements TaskItemState {

		private OwnedState() {

		}

		public void updateState(Command command) {

		}

		public String getStateName() {
			return "state";
		}
	}
	
	/**
	 * Concrete class that represents the processing state of the Scrum Backlog FSM
	 * 
	 * @author Steven Mayo
	 */
	private class ProcessingState implements TaskItemState {

		private ProcessingState() {

		}

		public void updateState(Command command) {

		}

		public String getStateName() {
			return "state";
		}
	}
	
	/**
	 * Concrete class that represents the verifying state of the Scrum Backlog FSM.
	 * 
	 * @author Steven Mayo
	 */
	private class VerifyingState implements TaskItemState {
		
		private VerifyingState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
	
	/**
	 * Concrete class that represents the done state of the Scrum Backlog FSM.
	 * 
	 * @author Steven Mayo
	 */
	private class DoneState implements TaskItemState {
		
		private DoneState() {
			
		}
		
		public void updateState(Command command) {
			
		}
		
		public String getStateName() {
			return "state";
		}
	}
	
	/**
	 * Concrete class that represents the rejected state of the Scrum Backlog FSM.
	 * 
	 * @author Steven Mayo
	 */
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
