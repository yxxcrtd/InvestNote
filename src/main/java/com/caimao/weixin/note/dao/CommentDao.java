package com.caimao.weixin.note.dao;

import java.util.List;

import com.caimao.weixin.note.domain.Comment;

public interface CommentDao extends BaseDao<Comment, Integer> {
	
	/**
	 * @param noteId
	 * 一个笔记可以被多人评论
	 * @return
	 */
    List<Comment> findCommentByNoteId(Integer noteId);
    
    /**
     * 添加评论
     * @param comment
     * @return
     */
    Long saveReturnId(Comment comment);
    
}
