package com.caimao.weixin.note.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.util.DateUtil;
import com.caimao.weixin.note.util.Pager;

@Repository
public class NoteDaoImpl extends BaseDaoImpl<Note, Integer, String, String> implements NoteDao {

    public Long saveReturnId(Note note) {
        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("wx_note").usingGeneratedKeyColumns("note_id");
        Map<String, Object> para = new HashMap<String, Object>(2);
        para.put("note_user_id", note.getNote_user_id());
        para.put("note_stock_code", note.getNote_stock_code());
        para.put("note_stock_name", note.getNote_stock_name());
        para.put("note_stock_open_price", note.getNote_stock_open_price());
        para.put("note_stock_high_price", note.getNote_stock_high_price());
        para.put("note_target_day", note.getNote_target_day());
        para.put("note_increase", note.getNote_increase());
		para.put("note_title", note.getNote_title());
        para.put("note_type", note.getNote_type());
        para.put("note_remark", note.getNote_remark());
        para.put("note_open_money", note.getNote_open_money());
        para.put("note_packet_money", note.getNote_packet_money());
        para.put("note_amount", note.getNote_amount());
        para.put("note_status", note.getNote_status());
        para.put("note_target_status", note.getNote_target_status());
        para.put("note_pay_status", note.getNote_pay_status());
        para.put("note_earnest_money", note.getNote_earnest_money());
        para.put("note_start_time", note.getNote_start_time());
        para.put("note_end_time", note.getNote_end_time());
		para.put("note_create_time", note.getNote_create_time());
        Number newId = insertActor.executeAndReturnKey(para);
        return Long.valueOf(newId.intValue());
    }

    /**
     * @param flag 0 开盘时间；1收盘时间
     * @param time 如果flag=0，则是每天上午的9点20；如果flag=1，则是每天下午的15点
     * @return
     */
    public List<Note> findNoteListByFlagAndDatetime(int flag, String time) {
        String sql = "";
        if (0 == flag) {
			sql = "SELECT * FROM wx_note WHERE note_status = 0 AND note_start_time < '" + time + "'";
        }
        if (1 == flag) {
			sql = "SELECT * FROM wx_note WHERE note_status = 1 AND note_end_time < '" + time + "'";
        }
		LOGGER.info("当天开始的SQL：{}，具体时间：{}", sql, time);
        List<Note> noteList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Note.class));
        return 0 == noteList.size() ? null : noteList;
    }

	/**
	 * 获取价格为0.00的，并且状态是进行中的所有笔记
	 */
	public List<Note> findNoteListWithZeroPriceOfCurrentDate() {
		String sql = "SELECT * FROM wx_note WHERE note_stock_open_price = 0.00 AND note_status = 1";
		LOGGER.info(sql);
		List<Note> noteList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Note.class));
		return 0 == noteList.size() ? null : noteList;
	}

    // 查询所有进行中的
    public List<Note> findAllIngNoteList() {
        String sql = "SELECT * FROM wx_note WHERE note_status = 1";
        List<Note> noteList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Note.class));
        return 0 == noteList.size() ? null : noteList;
    }

    // 查询所有未达标的，并且时间小于结束时间的
    public List<Note> findStartedList() {
		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date());
        String sql = "SELECT * FROM wx_note WHERE note_target_status = 0 AND note_end_time > '" + currentDate + " 14:59:59'";
        LOGGER.info(sql + " : " + currentDate + " 14:59:59'");
        List<Note> noteList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Note.class));
        return 0 == noteList.size() ? null : noteList;
    }

    // 查找当前用户成功的笔记数
    public List<Note> findSuccessNoteList(Integer userId) {
        String sql = "SELECT * FROM wx_note WHERE note_target_status = 1 AND note_user_id = ?";
        LOGGER.info(sql + " : " + userId);
        List<Note> noteList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Note.class), new Object[] { userId });
        return 0 == noteList.size() ? null : noteList;
    }

    @Override
    public int findNoteCount(Integer userId) {
        String sql = "SELECT COUNT(*) FROM wx_note WHERE note_user_id = " + userId + " AND note_status = 2 AND note_target_status != 2";
        LOGGER.info(sql + " : " + userId);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Float findUserYield(Integer userId) {
        String sql = "SELECT SUM(note_increase) / COUNT(*) FROM wx_note WHERE note_target_status = 1 AND note_user_id = " + userId;
        LOGGER.info(sql + " : " + userId);
        return jdbcTemplate.queryForObject(sql, Float.class);
    }

    /**
     * 后台查询笔记数
     * @param where
     * @return
     */
    @Override
    public Integer findAdminNoteListCount(String where) {
		String sql = "SELECT count(*) as cnt FROM wx_note AS n LEFT JOIN wx_user AS u ON n.note_user_id = u.user_id WHERE " + where;
        //List<Integer> integerList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Integer.class));
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        return Integer.valueOf(map.get("cnt").toString());
    }

    /**
     * 后台查询笔记列表
     * @param pager
     * @param where
     * @param order
     * @return
     */
    @Override
    public List<Note> findAdminNoteListPage(Pager pager, String where, String order) {
		String sql = "SELECT * FROM wx_note AS n LEFT JOIN wx_user AS u ON n.note_user_id = u.user_id WHERE " + where;
        if (order != null) sql += " ORDER BY " + order;
        if (pager != null) sql += " LIMIT " + pager.getPageSize() + " OFFSET " + pager.getOffset();
        return jdbcTemplate.query(sql, new RowMappers.NoteOtherMapper());
    }

	@Override
	public List<Map<String, Object>> findAdminTotalReadingDay() {
		//String sql = "SELECT  SUM(user_available_money)  AS cnt FROM wx_user";
		String sql = "SELECT SUM(c.capital_freeze) AS money, DATE_FORMAT(n.note_create_time, '%Y-%m-%d') AS ct  FROM wx_capital c  LEFT JOIN wx_note n  ON n.note_user_id=c.capital_user_id  WHERE n.note_status = 1  AND c.capital_type = 6 GROUP BY ct  ORDER BY ct DESC LIMIT 20";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> findAdminTotalPacketsDay() {
		//String sql = "SELECT  SUM(user_available_money)  AS cnt FROM wx_user";
		String sql = "SELECT SUM(c.capital_freeze) AS money, DATE_FORMAT(n.note_create_time, '%Y-%m-%d') AS ct  FROM wx_capital c  LEFT JOIN wx_note n  ON n.note_user_id=c.capital_user_id  WHERE n.note_status = 1  AND c.capital_type = 3 GROUP BY ct  ORDER BY ct DESC LIMIT 20";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public Integer findMyFocusNotesCount(Integer userId) {
		String sql = "SELECT COUNT(note_id) FROM wx_note WHERE note_user_id IN (SELECT focus_other_id FROM wx_focus WHERE focus_user_id = ?)";
		LOGGER.info("{} : {}", sql, userId);
		return jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { userId });
	}

	@Override
	public List<Note> findMyFocusNotesWithPage(Integer userId, Pager pager) {
		String sql = new StringBuffer("SELECT n.note_id, n.note_user_id, n.note_title, n.note_remark, note_open_money, n.note_target_day, n.note_increase, n.note_status, n.note_target_status, n.note_create_time, u.user_nickname, u.user_header_img ").append("FROM wx_note n ").append("LEFT JOIN wx_user u ON n.note_user_id = u.user_id ").append("WHERE n.note_user_id IN (SELECT focus_other_id FROM wx_focus WHERE focus_user_id = ?) ").append("ORDER BY n.note_create_time DESC LIMIT ").append(pager.getPageSize()).append(" OFFSET ").append(pager.getOffset()).toString();
		LOGGER.info("{} : {} : {} : {}", sql, userId, pager.getPageSize(), pager.getOffset());
		return jdbcTemplate.query(sql, new RowMappers.NoteMyFocusMapper(), new Object[] { userId });
	}

}
