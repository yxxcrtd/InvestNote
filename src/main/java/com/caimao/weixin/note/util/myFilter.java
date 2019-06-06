package com.caimao.weixin.note.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class myFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 HttpServletRequest req = (HttpServletRequest) request;
		 HttpServletResponse res = (HttpServletResponse) response;
		 String userAgent = req.getHeader("User-Agent");
		 System.out.println("===========================" + userAgent + "=========================");
		 //http://gupiao.caimao.com/weixin/note/reader/read/1691
		 String url = req.getRequestURL().toString();
		 Pattern pattern = Pattern.compile("http://gupiao.caimao.com/weixin/note/reader/read/\\d+");
		 Matcher matcher = pattern.matcher(url);
		 if(!userAgent.contains("MicroMessenger") && matcher.matches()) {
			 String domain = "/weixin/note/qrcode/" + url.substring(url.lastIndexOf("/") + 1);
			 RequestDispatcher rd = request.getRequestDispatcher(domain);
			 rd.forward(req, res);
			 return;
		 }else {
			 chain.doFilter(request, response);
		 }
	}
	@Override
	public void destroy() {
	}

}
