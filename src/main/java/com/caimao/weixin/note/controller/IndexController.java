package com.caimao.weixin.note.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;

@RestController
@RequestMapping("weixin/note/index")
public class IndexController extends BaseController {

    /**
     * 首页
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public ModelAndView Index() throws Exception {
        ModelAndView mav = new ModelAndView("Index");
        baseAuthInfo(mav);

        // 获取总的笔记数，阅读数
        Object noteCount = this.redisUtil.get("_note_count");
        Object readCount = this.redisUtil.get("_read_count");
		if (null == noteCount) {
			noteCount = noteService.findAllCount(new Note(), " note_pay_status = 2");
			redisUtil.set("_note_count", noteCount.toString(), 10 * 60);
        }
		if (null == readCount) {
			readCount = readerService.findAllCount(new Reader(), " 1=1");
			redisUtil.set("_read_count", readCount.toString(), 10 * 60);
        }
        mav.addObject("noteCount", noteCount);
        mav.addObject("readCount", readCount);
        return mav;
    }

    @RequestMapping(value = "/about")
    public ModelAndView about() throws Exception {
        ModelAndView mav = new ModelAndView("about");
        mav.addObject("d", System.currentTimeMillis());
        return mav;
    }

    /**
     * 创建菜单方法
     *
     * @throws Exception
     */
//    @RequestMapping(value = "menus", method = RequestMethod.GET)
//    public void test() throws Exception {
//        // 删除并创建菜单
//        wxMenus.delAll();
//        Thread.sleep(5000);
//        wxMenus.createMeuns();
//    }

}
