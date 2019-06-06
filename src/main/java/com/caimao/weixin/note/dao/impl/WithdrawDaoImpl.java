package com.caimao.weixin.note.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.caimao.weixin.note.dao.WithdrawDao;
import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.util.Pager;

@Repository
public class WithdrawDaoImpl extends BaseDaoImpl<Withdraw, Integer, String, String> implements WithdrawDao {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Withdraw findByBatchNo(String batchNo) {
        String sql = "SELECT * FROM wx_withdraw WHERE withdraw_batch_no = '?'";
        return (Withdraw) jdbcTemplate.query(sql, new BeanPropertyRowMapper(Withdraw.class), new Object[] {batchNo});
    }


    /**
     * 后台获取提现总数
     * @param where
     * @return
     */
    @Override
    public Integer findAdminWithdrawListCount(String where) {
        String sql = "SELECT count(*) as cnt FROM wx_withdraw AS w LEFT JOIN wx_user AS u ON w.`withdraw_user_id` = u.`user_id` LEFT JOIN wx_bank AS b ON w.`withdraw_bank_id` = b.`bank_id` WHERE " + where;
        Map<String, Object> map = this.jdbcTemplate.queryForMap(sql);
        return Integer.valueOf(map.get("cnt").toString());
    }

    /**
     * 后台获取提现列表
     * @param pager
     * @param where
     * @param order
     * @return
     */
    @Override
    public List<Withdraw> findAdminWithdrawList(Pager pager, String where, String order) {
        String sql = "SELECT * FROM wx_withdraw AS w LEFT JOIN wx_user AS u ON w.`withdraw_user_id` = u.`user_id` LEFT JOIN wx_bank AS b ON w.`withdraw_bank_id` = b.`bank_id` WHERE";
        sql += " " + where;
        if (order != null) sql += " ORDER BY " + order + " ";
        if (pager != null) sql += " LIMIT " + pager.getPageSize() + " OFFSET " + pager.getOffset();
		LOGGER.info(sql);
        return jdbcTemplate.query(sql, new RowMappers.WithdrawOtherMapper());
    }

    @Override
    public List<Map<String, Object>> findAdminWithdrawDay() {
        String sql = "SELECT  SUM(withdraw_money)  AS money, DATE_FORMAT(withdraw_create_time, '%Y-%m-%d') AS ct FROM wx_withdraw WHERE withdraw_status = 3 GROUP BY ct ORDER BY ct DESC LIMIT 20";
        return this.jdbcTemplate.queryForList(sql);
    }
}
