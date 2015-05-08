package com.vcredit.jdev.p2p.base;

import javax.servlet.http.HttpSession;

import net.gplatform.sudoor.server.security.model.auth.SSAuth;
import net.gplatform.sudoor.server.social.model.SimpleSignInAdapter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class P2pSessionContext {

	@Autowired
	private HttpSession session;

	@Autowired
	private SSAuth auth;
	
	public Object getAttribute(String name){
		return session.getAttribute(name);
	} 
	
	public void setAttribute(String name,Object value){
		session.setAttribute(name, value);
	}
	
	public void removeAttribute(String name){
		session.removeAttribute(name);
	}
	
	public String getSessionId(){
		return session.getId();
	}
	


	public String getCurrentUserName() {
		String aid = (String) session.getAttribute("aid");
		if (StringUtils.isNotBlank(aid)) {
			return (String) session.getAttribute("username");
		} else {
			if (auth.getIsPostLogin()) {
				try {
					return (String) PropertyUtils.getProperty(session.getAttribute(SimpleSignInAdapter.CONNECTION_KEY), "displayName");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
	public String getLoginName() {
		String aid = (String) session.getAttribute("aid");
		if (StringUtils.isNotBlank(aid)) {
			return (String) session.getAttribute("username");
		} else {
			if (auth.getIsPostLogin()) {
				try {
					return auth.getCurrentUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	public Long getCurrentAid() {
		String aid = (String) getAttribute("aid");
		if(StringUtils.isNotBlank(aid)){
			return Long.valueOf(aid);
		}
		return null;
	}


	public String getCurrentAidString() {
		String aid = (String) getAttribute("aid");
		if(StringUtils.isNotBlank(aid)){
			return aid;
		}
		return null;
	}

}
