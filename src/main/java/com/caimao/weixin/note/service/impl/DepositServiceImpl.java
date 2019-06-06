package com.caimao.weixin.note.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.caimao.weixin.note.dao.CapitalDao;
import com.caimao.weixin.note.dao.DepositDao;
import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.dao.ReaderDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.domain.Deposit;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.enums.ECapitalType;
import com.caimao.weixin.note.enums.EDepositStatus;
import com.caimao.weixin.note.enums.EDepositType;
import com.caimao.weixin.note.enums.ENotePayStatus;
import com.caimao.weixin.note.service.DepositService;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.execption.BusinessException;
import com.caimao.weixin.note.util.heepay.Common.HeepayHelper;
import com.caimao.weixin.note.util.heepay.HeepayEntity.GatewayEntity;

@Service
public class DepositServiceImpl extends BaseServiceImpl<Deposit, Integer> implements DepositService {

	@Autowired
	private NoteDao noteDao;

	@Autowired
	private ReaderDao readerDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CapitalDao capitalDao;

	@Autowired
	private DepositDao depositDao;

	@Autowired
	private HeepayHelper heepayHelper;

	/**
	 * 处理笔记支付保证金的过程
	 * 
	 * @param deposit
	 * @param note
	 */
	public void proccessNotePay(Deposit deposit, Note note) {
		deposit = this.depositDao.getById(deposit.getDeposit_id());
		note = this.noteDao.getById(note.getNote_id());

		// 如果笔记保证金已经支付，直接退出
		if (note.getNote_pay_status() == 2) {
			return;
		}

		// 如果不是未处理的，不那个啥
		if (deposit.getDeposit_status() != EDepositStatus.INIT.getCode()) {
			return;
		}

		User user = this.userDao.findById(deposit.getDeposit_user_id());
		// 笔记数量加1
		// user.setUser_note_count(user.getUser_note_count() + 1);

		// 添加用户资金记录
		user.setUser_available_money(user.getUser_available_money() + deposit.getDeposit_money());
		this.userDao.update(user);

		// 记录用户资金流水
		Capital capital = new Capital();
		capital.setCapital_user_id(deposit.getDeposit_user_id());
		capital.setCapital_type(ECapitalType.DEPOSIT.getCode());
		capital.setCapital_mount(deposit.getDeposit_money());
		capital.setCapital_available(user.getUser_available_money());
		capital.setCapital_freeze(user.getUser_freeze_money());
		capital.setCapital_create_time(new Date());
		this.capitalDao.save(capital);

		// 在扣除掉用户资金
		user.setUser_available_money(user.getUser_available_money() - deposit.getDeposit_money());
		user.setUser_freeze_money(user.getUser_freeze_money() + deposit.getDeposit_money());
		this.userDao.update(user);
		// 在记录用户流水
		Capital capital2 = new Capital();
		capital2.setCapital_user_id(deposit.getDeposit_user_id());
		capital2.setCapital_type(ECapitalType.SENDREDPACKET.getCode());
		capital2.setCapital_mount(-deposit.getDeposit_money());
		capital2.setCapital_available(user.getUser_available_money());
		capital2.setCapital_freeze(user.getUser_freeze_money());
		capital2.setCapital_create_time(new Date());
		this.capitalDao.save(capital2);

		if (deposit.getDeposit_status() != EDepositStatus.OK.getCode()) {
			// 更新充值订单状态
			deposit.setDeposit_status(EDepositStatus.OK.getCode());
			this.update(deposit);
		}
		if (note.getNote_pay_status() != ENotePayStatus.OK.getCode()) {
			// 更新笔记状态
			note.setNote_pay_status(ENotePayStatus.OK.getCode());
			this.noteDao.update(note);
		}
	}

	/**
	 * 处理阅读笔记支付成功后的过程
	 * 
	 * @param deposit
	 */
	public void proccessReadPay(Deposit deposit) {
		deposit = this.depositDao.getById(deposit.getDeposit_id());
		Note note = this.noteDao.getById(deposit.getDeposit_note_id());

		if (deposit.getDeposit_status() != EDepositStatus.INIT.getCode()) {
			return;
		}
		// 查詢他是否已經購買閱讀過，有則直接返回
		List<Reader> readerList = this.readerDao.findReaderByNoteIdAndUserId(deposit.getDeposit_note_id(), deposit.getDeposit_user_id());
		if (readerList != null && readerList.size() > 0) {
			return;
		}

		// 增加笔记的阅读数
		note.setNote_amount(note.getNote_amount() + 1);
		this.noteDao.update(note);

		User user = this.userDao.findById(deposit.getDeposit_user_id());
		// 添加用户资金记录
		user.setUser_available_money(user.getUser_available_money() + deposit.getDeposit_money());
		this.userDao.update(user);
		// 记录用户资金流水
		Capital capital = new Capital();
		capital.setCapital_user_id(deposit.getDeposit_user_id());
		capital.setCapital_type(ECapitalType.DEPOSIT.getCode());
		capital.setCapital_mount(deposit.getDeposit_money());
		capital.setCapital_available(user.getUser_available_money());
		capital.setCapital_freeze(user.getUser_freeze_money());
		capital.setCapital_create_time(new Date());
		this.capitalDao.save(capital);

		// 在扣除掉用户资金
		user.setUser_available_money(user.getUser_available_money() - deposit.getDeposit_money());
		this.userDao.update(user);
		// 在记录用户流水
		Capital capital2 = new Capital();
		capital2.setCapital_user_id(deposit.getDeposit_user_id());
		capital2.setCapital_type(ECapitalType.READ.getCode());
		capital2.setCapital_mount(-deposit.getDeposit_money());
		capital2.setCapital_available(user.getUser_available_money());
		capital2.setCapital_freeze(user.getUser_freeze_money());
		capital2.setCapital_create_time(new Date());
		this.capitalDao.save(capital2);

		if (deposit.getDeposit_status() != EDepositStatus.OK.getCode()) {
			// 更新充值订单状态
			deposit.setDeposit_status(EDepositStatus.OK.getCode());
			this.update(deposit);
		}
		// 添加用户购买阅读笔记的记录
		Reader reader = new Reader();
		reader.setReader_note_id(deposit.getDeposit_note_id());
		reader.setReader_user_id(deposit.getDeposit_user_id());
		reader.setReader_money(deposit.getDeposit_money());
		reader.setReader_create_time(new Date());
		this.readerDao.save(reader);
	}

	/**
	 * 用户使用余额进行支付的操作
	 * 
	 * @param userId
	 * @param noteId
	 * @param type
	 * @param money
	 */
	public void processYuePay(Integer userId, Integer noteId, Integer type, Double money) throws Exception {
		Note note = this.noteDao.getById(noteId);
		money = note.getNote_open_money();

		if (type == 1) {
			// 支付保證金，如果已經支付，直接退出
			if (note.getNote_pay_status() == 2) {
				return;
			}
		}
		if (type == 2) {
			// 支付閱讀費用，如果已經支付，直接退出
			List<Reader> readerList = this.readerDao.findReaderByNoteIdAndUserId(noteId, userId);
			if (readerList != null && readerList.size() > 0) {
				return;
			}
		}

		// 获取用户信息
		User user = this.userDao.findById(userId);
		if (money > 0) {
			if (user.getUser_available_money() < money) {
				throw new BusinessException("用户余额不足", 888888);
			}

			// 减少用户可用资金，增加用户冻结资金
			user.setUser_available_money(user.getUser_available_money() - money);
			// 保证金的，需要冻结资金
			if (Objects.equals(type, EDepositType.NOTE.getCode())) {
				user.setUser_freeze_money(user.getUser_freeze_money() + money);
			}

			// 记录资金变动历史
			Capital capital = new Capital();
			capital.setCapital_user_id(userId);
			if (Objects.equals(type, EDepositType.NOTE.getCode())) {
				capital.setCapital_type(ECapitalType.SENDREDPACKET.getCode());
			} else {
				capital.setCapital_type(ECapitalType.READ.getCode());
			}

			capital.setCapital_mount(-money);
			capital.setCapital_available(user.getUser_available_money());
			capital.setCapital_freeze(user.getUser_freeze_money());
			capital.setCapital_create_time(new Date());
			this.capitalDao.save(capital);
			// 更新用户
			this.userDao.update(user);
		}

		if (type.equals(EDepositType.NOTE.getCode())) {
			// 变更笔记状态
			note.setNote_pay_status(ENotePayStatus.OK.getCode());
			this.noteDao.update(note);
			// 添加用户笔记数
			// user.setUser_note_count(user.getUser_note_count() + 1);
		}
		if (type.equals(EDepositType.READ.getCode())) {
			// 添加用户购买阅读笔记的记录
			Reader reader = new Reader();
			reader.setReader_note_id(noteId);
			reader.setReader_user_id(userId);
			reader.setReader_money(money);
			reader.setReader_create_time(new Date());
			this.readerDao.save(reader);
			// 添加笔记阅读的数量
			note.setNote_amount(note.getNote_amount() + 1);
			this.noteDao.update(note);
		}
	}

	/**
	 * 获取充值跳转的URL
	 * 
	 * @param noteId
	 * @param optType 1 写笔记 2 阅读笔记
	 * @param payType
	 * @return
	 * @throws DocumentException
	 */
	@Override
	public String getPayUrl(Integer userId, Integer noteId, Integer optType, Integer payType) throws Exception {
		Note note = this.noteDao.getById(noteId);
		if (note == null) {
			return "/weixin/note/my";
		}

		// 检查用户关于这个笔记支付的充值订单，状态为 1 未处理的状态订单，返回的是一个LIST
		List<Deposit> depositList = depositDao.findDepositList(userId, noteId, optType);

		// 已经有支付记录的
		if (null != depositList) {
			for (Deposit deposit : depositList) {
				// 锁住当前用户的充值记录
				deposit = depositDao.getById(deposit.getDeposit_id());

				// 充值成功的，直接返回笔记地址
				if (deposit.getDeposit_status() == EDepositStatus.OK.getCode()) {
					return "/weixin/note/reader/read/" + noteId;
				}

				// 查询汇付宝的充值状态
				Boolean payRes = heepayHelper.checkPayRes(deposit.getDeposit_bill_id());

				// 接口返回支付成功
				if (payRes) {
					if (1 == optType) {
						// 写笔记
						proccessNotePay(deposit, note);
						return "/weixin/note/reader/view/" + noteId;
					} else {
						// 阅读笔记（阅读费）
						this.proccessReadPay(deposit);
						return "/weixin/note/reader/read/" + noteId;
					}
				}
			}
		}

		// 生成订单ID
		String orderId = heepayHelper.createOrderNo(noteId, userId);

		// 创建一个充值订单
		Deposit deposit = new Deposit();
		deposit.setDeposit_bill_id(orderId);
		deposit.setDeposit_user_id(userId);
		deposit.setDeposit_note_id(noteId);
		deposit.setDeposit_money(note.getNote_open_money());
		deposit.setDeposit_status(EDepositStatus.INIT.getCode());
		deposit.setDeposit_action(optType);
		deposit.setDeposit_create_time(new Date());
		// 写入充值记录表
		this.depositDao.save(deposit);

		// 创建一个请求的对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		GatewayEntity gatewayEntity = new GatewayEntity();
		heepayHelper.fullGateway(gatewayEntity);
		gatewayEntity.setAgent_bill_id(orderId);
		gatewayEntity.setPay_amt(String.valueOf(note.getNote_open_money()));
		if (payType == 1) {
			// H5支付
			gatewayEntity.setPay_type("20");
		} else {
			// WX支付
			gatewayEntity.setPay_type("30");
			gatewayEntity.setIs_frame("1");
		}
		gatewayEntity.setUser_ip(request.getRemoteAddr().replace(".", "_"));

		// 生成签名
		String sign = this.heepayHelper.SignMd5(gatewayEntity);

		// 生成请求的url
		return this.heepayHelper.GatewaySubmitUrl(sign, gatewayEntity);
	}

	/**
	 * 查询后台充值列表
	 * 
	 * @param pager
	 * @param nickName
	 * @param billNo
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findAdminDepositList(Pager pager, String userId, String nickName, String billNo, String startDate, String endDate, Integer status) {
		String where = " 1=1 ";
		if (userId != null && !Objects.equals(userId, ""))
			where += " AND u.user_id = '" + userId + "' ";
		if (nickName != null && !Objects.equals(nickName, ""))
			where += " AND u.user_nickname LIKE '%" + nickName + "%' ";
		if (billNo != null && !Objects.equals(billNo, ""))
			where += " AND d.deposit_bill_id = '" + billNo + "' ";
		if (startDate != null && !Objects.equals(startDate, ""))
			where += " AND d.deposit_create_time >= '" + startDate + " 00:00:00'";
		if (endDate != null && !Objects.equals(endDate, ""))
			where += " AND d.deposit_create_time <= '" + endDate + " 23:59:59'";
		if (status != null && status != 0)
			where += " AND d.deposit_status = '" + status + "' ";

		Integer count = this.depositDao.findAdminListCount(where);
		pager.setTotalCount(count);
		List<Map<String, Object>> depositList = this.depositDao.findAdminListByLeftJoin(pager, where, "d.deposit_id DESC");
		return depositList;
	}
		/**
		 *  userId=0:统计所有用户
		 *  userId!=0:统计每个用户
		 */
		@Override
		public Double findcountByCondition(String select,String tableName,String where) throws Exception { 
			return this.depositDao.findSumMoneyBy(tableName,select,where);
		}

}
