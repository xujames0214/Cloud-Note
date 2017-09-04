package cn.tedu.note.service;

import java.util.List;
import java.util.Map;

import cn.tedu.note.entity.Note;

public interface NoteService {
	List<Map<String,Object>> listNotes(String notebookId);
	
	Note addNote(String userId, String notebookId, String noteTitle);
	
	Note getNote(String noteId);
	
	boolean moveNote(String notebookId, String noteId);
	
	List<Map<String, Object>> listNotesInTrashBin(String userId);
	
	int deleteNotes(String... noteIds);
	
	boolean addStars(String userId,int stars);
}
