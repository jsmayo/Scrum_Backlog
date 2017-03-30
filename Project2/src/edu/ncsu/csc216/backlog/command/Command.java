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
	
	public Command(CommandValue command, String note, String noteAuthor) {
		if(command == null || note == null || note.isEmpty() || noteAuthor == null || noteAuthor.isEmpty())
			throw new IllegalArgumentException();
		this.note = note;
		this.noteAuthor = noteAuthor;
		switch (command) {
		case BACKLOG:
			this.c = CommandValue.BACKLOG;
			break;

		case CLAIM:
			this.c = CommandValue.CLAIM;
			break;

		case PROCESS: 
			this.c = CommandValue.PROCESS;
			break;

		case VERIFY:
			this.c = CommandValue.PROCESS;
			break;

		case COMPLETE:
			this.c = CommandValue.COMPLETE;
			break;

		case REJECT:
			this.c = CommandValue.REJECT;
			break;

		default:
			throw new IllegalArgumentException();		
		}
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
