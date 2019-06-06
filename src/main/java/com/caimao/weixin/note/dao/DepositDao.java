package com.caimao.weixin.note.dao;

import java.util.List;
import java.util.Map;

import com.caimao.weixin.note.domain.Deposit;
import com.caimao.weixin.note.util.Pager;

public interface DepositDao extends BaseDao<Deposit, Integer> {

    public Deposit findByBillId(String billId);
    
    public Integer findAdminListCount(String where);

    public List<Map<String, Object>> findAdminListByLeftJoin(Pager pager, String where, String orderbyString);

    public List<Map<String, Object>> findAdminDepositDay();
    
    public Double findSumMoneyBy(String tableName, String select, String where);
    
	List<Deposit> findDepositList(Integer userId, Integer noteId, Integer depositAction);
}
