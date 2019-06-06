package com.caimao.weixin.note.service;

import java.util.List;

import com.caimao.weixin.note.domain.Comment;

public interface CommentService extends BaseService<Comment, Integer> {
	
	public List<Comment> findCommentByNoteId(Integer noteId) throws Exception;
	
	public  Comment findCommentById(Integer commentId) throws Exception;
	
	Long saveReturnId(Comment comment) throws Exception;
	
}
