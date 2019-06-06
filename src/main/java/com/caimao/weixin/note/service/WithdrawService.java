package com.caimao.weixin.note.service;

import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.util.heepay.HeepayEntity.BatchResEntity;

public interface WithdrawService extends BaseService<Withdraw, Integer> {

	public boolean recordWithdraw(Withdraw withdraw) throws Exception;

	public Boolean doWithdraw(Withdraw withdraw, Integer status) throws Exception;

	public boolean resWithdraw(BatchResEntity batchResEntity) throws Exception;

	public boolean checkWithdraw(Integer wId) throws Exception;

	public boolean todayWithdraw(Integer userId) throws Exception;

}
