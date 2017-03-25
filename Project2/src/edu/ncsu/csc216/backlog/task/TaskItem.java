package edu.ncsu.csc216.backlog.task;

import java.util.ArrayList;
import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
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
	private Type type;
	private Note notes;
	
	
	/**
	 * Constructor for a TaskItem.
	 * @param title Title of the TaskItem.
	 * @param type Type of the TaskItem.
	 * @param creator Creator of the TaskItem.
	 * @param note Note contents associated with the TaskItem.
	 */
	public TaskItem(String title, Type type, String creator, String note) {
		this.title = title;
		setState(BACKLOG_NAME);
		this.creator = creator;
		isVerified = false;
		creator = getCreator();
		this.type = type;
		
		
		
		
		
		
	}
	
	/**
	 * Constructs a TaskItem Object using a Task Object as parameter input.
	 * @param task Task to make the TaskItem from.
	 */
	public TaskItem(Task task) {
		//make these strings and call this()?
//		this.title = task.getTitle();
//		this.type = setType(task.getType());
//		this.creator = task.getCreator();
//		this.creator = task.getNoteList().getNotes().get(task.getId()).getNoteAuthor();
//		//this.note = task.getNoteList().getNotes().get(task.getId()).getNoteText();
		
	}
	
	/**
	 * Increments the counter variable by one each time
	 * a new TaskItem is constructed.
	 */
	public static void incrementCounter() {
		counter++;
	}
	
	/**
	 * Getter method for the current TaskItem's ID number.
	 * @return The unique ID number assigned to the current TaskItem.
	 */
	public int getTaskItemId() {
		return this.taskId;
	}

	/**
	 * Getter method that returns the current TaskItem's State name.
	 * @return State name of the current Task Item.
	 */
	public String getStateName() {
		return title;
	}
	
	/**
	 * Sets the current TaskItem to one of the six State subclasses required
	 * for ScrumBacklog's FSM.
	 * @param state String representation of the State to set the current
	 * TaskItem to.
	 * @throws IllegalArgumentException if the state parameter is null.
	 */
	private void setState(String state) {
		if(state == null ) throw new IllegalArgumentException();
		if(state == TaskItem.DONE_NAME) this.state = doneState;
		
		
	
	}
	
	/**
	 * Sets the current TaskItem Type using a string parameter. The string 
	 * will correspond to one of the four Types specified in the
	 * Type enumerator. 
	 * @param type String representation of one of the four Type enumerators.
	 */
	private void setType(String type) {
		if(type == null || type.isEmpty()) throw new IllegalArgumentException();
		if(type == this.getTypeString() || type == "T_FEATURE") this.type = Type.BUG;
		
		
	}
	
	/**
	 * Getter method for the current TaskItem's Type.
	 * @return type Type of the TaskItem.
	 */
	public Type getType() {
		if(this.type == Type.BUG) return Type.BUG;
		return Type.FEATURE;
	}
	
	/**
	 * Getter method for the abbreviated string representation 
	 * of the current TaskItem's Type.
	 * @return typeString String representation of the current
	 * TaskItem Type.
	 */
	public String getTypeString() {
		return "type";
	}
	
	/**
	 * Getter method for the non-abbreviated String representation
	 * of the current TaskItem.
	 * @return fullTypeString Non-abbreviated String representation
	 * of the current TaskItem.
	 */
	public String getTypeFullString() {
		return "type";
	}
	
	/**
	 * Getter method for the TaskItem owner.
	 * @return Owner of the current TaskItem.
	 */
	public String getOwner() { 
		if (creator != null && owner != null) return notes.getNoteAuthor();
		return "owner";
	}
	
	/**
	 * Getter method for the TaskItem title.
	 * @return Title of the TaskItem.
	 */
	public String getTitle() {
		return "title";
	}
	
	/**
	 * Getter method for the TaskItem creator.
	 * @return Creator of the current TaskItem.
	 */
	public String getCreator() {
		if(state == ownedState) {
			return creator = notes.getNoteAuthor();
		}
		else return "creator";
	}
	
	/**
	 * Getter method that returns an ArrayList for all note
	 * entries associated with the current TaskItem.
	 * @return noteArray ArrayList of all note entries associated with 
	 * the current TaskItem.
	 */
	public ArrayList<Note> getNotes() {
		return new ArrayList<Note>();
	}
	
	/**
	 * Updates the TaskItem's state using a Command parameter propagated from
	 * the UI.
	 * @param command Enumerator value from the Command class  used to update the State of the TaskItem.
	 */
	public void update(Command command) {
		state.updateState(command);
	}
	
	/**
	 * Getter method that returns the TaskItem Object as a Task Object.
	 * @return Task Object corresponding to the current TaskItem.
	 */
	public Task getXMLTask() {
		return null;
		
	}

	/**
	 * Sets the TaskItem's counter variable to that of the intriguer parameter. 
	 * @param counter Number to assign as the current counter value.
	 */
	public static void setCounter(int counter) {
		TaskItem.counter = counter;
	}
	
	/**
	 * Returns a 2D string array for Notes associated with the current TaskItem. 
	 * There is a row for each note entry, as well as, two columns for displaying
	 * the note author's name and note contents. 
	 * @return
	 */
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
		
		/**  
		 * Constructor for the BacklogState transition.
		 */
		private BacklogState() {
			
		}
		
		/**
		 * Method used to progress from the BacklogState.
		 * @param Command The Command used to update the current state.
		 */
		public void updateState(Command command) {
			command.getCommand();
			owner = getOwner();
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return BACKLOG_NAME;
			
		}
	}
	
	/**
	 * Concrete class that represents the owned state of the ScrumBacklog FSM.
	 * 
	 * @author Steven Mayo
	 */
	private class OwnedState implements TaskItemState {

		/**
		 * Constructor for the OwnedState.
		 */
		private OwnedState() {
			owner = "owner";
			state = ownedState;
			
		}

		
		/**
		 * Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 */
		public void updateState(Command command) {
			if(command.getCommand() == CommandValue.PROCESS);
			if(isVerified) state = verifyingState;
			else state = processingState;
		}
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
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

		/**
		 * Constructor for the ProcessingState
		 */
		private ProcessingState() {
			isVerified = false;
			title = "title";
			state = processingState;
			
			
		}

		/**
		 * Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 * 
		 */
		public void updateState(Command command) {
			state = verifyingState;
		}

		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
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
			setType("KA");
			isVerified = true;
			state = verifyingState;
		}
		
		/**Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 * TaskItem
		 */
		public void updateState(Command command) {
			
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
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
		
		/**
		 * Constructor for the DoneState
		 */
		private DoneState() {
			state = doneState;
		}
		
		/**
		 * Method used to progress from the BacklogState.
		 * @param Command The Command used to update the current state.
		 */
		public void updateState(Command command) {
			state = rejectedState;
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return this.getStateName();
		}
	}
	
	/**
	 * Concrete class that represents the rejected state of the Scrum Backlog FSM.
	 * 
	 * @author Steven Mayo
	 */
	private class RejectedState implements TaskItemState {
		
		/**
		 * Constructor for the RejectedState.
		 */
		private RejectedState() {
			isVerified = false;
		}
		
		/**
		 * Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 * 
		 */
		public void updateState(Command command) {
			if(command.getCommand() == CommandValue.BACKLOG) state = backlogState;
			
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			state = backlogState;
			isVerified = false;
			return REJECTED_NAME;
			
			
		}
	}
	
}
