package com.caimao.weixin.note.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.caimao.weixin.note.dao.DepositDao;
import com.caimao.weixin.note.domain.Deposit;
import com.caimao.weixin.note.util.Pager;

@Repository
public class DepositDaoImpl extends BaseDaoImpl<Deposit, Integer, String, String> implements DepositDao {

    /**
     * 根据业务订单ID，获取充值记录
     * @param billId
     * @return
     */
    public Deposit findByBillId(String billId) {
        String sql = new StringBuffer("SELECT * FROM wx_deposit").append(" WHERE ").append("deposit_bill_id = ?").toString();
        LOGGER.info(sql + " : " + billId);
        RowMapper<Deposit> rowMapper = BeanPropertyRowMapper.newInstance(Deposit.class);
        List<Deposit> list = jdbcTemplate.query(sql, rowMapper, new Object[] { billId });
		return (null != list && !list.isEmpty() && 0 < list.size()) ? list.get(0) : null;
    }

    /**
     * 获取后台充值总数
     * @param where
     * @return
     */
    @Override
    public Integer findAdminListCount(String where) {
        String sql = "SELECT count(*) as cnt FROM wx_deposit AS d LEFT JOIN wx_user AS u ON d.`deposit_user_id` = u.`user_id` WHERE " + where;
        LOGGER.info(sql);
        Map<String, Object> map = this.jdbcTemplate.queryForMap(sql);
        return Integer.valueOf(map.get("cnt").toString());
    }

    /**
     * 获取后台充值列表
     * @param pager
     * @param where
     * @param orderbyString
     * @return
     */
    @Override
    public List<Map<String, Object>> findAdminListByLeftJoin(Pager pager, String where, String orderbyString) {
        String sql = "SELECT * FROM wx_deposit AS d LEFT JOIN wx_user AS u ON d.`deposit_user_id` = u.`user_id` WHERE " + where;
        if (orderbyString != null) sql += " ORDER BY " + orderbyString;
        if (pager != null) sql += " LIMIT " + pager.getPageSize() + " OFFSET " + pager.getOffset();
        return this.jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findAdminDepositDay() {
        String sql = "SELECT SUM(deposit_money) as money, DATE_FORMAT(deposit_create_time, '%Y-%m-%d') AS ct FROM `wx_deposit` WHERE deposit_status = 2 GROUP BY ct ORDER BY ct DESC LIMIT 20";
        return this.jdbcTemplate.queryForList(sql);
    }

	@Override
	public Double findSumMoneyBy(String tableName, String select, String where) {
		String sql = "SELECT  " + select + "  as cnt FROM wx_"+tableName+" WHERE  " + where; 
        LOGGER.info(sql);
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
        if(rows==null || rows.size() <= 0) {
        	return 0.0;
        }
        return (Double) rows.get(0).get("cnt");
	}

	@Override
	public List<Deposit> findDepositList(Integer userId, Integer noteId, Integer depositAction) {
		String sql = "SELECT * FROM wx_deposit WHERE deposit_user_id = ? AND deposit_note_id = ? AND deposit_action = ?";
		LOGGER.info(sql + " : " + userId + "，" + noteId + "，" + depositAction);
		List<Deposit> depositList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Deposit.class), new Object[] { userId, noteId, depositAction });
		return null != depositList && 0 < depositList.size() ? depositList : null;
	}

}
