package edu.ncsu.csc216.backlog.task;

/**
 * Represents a note for a task item and includes the note
 * text and author.
 * 
 * @author Steven Mayo
 */
public class Note {
	
	/** Author of the note */
	private String noteAuthor;
	/** Text contents of the note */
	private String noteText;
	
	/**
	 * Constructor for a Note object. Each note must have 
	 * an author and non-null/non-empty text content.
	 * @param author Author of the note.
	 * @param text The note's content. 
	 * @throws IllegalArgumentException if the author or text
	 * parameters are empty or null.
	 */
	public Note(String author, String text) {
		if(author == null || author.isEmpty() || text == null || text.isEmpty()) 
			throw new IllegalArgumentException();
		noteAuthor = author;
		noteText = text;
	}
	
	/**
	 * Getter method that returns the Author of the note.
	 * @return The note author.
	 */
	public String getNoteAuthor() {
		return this.noteAuthor;
	}
	
	/**
	 * Getter method that returns the note content.
	 * @return The note content.
	 */
	public String getNoteText() {
		return this.noteText;
	}
	
	/**
	 * Getter method that returns a String array containing the 
	 * note author and note content.
	 * @return String array containing the note author and content.
	 */
	public String[] getNoteArray() {
		return new String[] { noteAuthor, noteText };
	}
}
