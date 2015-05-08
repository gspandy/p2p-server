package com.vcredit.jdev.p2p.account.modal;

import java.util.ArrayList;
import java.util.List;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.vcredit.jdev.p2p.bizsync.model.BizDataManager;
import com.vcredit.jdev.p2p.dto.LoanDataDto;
import com.vcredit.jdev.p2p.dto.LoanDataDtoList;
import com.vcredit.jdev.p2p.dto.LoanDataList;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.enums.BlDataStatusEnum;
import com.vcredit.jdev.p2p.enums.LoanDataRecordStatusEnum;

@MessageEndpoint
public class RegisterManagerActivator {
	
	private static Logger logger = LoggerFactory.getLogger(RegisterManagerActivator.class);
	
	//private EventMessageGateway eventMessageGateway;
	@Autowired
	private AsyncEventMessageGateway asyncEventMessageGateway;//异步
	
	@Autowired
	private RegisterManager registerManager;//同步
	
	@Autowired
	private BizDataManager bizDataManager;
	
	/** 
	* @Title: handle 
	* @Description: 接收 VBS传送过来的LoanData,注册借款人，发送标的事件 
	*  @param event loanData列表
	*  @return
	*  @throws Exception
	* @return Object    返回类型 
	* @throws 
	*/
	@SuppressWarnings("unchecked")
	@ServiceActivator(inputChannel = "asyncEventPublishChannel")
	public void handle(Object event) throws Exception {
		if (event instanceof LoanDataList) {
			logger.debug("handle-->接收 VBS传送过来的LoanData begin..........");
			List<LoanData> loanDatas = ((LoanDataList) event).getLoanDatas();
			List<LoanDataDto> loanDataDtos = new ArrayList<LoanDataDto>();
			for (LoanData loanData : loanDatas) {
				try {
					//注册借款人
					Account account = registerManager.registerBorrowerFromVBS(loanData);
					LoanDataDto loanDataDto = new LoanDataDto(account.getAccountSequence(),loanData);
					loanData.setProgressStauts(BlDataStatusEnum.accountHandleCompleted.getCode());
					bizDataManager.dataImport(loanData);
					//当能正常保存，更改LoanData状态
					loanDataDtos.add(loanDataDto);
				} catch (Exception e) {
					logger.error("handle-->registerManager.registerBorrowerFromVBS error begin");
					loanData.setRecordStatus(LoanDataRecordStatusEnum.ERROR.getCode());
					loanData.setSynchronizeDescription(BlDataStatusEnum.accountHandleCompleted.getCode()); //处理开平台账户这步出错
					bizDataManager.dataImport(loanData);
					//保存account error log
					logger.error("接收 VBS传送过来的LoanData中的"+loanData.getLoanDataSequence()+"注册借款人 error"+e.getMessage());
					logger.error("handle-->registerManager.registerBorrowerFromVBS error end");
				}
			}
			
			//派发event,发送标的
			if(!loanDataDtos.isEmpty()){
				logger.debug("handle-->接收 VBS传送过来的LoanData 派发event,发送标的 begin..........");
				asyncEventMessageGateway.publishEvent(new LoanDataDtoList(loanDataDtos));
				logger.debug("handle-->接收 VBS传送过来的LoanData 派发event,发送标的 end..........");
			}
			logger.debug("handle-->接收 VBS传送过来的LoanData end..........");
		}
	}

}
