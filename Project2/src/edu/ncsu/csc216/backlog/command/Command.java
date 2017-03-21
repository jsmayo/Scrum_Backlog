package edu.ncsu.csc216.backlog.command;

public class Command {

	private String note;
	private String noteAuthor;
	public static enum CommandValue { BACKLOG, CLAIM, PROCESS, VERIFY, COMPLETE, REJECT }
	
	public Command(CommandValue command, String note, String noteAuthor) {
		if(command == null || note == null || note.isEmpty() || noteAuthor == null || noteAuthor.isEmpty())
			throw new IllegalArgumentException();
		this.note = note;
		this.noteAuthor = noteAuthor;
	}
	
	public CommandValue getCommand() {
		return CommandValue.BACKLOG;
	}
	
	public String getNoteText() {
		return this.note;
	}
	
	public String getNoteAuthor() {
		return this.noteAuthor;
	}
}
