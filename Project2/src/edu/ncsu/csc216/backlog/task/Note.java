package edu.ncsu.csc216.backlog.task;

public class Note {
	
	private String noteAuthor;
	private String noteText;
	
	public Note(String author, String text) {
		if(author == null || author.isEmpty() || text == null || text.isEmpty()) 
			throw new IllegalArgumentException();
		noteAuthor = author;
		noteText = text;
	}
	
	public String getNoteAuthor() {
		return this.noteAuthor;
	}
	
	public String getNoteText() {
		return this.noteText;
	}
	
	public String[] getNoteArray() {
		return new String[] { noteAuthor, noteText };
	}
}
