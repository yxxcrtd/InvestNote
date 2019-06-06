package com.caimao.weixin.note.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.caimao.weixin.note.dao.CapitalDao;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.util.Pager;

@Repository
public class CapitalDaoImpl extends BaseDaoImpl<Capital, Integer, String, String> implements CapitalDao {


    @Override
    public Integer findAdminListCount(String where) {
        String sql = "SELECT COUNT(*) as cnt FROM wx_capital AS c LEFT JOIN wx_user AS u ON u.`user_id` = c.`capital_user_id` WHERE " + where;
        //List<Integer> integerList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Integer.class));
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        return Integer.valueOf(map.get("cnt").toString());
    }

    @Override
    public List<Capital> findAdminListPage(Pager pager, String where, String order) {
        String sql = "SELECT * FROM wx_capital AS c LEFT JOIN wx_user AS u ON u.`user_id` = c.`capital_user_id` WHERE "+where;
        if (order != null) sql += " ORDER BY " + order;
        if (pager != null) sql += " LIMIT " + pager.getPageSize() + " OFFSET " + pager.getOffset();
		LOGGER.info(sql);
        return jdbcTemplate.query(sql, new RowMappers.CapitalOtherMapper());
    }
}
