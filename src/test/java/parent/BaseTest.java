package parent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.service.AdminUserService;
import com.caimao.weixin.note.service.NoteService;
import com.caimao.weixin.note.service.WithdrawService;
import com.caimao.weixin.note.util.RedisUtil;
import com.caimao.weixin.note.util.SmsUtil;
import com.caimao.weixin.note.util.StockUtil;
import com.caimao.weixin.note.util.Task;
import com.caimao.weixin.note.util.heepay.Common.HeepayHelper;

/**
 * 测试类
 * Created by Administrator on 2016/3/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/applicationContext.xml", "classpath*:/mvc-servlet.xml" })
@WebAppConfiguration(value = "src/main/webapp")

@SuppressWarnings("unused")
public class BaseTest {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private Task task;

	@Autowired
    private SmsUtil smsUtil;

    @Autowired
    private StockUtil stockUtil;

    @Autowired
    private AdminUserService adminUserService;

	@Autowired
	private HeepayHelper HeepayHelper;

    @Test
    public void ccTest() throws Exception {
		String billNo = "20160503173927N1686U632T26";
		Boolean res = this.HeepayHelper.checkPayRes(billNo);
		System.out.println(res);

//        Withdraw withdraw = new Withdraw();
//        withdraw.setWithdraw_user_id(6);
//        withdraw.setWithdraw_money(300.0);
//        withdraw.setWithdraw_bank_id(2);
//        withdraw.setWithdraw_status(EWithdrawStatus.INHAND.getCode());
//        withdrawService.recordWithdraw(withdraw);

        //this.withdrawService.doW();
        //this.withdrawService.findById(35);


//        Pattern pat = Pattern.compile(regex);
//        Matcher matcher = pat.matcher(str);
//        while (matcher.find()) {
//            String temp = str.substring(matcher.start(),matcher.end());
//            str = str.replace(temp, "");
//        }
//        System.out.println(System.currentTimeMillis());
//        String regex = "[?&]{1}code=[0-9a-zA-Z]{32}";
//        String str = "http://gupiao.caimao.com/weixin/note/my?code=0315766344c9e21b6c5a7e80bb9d4cbt&state=snsapi_base";
//        //String str = "http://gupiao.caimao.com/weixin/note/my";
//        str = str.replaceAll(regex, "");
//        regex = "[?&]{1}state=[a-zA-Z_]{11,15}";
//        str = str.replaceAll(regex, "");
//        System.out.println(str);
//
//        System.out.println(System.currentTimeMillis());
//
//        Ticker ticker = this.stockUtil.getTicket("600001");
//        System.out.println(ToStringBuilder.reflectionToString(ticker));

//        this.redisUtil.set("test", "123", 0);
//        String res = this.redisUtil.get("test");
//        System.out.println(res);
//        this.redisUtil.del("test");

//        System.out.println(0/100);

//        System.out.println(this.redisUtil.setnx("key", "lock", 10));
//        System.out.println(this.redisUtil.setnx("key", "lock", 10));

//        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><ret_code>E100</ret_code><ret_msg>接口没有开通</ret_msg><sign>c67e84cb413458159fe5fca942c13902</sign></root>";
//        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder=factory.newDocumentBuilder();
//        Document doc = builder.parse(xml);
//        String resCode = doc.getElementsByTagName("ret_code").item(0).getFirstChild().getNodeValue();

//        Document document = null;
//        try {
//            document = DocumentHelper.parseText(xml);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//            throw e;
//        }
//        Element rootElement = document.getRootElement();
//        String retCode = rootElement.elementTextTrim("ret_code");
//
//        System.out.println(retCode);


//        task.changeNoteTargetStatus();

//        Boolean res = this.smsUtil.sendSms("18612839215", "投资笔记短信验证码：12345，请在五分钟内使用。");
//        System.out.println(res);


//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//        String time = df.format(new Date());
//        Integer rand = (int) (Math.random()*100000);
//        String tt = time.substring(2) + rand.toString();
//        System.out.println(tt);
//        System.out.println(Integer.valueOf(tt));

//        System.out.println("原");
//        String str = "abcdfef";
//        System.out.println(str);
//        System.out.println(Md5Tools.MD5(str));
//
//        System.out.println("GB2312");
//        str = new String(str.getBytes(), "GB2312");
//        System.out.println(str);
//        System.out.println(Md5Tools.MD5(str));
//
//        System.out.println("UTF-8");
//        str = new String(str.getBytes(), "UTF-8");
//        System.out.println(str);
//        System.out.println(Md5Tools.MD5(str));

//        String url = "http://gupiao.caimao.com/weixin/note/withdraw/asyncNotice?ret_msg=批付成功&ret_code=000&agent_id=123&hy_bill_no=123&status=1&batch_no=123&batch_amt=1&batch_num=1&detail_data=12成功3&ext_param1=123&sign=123";
//        url = new String(url.getBytes(), "GB2312");
//        System.out.println(url);
//        String res = HttpHelper.doGet(url);
//        System.out.println(res);

//
		System.out.println(this.adminUserService.createPwd("touzibiji0401"));
    }
}
