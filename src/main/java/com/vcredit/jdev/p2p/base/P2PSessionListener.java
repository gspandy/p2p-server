package com.vcredit.jdev.p2p.base;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

@Component
public class P2PSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//new Exception("create").printStackTrace();
		HttpSession session = event.getSession();
		//System.out.println(session.getMaxInactiveInterval());
		// 取得登录的用户名
		String aid = (String) session.getAttribute("aid");
		//System.out.println(aid + "创建");

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		//new Exception("Destroye").printStackTrace();
		HttpSession session = event.getSession();
		//System.out.println(session.getMaxInactiveInterval());
		// 取得登录的用户名
		String aid = (String) session.getAttribute("aid");
		//System.out.println(aid + "超时退出。");

	}

}
