package com.caimao.weixin.note.dao;

import java.util.List;

import com.caimao.weixin.note.domain.Reader;

public interface ReaderDao extends BaseDao<Reader, Integer> {

    List<Reader> findReaderByNoteId(Integer noteId);

    List<Reader> findReaderByNoteIdAndUserId(Integer noteId, Integer userId);

    List<Reader> findReaderList(String whereString);

	List<Reader> findReadersOfNote();

}
