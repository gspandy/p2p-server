package com.vcredit.jdev.p2p.chinapnr.base;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;


/** 
* @ClassName: ChinapnrLogManager 
* @Description: 汇付日志
* @author dk 
* @date 2015年3月5日 下午2:17:01 
*  
*/
@Component
public class ChinapnrLogManager {
	
	private final static Logger logger = LoggerFactory.getLogger(ChinapnrLogManager.class);

	@Autowired
	private SignUtils signUtils;

	public void addResponseLogRote(String cmdId, Map<String, String> parameter,String responseCode,String responseDescription) throws Exception {
		if (StringUtils.isBlank(cmdId)||ThirdChannelEnum.RESPCODEVALUE.getCode().equals(responseCode)) {//命令为空
			return;
		}
		logger.debug("addResponseLogRote---> begin...........");
		logger.debug("addResponseLogRote---> cmdId:"+cmdId+" *****");
		String  responseKey ="";
		if (ThirdChannelEnum.ADDBIDINFO.getCode().equals(cmdId)) {//标的录入
			String proId = parameter.get("ProId");
			responseKey = "ProId_"+proId;
		} else if (ThirdChannelEnum.CASH.getCode().equals(cmdId)) {//取现
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.DELCARD.getCode().equals(cmdId)) {//删除银行卡
			
		} else if (ThirdChannelEnum.INITIATIVETENDER.getCode().equals(cmdId)) {//主动投标
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
			
		} else if (ThirdChannelEnum.AUTOTENDERPLAN.getCode().equals(cmdId)) {//自动投标计划
			
		} else if (ThirdChannelEnum.AUTOTENDER.getCode().equals(cmdId)) {//自动投标
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.AUTOTENDERPLANCLOSE.getCode().equals(cmdId)) {//自动投标计划关闭
			
		} else if (ThirdChannelEnum.LOANS.getCode().equals(cmdId)) {//自动扣款（放款）
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.MERCASH.getCode().equals(cmdId)) {//商户代取现
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.NETSAVE.getCode().equals(cmdId)) {//充值
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.REPAYMENT.getCode().equals(cmdId)) {//自动扣款（还款）
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.TRANSFER.getCode().equals(cmdId)) {//自动扣款转账（商户用）
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.USERBINDCARD.getCode().equals(cmdId)) {//绑卡
			String usrCustId = parameter.get("UsrCustId");
			responseKey = "UsrCustId_"+usrCustId;
		} else if (ThirdChannelEnum.USERLOGIN.getCode().equals(cmdId)) {//登陆
			
		} else if (ThirdChannelEnum.USERREGISTER.getCode().equals(cmdId)) {//注册
			String usrId = parameter.get("UsrId");
			responseKey = "UsrId_"+usrId;
		} else if (ThirdChannelEnum.USRFREEZEBG.getCode().equals(cmdId)) {//资金冻结
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		} else if (ThirdChannelEnum.USRUNFREEZE.getCode().equals(cmdId)) {//资金解冻
			String ordId = parameter.get("OrdId");
			responseKey = "OrdId_"+ordId;
		}
		if(StringUtils.isNotBlank(responseKey)){
			logger.debug("addResponseLogRote---> addResponseChinapnrLogEventPublish begin...........");
			signUtils.addResponseChinapnrLogEventPublish(cmdId, responseCode, responseKey, responseDescription);
			logger.debug("addResponseLogRote---> addResponseChinapnrLogEventPublish end...........");
		}
		
		logger.debug("addResponseLogRote---> end...........");
	}
}
