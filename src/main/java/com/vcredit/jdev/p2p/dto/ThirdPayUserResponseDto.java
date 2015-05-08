package com.vcredit.jdev.p2p.dto;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vcredit.jdev.p2p.base.util.P2pUtil;

/**
 * 三方支付的用户传输类
 * 可选字段
 * @author 周佩
 *
 */
public class ThirdPayUserResponseDto extends ThirdPayBaseResponseDto{
	/** 本平台唯一标识 */
	@JsonProperty("TrxId")
	private String trxId;
	/** 页面返回URL */
	@JsonProperty("RetUrl")
	private String retUrl;
	/**订单号*/
	@JsonProperty("OrdId")
	private String ordId;
	/** 身份类型 默认身份证：00 */
	@JsonProperty("IdType")
	private String idType = "00";
	/** 证件号码 */
	@JsonProperty("IdNo")
	private String idNo;
	/** 用户Id */
	@JsonProperty("UsrId")
	private String usrId;
	/** 手机号 */
	@JsonProperty("UsrMp")
	private String usrMp;
	/** 用户Email */
	@JsonProperty("UsrEmail")
	private String usrEmail;
	/** 真实姓名 */
	@JsonProperty("UsrName")
	private String usrName;
	/**可用余额*/
	@JsonProperty("AvlBal")
	private String avlBal;
	/**账户余额*/
	@JsonProperty("AcctBal")
	private String acctBal;
	/**冻结金额*/
	@JsonProperty("FrzBal")
	private String frzBal;
	/**银行卡代码*/
	@JsonProperty("OpenBankId")
	private String openBankId;
	/**银行卡号*/
	@JsonProperty("OpenAcctId")
	private String openAcctId;
	/**银行卡名*/
	@JsonProperty("Province")
	private Integer province;
	/**银行卡名*/
	@JsonProperty("BankCity")
	private Integer bankCity;
	/**银行卡名*/
	@JsonProperty("BankTown")
	private Integer bankTown;
	/**银行卡名*/
	@JsonProperty("OpenBankName")
	private String openBankName;
	/**手机号*/
	@JsonProperty("Mobile")
	private String mobile;
	/**冻结订单号*/
	@JsonProperty("FreezeOrdId")
	private String freezeOrdId;
	/**冻结标识*/
	@JsonProperty("FreezeTrxId")
	private String freezeTrxId;
	/**金额*/
	@JsonProperty("TransAmt")
	private String transAmt;
	/**子账户*/
	@JsonProperty("AcctDetails")
	private List<AcctDetail> acctDetails;
	/**子账户*/
	@JsonProperty("PlaintStr")
	private String plaintStr;
	@JsonProperty("OutCustId")
	private String outCustId;
	@JsonProperty("InCustId")
	private String inCustId;
	@JsonProperty("InAcctId")
	private String inAcctId;
	/**银行卡简称*/
	@JsonProperty("GateBankId")
	private String gateBankId;
	/**银行卡信息*/
	@JsonProperty("UsrCardInfolist")
	private List<UsrCardInfoDto> usrCardInfolist;
	/**银行卡信息*/
	@JsonProperty("UnFreezeOrdId")
	private String unFreezeOrdId;
	/**手续费金额-指汇付扣除我们p2p平台的*/
	@JsonProperty("FeeAmt")
	private String feeAmt;
	/**手续费扣款客户号-指汇付扣除我们p2p平台的*/
	@JsonProperty("FeeCustId")
	private String feeCustId;
	/**手续费扣款子账户-指汇付扣除我们p2p平台的，主要是MDT账户*/
	@JsonProperty("FeeAcctId")
	private String feeAcctId;
	
	public String getTrxId() {
		return trxId;
	}
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	public String getRetUrl() {
		return retUrl;
	}
	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getUsrMp() {
		return usrMp;
	}
	public void setUsrMp(String usrMp) {
		this.usrMp = usrMp;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	public String getAvlBal() {
		return avlBal;
	}
	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}
	public String getAcctBal() {
		return acctBal;
	}
	public void setAcctBal(String acctBal) {
		this.acctBal = acctBal;
	}
	public String getFrzBal() {
		return frzBal;
	}
	public void setFrzBal(String frzBal) {
		this.frzBal = frzBal;
	}

	public static ThirdPayUserResponseDto formateJson(String jsonStr) throws JsonParseException, JsonMappingException, IOException, IllegalAccessException, InvocationTargetException{
		ThirdPayUserResponseDto dto=new ThirdPayUserResponseDto();
		if(jsonStr==null||jsonStr.trim().equals("")){//为null或者为""那么返回的对象为空
			return dto;
		}
		Map<String,Object> parameter=P2pUtil.getBeanFromJson(jsonStr, HashMap.class);
		Set<String> set=parameter.keySet();
		for(String key:set){
			Object v=parameter.get(key);
			String tmp=key.substring(0,1).toLowerCase()+key.substring(1);
			Object value=v;
			if(v!=null&&v instanceof List&&((List)v).size()>0){
				if(tmp.equals("acctDetails")){
					//格式化
					String json=P2pUtil.getBeanToJson(v);
					value=P2pUtil.getBeansFromJson(json, AcctDetail.class);
				}
				if(tmp.equals("usrCardInfolist")){
					//格式化
					String json=P2pUtil.getBeanToJson(v);
					value=P2pUtil.getBeansFromJson(json, UsrCardInfoDto.class);
				}
			}
			BeanUtils.copyProperty(dto, tmp,value);
		}
		return dto;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getOpenBankId() {
		return openBankId;
	}
	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}
	public String getOpenAcctId() {
		return openAcctId;
	}
	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}
	public String getOpenBankName() {
		return openBankName;
	}
	public void setOpenBankName(String openBankName) {
		this.openBankName = openBankName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFreezeOrdId() {
		return freezeOrdId;
	}
	public void setFreezeOrdId(String freezeOrdId) {
		this.freezeOrdId = freezeOrdId;
	}
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public String getFreezeTrxId() {
		return freezeTrxId;
	}
	public void setFreezeTrxId(String freezeTrxId) {
		this.freezeTrxId = freezeTrxId;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public List<AcctDetail> getAcctDetails() {
		return acctDetails;
	}
	public void setAcctDetails(List<AcctDetail> acctDetails) {
		this.acctDetails = acctDetails;
	}
	public String getPlaintStr() {
		return plaintStr;
	}
	public void setPlaintStr(String plaintStr) {
		this.plaintStr = plaintStr;
	}
	public String getOutCustId() {
		return outCustId;
	}
	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}
	public String getInCustId() {
		return inCustId;
	}
	public void setInCustId(String inCustId) {
		this.inCustId = inCustId;
	}
	public String getInAcctId() {
		return inAcctId;
	}
	public void setInAcctId(String inAcctId) {
		this.inAcctId = inAcctId;
	}
	public List<UsrCardInfoDto> getUsrCardInfolist() {
		return usrCardInfolist;
	}
	public void setUsrCardInfolist(List<UsrCardInfoDto> usrCardInfolist) {
		this.usrCardInfolist = usrCardInfolist;
	}
	public String getGateBankId() {
		return gateBankId;
	}
	public void setGateBankId(String gateBankId) {
		this.gateBankId = gateBankId;
	}
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getBankCity() {
		return bankCity;
	}
	public void setBankCity(Integer bankCity) {
		this.bankCity = bankCity;
	}
	public Integer getBankTown() {
		return bankTown;
	}
	public void setBankTown(Integer bankTown) {
		this.bankTown = bankTown;
	}
	public String getUnFreezeOrdId() {
		return unFreezeOrdId;
	}
	public void setUnFreezeOrdId(String unFreezeOrdId) {
		this.unFreezeOrdId = unFreezeOrdId;
	}
	public String getFeeAmt() {
		if(StringUtils.isBlank(feeAmt)){
			feeAmt="0.00";//设置默认的金额如果为null那么就强制为0
		}
		return feeAmt;
	}
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}
	public String getFeeCustId() {
		return feeCustId;
	}
	public void setFeeCustId(String feeCustId) {
		this.feeCustId = feeCustId;
	}
	public String getFeeAcctId() {
		return feeAcctId;
	}
	public void setFeeAcctId(String feeAcctId) {
		this.feeAcctId = feeAcctId;
	}
	
}
