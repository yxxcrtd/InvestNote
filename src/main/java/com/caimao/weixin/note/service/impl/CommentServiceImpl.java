package com.caimao.weixin.note.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.CommentDao;
import com.caimao.weixin.note.domain.Comment;
import com.caimao.weixin.note.service.CommentService;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, Integer> implements CommentService {

	@Autowired
	private CommentDao commentDao;
	
	@Override
	public List<Comment> findCommentByNoteId(Integer noteId) throws Exception {
		List<Comment> commentList = commentDao.findCommentByNoteId(noteId);
		if(commentList==null || commentList.size() <= 0) {
			LOGGER.info("【noteId笔记无人评论】",noteId);
			throw new Exception("noteId笔记无人评论" +noteId);
		}
		return commentList;
	}

	@Override
	public Long saveReturnId(Comment comment) throws Exception {
		// 保存
		Long newId = ((CommentDao) baseDao).saveReturnId(comment);
		// 返回新的主键ID
		return newId;
	}

	@Override
	public Comment findCommentById(Integer commentId) throws Exception {
		return ((CommentDao) baseDao).findById(commentId);
	}
}
