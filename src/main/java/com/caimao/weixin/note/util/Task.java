package com.caimao.weixin.note.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.service.NoteService;
import com.caimao.weixin.note.service.ReaderService;

/**
 * 定时任务（ 更改股票状态： 1，进行中（09:20）；2，进行中（12:55）；3，是否达标；4，已结束；5，无效）
 */
@Component
// @EnableScheduling
public class Task {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

	private @Autowired NoteService noteService;

	private @Autowired ReaderService readerService;

	private @Autowired NoteDao noteDao;

	private @Autowired RedisUtil redisUtil;

	private @Autowired WXUtil wxUtil;

	private @Value("${holiday}") String holiday;

	private @Value("${push_stat_21_00}") String push_stat_21_00;

	private @Value("${wx.domain}") String domain;

	/** 推送消息中的时间 */
	private String datetime = "";

	/** 推送消息中的消息主体 */
	private String message = "";

	public static void main(String[] args) {
		System.out.println(DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", new Date()));
		System.out.println(DateUtil.convertDateToString("HH:mm", new Date()));
	}

	/**
	 * Redis锁，如果没有锁返回 true，有锁返回false
	 * 
	 * @param key
	 * @return
	 */
	private Boolean redisLock(String key) {
		return redisUtil.setnx(key, "true", 60); // 不存在的时候才设置
	}

	/**
	 * 1，将09:20的股票状态设置为：进行中
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 20,25 9 ? * MON-FRI")
	public void changeNoteStartStatus1() throws Exception {
		LOGGER.info("【定时任务 - 1，进行中（09点20/25分）】 开始执行 ......");

		if (!redisLock("noteStartStatus1")) {
			LOGGER.error("【定时任务 - 1，进行中（09点20/25分）】 遇到 Redis 锁，任务停止!\n");
			return;
		}

		// 1，如果当天是不交易的工作日，则不用设置
		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date()) + " " + DateUtil.START_TIME_09_21_00;
		if (DateUtil.isInHoliday(currentDate, holiday)) {
			LOGGER.info("【定时任务 - 1，进行中（09点20/25分）】 当天是不交易的工作日！\n");
			return;
		}

		try {
			// 2，查找所有待开盘的并且起算时间为9点20的笔记
			List<Note> noteList = noteDao.findNoteListByFlagAndDatetime(0, currentDate);
			if (null == noteList) {
				LOGGER.info("【定时任务 - 1，进行中（09点20/25分）】 没有验证数据！\n");
				return;
			}
			for (Note note : noteList) {
				try {
					if (note.getNote_pay_status() != 2) {
						// 3，先将未支付的笔记进行物理删除
						noteService.delete(note.getNote_id());
					} else {
						// 4，将股票状态设置为：进行中
						note.setNote_status(1);
						noteService.update(note);
					}
				} catch (Exception e) {
					LOGGER.error("【定时任务 - 1，进行中（09点20/25分）】 出错：{}", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("【定时任务 - 1，进行中（09点20/25分）】 定时任务异常：{}", e);
		}

		LOGGER.info("【定时任务 - 1，进行中（09点20/25分）】 执行完毕！\n");
	}

	/**
	 * 2，将12:55的股票状态设置为：进行中
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 55,58 12 ? * MON-FRI")
	public void changeNoteStartStatus2() throws Exception {
		LOGGER.info("【定时任务 - 2，进行中（12点55/58分）】 开始执行 ......");

		if (!redisLock("noteStartStatus2")) {
			LOGGER.error("【定时任务 - 2，进行中（12点55/58分）】 遇到 Redis 锁，任务停止!\n");
			return;
		}

		// 1，如果当天是不交易的工作日，则不用设置
		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date()) + " " + DateUtil.START_TIME_12_56_00;
		if (DateUtil.isInHoliday(currentDate, holiday)) {
			LOGGER.info("【定时任务 - 2，进行中（12点55/58分）】 当天是不交易的工作日！\n");
			return;
		}

		try {
			// 2，查找所有待开盘的并且起算时间为12点55的笔记
			List<Note> noteList = noteDao.findNoteListByFlagAndDatetime(0, currentDate);
			if (null == noteList) {
				LOGGER.info("【定时任务 - 2，进行中（12点55/58分）】 没有验证数据！\n");
				return;
			}
			for (Note note : noteList) {
				try {
					if (1 == note.getNote_pay_status()) {
						// 3，将未支付的笔记进行物理删除
						noteService.delete(note.getNote_id());
					} else {
						// 4，将股票状态设置为：进行中
						note.setNote_status(1);
						noteService.update(note);
					}
				} catch (Exception e) {
					LOGGER.error("【定时任务 - 2，进行中（12点55/58分）】 出错：{}", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("【定时任务 - 2，进行中（12点55/58分）】 定时任务异常：{}", e);
		}

		LOGGER.info("【定时任务 - 2，进行中（12点55/58分）】 执行完毕！\n");
	}

	/**
	 * 3，将股票状态设置为：是否达标
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0/10 9-15 ? * MON-FRI")
	public void changeNoteTargetStatus() throws Exception {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		if ((9 == hour && (0 == minute || 10 == minute || 20 == minute)) || (13 == hour && 0 == minute) || (15 == hour && 10 < minute)) {
			LOGGER.info("9点、9点10分、9点20分、13点、15点10分以后都不执行！【3，是否达标】\n");
			return;
		}
		
		LOGGER.info("【定时任务 - 3，是否达标（每隔10分钟）】 开始执行 ......");

		if (!redisLock("noteTargetStatus")) {
			LOGGER.error("【定时任务 - 3，是否达标（每隔10分钟）】 遇到 Redis 锁，任务停止!\n");
			return;
		}

		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date());
		if (DateUtil.isInHoliday(currentDate, holiday)) {
			LOGGER.info("【定时任务 - 3，是否达标（每隔10分钟）】 当天是不交易的工作日！\n");
			return;
		}

		try {
			// 查询所有进行中的笔记（note_status 状态为 1）
			List<Note> noteList = noteDao.findAllIngNoteList();
			// 如果没有数据，则直接返回，不再验证
			if (null == noteList) {
				LOGGER.info("【定时任务 - 3，是否达标（每隔10分钟）】 没有验证数据！\n");
				return;
			}
			for (Note note : noteList) {
				try {
					noteService.changeNoteTargetStatus(note.getNote_id());
				} catch (Exception e) {
					LOGGER.info("【定时任务 - 3，是否达标（每隔10分钟）】  出错：{}", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("【定时任务 - 3，是否达标（每隔10分钟）】 定时任务异常：{}", e);
		}

		LOGGER.info("【定时任务 - 3，是否达标（每隔10分钟）】 执行完毕！\n");
	}

	/**
	 * 4，将股票状态设置为：已结束
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 10,15 15 ? * MON-FRI")
	public void changeNoteEndStatus() throws Exception {
		LOGGER.info("【定时任务 - 4，已结束（15点10/15分）】 开始执行 ......");

		if (!redisLock("noteEndStatus")) {
			LOGGER.error("【定时任务 - 4，已结束（15点10/15分）】 遇到 Redid 锁，任务停止！\n");
			return;
		}

		// 1，如果当天是不交易的工作日，则不用设置
		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date()) + " " + DateUtil.END_TIME_15_00_01;
		if (DateUtil.isInHoliday(currentDate, holiday)) {
			LOGGER.info("【定时任务 - 4，已结束（15点10/15分）】 当天是不交易的工作日！\n");
			return;
		}

		try {
			// 2，查找所有进行中的并且结束时间为15点整的笔记
			List<Note> noteList = noteDao.findNoteListByFlagAndDatetime(1, currentDate);
			if (null == noteList) {
				LOGGER.info("【定时任务 - 4，已结束（15点10/15分）】 没有验证数据！\n");
				return;
			}

			// 3，将股票状态设置为：已结束
			for (Note note : noteList) {
				try {
					noteService.changeNoteEndStatus(note.getNote_id());
				} catch (Exception e) {
					LOGGER.error("【定时任务 - 4，已结束（15点10/15分）】 执行失败：{}", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("【定时任务 - 4，已结束（15点10/15分）】 出现异常：{}", e);
		}

		LOGGER.info("【定时任务 - 4，已结束（15点10/15分）】 执行完毕！\n");
	}

	/**
	 * 5，将股票状态设置为：无效
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 20 15 ? * MON-FRI")
	public void changeNoteVoidStatus() throws Exception {
		LOGGER.info("【定时任务 - 5，无效（15点20分）】 开始执行 ......");

		if (!redisLock("wx_note_status_void")) {
			LOGGER.error("【定时任务 - 5，无效（15点20分）】 遇到 Redid 锁，任务停止！\n");
			return;
		}

		String currentDate = DateUtil.convertDateToString("yyyy-MM-dd", new Date());

		// 1，如果当天是不交易的工作日，则不用设置
		if (DateUtil.isInHoliday(currentDate, holiday)) {
			LOGGER.info("【定时任务 - 5，无效（15点20分）】 当天是不交易的工作日！\n");
			return;
		}

		try {
			// 2，获取起算日期是当天的（包括09:20和12:55），并且价格为0.00的，状态为进行中的笔记列表
			List<Note> noteList = noteDao.findNoteListWithZeroPriceOfCurrentDate();
			if (null == noteList) {
				LOGGER.info("【定时任务 - 5，无效（15点20分）】 没有验证数据！\n");
				return;
			}

			// 3，将股票状态设置为：无效
			for (Note note : noteList) {
				try {
					noteService.changeNoteVoidStatus(note.getNote_id());
				} catch (Exception e) {
					LOGGER.error("【定时任务 - 5，无效（15点20分）】 执行失败：{}", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("【定时任务 - 5，无效（15点20分）】 出现异常：{}", e);
		}

		LOGGER.info("【定时任务 - 5，无效（15点20分）】 执行完毕！\n");
	}

	/**
	 * 每天21:00推送消息
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 21 * * ?")
	public void pushStat() throws Exception {
		LOGGER.info("【定时推送 - 每天21:00】 开始执行 ......");

		if (!redisLock("wx_note_push_stat_21_00")) {
			LOGGER.error("【定时推送 - 每天21:00】 遇到 Redis 锁，任务停止!\n");
			return;
		}

		try {
			// 查询前一天21点到今天21点之间的所有阅读数据（进行中）
			List<Reader> readers = readerService.findReadersOfNote();
			for (Reader reader : readers) {
				LOGGER.info("【定时推送 - 每天21:00】 给（{}，{}，{}）推送了消息（今日笔记({})的阅读数是：{}）！\n", reader.getUser().getUser_id(), reader.getUser().getUser_nickname(), reader.getUser().getUser_open_id(), reader.getNote().getNote_stock_name(), reader.getCount());
				datetime = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", reader.getNote().getNote_create_time());
				message = "您的笔记（" + reader.getNote().getNote_stock_name() + "）今日" + reader.getCount() + "人阅读";
				wxUtil.sendTemplateMsg("笔记进行中...", datetime, message, reader.getUser().getUser_open_id(), push_stat_21_00, domain + "/weixin/note/reader/read/" + reader.getReader_note_id());
			}
		} catch (Exception e) {
			LOGGER.error("【定时推送 - 每天21:00】 出现异常：{}", e);
		}

		LOGGER.info("【定时推送 - 每天21:00】 执行完毕！\n");
	}

}
