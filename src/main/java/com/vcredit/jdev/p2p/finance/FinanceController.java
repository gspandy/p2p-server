package com.vcredit.jdev.p2p.finance;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * @author ChenChang
 *
 */
@Path("/finance")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class FinanceController {

	@Autowired
	private DictionaryUtil dictionaryUtil;

	// 投资金额 pv
	// 投资期限 nper
	// 每月本金收回后再次进行投资 isRoll
	// 年利率 r
	// 对比类型:平台贷款项目 1,银行活期存款 2,银行定期存款 3,货币基金 4,
	// return: 1,y

	@POST
	@Path("/queryYieldComparison")
	public Response queryYieldComparison(Map<String, Object> paramMap) {

		//		double rP2p = 0.13;//平台年利率
		//		double rBankLive = 0.0035;//银行活期年利率
		//		double rBankFix = 0.0275;//银行定期年利率
		//		double rFund = 0.045;//基金年利率

		double rP2p = Double.valueOf(dictionaryUtil.getDicChinaMean("t_invinv_irate1"));//平台年利率
		double rBankLive = Double.valueOf(dictionaryUtil.getDicChinaMean("t_dicbank_lrate1"));//银行活期年利率
		double rBankFix = Double.valueOf(dictionaryUtil.getDicChinaMean("t_dicbank_frate2"));//银行定期年利率
		double rFund = Double.valueOf(dictionaryUtil.getDicChinaMean("t_dicfund_rate1"));//基金年利率

		try {
			double pv = Double.valueOf(paramMap.get("pv").toString());
			int nper = Integer.valueOf(paramMap.get("nper").toString());
			boolean isRoll = Boolean.valueOf(paramMap.get("isRoll") == null ? "false" : paramMap.get("isRoll").toString());
			double rCustom = Double.valueOf(paramMap.get("rate") == null ? "0" : paramMap.get("rate").toString());

			double yP2p = FinanceUtil.calcYield4P2p(rP2p / 12, nper, pv, isRoll);
			double yBankLive = FinanceUtil.calcYieldBase(rBankLive / 12, nper, pv);
			double yBankFix = FinanceUtil.calcYieldBase(rBankFix / 12, nper, pv);
			double yFund = FinanceUtil.calcYieldBase(rFund / 12, nper, pv);

			double yCustom = FinanceUtil.calcYield4P2p(rCustom / 12, nper, pv, isRoll);

			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

			Map<Integer, Object> yMap = new HashMap<Integer, Object>();

			if (yCustom > 0) {
				yP2p = yCustom;
			}
			yMap.put(1, df.format(yP2p));
			yMap.put(2, df.format(yBankLive));
			yMap.put(3, df.format(yBankFix));
			yMap.put(4, df.format(yFund));
			//	yMap.put(5, df.format(yCustom));

			return Response.successResponse(yMap);

		} catch (Exception e) {
			e.printStackTrace();
			// 查询收益对比失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_A99, ResponseConstants.CommonMessage.RESPONSE_MSG_A99);
		}
	}
}
