package com.caimao.weixin.note.service;

import java.util.List;
import java.util.Map;

import com.caimao.weixin.note.domain.Deposit;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.util.Pager;

public interface DepositService extends BaseService<Deposit, Integer> {

    public void proccessNotePay(Deposit deposit, Note note);

    public void proccessReadPay(Deposit deposit);

    public void processYuePay(Integer userId, Integer noteId, Integer type, Double money) throws Exception;

	public String getPayUrl(Integer userId, Integer noteId, Integer optType, Integer payType) throws Exception;
    
    public Double findcountByCondition(String select,String tableName,String where) throws Exception;
    
	public List<Map<String, Object>> findAdminDepositList(Pager pager, String userId, String nickName, String billNo, String startDate, String endDate, Integer status);

}
