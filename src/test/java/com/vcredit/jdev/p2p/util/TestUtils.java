package com.vcredit.jdev.p2p.util;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

	@Autowired
	AbstractConfigurableEmbeddedServletContainer abstractConfigurableEmbeddedServletContainer;

	public String getEmbeddedServletContainerBaseURL() {
		InetAddress address = abstractConfigurableEmbeddedServletContainer.getAddress();
		String addressStr = "localhost";
		if (address != null) {
			addressStr = address.getHostName();
		}

		int port = abstractConfigurableEmbeddedServletContainer.getPort();
		String contextPath = abstractConfigurableEmbeddedServletContainer.getContextPath();

		return "http://" + addressStr + ":" + port + contextPath;
	}

}
