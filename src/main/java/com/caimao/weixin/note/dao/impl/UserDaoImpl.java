package com.caimao.weixin.note.dao.impl;

import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Integer, String, String> implements UserDao {

    public User findByOpenId(String openId) {
        String sql = new StringBuffer("SELECT * FROM wx_user WHERE user_open_id = ?").toString();
        LOGGER.info(sql + " : " + openId);
        List<User> userList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), openId);
        return 0 == userList.size() ? null : userList.get(0);
    }

    public List<User> findListByLeftJoin(String whereString) {
        String sql = new StringBuffer("SELECT u.user_header_img FROM wx_reader r LEFT JOIN wx_user u ON r.reader_user_id = u.user_id WHERE ").append(whereString).toString();
        LOGGER.info(sql);
        return jdbcTemplate.query(sql, new RowMappers.ReaderMapper());
    }

    @Override
    public User findByPhone(String phone) {
        String sql = new StringBuffer("SELECT * FROM wx_user WHERE user_phone = ?").toString();
        LOGGER.info(sql + " : " + phone);
        List<User> userList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), phone);
        return 0 == userList.size() ? null : userList.get(0);
    }

	@Override
	public List<Map<String, Object>> findAdminTotalAmountDay() {
		String sql = "SELECT  SUM(user_available_money)  AS money, DATE_FORMAT(user_create_time, '%Y-%m-%d') AS ct FROM wx_user GROUP BY ct  ORDER BY ct DESC LIMIT 20";
		return this.jdbcTemplate.queryForList(sql);
	}
}
