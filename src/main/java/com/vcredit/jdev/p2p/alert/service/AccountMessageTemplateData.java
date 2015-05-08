package com.vcredit.jdev.p2p.alert.service;

/**
 * @ClassName: AccountMessageTemplateData
 * @Description:用户消息模板需绑定的数据/不需要的不用进行set*
 * @author ChenChang
 * @date 2014年12月30日 下午5:42:37
 */
public class AccountMessageTemplateData {

	private String p1;
	private String p2;
	private String p3;
	private String p4;
	private String p5;

	/**
	 * 附件路径
	 */
	private String attachPath;
	/**
	 * 手机验证码
	 */
	private String mobileVerificationCode;
	/**
	 * <项目ID>
	 */
	private String projectId;
	/**
	 * <项目名称>
	 */
	private String projectName;
	/**
	 * <项目IdURL>
	 */
	private String projectIdURL;

	/**
	 * <帮助中心>
	 */
	private String helpCenterURL = "#/help";
	/**
	 * <期数>
	 */
	private String period;
	/**
	 * <应还本息>
	 */
	private String sum;
	/**
	 * <红包名称>
	 */
	private String redPacketName;
	/**
	 * <我的红包URL>
	 */
	private String myRedPacketURL;
	/**
	 * <修改登录密码时间 YYYY/MM/DD HH:MM:SS>
	 */
	private String pwdModifyDateTime;

	/**
	 * 邮件地址
	 */
	private String mail;

	/**
	 * 垫付日
	 */
	private String debourDate;
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 验证地址
	 */
	private String validateURL;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getHelpCenterURL() {
		return helpCenterURL;
	}

	public void setHelpCenterURL(String helpCenterURL) {
		this.helpCenterURL = helpCenterURL;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getRedPacketName() {
		return redPacketName;
	}

	public void setRedPacketName(String redPacketName) {
		this.redPacketName = redPacketName;
	}

	public String getMyRedPacketURL() {
		return myRedPacketURL;
	}

	public void setMyRedPacketURL(String myRedPacketURL) {
		this.myRedPacketURL = myRedPacketURL;
	}

	public String getPwdModifyDateTime() {
		return pwdModifyDateTime;
	}

	public void setPwdModifyDateTime(String pwdModifyDateTime) {
		this.pwdModifyDateTime = pwdModifyDateTime;
	}

	public String getMobileVerificationCode() {
		return mobileVerificationCode;
	}

	public void setMobileVerificationCode(String mobileVerificationCode) {
		this.mobileVerificationCode = mobileVerificationCode;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDebourDate() {
		return debourDate;
	}

	public void setDebourDate(String debourDate) {
		this.debourDate = debourDate;
	}

	public String getProjectIdURL() {
		return projectIdURL;
	}

	public void setProjectIdURL(String projectIdURL) {
		this.projectIdURL = projectIdURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getValidateURL() {
		return validateURL;
	}

	public void setValidateURL(String validateURL) {
		this.validateURL = validateURL;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

}
