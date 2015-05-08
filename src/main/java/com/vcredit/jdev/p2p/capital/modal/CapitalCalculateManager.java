package com.vcredit.jdev.p2p.capital.modal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.dto.CapitalCalculateQueryDto;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.CapitalCalculateQueryRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;

@Component
public class CapitalCalculateManager {
	private Logger logger = LoggerFactory.getLogger(CapitalCalculateManager.class);
	
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;
	
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;
	
	@Autowired
	private CapitalCalculateQueryRepository capitalCalculateQueryRepository;

	public List<CapitalCalculateQueryDto> queryInvestment(Long s) {
		Integer accountInvestmentPayedPeriod = 0;
		
		List<CapitalCalculateQueryDto> dtoList = capitalCalculateQueryRepository.queryInvestment(s);
		//todo 学彦确认 连续垫付次数 1期不获取
		/*for(CapitalCalculateQueryDto dto: dtoList){
			HashMap<String, Object> map = new HashMap<String, Object>();
			List<AccountInvestment> list = accountInvestmentRepository.findCapitalByInvestmentId(s); 
			for(AccountInvestment a : list){
				if(a.getAccountGetInvestmentForm() == 5){
					
				}
			}
		}*/
		
		if(!dtoList.isEmpty()){
			ClaimPayPlan claimPayPlan = claimPayPlanRepository.findPayedPeriodByInvestmentSequenceAndStatus(s,
	                ClaimPayPlanStatusEnum.PAID.getCode(), ClaimPayPlanStatusEnum.PAYING.getCode(),
	                ClaimPayPlanStatusEnum.VBS_PAID.getCode());
			if (claimPayPlan != null) {
				accountInvestmentPayedPeriod = claimPayPlan.getClaimPayPlanNumber();
			}
			dtoList.get(0).setClaimPayPlanPeriod(accountInvestmentPayedPeriod);
			logger.info("还款期数"+accountInvestmentPayedPeriod);
			//放款日为27日以后则 设置为27
			if(null != dtoList.get(0).getInvRepayDate()){
				if(dtoList.get(0).getInvRepayDate() > 26){ 
					dtoList.get(0).setInvRepayDate(27);
				}	
			}
			
			return dtoList;
		}else{
			return null;
		}
		
	}
	
}
