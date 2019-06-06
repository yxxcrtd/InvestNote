package com.caimao.weixin.note.controller.admin;

import com.caimao.weixin.note.service.*;
import com.caimao.weixin.note.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseController
 */
public class AdminController {

	/** LOGGER */
	protected static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	protected RedisUtil redisUtil;

	@Autowired
	protected NoteService noteService;

	@Autowired
	protected StatService statService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected DepositService depositService;

	@Autowired
	protected ReaderService readerService;

	@Autowired
	protected BankService bankService;

	@Autowired
	protected WithdrawService withdrawService;

	@Autowired
	protected CapitalService capitalService;

	/**
	 * 检查是否登陆
	 * @return
	 * @throws Exception
     */
	public boolean checkLogin() throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		Object adminUserName = request.getSession().getAttribute("adminUserName");
		if (adminUserName == null) {
			response.sendRedirect("/weixin/note/admin/login");
			return false;
		}
		return true;
	}

}
