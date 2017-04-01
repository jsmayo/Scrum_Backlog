package edu.ncsu.csc216.backlog.task;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
import edu.ncsu.csc216.task.xml.NoteItem;
import edu.ncsu.csc216.task.xml.NoteList;
import edu.ncsu.csc216.task.xml.Task;

/**
 * Interface that describes behaviors of any concrete TaskItem 
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
	private ArrayList<Note> notes = new ArrayList<Note>();
	
	
	
	/**
	 * Constructor for a TaskItem.
	 * @param title Title of the TaskItem.
	 * @param type Type of the TaskItem.
	 * @param creator Creator of the TaskItem.
	 * @param note Note contents associated with the TaskItem.
	 */
	public TaskItem(String title, Type type, String creator, String note) {
		if(title == null || title.isEmpty() || creator == null || creator.isEmpty() || note == null || note.isEmpty())
			throw new IllegalArgumentException();
		this.title = title;
		this.creator = creator;
		//this.owner = "";
		this.notes.add(new Note(creator, note));
		this.state = backlogState;
		if(type == Type.FEATURE) setType(T_FEATURE);
		else if(type == Type.BUG) setType(T_BUG);
		else if(type == Type.KNOWLEDGE_ACQUISITION) setType(T_KNOWLEDGE_ACQUISITION);
		else if(type == Type.TECHNICAL_WORK) setType(T_TECHNICAL_WORK);
		else throw new IllegalArgumentException();
		//isVerified = false;
		TaskItem.setCounter(counter); //set the taskId to the counter 
		this.taskId = counter;
		TaskItem.incrementCounter(); //increments counter AFTER setting taskID
		
		
	}
	
	/**
	 * Constructs a TaskItem Object using a Task Object as parameter input.
	 * @param task Task to make the TaskItem from.
	 */
	public TaskItem(Task task) {
		String taskTitle = task.getTitle();
		String typeString = task.getType();
		String taskCreator = task.getCreator();
		String taskState = task.getState();
		String taskOwner = task.getOwner();
		setCounter(task.getId());
		taskId = task.getId();
		List<NoteItem> notelist = task.getNoteList().getNotes();
		//take notes from task and place into notes for taskitem
		for(int i = 0; i < notelist.size(); i++) {
			notes.add(new Note(notelist.get(i).getNoteAuthor(), notelist.get(i).getNoteText()));
		}
		Type taskType = null;
		if(typeString.equals(T_BUG)) taskType = Type.BUG;
		if(typeString.equals(T_FEATURE)) taskType = Type.FEATURE;
		if(typeString.equals(T_KNOWLEDGE_ACQUISITION)) taskType = Type.KNOWLEDGE_ACQUISITION;
		if(typeString.equals(T_TECHNICAL_WORK)) taskType = Type.TECHNICAL_WORK;
		setState(taskState);
		this.title = taskTitle;
		this.type = taskType;
		this.creator = taskCreator;
		this.owner = taskOwner;
		
		
		
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
		return state.getStateName();

	}
	
	/**
	 * Sets the current TaskItem to one of the six State subclasses required
	 * for ScrumBacklog's FSM.
	 * @param state String representation of the State to set the current
	 * TaskItem to.
	 * @throws IllegalArgumentException if the state parameter is null.
	 */
	private void setState(String state) {
		if(state == null || state.isEmpty()) throw new IllegalArgumentException();
		switch(state) {
		case BACKLOG_NAME:
			this.state = backlogState;
			break;
			
		case OWNED_NAME:
			this.state = ownedState;
			break;
			
		case PROCESSING_NAME:
			this.state = processingState;
			break;
			
		case VERIFYING_NAME:
			this.state = verifyingState;
			break;
			
		case DONE_NAME:
			this.state = doneState;
			break;
			
		case REJECTED_NAME:
			this.state = rejectedState;
			break;
			
			default:
				throw new IllegalArgumentException();
		}
	
	}
	
	/**
	 * Sets the current TaskItem Type using a string parameter. The string 
	 * will correspond to one of the four Types specified in the
	 * Type enumerator. 
	 * @param type String representation of one of the four Type enumerators.
	 */
	private void setType(String type) {
		if(type == null || type.isEmpty()) throw new IllegalArgumentException();
		//if(type == this.getTypeString() || type == "T_FEATURE") this.type = Type.BUG;
		switch (type) {
		case T_BUG: //compare passed in string type against enum String Type;
			this.type = Type.BUG;
			break;
		case T_FEATURE:
			this.type = Type.FEATURE;
			break;
		case T_KNOWLEDGE_ACQUISITION:
			this.type = Type.KNOWLEDGE_ACQUISITION;
			break;
		case T_TECHNICAL_WORK:
			this.type = Type.TECHNICAL_WORK;
			break;
			default:
				throw new IllegalArgumentException();
		}
	
	}
	
	/**
	 * Getter method for the current TaskItem's Type.
	 * @return type Type of the TaskItem.
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * Getter method for the abbreviated string representation 
	 * of the current TaskItem's Type.
	 * @return typeString String representation of the current
	 * TaskItem Type.
	 */
	public String getTypeString() {
		switch (getType()) {
		case BUG:
			return T_BUG;
		case FEATURE:
			return T_FEATURE;
		case KNOWLEDGE_ACQUISITION:
			return T_KNOWLEDGE_ACQUISITION;
		case TECHNICAL_WORK:
			return T_TECHNICAL_WORK;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Getter method for the non-abbreviated String representation
	 * of the current TaskItem.
	 * @return fullTypeString Non-abbreviated String representation
	 * of the current TaskItem.
	 */
	public String getTypeFullString() {
		switch(getType()) {
		case BUG:
			return "Bug";
		case FEATURE:
			return "Feature";
		case KNOWLEDGE_ACQUISITION:
			return "Knowledge Acquisition";
		case TECHNICAL_WORK:
			return "Technicial Work";
			default: 
				throw new IllegalArgumentException();
		}
			
	}
	
	/**
	 * Getter method for the TaskItem owner.
	 * @return Owner of the current TaskItem.
	 */
	public String getOwner() { 
		 return this.owner;
		
	}
	
	/**
	 * Getter method for the TaskItem title.
	 * @return Title of the TaskItem.
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Getter method for the TaskItem creator.
	 * @return Creator of the current TaskItem.
	 */
	public String getCreator() {
		return this.creator;
	}
	
	/**
	 * Getter method that returns an ArrayList for all note
	 * entries associated with the current TaskItem.
	 * @return noteArray ArrayList of all note entries associated with 
	 * the current TaskItem.
	 */
	public ArrayList<Note> getNotes() {
		return this.notes;
		
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
		Task task = new Task();
		task.setTitle(getTitle());
		task.setOwner(getOwner());
		task.setId(this.getTaskItemId());
		task.setCreator(getCreator());
		task.setState(state.getStateName());
		task.setVerified(isVerified);
		task.setType(getTypeString());
		NoteList noteList = new NoteList();
		for(int i = 0; i < notes.size(); i++) {
			NoteItem noteitem = new NoteItem();
			noteitem.setNoteAuthor(notes.get(i).getNoteAuthor());
			noteitem.setNoteText(notes.get(i).getNoteText());
			noteList.getNotes().add(noteitem);
	
		}
		task.setNoteList(noteList);

		return task;
	}

	/**
	 * Sets the TaskItem's counter variable to that of the intriguer parameter. 
	 * @param counter Number to assign as the current counter value.
	 */
	public static void setCounter(int counter) {
		if(counter <= 0) throw new IllegalArgumentException();
		TaskItem.counter = counter;
	}
	
	/**
	 * Returns a 2D string array for Notes associated with the current TaskItem. 
	 * There is a row for each note entry, as well as, two columns for displaying
	 * the note author's name and note contents. 
	 * @return 2D String array containing the Note author and Note text for
	 * each note of the current TaskItem.
	 */
	public String[][] getNotesArray() {
		String[][] noteArray = new String[notes.size() + 1 ][2];
		for(int i = 0; i < notes.size(); i++) {
			noteArray[i][0] = notes.get(i).getNoteAuthor();
			noteArray[i][1] = notes.get(i).getNoteText();
		}
		return noteArray;
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
			owner = null; 
			isVerified = false;
		}
		
		/**
		 * Method used to progress from the BacklogState.
		 * @param Command The Command used to update the current state.
		 */
		public void updateState(Command command) {
			if(CommandValue.CLAIM == command.getCommand()) {
				owner = command.getNoteAuthor();
				setState(TaskItem.OWNED_NAME); 
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
			}
			
			else if(CommandValue.REJECT == command.getCommand()) {
				owner = null;
				setState(TaskItem.REJECTED_NAME);
			}
			else throw new UnsupportedOperationException();
			
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
			state = ownedState;
			isVerified = false;
		}

		
		/**
		 * Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 */
		public void updateState(Command command) {
			if(CommandValue.PROCESS == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				setState(TaskItem.PROCESSING_NAME); 
			}
			else if(CommandValue.BACKLOG == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				owner = null;
				setState(TaskItem.BACKLOG_NAME);
			}
			else if(CommandValue.REJECT == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				owner = null;
				setState(TaskItem.REJECTED_NAME);
			}
			else throw new UnsupportedOperationException();
		}
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return TaskItem.OWNED_NAME;
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
			state = processingState;
			isVerified = false;
		}

		/**
		 * Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 * 
		 */
		public void updateState(Command command) {
			if(CommandValue.VERIFY == command.getCommand() && !(getType() == Type.KNOWLEDGE_ACQUISITION)) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				setState(TaskItem.VERIFYING_NAME); 
			}
			else if(CommandValue.PROCESS == command.getCommand()){
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
			}
			else if(CommandValue.BACKLOG == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				owner = null;
				setState(TaskItem.BACKLOG_NAME);
			}
			else if(CommandValue.COMPLETE == command.getCommand() && (getType() == Type.KNOWLEDGE_ACQUISITION)) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				owner = null;
				setState(TaskItem.DONE_NAME);
			}
			else throw new UnsupportedOperationException();
		}

		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return TaskItem.PROCESSING_NAME;
		}
	}
	
	/**
	 * Concrete class that represents the verifying state of the Scrum Backlog FSM.
	 * 
	 * @author Steven Mayo
	 */
	private class VerifyingState implements TaskItemState {
		
		private VerifyingState() {
			state = verifyingState;
		}
		
		/**Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 * TaskItem
		 */
		public void updateState(Command command) {
			if(CommandValue.COMPLETE == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				isVerified = true;
				setState(TaskItem.DONE_NAME); 
			}
			else if(CommandValue.PROCESS == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				setState(TaskItem.PROCESSING_NAME);
			}
			else throw new UnsupportedOperationException();
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return TaskItem.VERIFYING_NAME;
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
			owner = null;
		}
		
		/**
		 * Method used to progress from the BacklogState.
		 * @param Command The Command used to update the current state.
		 */
		public void updateState(Command command) {
			if(CommandValue.BACKLOG == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				setState(TaskItem.BACKLOG_NAME); 
			}
			else if(CommandValue.PROCESS == command.getCommand()) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				setState(TaskItem.PROCESSING_NAME);
			}
			else throw new UnsupportedOperationException();
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return TaskItem.DONE_NAME;
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
			owner = null;
		}
		
		/**
		 * Method used to handle FSM transitions.
		 * @param commmand Command Object used to update the state of the current
		 * 
		 */
		public void updateState(Command command) {
			if(command.getCommand() == CommandValue.BACKLOG) {
				notes.add(new Note(command.getNoteAuthor(), command.getNoteText()));
				setState(TaskItem.BACKLOG_NAME);
			}
			else throw new UnsupportedOperationException();
			
		}
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		public String getStateName() {
			return TaskItem.REJECTED_NAME;
			
			
		}
	}
	
}
