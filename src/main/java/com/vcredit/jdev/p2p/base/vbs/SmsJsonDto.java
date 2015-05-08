package com.vcredit.jdev.p2p.base.vbs;

/** 
* @ClassName: SmsJsonDto 
* @Description: 短信模板Dto
* @author dk 
* @date 2015年3月10日 下午4:45:49 
*  
*/
public class SmsJsonDto {
	private int OperationUserID;
	private String MessageContent;
	private String Mobile;
	private String SignStr;
	private String SmsSource;
	private String SmsTypeName;
	private int CustomerType;
	private String Parameters;
	private String Remark;

	public SmsJsonDto(int operationUserID, String messageContent,
			String mobile, String signStr, String smsSource,
			String smsTypeName, int customerType, String parameters,
			String remark) {
		super();
		OperationUserID = operationUserID;
		MessageContent = messageContent;
		Mobile = mobile;
		SignStr = signStr;
		SmsSource = smsSource;
		SmsTypeName = smsTypeName;
		CustomerType = customerType;
		Parameters = parameters;
		Remark = remark;
	}
	
	public int getOperationUserID() {
		return OperationUserID;
	}
	public void setOperationUserID(int operationUserID) {
		OperationUserID = operationUserID;
	}
	public String getMessageContent() {
		return MessageContent;
	}
	public void setMessageContent(String messageContent) {
		MessageContent = messageContent;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getSignStr() {
		return SignStr;
	}
	public void setSignStr(String signStr) {
		SignStr = signStr;
	}
	public String getSmsSource() {
		return SmsSource;
	}
	public void setSmsSource(String smsSource) {
		SmsSource = smsSource;
	}
	public String getSmsTypeName() {
		return SmsTypeName;
	}
	public void setSmsTypeName(String smsTypeName) {
		SmsTypeName = smsTypeName;
	}
	public int getCustomerType() {
		return CustomerType;
	}
	public void setCustomerType(int customerType) {
		CustomerType = customerType;
	}
	public String getParameters() {
		return Parameters;
	}
	public void setParameters(String parameters) {
		Parameters = parameters;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}

	@Override
	public String toString() {
		return "SmsJsonDto [OperationUserID=" + OperationUserID
				+ ", MessageContent=" + MessageContent + ", Mobile=" + Mobile
				+ ", SignStr=" + SignStr + ", SmsSource=" + SmsSource
				+ ", SmsTypeName=" + SmsTypeName + ", CustomerType="
				+ CustomerType + ", Parameters=" + Parameters + ", Remark="
				+ Remark + "]";
	}
	
	
	
	

}
