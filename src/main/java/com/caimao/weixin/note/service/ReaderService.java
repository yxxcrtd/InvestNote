package com.caimao.weixin.note.service;

import java.util.List;

import com.caimao.weixin.note.domain.Reader;

public interface ReaderService extends BaseService<Reader, Integer> {

    List<Reader> findReaderByNoteIdAndUserId(Integer noteId, Integer userId);

    List<Reader> findReaderList(String whereString);
    
    void changeReaderTargetStatus(Reader reader) throws Exception;

	List<Reader> findReadersOfNote();
	
}
