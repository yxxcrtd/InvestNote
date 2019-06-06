package com.caimao.weixin.note.service;

import java.util.List;

import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.util.Pager;

public interface NoteService extends BaseService<Note, Integer> {

    Long saveReturnId(Note note) throws Exception;

	void changeNoteTargetStatus(Integer noteId) throws Exception;

	void changeNoteEndStatus(Integer noteId) throws Exception;

	void changeNoteVoidStatus(Integer noteId) throws Exception;

	Integer findMyFocusNotesCount(Integer userId);
	
	List<Note> findMyFocusNotesWithPage(Integer userId, Pager pager);

}
