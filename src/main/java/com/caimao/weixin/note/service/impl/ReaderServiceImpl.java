package com.caimao.weixin.note.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.dao.ReaderDao;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.service.ReaderService;

@Service
public class ReaderServiceImpl extends BaseServiceImpl<Reader, Integer> implements ReaderService {

    @Autowired
    protected NoteDao notedao;

    public List<Reader> findReaderByNoteIdAndUserId(Integer noteId, Integer userId) {
        return ((ReaderDao) baseDao).findReaderByNoteIdAndUserId(noteId, userId);
    }

    @Override
    public List<Reader> findReaderList(String whereString) {
        return ((ReaderDao) baseDao).findReaderList(whereString);
    }

	@Override
	public void changeReaderTargetStatus(Reader reader) throws Exception {
		if (null != reader) {
			((ReaderDao) baseDao).update(reader);
		}
	}

	@Override
	public List<Reader> findReadersOfNote() {
		return ((ReaderDao) baseDao).findReadersOfNote();
	}

}
