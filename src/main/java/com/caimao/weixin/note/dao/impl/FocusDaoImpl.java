package com.caimao.weixin.note.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.caimao.weixin.note.dao.FocusDao;
import com.caimao.weixin.note.domain.Focus;

@Repository
public class FocusDaoImpl extends BaseDaoImpl<Focus, Integer, String, String> implements FocusDao {

	@Override
	public Integer findFocusCountByUserId(String where, Integer userId) {
		String sql = "SELECT COUNT(focus_id) as count FROM wx_focus WHERE " + where + " = " + userId;
		LOGGER.info("{} : {} : {}", sql, where, userId);
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		return null == map.get("count") ? 0 : Integer.valueOf(map.get("count").toString());
	}

	@Override
	public Focus findFocusByUserIdAndFocusId(Integer userId, Integer otherId) {
		String sql = "SELECT * FROM wx_focus WHERE focus_user_id = ? AND focus_other_id = ?";
		LOGGER.info("{} : {} : {}", sql, userId, otherId);
		List<Focus> focusList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Focus.class), new Object[] { userId, otherId });
		return (null != focusList && !focusList.isEmpty() && 0 < focusList.size()) ? focusList.get(0) : null;
	}

}
