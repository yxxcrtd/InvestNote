package com.caimao.weixin.note.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import com.caimao.weixin.note.dao.CommentDao;
import com.caimao.weixin.note.domain.Comment;
import com.caimao.weixin.note.util.Pager;

@Service
public class CommentDaoImpl extends BaseDaoImpl<Comment, Integer, String, String> implements CommentDao {

	@Override
	public List<Comment> findCommentByNoteId(Integer noteId) {
        String sql = "SELECT * FROM wx_comment WHERE comment_note_id = ?";
        LOGGER.info("{} : {}", sql, noteId);
        List<Comment> commentList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Comment.class), new Object[] { noteId });
        return 0 == commentList.size() ? null : commentList;
	}
	
    public List<Comment> findListByLeftJoin(Pager pager, String whereString) {
        StringBuffer sql = new StringBuffer("SELECT * ")
                .append(" FROM wx_comment c ")
                .append(" WHERE ")
                .append(whereString)
                .append(" ORDER BY c.comment_create_time DESC");
        if (pager != null) {
            sql.append(" LIMIT ").append(pager.getPageSize()).append(" OFFSET ").append(pager.getOffset());
        }
        LOGGER.info(sql.toString());
        return jdbcTemplate.query(sql.toString(),new RowMappers.CommentMapper());
    }

	@Override
	public Long saveReturnId(Comment comment) {
	    SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("wx_comment").usingGeneratedKeyColumns("comment_id");
	    Map<String, Object> para = new HashMap<String, Object>(2);
	    para.put("comment_id",comment.getComment_id() );
	    para.put("comment_user_id", comment.getComment_user_id());
	    para.put("comment_user_nickname", comment.getComment_user_nickname());
	    para.put("comment_user_header_img", comment.getComment_user_header_img());
	    para.put("comment_stock_code", comment.getComment_stock_code());
	    para.put("comment_stock_name", comment.getComment_stock_name());
	    para.put("comment_note_remark", comment.getComment_note_remark());
	    para.put("comment_note_id", comment.getComment_note_id());
	    para.put("comment_content", comment.getComment_content());
	    para.put("comment_create_time", comment.getComment_create_time());
	    Number newId = insertActor.executeAndReturnKey(para);
	    return Long.valueOf(newId.intValue());
	}
}
