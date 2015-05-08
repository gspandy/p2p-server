package com.vcredit.jdev.p2p.merchant.modal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.capital.modal.CapitalPlatformManager;
import com.vcredit.jdev.p2p.dto.AccountWelfareDto;
import com.vcredit.jdev.p2p.entity.AccountWelfare;
import com.vcredit.jdev.p2p.entity.Welfare;
import com.vcredit.jdev.p2p.enums.AccountWelfareEnum;
import com.vcredit.jdev.p2p.repository.AccountWelfareRepository;
import com.vcredit.jdev.p2p.repository.WelfareRepository;

/**
 * 福利：红包的管理
 * 
 * @author 周佩
 *
 */
@Component
@MessageEndpoint
public class AccountWelfareManager {

	/**福利*/
	@Autowired
	private WelfareRepository welfareRepository;
	/**用户得到的福利*/
	@Autowired
	private AccountWelfareRepository accountWelfareRepository;
	@Autowired
	private CapitalPlatformManager capitalPlatformManager;

	private Page<AccountWelfareDto> formate(Page<AccountWelfare> result){
		List<AccountWelfare> list=result.getContent();
		List<AccountWelfareDto> res=new ArrayList<AccountWelfareDto>();
		for(AccountWelfare a:list){
			AccountWelfareDto d=new AccountWelfareDto();
			Welfare wf=welfareRepository.findOne(a.getWelfareSequence());
			try {
				BeanUtils.copyProperties(d, a);
				BeanUtils.copyProperties(d, wf);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			res.add(d);
		}
		Pageable pageRequest = new PageRequest(result.getNumber(),result.getSize());
		PageImpl<AccountWelfareDto> dto=new PageImpl<AccountWelfareDto>(res,pageRequest,result.getTotalElements());
		return dto;
	}
	
	/**
	 * 
	 * 通过当前用户下的所有福利检索数据分页方式
	 * 
	 * @param accountWelfare	红包对象
	 * @param page				页数
	 * @param size				数量
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Page<AccountWelfareDto> queryAccountWelfareByAccount(Long accountId,int page,int size) {
		//检查当前账户下所有的未兑付红包，将其更新
		checkAccountWelfare(accountId);
		Page<AccountWelfare> result= accountWelfareRepository.findByAccountSequence(accountId, new PageRequest(page-1, size));
		return formate(result);
	}
	
	/**
	 * 通过当前用户以及状态检索数据
	 * 
	 * @param accountWelfare
	 *            红包对象
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Page<AccountWelfareDto> queryAccountWelfareByAccountAndStatus(AccountWelfare accountWelfare,int page,int size) {
		//检查当前账户下所有的未兑付红包，将其更新
		checkAccountWelfare(accountWelfare.getAccountSequence());
		Page<AccountWelfare> result = accountWelfareRepository.findByAccountSequenceAndWelfareStatus(accountWelfare.getAccountSequence(),accountWelfare.getWelfareStatus(), new PageRequest(page-1, size));
		return formate(result);
	}
	
	/**
	 * 检查当前的账户福利是否有效
	 * @param awf
	 * @return
	 */
	private void checkAccountWelfare(Long accountId){
		List<AccountWelfare> parameter=accountWelfareRepository.findByAccountSequenceAndWelfareStatus(accountId,AccountWelfareEnum.NO_EXCHANGE.getCode());
		//更新当前用户下的红包状态
		for(int i=parameter.size()-1;i>=0;i--){
			AccountWelfare awf=parameter.get(i);
			Welfare wf=welfareRepository.findOne(awf.getWelfareSequence());
			if(wf.getWelfareExpiredDate().compareTo(new Date())<0&&awf.getWelfareStatus().equals(AccountWelfareEnum.NO_EXCHANGE.getCode())){//失效而且红包未兑换
				awf.setWelfareStatus(AccountWelfareEnum.EXPIRED.getCode());
				awf.setWelfareConsumDate(new Date());
				accountWelfareRepository.save(awf);
			}
		}
	}
	
	/**
	 * 赠送红包
	 * @param accountId		账户Id
	 * @param welfareName	福利名称
	 * @param welfareSource 福利来源
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void giveWelfare(Long accountId,Long welfareId,Integer welfareSource){
		Welfare w=welfareRepository.findOne(welfareId);
		//验证福利是否存在
		if(w!=null){
			//验证福利数量是否在数量范围内
			Integer currentCount=w.getWelfareCurrentCount();
			if(currentCount>0){
				//新增一个红包
				AccountWelfare awf = new AccountWelfare();
				awf.setAccountSequence(accountId);
				awf.setWelfareSequence(w.getWelfareSequence());
				awf.setWelfareEarnDate(new Date());
				awf.setWelfareSource(welfareSource);
				awf.setWelfareStatus(AccountWelfareEnum.NO_EXCHANGE.getCode());
				accountWelfareRepository.save(awf);
			}
			//更新福利数量
			currentCount--;
			w.setWelfareCurrentCount(currentCount);
			welfareRepository.save(w);
		}
	}
	
	/**
	 * 红包兑换
	 * 
	 * @param accountWelfare
	 *            红包对象
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountWelfare exchangeAccountWelfare(Long accountId,Long accountWelfareId,Integer welfareConsume) {
		AccountWelfare awf=accountWelfareRepository.findOne(accountWelfareId);
		if(awf==null||!accountId.equals(awf.getAccountSequence())){
			return null;
		}
		Integer status=AccountWelfareEnum.NO_EXCHANGE.getCode();//未兑换
		if(awf.getWelfareStatus().equals(AccountWelfareEnum.NO_EXCHANGE.getCode())){//未兑换
			Welfare wf=welfareRepository.findOne(awf.getWelfareSequence());
			if(wf.getWelfareExpiredDate().compareTo(new Date())>=0){//消费的时间大于当前时间表示红包有效

				//调用三方接口将红包兑换
				//*********************
//				capitalPlatformManager.transfer2User();
				//*********************
				status=AccountWelfareEnum.EXCHANGE.getCode();//已兑换
			}else{//过期
				//红包无效更新状态为失效
				status=AccountWelfareEnum.EXPIRED.getCode();//失效
			}
			awf.setWelfareStatus(status);//福利状态
			awf.setWelfareConsume(welfareConsume);//消费行为
			awf.setWelfareConsumDate(new Date());
			//发送消息更新红包状态
			//修改当前用户下的红包状态
			accountWelfareRepository.save(awf);
		}
		return awf;
	}
	
}
