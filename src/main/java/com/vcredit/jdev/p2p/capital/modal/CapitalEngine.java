package com.vcredit.jdev.p2p.capital.modal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * 资金计算引擎
 * @author 周佩
 *
 */
public class CapitalEngine {
	
	/**
	 * 计算：根据借款金额、总期数、年利率计算出对应的应收本息、应收利息、应收本金、剩余本金
	 * @param transAmount	借款金额
	 * @param period		总期数
	 * @param rate			年利率
	 * @return
	 * @throws Exception 
	 */
	public static List<ClaimPayPlan> capitalMatchingService(BigDecimal transAmount,Integer period,BigDecimal rate) throws Exception{
		if(transAmount==null){
			throw new Exception("transAmount is null");
		}
		if(period==null){
			throw new Exception("period is null");
		}
		if(rate==null){
			throw new Exception("rate is null");
		}
		//应收本息
		BigDecimal claimPayPlanTotalAmount= BigDecimal.ZERO;
		//应还利息
		BigDecimal claimPayPlanInterest= BigDecimal.ZERO;
		//应还本金
		BigDecimal claimPayPlanPrincipal= BigDecimal.ZERO;
		//剩余本金
		BigDecimal claimPayPlanSurplus = transAmount;
		//之前的剩余本金总和
		BigDecimal claimPayPlanSurplusLast = BigDecimal.ZERO;
		double monthRate = rate.divide(new BigDecimal(12),ConstantsUtil.FLOD_INDEX,ConstantsUtil.ROUND_HALF_UP).doubleValue();
		List<ClaimPayPlan> result=new ArrayList<ClaimPayPlan>();
		for(int i=1;i<=period;i++){
			//应还本息
			claimPayPlanTotalAmount=BigDecimal.valueOf(FinanceUtil.PMT(Double.valueOf(monthRate), period, Double.valueOf(transAmount.toString()))).setScale(ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP);
			
			if(i==period){//最后一期
				//应还本金
				claimPayPlanPrincipal=transAmount.subtract(claimPayPlanSurplusLast);
				//应还利息，最后的本金*月利率
				claimPayPlanInterest = claimPayPlanPrincipal.multiply(rate).divide(BigDecimal.valueOf(12),ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP);
				//剩余本金
				claimPayPlanSurplus=BigDecimal.ZERO;
				//最后一期本息,本金+利息
				claimPayPlanTotalAmount = claimPayPlanPrincipal.add(claimPayPlanInterest);
			}else{
				//应还利息
				claimPayPlanInterest = claimPayPlanSurplus.multiply(rate).divide(BigDecimal.valueOf(12),ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP);
				//应还本金
				claimPayPlanPrincipal=claimPayPlanTotalAmount.subtract(claimPayPlanInterest);
				//剩余本金
				claimPayPlanSurplus=claimPayPlanSurplus.subtract(claimPayPlanPrincipal);
			}
			ClaimPayPlan c=new ClaimPayPlan();
			c.setClaimPayPlanInterest(claimPayPlanInterest.setScale(ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP));
			c.setClaimPayPlanPrincipal(claimPayPlanPrincipal.setScale(ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP));
			c.setClaimPayPlanSurplus(claimPayPlanSurplus.setScale(ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP));
			c.setClaimPayPlanTotalAmount(claimPayPlanTotalAmount.setScale(ConstantsUtil.FLOD_INDEX2,ConstantsUtil.ROUND_HALF_UP));
			claimPayPlanSurplusLast=claimPayPlanSurplusLast.add(c.getClaimPayPlanPrincipal());
			result.add(c);
		}
		return result;
	}
//	public static void main(String[] args) throws Exception {
//		List<ClaimPayPlan> r=capitalMatchingService(new BigDecimal(50000), 36, new BigDecimal(0.13));
//		for(ClaimPayPlan c:r){
//			System.out.println(c.getClaimPayPlanInterest()+"$"+
//			c.getClaimPayPlanPrincipal()+"$"+
//			c.getClaimPayPlanSurplus()+"$"+
//			c.getClaimPayPlanTotalAmount());
//		}
//	}
}
