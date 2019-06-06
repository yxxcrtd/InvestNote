package com.caimao.weixin.note.dao;

import java.util.List;
import java.util.Map;

import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.util.Pager;

public interface NoteDao extends BaseDao<Note, Integer> {

    Long saveReturnId(Note note);

    List<Note> findNoteListByFlagAndDatetime(int flag, String time);

	List<Note> findNoteListWithZeroPriceOfCurrentDate();

    List<Note> findStartedList();

    List<Note> findAllIngNoteList();

    List<Note> findSuccessNoteList(Integer userId);

    int findNoteCount(Integer userId);

    Float findUserYield(Integer userId);

	Integer findAdminNoteListCount(String where);

	List<Note> findAdminNoteListPage(Pager pager, String where, String order);
	
	public List<Map<String, Object>> findAdminTotalReadingDay();
	
	public List<Map<String, Object>> findAdminTotalPacketsDay();

	Integer findMyFocusNotesCount(Integer userId);

	List<Note> findMyFocusNotesWithPage(Integer userId, Pager pager);

}
