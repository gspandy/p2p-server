package com.vcredit.jdev.p2p.alert.service;

//import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Properties;

//import org.apache.velocity.app.VelocityEngine;
//import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
//import org.springframework.ui.velocity.VelocityEngineFactoryBean;
//import org.springframework.ui.velocity.VelocityEngineUtils;

import com.vcredit.jdev.p2p.Application;

/**
 * @ClassName: AccountMessageType
 * @Description:消息类型
 * @author ChenChang
 * @date 2014年12月30日 下午4:15:25
 */
@Component
public class AccountMessageType {

	@Autowired
	private TemplateEngine templateEngine;

	private AccountMessageTemplateEnum template;
	private List<AccountMessageChannelEnum> channels;
	private AccountMessageChannelEnum channel;
	private AccountMessageTemplateData templateData;

	private String sendSubject = "";
	private String sendContent = "";

	public void buildAccountMessageType() {
		buildSubjectAndContent(this.template, this.templateData);
		if (null == this.getChannel()) {
			buildChannel(this.template);
		}
	}

	private void buildChannel(AccountMessageTemplateEnum template2) {
		switch (template) {
		case FANG_KUAN_TONG_ZHI:
		case LIU_BIAO_TONG_ZHI:
		case ZHENG_CHANG_HUAN_KUAN:
		case YU_QI_HUAN_KUAN:
		case YU_QI_TONG_ZHI:
		case DIAN_FU_TONG_ZHI:
		case HUI_GOU_TONG_ZHI:
		case JIE_QING_TONG_ZHI:
		case ZHAI_QUAN_ZHUANG_RANG:
		case HONG_BAO_TONG_ZHI:
		case ZHU_CE_CHENG_GONG:
		case XIU_GAI_MI_MA:
			this.setChannel(AccountMessageChannelEnum.SITE);
			break;
		case ZHU_CE_YAN_ZHENG:
		case ZHAO_HUI_MI_MA:
		case TI_XIAN_YAN_ZHENG:
		case XIU_GAI_SHOU_JI:
		case ZHAO_HUI_AN_QUAN_WENTI:
		case FANG_KUAN_DUAN_XING:
		case HOU_TAI_CHONG_SHE_MIMA:
			this.setChannel(AccountMessageChannelEnum.MOBILE);
			break;
		case JIE_KUAN_XIE_YI:
		case ZHAI_QUAN_ZHUANG_RANG_YOUJIAN:
		case EMAIL_ADDR_YAN_ZHENG:
		case FANG_KUAN_TONG_ZHI_JKR:
			this.setChannel(AccountMessageChannelEnum.EMAIL);
			break;
		default:
			break;
		}
	}

	public void buildSubjectAndContent(AccountMessageTemplateEnum template, AccountMessageTemplateData templateData) {

		Context ctx = new Context();
		try {
			Class<?> cls = templateData.getClass();
			Field[] fields = cls.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				String var = fields[i].getName();
				Method method = cls.getMethod("get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1));
				try {
					ctx.setVariable(var, method.invoke(templateData));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		sendContent = templateEngine.process("mail/" + template, ctx);

		if (this.getChannel() != AccountMessageChannelEnum.MOBILE) {
			sendSubject = templateEngine.process("mail/" + template + "_HEAD", ctx);
		}
		sendSubject = replaceHtml(sendSubject);
		sendContent = replaceHtml(sendContent);

	}

	private String replaceHtml(String s) {
		return s.replace("</span>", "").replace("<span>", "");
	}

	// getter & setter
	public AccountMessageTemplateEnum getTemplate() {
		return template;
	}

	public void setTemplate(AccountMessageTemplateEnum template) {
		this.template = template;
	}

	public List<AccountMessageChannelEnum> getChannels() {
		return channels;
	}

	public void setChannels(List<AccountMessageChannelEnum> channels) {
		this.channels = channels;
	}

	public void setChannels(AccountMessageChannelEnum channel) {
		List<AccountMessageChannelEnum> _ch = new ArrayList<AccountMessageChannelEnum>();
		_ch.add(channel);
		this.channels = _ch;
	}

	public AccountMessageTemplateData getTemplateData() {
		return templateData;
	}

	public void setTemplateData(AccountMessageTemplateData templateData) {
		this.templateData = templateData;
	}

	public String getSendSubject() {
		return sendSubject;
	}

	public void setSendSubject(String sendSubject) {
		this.sendSubject = sendSubject;
	}

	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public AccountMessageChannelEnum getChannel() {
		return channel;
	}

	public void setChannel(AccountMessageChannelEnum channel) {
		this.channel = channel;
	}

}
