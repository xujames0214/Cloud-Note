package cn.tedu.note.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.note.dao.NoteDao;
import cn.tedu.note.dao.NotebookDao;
import cn.tedu.note.dao.StarsDao;
import cn.tedu.note.dao.UserDao;
import cn.tedu.note.entity.Note;
import cn.tedu.note.entity.Stars;
import cn.tedu.note.entity.User;
import cn.tedu.note.service.NoteService;
import cn.tedu.note.service.NotebookNotFoundException;
import cn.tedu.note.service.UserNotFoundException;

@Service("noteService")
public class NoteServiceImpl implements NoteService {

	@Resource
	private NoteDao noteDao;
	
	@Resource
	private NotebookDao notebookDao;
	
	@Resource
	private UserDao userDao;
	
	public List<Map<String, Object>> listNotes(String notebookId) throws NotebookNotFoundException{
		if(notebookId == null || notebookId.trim().isEmpty()){
			throw new NotebookNotFoundException("notebookId不能为空");
		}
				
		int n = notebookDao.countNotebookById(notebookId);
		if(n != 1){
			throw new NotebookNotFoundException("没有该notebook");
		}
		
		return noteDao.findNotesByNotebookId(notebookId);
	}
	
	@Transactional
	public Note addNote(String userId, String notebookId, String noteTitle)throws UserNotFoundException,
	NotebookNotFoundException,NoteNotFoundException{
		if(userId==null||userId.trim().isEmpty()){
			throw new UserNotFoundException("ID空");
		}
		User user=userDao.findUserById(userId);
		if(user==null){
			throw new UserNotFoundException("木有人");
		}
		if(notebookId==null||notebookId.trim().isEmpty()){
			throw new NotebookNotFoundException("ID空");
		}
		int n=notebookDao.countNotebookById(notebookId);
		if(n!=1){
			throw new NotebookNotFoundException("没有笔记本");
		}
		
		
		Note note = new Note();
		note.setId(UUID.randomUUID().toString());
		note.setUserId(userId);
		note.setNotebookId(notebookId);
		note.setStatusId("1");
		note.setTypeId(null);
		note.setTitle(noteTitle);
		note.setBody("");
		note.setCreateTime(System.currentTimeMillis());
		note.setModifyTime(System.currentTimeMillis());
		
		n = noteDao.addNote(note);
		if(n != 1){
			throw new NoteNotFoundException("保存失败");
		}
		
		addStars(userId,5);
		return note;
	}

	public Note getNote(String noteId)throws NoteNotFoundException{
		if(noteId==null||noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID空");
		}
		Note note = noteDao.findNoteById(noteId);
		if(note==null){
			throw new NoteNotFoundException("id错误");
		}
		return note;
	}

	public boolean moveNote(String notebookId, String noteId) {
		if(notebookId == null || notebookId.trim().isEmpty()){
			throw new NotebookNotFoundException("ID空");
		}
		if(noteId==null||noteId.trim().isEmpty()){
			throw new NoteNotFoundException("ID空");
		}
		Note note = new Note();
		note.setNotebookId(notebookId);
		note.setId(noteId);
		
		int n = noteDao.updateNote(note);
		if(n == 1){
			return true;
		}else{
			throw new NoteNotFoundException("更改失败!");
		}	
	}
	
	public List<Map<String, Object>> listNotesInTrashBin(
	        String userId) throws UserNotFoundException {
	    if(userId==null||userId.trim().isEmpty()){
	        throw new UserNotFoundException("ID空");
	    }
	    User user=userDao.findUserById(userId);
	    if(user==null){
	        throw new UserNotFoundException("木有人");
	    }
	    return noteDao.findDeleteNotesByUserId(userId);
	}

	
	public int deleteNotes(String... noteIds) throws NoteNotFoundException{
		for(String id:noteIds){
			int n = noteDao.deleteNoteById(id);
			if(n != 1){
				throw new NoteNotFoundException("noteId错误");
			}
		}
		return noteIds.length;
	}

	@Resource
	StarsDao starsDao;
	
	@Transactional
	public boolean addStars(String userId, int stars) throws UserNotFoundException{
		if(userId==null||userId.trim().isEmpty()){
			throw new UserNotFoundException("ID空");
		}
		User user=userDao.findUserById(userId);
		if(user==null){
			throw new UserNotFoundException("木有人");
		}
		//检查是否有星了
		Stars st = starsDao.findStarsByUserId(userId);
		if(st == null){
			String id = UUID.randomUUID().toString();
			st = new Stars(id,userId,stars);
			int n = starsDao.insertStars(st);
			if(n != 1){
				throw new RuntimeException("失败");
			}
		}else{
			int n = st.getStars() + stars;
			if(n < 0){
				throw new RuntimeException("star扣得太多");
			}
			st.setStars(n);
			n = starsDao.updateStars(st);
			if(n != 1){
				throw new RuntimeException("失败");
			}
		}
		return true;
	}

}
