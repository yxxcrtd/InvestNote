package com.caimao.weixin.note.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.CapitalDao;
import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.dao.ReaderDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.domain.HourK;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.Ticker;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.enums.ECapitalType;
import com.caimao.weixin.note.service.NoteService;
import com.caimao.weixin.note.util.DateUtil;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.StockUtil;
import com.caimao.weixin.note.util.WXUtil;

@Service
public class NoteServiceImpl extends BaseServiceImpl<Note, Integer> implements NoteService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ReaderDao readerDao;

	@Autowired
	private NoteDao noteDao;

	@Autowired
	private CapitalDao capitalDao;

	private @Autowired WXUtil wxUtil;

	private @Value("${wx.domain}") String domain;

	private @Value("${push_stat_21_00}") String push_stat_21_00;

	/** 推送消息中的时间 */
	private String datetime = "";

	/** 推送消息中的消息主体 */
	private String message = "";

	/**
	 * 保存，并返回刚插入的主键ID
	 *
	 * @param note
	 * @return
	 * @throws Exception
	 */
	public Long saveReturnId(Note note) throws Exception {
		// 保存
		Long newId = ((NoteDao) baseDao).saveReturnId(note);

		// 返回新的主键ID
		return newId;
	}

	/**
	 * 3，将股票状态设置为：是否达标
	 * 
	 * 涨停：【最低价 = 昨收 x (1 + 10%)】 或 【昨收 x 1.09 <= 最高价 && 最高价 = 最低价】
	 * 停牌：当前价为0.00
	 * 
	 * 1，设置笔记达标/结束
	 * 2，返还红包费
	 * 2.1，记录返还红包的流水
	 * 3，给作者增加获得的总资金
	 * 3.1，记录作者的总资金流水
	 * // 4，减去作者的服务费
	 * // 4.1，记录平台服务费流水
	 * 5，给作者发推送消息
	 * 5.1，给读者发推送消息
	 * 6.1，计算用户笔记数
	 * 6.2，计算用户个人信息的成功率
	 * 6.3，计算用户个人信息的平均收益（每次成功涨幅的平均数）
	 * 
	 * @param noteId
	 */
	public void changeNoteTargetStatus(Integer noteId) throws Exception {
		Note note = noteDao.getById(noteId);

		Ticker ticker = null;
		HourK hourK = null;
		if (DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_start_time()).contains(DateUtil.START_TIME_09_20_00)) {
			ticker = StockUtil.getTicket(note.getNote_stock_code());
			if (null == ticker) {
				return;
			}
		} else {
			hourK = StockUtil.getTicketByHourK(note.getNote_stock_code());
			if (null == hourK) {
				return;
			}
		}

		// 起算价只更新一次
		if (0.00 == note.getNote_stock_open_price()) {

			// 更新起算时间是9点20的笔记的开盘价
			if (null != ticker) {
				LOGGER.info("【更新起算时间是9点20的笔记的开盘价：{}（{}）{}】", note.getNote_stock_name(), note.getNote_stock_code(), ticker.getOpenPrice());

				// 判断涨停
				if (Double.valueOf(ticker.getClosePrice()) * 1.09 <= Double.valueOf(ticker.getHighPrice()) && Objects.equals(ticker.getHighPrice(), ticker.getLowPrice())) {
					return;
				}
				// 判断停牌
				if (0.00 == ticker.getCurrentPrice()) {
					return;
				}
				note.setNote_stock_open_price(Double.valueOf(ticker.getOpenPrice()));
			}

			// 更新起算时间是12点55的笔记的开盘价
			if (null != hourK) {
				LOGGER.info("【更新起算时间是12点55的笔记的开盘价：{}（{}）{}】", note.getNote_stock_name(), note.getNote_stock_code(), hourK.getOpen());

				// 判断涨停
				if (Double.valueOf(hourK.getClose()) * 1.09 <= Double.valueOf(hourK.getHigh()) && Objects.equals(hourK.getHigh(), hourK.getLow())) {
					return;
				}

				// 判断停牌
				String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date());
				if (!hourK.getDay().contains(currentDate)) {
					return;
				}
				note.setNote_stock_open_price(Double.valueOf(hourK.getOpen())); // 13:10 执行
			}

			noteDao.update(note);
		}

		// 如果是今天的笔记，今天不检测是否达标
		if (DateUtil.convertDateToString("yyyy-MM-dd", new Date()).equals(DateUtil.convertDateToString("yyyy-MM-dd", note.getNote_start_time()))) {
			return;
		}

		// 如果从接口中获取到的当前股票最高价 > Note中存储的最高价，则更新Note中的最高价
		if (null != ticker) {
			if (new BigDecimal(ticker.getHighPrice()).compareTo(new BigDecimal(note.getNote_stock_high_price())) >= 1) {
				note.setNote_stock_high_price(Float.parseFloat(ticker.getHighPrice()));
				noteDao.update(note);
				LOGGER.info("【（9点20的笔记）更新当前笔记（{}({})）的最高价！】 当前股票的最高价：{}，Note中存储的最高价：{}", note.getNote_stock_name(), note.getNote_stock_code(), ticker.getHighPrice(), note.getNote_stock_high_price());
			}
		}
		if (null != hourK) {
			if (new BigDecimal(hourK.getHigh()).compareTo(new BigDecimal(note.getNote_stock_high_price())) >= 1) {
				note.setNote_stock_high_price(Float.parseFloat(hourK.getHigh()));
				noteDao.update(note);
				LOGGER.info("【（12点55的笔记）更新当前笔记（{}({})）的最高价！】 当前股票的最高价：{}，Note中存储的最高价：{}", note.getNote_stock_name(), note.getNote_stock_code(), hourK.getHigh(), note.getNote_stock_high_price());
			}
		}

		// 计算：目标价 = （（开盘价 * 涨幅）/ 100 + 开盘价）
		double targetPrice = note.getNote_stock_open_price() * note.getNote_increase() / 100 + note.getNote_stock_open_price();

		// 用户
		User user = userDao.getById(note.getNote_user_id());

		// 如果最高价达到目标价，则该笔记达标了
		if (null != ticker) {
			LOGGER.info("（9点20的笔记）当前股票：{}({},{})，最高价是：{}；目标价是：{}", note.getNote_stock_name(), note.getNote_stock_code(), ticker.getCurrentPrice(), ticker.getHighPrice(), targetPrice);
		}
		if (null != hourK) {
			LOGGER.info("（12点55的笔记）当前股票：{}({},{})，最高价是：{}；目标价是：{}", note.getNote_stock_name(), note.getNote_stock_code(), hourK.getOpen(), hourK.getHigh(), targetPrice);
		}

		String highPrice = "";
		if (null != ticker) {
			highPrice = ticker.getHighPrice();
			LOGGER.info("（9点20的笔记）最高价：{}\n", highPrice);
		}
		if (null != hourK) {
			highPrice = hourK.getHigh();
			LOGGER.info("【（12点55的笔记）最高价：{}】\n", highPrice);
		}

		if (new BigDecimal(highPrice).compareTo(new BigDecimal(String.valueOf(targetPrice))) >= 0) {
			if (null != ticker) {
				LOGGER.info("【（9点20的笔记）当前股票：{}（{}，{}） 达标了 ！！！】", note.getNote_stock_name(), note.getNote_stock_code(), ticker.getCurrentPrice());
			}
			if (null != hourK) {
				LOGGER.info("【（12点55的笔记）当前股票：{}（{}，{}） 达标了 ！！！】", note.getNote_stock_name(), note.getNote_stock_code(), hourK.getOpen());
			}

			// 1，设置笔记达标
			note.setNote_status(2);
			note.setNote_target_status(1);
			noteDao.update(note);

			// 阅读费大于0的才给用户清算
			if (0 < note.getNote_open_money()) {
				// 2，返还红包费
				user.setUser_available_money(user.getUser_available_money() + note.getNote_packet_money()); // 将用户之前的可用资金 + 红包费
				user.setUser_freeze_money(user.getUser_freeze_money() - note.getNote_packet_money()); // 将之前冻结的红包返回到可用资金

				// 2.1，记录返还红包的流水
				Capital capital1 = new Capital();
				capital1.setCapital_user_id(note.getNote_user_id());
				capital1.setCapital_type(ECapitalType.RETURNREDPACKET.getCode()); // 退回红包费
				capital1.setCapital_mount(note.getNote_packet_money()); // 资金变动金额（笔记的红包费）
				capital1.setCapital_available(user.getUser_available_money()); // 资金变动后可用资金（加上用户返回红包费之后的可用资金）
				capital1.setCapital_freeze(user.getUser_freeze_money()); // 还是当前用户的总冻结资金
				capital1.setCapital_create_time(new Date());
				capitalDao.save(capital1);

				// 参与人数大于0的时候，才给作者结算总资金
				if (0 < note.getNote_amount()) {
					// 3，给作者增加获得的总资金（ = 阅读费 * 阅读人数）
					double totalIncome = note.getNote_open_money() * note.getNote_amount();
					user.setUser_available_money(user.getUser_available_money() + totalIncome); // 将用户之前的可用资金 + （阅读费 * 阅读人数）

					// 3.1，记录作者的总资金流水
					Capital capital2 = new Capital();
					capital2.setCapital_user_id(note.getNote_user_id());
					capital2.setCapital_type(ECapitalType.READINCOME.getCode()); // 阅读费收益
					capital2.setCapital_mount(totalIncome); // 资金变动金额（阅读费收益金额）
					capital2.setCapital_available(user.getUser_available_money()); // 资金变动后可用资金（加上阅读费收益之后的可用资金 = 当前用户的可用资金）
					capital2.setCapital_freeze(user.getUser_freeze_money()); // 还是当前用户的总冻结资金
					capital2.setCapital_create_time(new Date());
					capitalDao.save(capital2);

					// 4，减去作者的服务费（5%作为服务费）
					// user.setUser_available_money(user.getUser_available_money() - totalIncome * 0.05);

					// 4.1，记录平台服务费流水
					// Capital capital3 = new Capital();
					// capital3.setCapital_user_id(note.getNote_user_id());
					// capital3.setCapital_type(ECapitalType.SERVICEFEE.getCode()); // 技术服务费
					// capital3.setCapital_mount(-(totalIncome * 0.05)); // 资金变动金额（5%的技术服务费）
					// capital3.setCapital_available(user.getUser_available_money()); // 资金变动后可用资金（减去5%的技术服务费之后的可用资金 = 当前用户的可用资金）
					// capital3.setCapital_freeze(user.getUser_freeze_money()); // 还是当前用户的总冻结资金
					// capital3.setCapital_create_time(new Date());
					// capitalDao.save(capital3);
				}
			}

			// 5，给作者发推送消息
			LOGGER.info("【定时推送 - 每隔10分钟】 给（{}, {}, {}）推送了消息【您的笔记（{}）已达目标，总阅读费" + note.getNote_packet_money() + "元】！\n", user.getUser_id(), user.getUser_nickname(), user.getUser_open_id(), note.getNote_stock_name());
			datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
			message = "您的笔记（" + note.getNote_stock_name() + "）已达目标，总阅读费" + note.getNote_packet_money() + "元";
			wxUtil.sendTemplateMsg("笔记成功", datetime, message, user.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());

			// 5.1，给读者发推送消息
			List<Reader> readerList = readerDao.findReaderByNoteId(note.getNote_id());
			if (null != readerList) {
				for (Reader reader : readerList) {
					User readerUser = userDao.getById(reader.getReader_user_id());
					LOGGER.info("【定时推送 - 每隔10分钟】 给（{}, {}, {}）推送了消息【{} 的笔记（{}）已达目标】！\n", readerUser.getUser_id(), readerUser.getUser_nickname(), readerUser.getUser_open_id(), user.getUser_nickname(), note.getNote_stock_name());
					datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
					message = user.getUser_nickname() + " 的笔记（" + note.getNote_stock_name() + "）已达目标";
					wxUtil.sendTemplateMsg("笔记成功", datetime, message, readerUser.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());
				}
			}

			// 6，计算个人成功率、笔记数和平均收益（不管达不达标都要计算）
			calculateSuccessAndYield(note, user);
		}
	}

	/**
	 * 4，将股票状态设置为：已结束
	 * 
	 * 一，未达标（给读者退还阅读费，同时发红包）
	 * 1，设置笔记为：已结束
	 * 2，将作者可用资金 - 红包费
	 * 2.1，记录作者发红包的流水
	 * 3.1，给读者退还阅读费
	 * 3.2，记录退还流水
	 * 4，给读者发红包
	 * 4.1，记录读者获得到的红包流水
	 * 5，给读者发推送消息
	 *
	 * 二，无效（退还阅读费给作者，也无需发红包）
	 * 1，给作者的可用资金 加上阅读费（即是作者创建笔记之后支付的红包费）
	 * 1.1，记录返回阅读费的流水
	 * 1.2，给作者发推送消息
	 * 
	 * 三，公共的
	 * 2，给读者退还阅读费
	 * 2.1，记录退还读者的阅读费流水
	 */
	public void changeNoteEndStatus(Integer noteId) throws Exception {
		Date date = new Date();
		Note note = noteDao.getById(noteId);
		User user = userDao.getById(note.getNote_user_id());

		// 如果是今天的笔记，并且该股票一直涨停或停牌（开盘价为0.00），则将该股票设置为：无效
		if (DateUtil.convertDateToString("yyyy-MM-dd", new Date()).equals(DateUtil.convertDateToString("yyyy-MM-dd", note.getNote_start_time())) && (0.00 == note.getNote_stock_open_price())) {
			note.setNote_target_status(2); // 无效
		} else {
			note.setNote_target_status(0); // 未达标
		}

		// 对无效的笔记进行清算
		if (2 == note.getNote_target_status()) {
			if (null != user) {
				// 只有阅读费大于0的笔记才去清算
				if (0 < note.getNote_packet_money()) {
					// 1，给作者的可用资金加上红包费（就是作者一开始支付的红包费）
					user.setUser_available_money(user.getUser_available_money() + note.getNote_packet_money());
					user.setUser_freeze_money(user.getUser_freeze_money() - note.getNote_packet_money());

					// 1.1，记录返回红包费的流水
					Capital capital = new Capital();
					capital.setCapital_user_id(note.getNote_user_id());
					capital.setCapital_type(ECapitalType.RETURNREDPACKET.getCode()); // 退回红包费
					capital.setCapital_mount(note.getNote_packet_money()); // 就是红包费
					capital.setCapital_available(user.getUser_available_money()); // 还是保存用户之后的可用资金
					capital.setCapital_freeze(user.getUser_freeze_money()); // 还是保存用户之后的冻结资金
					capital.setCapital_create_time(date);
					capitalDao.save(capital);
				}
				userDao.update(user);

				// 1.2，给作者发推送消息
				LOGGER.info("【定时推送 - 15点10/15分】 给（{}, {}）推送了消息【您的笔记（{}）无效，已退回红包费" + note.getNote_packet_money() + "元】！\n", user.getUser_id(), user.getUser_nickname(), note.getNote_stock_name());
				datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
				message = "您的笔记（" + note.getNote_stock_name() + "）无效，已退回红包费" + note.getNote_packet_money() + "元";
				wxUtil.sendTemplateMsg("笔记无效", datetime, message, user.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());
			}
		} else { // 未达标
			// 笔记未达标，先要扣除作者冻结中的红包费
			if (0 < note.getNote_amount()) { // 有人买
				// 2，将作者可用资金 - 红包费（先将作者的红包费扣除，将要给作者均分这个红包费）
				user.setUser_freeze_money(user.getUser_freeze_money() - note.getNote_packet_money());
				userDao.update(user);
			} else { // 没人买
				if (0 < note.getNote_packet_money()) {
					// 2，没人买，返还红包费
					user.setUser_available_money(user.getUser_available_money() + note.getNote_packet_money()); // 将作者之前的可用资金 + 红包费
					user.setUser_freeze_money(user.getUser_freeze_money() - note.getNote_packet_money());
					userDao.update(user);

					// 2.1，记录返还红包的流水
					Capital capital1 = new Capital();
					capital1.setCapital_user_id(note.getNote_user_id());
					capital1.setCapital_type(ECapitalType.RETURNREDPACKET.getCode()); // 退回红包费
					capital1.setCapital_mount(note.getNote_packet_money()); // 资金变动金额（笔记的红包费）
					capital1.setCapital_available(user.getUser_available_money()); // 资金变动后可用资金（加上用户返回红包费之后的可用资金）
					capital1.setCapital_freeze(user.getUser_freeze_money()); // 还是当前用户的总冻结资金
					capital1.setCapital_create_time(date);
					capitalDao.save(capital1);
				}

				// 2.2，给作者发推送消息
				LOGGER.info("【定时推送 - 15点10/15分】 给（{}, {}）推送了消息【您的笔记（{}）失败，已退回红包费" + note.getNote_packet_money() + "元】！\n", user.getUser_id(), user.getUser_nickname(), note.getNote_stock_name());
				datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
				message = "您的笔记（" + note.getNote_stock_name() + "）失败，0人阅读，已退回红包费" + note.getNote_packet_money() + "元";
				wxUtil.sendTemplateMsg("笔记失败", datetime, message, user.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());
			}
		}

		// 1，设置：已结束
		note.setNote_status(2);
		LOGGER.info("当前股票{}({})被设置为：已结束！", note.getNote_stock_name(), note.getNote_stock_code());
		baseDao.update(note);

		// 3，给读者退还阅读费
		List<Reader> readerList = readerDao.findReaderByNoteId(note.getNote_id());
		if (null != readerList && 0 < note.getNote_open_money()) {
			for (Reader reader : readerList) {
				// 3.1，给读者退还阅读费
				User readerUser = userDao.getById(reader.getReader_user_id());
				readerUser.setUser_available_money(readerUser.getUser_available_money() + note.getNote_open_money()); // 阅读费
				userDao.update(readerUser);

				// 3.2，记录退还阅读费的流水
				Capital capital2 = new Capital();
				capital2.setCapital_user_id(readerUser.getUser_id()); // 读者的用户ID
				capital2.setCapital_type(ECapitalType.RETURNREAD.getCode()); // 阅读费退回
				capital2.setCapital_mount(note.getNote_open_money()); // 流水的变动资金就是当前笔记的阅读费
				capital2.setCapital_available(readerUser.getUser_available_money()); // 当前读者的可用资金
				capital2.setCapital_freeze(readerUser.getUser_freeze_money()); // 当前读者的的冻结资金
				capital2.setCapital_create_time(date);
				capitalDao.save(capital2);

				// 只有笔记未达标，才发红包，无效的笔记，无需发红包
				if (0 == note.getNote_target_status() && 0 != note.getNote_packet_money()) {
					// 4，给读者发红包（每个读者获得的红包金额 = 笔记的红包费 / 阅读人数（也即读者总数））
					DecimalFormat df = new DecimalFormat("#.00");
					double packageMoney = Double.valueOf(df.format(note.getNote_packet_money() / readerList.size()));

					readerUser.setUser_available_money(readerUser.getUser_available_money() + packageMoney);
					userDao.update(readerUser);

					// 4.1，记录读者获得到的红包流水
					Capital capital3 = new Capital();
					capital3.setCapital_user_id(readerUser.getUser_id()); // 读者的用户ID
					capital3.setCapital_type(ECapitalType.REDPACKET.getCode()); // 平分的红包费
					capital3.setCapital_mount(packageMoney); // 平分的红包费
					capital3.setCapital_available(readerUser.getUser_available_money()); // 当前读者的可用资金
					capital3.setCapital_freeze(readerUser.getUser_freeze_money()); // 当前读者的的冻结资金
					capital3.setCapital_create_time(date);
					capitalDao.save(capital3);
				}

				// 5，给读者发推送消息
				LOGGER.info("【定时推送 - 15点10/15分】 给（{}, {}）推送了消息【{} 的笔记({})未达目标，已发红包】！\n", readerUser.getUser_id(), readerUser.getUser_nickname(), user.getUser_nickname(), note.getNote_stock_name());
				datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
				message = user.getUser_nickname() + " 的笔记（" + note.getNote_stock_name() + "）未达目标，已发红包";
				wxUtil.sendTemplateMsg("笔记失败", datetime, message, readerUser.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());
			}
		}

		// 计算个人成功率、笔记数和平均收益
		calculateSuccessAndYield(note, user);
	}

	/**
	 * 5，将股票状态设置为：无效
	 * 
	 * 1，给作者的可用资金 加上阅读费（即是作者创建笔记之后支付的红包费）
	 * 1.1，记录返回阅读费的流水
	 * 2，给作者发推送消息
	 * 3，给读者退还阅读费
	 * 3.1，记录退还读者的阅读费流水
	 * 4，给读者发推送消息
	 * 
	 * @throws Exception
	 */
	public void changeNoteVoidStatus(Integer noteId) throws Exception {
		Date date = new Date();
		Note note = noteDao.getById(noteId);
		User user = userDao.getById(note.getNote_user_id());

		if (null != user) {
			// 只有阅读费大于0的笔记才去清算
			if (0 < note.getNote_packet_money()) {
				// 1，给作者的可用资金加上红包费（就是作者一开始支付的红包费）
				user.setUser_available_money(user.getUser_available_money() + note.getNote_packet_money());
				user.setUser_freeze_money(user.getUser_freeze_money() - note.getNote_packet_money());
				userDao.update(user);

				// 1.1，记录返回红包费的流水
				Capital capital = new Capital();
				capital.setCapital_user_id(note.getNote_user_id());
				capital.setCapital_type(ECapitalType.RETURNREDPACKET.getCode()); // 退回红包费
				capital.setCapital_mount(note.getNote_packet_money()); // 就是红包费
				capital.setCapital_available(user.getUser_available_money()); // 还是保存用户之后的可用资金
				capital.setCapital_freeze(user.getUser_freeze_money()); // 还是保存用户之后的冻结资金
				capital.setCapital_create_time(date);
				capitalDao.save(capital);

				// 2，给作者发推送消息
				LOGGER.info("【定时推送 - 15点20分】 给（{}, {}）推送了消息【您的笔记（{}）无效，已退回红包费" + note.getNote_packet_money() + "元】！\n", user.getUser_id(), user.getUser_nickname(), note.getNote_stock_name());
				datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
				message = "您的笔记（" + note.getNote_stock_name() + "）无效，已退回红包费" + note.getNote_packet_money() + "元";
				wxUtil.sendTemplateMsg("笔记无效", datetime, message, user.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());

				// 根据笔记ID获取所有读者列表
				List<Reader> readerList = readerDao.findReaderByNoteId(note.getNote_id());
				// 可能存在没有读者的情况
				if (null != readerList) {
					for (Reader reader : readerList) {
						// 3，给读者退还阅读费
						User readerUser = userDao.getById(reader.getReader_user_id());
						readerUser.setUser_available_money(readerUser.getUser_available_money() + note.getNote_packet_money()); // 阅读费
						userDao.update(readerUser);
						LOGGER.info("【给读者（{}，{}）退还阅读费，阅读费：{}；退还后的可用：{}】", readerUser.getUser_id(), readerUser.getUser_nickname(), note.getNote_packet_money(), readerUser.getUser_available_money());

						// 3.1，记录退还读者的阅读费流水
						Capital capital1 = new Capital();
						capital1.setCapital_user_id(readerUser.getUser_id()); // 读者的用户ID
						capital1.setCapital_type(ECapitalType.RETURNREAD.getCode()); // 阅读费退回
						capital1.setCapital_mount(note.getNote_open_money()); // 流水的变动资金就是当前笔记的阅读费
						capital1.setCapital_available(readerUser.getUser_available_money()); // 当前读者的可用资金
						capital1.setCapital_freeze(readerUser.getUser_freeze_money()); // 当前读者的的冻结资金
						capital1.setCapital_create_time(date);
						capitalDao.save(capital1);
						LOGGER.info("【记录读者（{}，{}）退还阅读费的流水，流水类型：{}；变动资金：{}】", readerUser.getUser_id(), readerUser.getUser_nickname(), ECapitalType.RETURNREAD.getValue(), capital1.getCapital_mount());

						// 4，给读者发推送消息
						LOGGER.info("【定时推送 - 15点20分】 给（{}, {}）推送了消息【{} 的笔记({})无效，已退回阅读费" + note.getNote_packet_money() + "元】！\n", readerUser.getUser_id(), readerUser.getUser_nickname(), user.getUser_nickname(), note.getNote_stock_name());
						datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", note.getNote_create_time());
						message = user.getUser_nickname() + " 的笔记（" + note.getNote_stock_name() + "）无效，已退回阅读费" + note.getNote_packet_money() + "元";
						wxUtil.sendTemplateMsg("笔记无效", datetime, message, readerUser.getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + note.getNote_id());
					}
				}
			}
		}

		note.setNote_status(2);
		note.setNote_target_status(2);
		baseDao.update(note);
	}

	/**
	 * 计算个人成功率、笔记数和平均收益
	 * 
	 * @param note 笔记对象
	 * @param user 用户对象
	 */
	private void calculateSuccessAndYield(Note note, User user) {
		// 6.1，计算用户笔记数（条件：note_status = 2 AND note_target_status != 2）
		int noteCount = noteDao.findNoteCount(note.getNote_user_id());
		LOGGER.info("当前用户：（{}）的‘有效’笔记数是：{}", note.getNote_user_id(), noteCount);
		user.setUser_note_count(noteCount);

		// 6.2，计算用户个人信息的成功率（计算用户个人信息的成功数（每次 Count 笔记表中当前用户的成功笔记数））
		List<Note> successNoteList = ((NoteDao) baseDao).findSuccessNoteList(note.getNote_user_id());
		Integer successSize = null == successNoteList ? 0 : successNoteList.size();
		BigDecimal successNum = new BigDecimal(successSize);
		BigDecimal countNum = new BigDecimal(user.getUser_note_count());
		user.setUser_success(successNum.divide(countNum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).floatValue());

		// 6.3，计算用户个人信息的平均收益（每次成功涨幅的平均数）
		Float yield = noteDao.findUserYield(note.getNote_user_id()); // 达标的笔记
		user.setUser_yield(null == yield ? 0 : yield);
		userDao.update(user);
	}

	@Override
	public Integer findMyFocusNotesCount(Integer userId) {
		return ((NoteDao) baseDao).findMyFocusNotesCount(userId);
	}

	@Override
	public List<Note> findMyFocusNotesWithPage(Integer userId, Pager pager) {
		return ((NoteDao) baseDao).findMyFocusNotesWithPage(userId, pager);
	}

}
