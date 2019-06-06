package com.caimao.weixin.note.controller.admin;

import com.caimao.weixin.note.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 后台用户登陆退出控制器
 */
@Controller
@RequestMapping(value = "/weixin/note/admin")
public class AdminLoginController extends AdminController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 后台用户登陆页面
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("/admin/login");
        return mav;
    }

    /**
     * 后台用户登陆操作
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Boolean loginRes = this.adminUserService.login(username, password);
        if (loginRes) {
            request.getSession().setAttribute("adminUserName", username);
            response.sendRedirect("/weixin/note/admin/welcome");
            return null;
        }
        ModelAndView mav = new ModelAndView("/admin/login");
        mav.addObject("errorMsg", "账户或密码错误");
        return mav;
    }

    /**
     * 欢迎页面
     * @return
     */
    @RequestMapping(value = "/welcome")
    public ModelAndView welcome() throws Exception {
        // 调用检查登陆的东东
        if (!this.checkLogin()) return null;

        return new ModelAndView("/admin/welcome");
    }

    /**
     * 登出
     * @return
     */
    @RequestMapping(value = "logout")
    public ModelAndView logout(HttpServletRequest request) {
        // 注销退出
        request.getSession().removeAttribute("adminUserName");

        return new ModelAndView("redirect:/weixin/note/admin/login");
    }
}
