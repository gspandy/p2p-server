package com.vcredit.jdev.p2p.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


//用户登录记录
@Entity
@Table(name="t_al_log")
public class AccountLoginRecord {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_al_log_seq", unique = true, nullable = false)
	private Long accountLoginSequence;// 流水号
	@Column(name = "login_date",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountLoginDate;// 登录时间
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequence;// 用户表流水号
	@Column(name = "login_ip",nullable = false,length=20)
	private String loginIp;// 登录IP
	@Column(name = "ip_prov")
	private Integer ipAddressProvince;// IP所在省份 邮编-参考区域表
	@Column(name = "ip_city")
	private Integer ipAddressCity;// IP所在地级市 邮编-参考区域表
	@Column(name = "ip_town")
	private Integer ipAddressTown;// IP所在区县 邮编-参考区域表
	@Column(name = "session_id",nullable = false,length=256)
	private String sessionId;// 会话id
      
	public Long getAccountLoginSequence() {
		return accountLoginSequence;
	}
	public void setAccountLoginSequence(Long accountLoginSequence) {
		this.accountLoginSequence = accountLoginSequence;
	}
	public Date getAccountLoginDate() {
		return accountLoginDate;
	}
	public void setAccountLoginDate(Date accountLoginDate) {
		this.accountLoginDate = accountLoginDate;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getIpAddressProvince() {
		return ipAddressProvince;
	}
	public void setIpAddressProvince(Integer ipAddressProvince) {
		this.ipAddressProvince = ipAddressProvince;
	}
	public Integer getIpAddressCity() {
		return ipAddressCity;
	}
	public void setIpAddressCity(Integer ipAddressCity) {
		this.ipAddressCity = ipAddressCity;
	}
	public Integer getIpAddressTown() {
		return ipAddressTown;
	}
	public void setIpAddressTown(Integer ipAddressTown) {
		this.ipAddressTown = ipAddressTown;
	}

}
