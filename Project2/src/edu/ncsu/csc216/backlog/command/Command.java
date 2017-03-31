package edu.ncsu.csc216.backlog.command;

/**
 * Concrete class that encapulates user interactions with the GUI for 
 * processing by the scrum backlog FSM in TaskItem.
 * 
 * @author Steven
 */
public class Command {
	
	/** Enum type for the types of CommandValues for each Command. */
	private CommandValue c;
	/** Note text attached with each Command construction. */
	private String note;
	/** Author of the Note */
	private String noteAuthor;
	/** CommandValue enumerator for the Scrum Backlog FSM. */
	public static enum CommandValue { BACKLOG, CLAIM, PROCESS, VERIFY, COMPLETE, REJECT }
	
	/**
	 * Constructor for the Command object. 
	 * @param command A CommandValue enumerator that will be used to update
	 * the TaskItem's state.
	 * @param noteAuthor Author of the TaskItem's note
	 * @param note Text content of the Note
	 */
	public Command(CommandValue command, String noteAuthor, String note) {
		if(command == null || note == null || note.isEmpty() || noteAuthor == null || noteAuthor.isEmpty())
			throw new IllegalArgumentException();
		this.note = note;
		this.noteAuthor = noteAuthor;
		if(command == CommandValue.BACKLOG) this.c = CommandValue.BACKLOG;
		else if(command == CommandValue.CLAIM) this.c = CommandValue.CLAIM;
		else if(command == CommandValue.PROCESS) this.c = CommandValue.PROCESS;
		else if(command == CommandValue.VERIFY) this.c = CommandValue.VERIFY;
		else if(command == CommandValue.COMPLETE) this.c = CommandValue.COMPLETE;
		else if(command == CommandValue.REJECT) this.c = CommandValue.REJECT;
		else throw new IllegalArgumentException();

	}
	
	/**
	 * Getter method that returns the CommandValue assigned during the
	 * Construction of the Command object.
	 * @return CommandValue CommandValue assigned during the Command 
	 * construction.
	 */
	public CommandValue getCommand() {
		return this.c;
						
	}
	
	/**
	 * Getter method that returns the text content of the Note object.
	 * @return text within the Note object.
	 */
	public String getNoteText() {
		return this.note;
	}
	
	/**
	 * Getter method that returns the Note objects author.
	 * @return Author of the Note object.
	 */
	public String getNoteAuthor() {
		return this.noteAuthor;
	}
}
