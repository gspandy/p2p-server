package com.vcredit.jdev.p2p.capital.modal;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.deal.service.BondPayOffManager;
import com.vcredit.jdev.p2p.deal.service.BondTransferManager;
import com.vcredit.jdev.p2p.deal.service.CapitalTopupServiceManager;
import com.vcredit.jdev.p2p.deal.service.CapitalWithdrawServiceManager;
import com.vcredit.jdev.p2p.deal.service.ReleaseCashServiceManager;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.MerPrivEnum;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;
import com.vcredit.jdev.p2p.merchant.modal.MerchantManager;

/**
 * 分发
 * 
 * @author 周佩
 *
 */
@Component
public class BaseManager {

	@Autowired
	private CapitalAccountManager capitalAccountManager;
	@Autowired
	private CapitalPlatformManager capitalPlatformManager;
	@Autowired
	private AccountBankCardManager accountBankCardManager;
	@Autowired
	private MerchantManager merchantManager;
	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;
	@Autowired
	private ReleaseCashServiceManager releaseCashServiceManager;
	@Autowired
	private CapitalWithdrawServiceManager capitalWithdrawServiceManager;
	@Autowired
	private CapitalTopupServiceManager capitalTopupServiceManager;
	@Autowired
	private BondPayOffManager bondPayOffManager;
	@Autowired
	private BondTransferManager bondTransferManager;
	@Autowired
	private DictionaryUtil dictionaryUtil;

	/**
	 * 汇付接口将调用此方法，来分发所有功能
	 * 
	 * @param parameter
	 * @throws Exception
	 */
	public void invokeMethod(String cmdId, Map<String, String> parameter) throws Exception {
		boolean isSuccess = true;
		if (StringUtils.isBlank(cmdId)) {//命令为空
			return;
		}
		String respCode = parameter.get(ThirdChannelEnum.RESPCODE.getCode());
		if (respCode == null || !ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
			//return;
			isSuccess = false;
		}
		ThirdPayUserResponseDto dto = new ThirdPayUserResponseDto();
		Set<String> set = parameter.keySet();
		for (String key : set) {
			String value = parameter.get(key);
			String tmp = key.substring(0, 1).toLowerCase() + key.substring(1);
			BeanUtils.copyProperty(dto, tmp, value);
		}
		if (ThirdChannelEnum.ADDBIDINFO.getCode().equals(cmdId)) {//标的录入
			//			merchantManager.addBidInfo(dto);
		} else if (ThirdChannelEnum.CASH.getCode().equals(cmdId)) {//取现
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			//取现回调处理
			String openBankId = dto.getOpenAcctId();
			if (openBankId == null) {
				openBankId = "";
			}
			Integer bankCode = 0;
			if (dto.getOpenBankId() != null) {
				String bankStr = dictionaryUtil.getDicValue(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.T_DIC_BANK_ENAME.getCode()
						+ dto.getOpenBankId());
				if (bankStr != null && !bankStr.trim().equals("")) {
					bankCode = Integer.valueOf(bankStr);
				}
			}
			capitalWithdrawServiceManager.withdrawAfter(dto.getOrdId(), isSuccess, dto.getRespDesc(), openBankId, bankCode,new BigDecimal(dto.getFeeAmt()));

		} else if (ThirdChannelEnum.DELCARD.getCode().equals(cmdId)) {//删除银行卡
			accountBankCardManager.deleteCard(parameter);
		} else if (ThirdChannelEnum.INITIATIVETENDER.getCode().equals(cmdId)) {//主动投标
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			// 主动投标InitiativeTender
			//更新orderid
			bondPackageInvestManager.manualOrAutoInvestBack(dto.getOrdId(), dto.getFreezeOrdId(), isSuccess, dto.getTrxId(), dto.getFreezeTrxId(),
					dto.getRespDesc());

		} else if (ThirdChannelEnum.AUTOTENDERPLAN.getCode().equals(cmdId)) {//自动投标计划
			// 自动投标计划开启
			bondPackageInvestManager.openInvestStrategy(Long.parseLong(dto.getMerPriv()));

		} else if (ThirdChannelEnum.AUTOTENDER.getCode().equals(cmdId)) {//自动投标
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			//自动投标回调处理
			bondPackageInvestManager.manualOrAutoInvestBack(dto.getOrdId(), dto.getFreezeOrdId(), isSuccess, dto.getTrxId(), dto.getFreezeTrxId(),
					dto.getRespDesc());
		} else if (ThirdChannelEnum.AUTOTENDERPLANCLOSE.getCode().equals(cmdId)) {//自动投标计划关闭
			// 自动投标计划关闭
			bondPackageInvestManager.closeInvestStrategy(Long.parseLong(dto.getMerPriv()));

		} else if (ThirdChannelEnum.LOANS.getCode().equals(cmdId)) {//自动扣款（放款）
			capitalAccountManager.updateCapital(dto);
			//  满标放款分账回调
			releaseCashServiceManager.releaseCashAfter(dto.getOrdId(), dto.getUnFreezeOrdId(), isSuccess, dto.getRespDesc());

		} else if (ThirdChannelEnum.MERCASH.getCode().equals(cmdId)) {//商户代取现
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			capitalWithdrawServiceManager.autoWithdrawAndFeeBack(dto.getOrdId(), isSuccess, dto.getRespDesc());
		} else if (ThirdChannelEnum.NETSAVE.getCode().equals(cmdId)) {//充值
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			//充值后处理
			String bankStr = dictionaryUtil.getDicValue(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.T_DIC_BANK_ENAME.getCode()
					+ dto.getGateBankId());
			Integer bankCode = null;
			if (bankStr != null && !bankStr.trim().equals("")) {
				bankCode = Integer.valueOf(bankStr);
			}
			capitalTopupServiceManager.capitalTopupAfter(dto.getOrdId(), isSuccess, "0", bankCode, dto.getRespDesc(),new BigDecimal(dto.getFeeAmt()));

		} else if (ThirdChannelEnum.REPAYMENT.getCode().equals(cmdId)) {//自动扣款（还款）
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			bondPayOffManager.gatheringAfter(dto.getOrdId(), isSuccess, dto.getRespDesc());
		} else if (ThirdChannelEnum.TRANSFER.getCode().equals(cmdId)) {//自动扣款转账（商户用）
			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			//还款分账后订单处理
			if (MerPrivEnum.NORMAL_PAID.getCode().equals(dto.getMerPriv())) {
				bondPayOffManager.transferHFAfterForNormalpay(dto.getOrdId(), isSuccess, dto.getRespDesc());
			} else if (MerPrivEnum.PREPAID.getCode().equals(dto.getMerPriv())) {
				bondTransferManager.transferHFAfterForPrepaid(dto.getOrdId(), isSuccess, dto.getRespDesc());
			}

		} else if (ThirdChannelEnum.USERBINDCARD.getCode().equals(cmdId)) {//绑卡
			accountBankCardManager.bindCardWeb(dto);
		} else if (ThirdChannelEnum.USERLOGIN.getCode().equals(cmdId)) {//登陆

		} else if (ThirdChannelEnum.USERREGISTER.getCode().equals(cmdId)) {//注册
			capitalAccountManager.openCreditCapitalAccount(dto);
		} else if (ThirdChannelEnum.USRFREEZEBG.getCode().equals(cmdId)) {//资金冻结

			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}

		} else if (ThirdChannelEnum.USRUNFREEZE.getCode().equals(cmdId)) {//资金解冻

			if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {//返回码为空或为失败
				capitalAccountManager.updateCapital(dto);
			}
			//流标回调处理
			bondPackageInvestManager.UnFreezeBack(dto.getOrdId(), isSuccess, dto.getRespDesc());
		}
		//同步商户下的所有虚拟账户,添加时间20150413，保证所有交易都给账户进行一下同步
		capitalAccountManager.synchronizeMerAccount();
	}
}
