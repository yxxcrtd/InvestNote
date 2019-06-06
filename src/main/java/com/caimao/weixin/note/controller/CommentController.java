package com.caimao.weixin.note.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.domain.Comment;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.util.Pager;


@RestController
@RequestMapping("/weixin/note/comment")
public class CommentController extends BaseController {
	
	//翻页时候异步查询显示评论信息
	@RequestMapping(value = "asyncQuery/{id:[\\d]+}/{p:[\\d]+}", method = {RequestMethod.GET,RequestMethod.POST})
	public String asyncQuery(@PathVariable(value = "id") int noteId,@PathVariable(value = "p") int p) throws Exception {
		ModelAndView mav = new ModelAndView("/reader/NoteRead");
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (user == null)
			return null;
		
		String where = "c.comment_note_id = " + noteId;
		Pager pager = new Pager();
		pager.setPageNo(p);
		Comment comment = new Comment();
		int count = this.commentService.findAllCount(comment, "comment_note_id = " + noteId);
		pager.setTotalCount(count);
		JSONObject obj = new JSONObject();
		obj.put("list", this.commentService.findListByLeftJoin(pager, where));
		obj.put("count", pager.getTotalPageCount());
		obj.put("p", p);
		return obj.toString();
	}
	
	/**
	 * 添加评论信息，之后查询出来显示在页面上面
	 * @param comment
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "asyncSave", method = {RequestMethod.GET,RequestMethod.POST})
	public String asyncSave(@RequestParam(value = "content", required = true) String content,@RequestParam(value = "noteId",required = true) int noteId) throws Exception {
		ModelAndView mav = new ModelAndView("note/NoteRead");
		JSONObject obj = new JSONObject();
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (null == user) {
			return null;
		}
		Note note = noteService.findById(noteId);
		Comment comment = new Comment();
		//当前正在读笔记的用户
		comment.setComment_user_id(user.getUser_id());
		comment.setComment_user_nickname(user.getUser_nickname());
		comment.setComment_user_header_img(user.getUser_header_img());
		//当前用户读笔记的ID
		comment.setComment_note_id(noteId);
		comment.setComment_stock_code(note.getNote_stock_code());
		comment.setComment_stock_name(note.getNote_stock_name());
		comment.setComment_note_remark(note.getNote_remark());
		//评论内容
		comment.setComment_content(content);
		// 保存，并返回刚插入的主键ID
		Long newId = this.commentService.saveReturnId(comment);
		Comment oneComment = this.commentService.findCommentById(new Long(newId).intValue());
		obj.put("oneComment", oneComment);
		return obj.toString();
	}
}
