package edu.ncsu.csc216.backlog.command;

/**
 * Concrete class that encapulates user interactions with the GUI for 
 * processing by the scrum backlog FSM in TaskItem.
 * 
 * @author Steven
 */
public class Command {
	
	private CommandValue c;
	private String note;
	private String noteAuthor;
	/** CommandValue enumerator for the Scrum Backlog FSM. */
	public static enum CommandValue { BACKLOG, CLAIM, PROCESS, VERIFY, COMPLETE, REJECT }
	
	public Command(CommandValue command, String note, String noteAuthor) {
		if(command == null || note == null || note.isEmpty() || noteAuthor == null || noteAuthor.isEmpty())
			throw new IllegalArgumentException();
		this.note = note;
		this.noteAuthor = noteAuthor;
		getCommand();
	}
	
	public CommandValue getCommand() {
		switch (c) {
			case BACKLOG:
				return c = CommandValue.BACKLOG;
				
			case CLAIM:
				return c = CommandValue.CLAIM;
				
			case PROCESS: 
				return c = CommandValue.PROCESS;
				
			case VERIFY:
				return c = CommandValue.PROCESS;
				
			case COMPLETE:
				return c = CommandValue.COMPLETE;
				
			case REJECT:
				return c = CommandValue.REJECT;
				
				default:
					throw new IllegalArgumentException();
		}
						
	}
	
	public String getNoteText() {
		return this.note;
	}
	
	public String getNoteAuthor() {
		return this.noteAuthor;
	}
}
