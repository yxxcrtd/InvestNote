package com.caimao.weixin.note.dao;

import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.util.Pager;

import java.util.List;
import java.util.Map;

public interface WithdrawDao extends BaseDao<Withdraw, Integer> {

    public Withdraw findByBatchNo(String batchNo);

    public Integer findAdminWithdrawListCount(String where);

    public List<Withdraw> findAdminWithdrawList(Pager pager, String where, String order);

    public List<Map<String, Object>> findAdminWithdrawDay();
}
