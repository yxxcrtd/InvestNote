package com.caimao.weixin.note.util;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.domain.HourK;
import com.caimao.weixin.note.domain.Ticker;

@Service
public class StockUtil {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(StockUtil.class);

	/** 股票接口地址 */
	private static String ADDRESS_STOCK = "http://hq.sinajs.cn/list=";

	/** 60分钟的K线 */
	private static String ADDRESS_STOCK_FIVE_K = "http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?scale=60&ma=no&datalen=1&symbol=";

	@Autowired
	// private RedisUtil redisUtil;

	public static void main(String[] args) throws Exception {
		// System.out.println(getTicket("000001").getName());

		System.out.println(getTicketByHourK("002225").getDay());
	}

	/**
	 * 根据股票代码获取股票的60分钟的K线
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static HourK getTicketByHourK(String code) throws Exception {
		if (6 != code.length()) {
			return null;
		}

		String marketType = getMarketType(code);
		if (null == marketType) {
			return null;
		}

		String stockInterfaceAddress = String.format(ADDRESS_STOCK_FIVE_K + marketType + code);
		String body = Jsoup.connect(stockInterfaceAddress).ignoreContentType(true).execute().body();
		String tickerString = body.substring(1, body.length() - 1);
		LOGGER.info("（60分钟的K线）股票数据：{}", tickerString);
		if ("".equals(tickerString)) {
			return null;
		}

		// 添加到 60分钟K线 对象中
		JSONObject jsonObject = JSONObject.parseObject(tickerString);
		HourK hourK = new HourK();
		hourK.setCode(code);
		hourK.setDay(String.valueOf(jsonObject.get("day")));
		hourK.setOpen(String.valueOf(jsonObject.get("open")));
		hourK.setHigh(String.valueOf(jsonObject.get("high")));
		hourK.setLow(String.valueOf(jsonObject.get("low")));
		hourK.setClose(String.valueOf(jsonObject.get("close")));
		hourK.setIncrease((Float.parseFloat(hourK.getOpen()) - Float.parseFloat(hourK.getClose())) / Float.parseFloat(hourK.getClose()) * 100);
		return hourK;
	}

	/**
	 * 根据股票代码获取股票的实时数据
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static Ticker getTicket(String code) throws Exception {
		if (6 != code.length()) {
			return null;
		}

		String marketType = getMarketType(code);
		if (null == marketType) {
			return null;
		}

		// TODO 有乱码
//		String body = null;
//		Object redisVal = this.redisUtil.get(code);
//		if (redisVal == null) {
//			String stockInterfaceAddress = String.format(this.ADDRESS_STOCK + marketType + code);
//			body = Jsoup.connect(stockInterfaceAddress).ignoreContentType(true).execute().body();
//			LOGGER.info("抓取结果：{}", body);
//			this.redisUtil.set(code, body, 60);
//		} else {
//			body = redisVal.toString();
//		}
		String stockInterfaceAddress = String.format(ADDRESS_STOCK + marketType + code);
		String body = Jsoup.connect(stockInterfaceAddress).ignoreContentType(true).execute().body();

		String tickerString = body.substring(body.indexOf("\"") + 1, body.lastIndexOf("\""));

		if ("".equals(tickerString)) {
			LOGGER.info("（实时）股票数据（{}）：没有获取到数据！", code);
			return null;
		} else {
			LOGGER.info("（实时）股票数据（{}）：{}", code, tickerString);
		}

		String[] tickerArray = tickerString.split(",");
		if (10 > tickerArray.length) {
			return null;
		}

		// 添加到 Ticker 对象中
		Ticker ticker = new Ticker();
		ticker.setName(tickerArray[0]);
		ticker.setCode(code);
		ticker.setOpenPrice(tickerArray[1]);
		ticker.setClosePrice(tickerArray[2]);
		ticker.setCurrentPrice(Float.parseFloat(tickerArray[3]));
		ticker.setHighPrice(tickerArray[4]);
		ticker.setLowPrice(tickerArray[5]);
		ticker.setTotalNum(tickerArray[8]);
		ticker.setTotalMoney(tickerArray[9]);
		ticker.setIncrease((ticker.getCurrentPrice() - Float.parseFloat(ticker.getClosePrice())) / Float.parseFloat(ticker.getClosePrice()) * 100);
		return ticker;
	}

	/**
	 * 返回股票的市场代码，sh：上海证券交易所（A股）；sz：深圳证券交易所（B股）；hk：香港证券交易所（H股）；hs：恒生指数
	 * 
	 * @param stockCode
	 * @return
	 */
	private static String getMarketType(String stockCode) {
		if ("000001".equals(stockCode)) {
			return "sz";
		}
		String marketType = null;
		String firstNumber = stockCode.substring(0, 1);
		switch (firstNumber) {
			case "0":
			case "1":
			case "2":
			case "3":
				marketType = "sz";
				break;
			case "5":
			case "6":
				marketType = "sh";
				break;
			default:
				return null;
		}
		return marketType;
	}

}
