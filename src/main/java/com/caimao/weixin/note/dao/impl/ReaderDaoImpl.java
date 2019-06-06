package com.caimao.weixin.note.dao.impl;

import com.caimao.weixin.note.dao.ReaderDao;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.util.DateUtil;
import com.caimao.weixin.note.util.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ReaderDaoImpl extends BaseDaoImpl<Reader, Integer, String, String> implements ReaderDao {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ReaderDaoImpl.class);

    /**
     * 根据笔记ID获取所有读者列表
     *
     * @param noteId
     * @return
     */
    public List<Reader> findReaderByNoteId(Integer noteId) {
        String sql = "SELECT * FROM wx_reader WHERE reader_note_id = ?";
        LOGGER.info("{} : {}", sql, noteId);
        List<Reader> readerList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Reader.class), new Object[] { noteId });
        return 0 == readerList.size() ? null : readerList;
    }

    public List<Reader> findReaderByNoteIdAndUserId(Integer noteId, Integer userId) {
        String sql = "SELECT * FROM wx_reader WHERE reader_note_id = ? AND reader_user_id = ?";
        LOGGER.info("{} : {}, {}", sql, noteId, userId);
        RowMapper<Reader> rowMapper = BeanPropertyRowMapper.newInstance(Reader.class);
        List<Reader> list = (List<Reader>) jdbcTemplate.query(sql, rowMapper, new Object[] { noteId, userId });
        return list;
    }

    @Override
    public List<Reader> findReaderList(String whereString) {
        String sql = new StringBuffer("SELECT u.user_id, u.user_header_img FROM wx_reader r LEFT JOIN wx_user u ON r.reader_user_id = u.user_id WHERE ").append(whereString).toString();
        LOGGER.info(sql);
        return jdbcTemplate.query(sql, new RowMappers.ReaderUserMapper());
    }

    public List<Reader> findListByLeftJoin(Pager pager, String whereString) {
        StringBuffer sql = new StringBuffer("SELECT * ")
                .append(" FROM wx_reader r ")
                .append(" LEFT JOIN wx_note n ON r.reader_note_id = n.note_id ")
                .append(" LEFT JOIN wx_user u ON n.note_user_id = u.user_id ")
                .append(" WHERE ")
                .append(whereString)
                .append(" ORDER BY n.note_status, n.note_create_time DESC");
        if (pager != null) {
            sql.append(" LIMIT ").append(pager.getPageSize()).append(" OFFSET ").append(pager.getOffset());
        }
        LOGGER.info(sql.toString());
        return jdbcTemplate.query(sql.toString(), new RowMappers.ReaderOtherMapper());
    }

	@Override
	public List<Reader> findReadersOfNote() {
		String privousDate = DateUtil.getPreviousDay("yyyy-MM-dd", new Date());
		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date());
		String sql = new StringBuffer("SELECT r.reader_note_id, COUNT(r.reader_note_id), n.note_stock_name, n.note_create_time, u.user_id, u.user_nickname, u.user_open_id FROM wx_reader r LEFT JOIN wx_note n ON r.reader_note_id = n.note_id LEFT JOIN wx_user u ON n.note_user_id = u.user_id WHERE n.note_status = 1 AND r.reader_create_time BETWEEN '" + privousDate + " 21:00:00' AND '" + currentDate + " 21:00:00' GROUP BY r.reader_note_id").toString();
		LOGGER.info(sql);
		return jdbcTemplate.query(sql, new RowMappers.ReaderOfNote());
	}

}
