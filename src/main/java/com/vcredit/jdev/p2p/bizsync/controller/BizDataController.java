package com.vcredit.jdev.p2p.bizsync.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.VbsResponse;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.bizsync.convert.BillInfoDto;
import com.vcredit.jdev.p2p.bizsync.convert.LoanQueryInfo;
import com.vcredit.jdev.p2p.bizsync.convert.VbsCreateDto;
import com.vcredit.jdev.p2p.bizsync.convert.VbsDetailRs;
import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryDto;
import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryResult;
import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryRsDetail;
import com.vcredit.jdev.p2p.bizsync.convert.VbsResult;
import com.vcredit.jdev.p2p.bizsync.model.BizDataManager;
import com.vcredit.jdev.p2p.dto.BondPayOffManagerMessageDto;
import com.vcredit.jdev.p2p.dto.LoanDataList;
import com.vcredit.jdev.p2p.entity.LoanCut;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.enums.BizQueryStatusEnum;
import com.vcredit.jdev.p2p.enums.BizSyncEnum;
import com.vcredit.jdev.p2p.enums.LoanDataRecordStatusEnum;
import com.vcredit.jdev.p2p.enums.VbsBillType;

/**
 * VBS和P2P之间同步数据
 * 
 * @author zhaopeijun
 *
 */
@Path("/bizsync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BizDataController {

	@Autowired
	private BizDataManager bizDataManager;

	@Autowired
	private AsyncEventMessageGateway asyncEventMessageGateway;
	
	private static Logger logger = LoggerFactory.getLogger(BizDataController.class);
	
	/**
	 * 贷前数据同步导入
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/dataimport")
	public VbsResponse create(Map<String, Object> paramMap) {
		ObjectMapper objectMapper = new ObjectMapper();
		int totalCount = 0;
		int errCount = 0;
		int successCount = 0; //成功入库数
		VbsResult rs = new VbsResult();
		ArrayList<VbsDetailRs> detailErrList = new ArrayList<VbsDetailRs>();
		ArrayList<VbsDetailRs> detailSuccessList = new ArrayList<VbsDetailRs>();
		VbsDetailRs detailRs = new VbsDetailRs();
		ArrayList<LoanData> cvtDatas = new ArrayList<LoanData>(); //成功入库的数据对象；
		LoanDataList eventDatas = new LoanDataList();

		try {
			//接受vbs 参数，syncType:1 单条-实时，2 批量-常规
			String syncType = paramMap.get(ConstantsUtil.SyncParam.SYNC_TYPE).toString();

			if (BizSyncEnum.sync_instant.getCode().equals(syncType)) {
				totalCount = 1;
				VbsCreateDto dto = new VbsCreateDto();
				String dtoJson;
				detailRs = new VbsDetailRs();
				LoanData loanData = new LoanData();
				try {
					HashMap<String, Map<String, Object>> dataMap = null;
					Object data = paramMap.get(ConstantsUtil.SyncParam.SYNC_DATA);
					if(data instanceof ArrayList){ //VBS应该传过来单条数据
						dataMap = (HashMap<String, Map<String, Object>>) PropertyUtils.getProperty(data, "[0]");
					}else{
						dataMap = (HashMap<String, Map<String, Object>>)data;
					}
					dtoJson = objectMapper.writeValueAsString(dataMap);
					dto = P2pUtil.getBeanFromJson(dtoJson, VbsCreateDto.class);
				} catch (Exception e) {
					e.printStackTrace();
					return parseJsonErrorResponse(rs);
				}

				//数据业务效验  记录正确及错误的记录信息 --todo
				if (!bizDataManager.checkBeforeLoanData(dto, detailErrList)) {
					try {
						//dto转换实体效验
						loanData = bizDataManager.changeDto2bean(dto,syncType);
					} catch (Exception e) {
						e.printStackTrace();
						errCount++;
						detailRs.setLoanBusinessId(dto.getBorrowInfo().getInvestmentBusinessCode());
						detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_811);
						detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_811 + e.getMessage());
						detailErrList.add(detailRs);
					}

					//入库
					LoanData rsData;
					try {
						if (null != loanData) {
							rsData = bizDataManager.dataImport(loanData);
							successCount++;
							detailRs.setLoanBusinessId(rsData.getInvestmentBusinessCode());
							detailSuccessList.add(detailRs);
							cvtDatas.add(rsData);
						}
					} catch (Exception e) {
						e.printStackTrace();
						errCount++;
						detailRs.setLoanBusinessId(loanData.getInvestmentBusinessCode());
						detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_822);
						detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_822 + e.getMessage());
						detailErrList.add(detailRs);
					}
				} else {
					errCount++;
				}
			} else if (BizSyncEnum.sync_batch.getCode().equals(syncType)) {
				String dtoListJson;
				List<VbsCreateDto> dtoBeans;
				try {
					ArrayList<VbsCreateDto> dtoList = (ArrayList<VbsCreateDto>) paramMap.get(ConstantsUtil.SyncParam.SYNC_DATA);
					dtoListJson = objectMapper.writeValueAsString(dtoList);
					dtoBeans = P2pUtil.getBeansFromJson(dtoListJson, VbsCreateDto.class);
					totalCount = dtoBeans.size();
				} catch (Exception e) {
					e.printStackTrace();
					return parseJsonErrorResponse(rs);
				}

				//数据业务效验  记录正确及错误的记录信息 --todo
				ArrayList<VbsCreateDto> checkedDtos = new ArrayList<VbsCreateDto>();
				for (VbsCreateDto dto : dtoBeans) {
					if (!bizDataManager.checkBeforeLoanData(dto, detailErrList)) {
						checkedDtos.add(dto);
					} else {
						errCount++;
					}
				}

				//dto转换实体效验
				ArrayList<LoanData> list = new ArrayList<LoanData>();
				for (VbsCreateDto dto : checkedDtos) {
					LoanData data = new LoanData();
					detailRs = new VbsDetailRs();
					try {
						data = bizDataManager.changeDto2bean(dto,syncType);
						list.add(data);
					} catch (Exception e) {
						errCount++;
						detailRs.setLoanBusinessId(dto.getBorrowInfo().getInvestmentBusinessCode());
						detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_811);
						detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_811 + e.getMessage());
						detailErrList.add(detailRs);
						continue;
					}
				}

				//入库
				int success = 0;
				for (LoanData data : list) {
					detailRs = new VbsDetailRs();
					try {
						LoanData d = bizDataManager.dataImport(data);
						success++;
						detailRs.setLoanBusinessId(d.getInvestmentBusinessCode());
						detailSuccessList.add(detailRs);
						cvtDatas.add(d);
					} catch (Exception e) {
						errCount++;
						detailRs.setLoanBusinessId(data.getInvestmentBusinessCode());
						detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_822);
						detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_822 + e.getMessage());
						detailErrList.add(detailRs);
						continue;
					}
				}
				successCount = success;

			}
		} catch (Exception e) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_830,
					ResponseConstants.CommonMessage.RESPONSE_MSG_830 + e.getMessage(), 0, 0, 0, null);
		}

		//设置反馈结果至VBS
		rs.setDetailErrList(detailErrList);
		rs.setDetailSuccessList(detailSuccessList);

		// 发布event begin
		if(cvtDatas.size() > 0){
			eventDatas.setLoanDatas(cvtDatas);
			asyncEventMessageGateway.publishEvent(eventDatas);
		}
		// 发布event end

		if (successCount == totalCount) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_800, ResponseConstants.CommonMessage.RESPONSE_MSG_800, totalCount,
					successCount, errCount, rs);
		} else if (successCount == 0) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_820, ResponseConstants.CommonMessage.RESPONSE_MSG_820, totalCount,
					successCount, errCount, rs);
		} else {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_810, ResponseConstants.CommonMessage.RESPONSE_MSG_810, totalCount,
					successCount, errCount, rs);
		}
	}

	/**
	 * 解析jsonStr出错
	 * 
	 * @param totalCount
	 * @param rs
	 * @return
	 */
	private VbsResponse parseJsonErrorResponse(VbsResult rs) {
		VbsDetailRs detailRs = new VbsDetailRs();
		ArrayList<VbsDetailRs> detailErrList = new ArrayList<VbsDetailRs>();
		detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_821);
		detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_821);
		detailErrList.add(detailRs);
		rs.setDetailErrList(detailErrList);
		return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_820, ResponseConstants.CommonMessage.RESPONSE_MSG_820, 0, 0, 0, rs);
	}
	
	/**
	 * 解析贷前查询jsonStr出错
	 * 
	 * @param totalCount
	 * @param rs
	 * @return
	 */
	private VbsResponse parseJsonBeforeLoanErrorResponse(VbsQueryResult rs) {
		VbsQueryRsDetail detailRs = new VbsQueryRsDetail();
		ArrayList<VbsQueryRsDetail> detailErrList = new ArrayList<VbsQueryRsDetail>();
		detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_821);
		detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_821);
		detailErrList.add(detailRs);
		rs.setDetailErrList(detailErrList);
		return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_820, ResponseConstants.CommonMessage.RESPONSE_MSG_820, 0, 0, 0, rs);
	}

	/**
	 * 贷后数据同步导入
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/afterloanImport")
	public VbsResponse afterloanImport(Map<String, Object> paramMap) {
		BondPayOffManagerMessageDto eventDto = new BondPayOffManagerMessageDto();
		ObjectMapper objectMapper = new ObjectMapper();
		int totalCount = 0;
		int errCount = 0;
		int successCount = 0; //成功入库数
		VbsResult rs = new VbsResult();
		ArrayList<VbsDetailRs> detailErrList = new ArrayList<VbsDetailRs>();
		ArrayList<VbsDetailRs> detailSuccessList = new ArrayList<VbsDetailRs>();
		VbsDetailRs detailRs = new VbsDetailRs();
		ArrayList<LoanCut> conDatas = new ArrayList<LoanCut>(); //成功入库的数据对象;

		try {
			String dtoListJson;
			List<BillInfoDto> dtoBeans;
			try {
				ArrayList<BillInfoDto> dtoList = (ArrayList<BillInfoDto>) paramMap.get(ConstantsUtil.SyncParam.SYNC_DATA);
				dtoListJson = objectMapper.writeValueAsString(dtoList);
				dtoBeans = P2pUtil.getBeansFromJson(dtoListJson, BillInfoDto.class);
				totalCount = dtoBeans.size();
			} catch (Exception e) {
				e.printStackTrace();
				return parseJsonErrorResponse(rs);
			}

			//数据业务效验  记录正确及错误的记录信息 --todo
			ArrayList<BillInfoDto> checkedDtos = new ArrayList<BillInfoDto>();
			for (BillInfoDto dto : dtoBeans) {
				if (!bizDataManager.checkAfterLoanData(dto, detailErrList)) {
					checkedDtos.add(dto);
				} else {
					errCount++;
				}
			}

			//dto转换实体效验
			ArrayList<LoanCut> list = new ArrayList<LoanCut>();
			for (BillInfoDto dto : checkedDtos) {
				LoanCut data = new LoanCut();
				detailRs = new VbsDetailRs();
				try {
					data = bizDataManager.changeDto2beanAfterLoan(dto);
					list.add(data);
				} catch (Exception e) {
					errCount++;
					detailRs.setLoanBusinessId(dto.getInvestmentBusinessCode());
					detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_811);
					detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_811 + e.getMessage());
					detailRs.setLoanPeriod(dto.getLoanPeriod());
					detailErrList.add(detailRs);
					continue;
				}
			}

			//入库
			int success = 0;
			for (LoanCut data : list) {
				detailRs = new VbsDetailRs();
				try {
					//更新效验
					String businessCode = data.getInvestmentBusinessCode();
					int loanPeriod = data.getLoanPeriod();
					LoanCut loanCut = bizDataManager.getLoanCutByBusinessCodeAndLoanPeriod(businessCode, loanPeriod);
					LoanCut d = new LoanCut();
					if (null != loanCut) {
						//提前账单，对于相同业务编号记录 只接受1次，不做更新，记录错误处理。
						if (VbsBillType.advanced_bill.getCode().equals(loanCut.getBillType())) {
							/*errCount++;
							detailRs.setLoanBusinessId(data.getInvestmentBusinessCode());
							detailRs.setLoanPeriod(0);
							detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_814);
							detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_814);
							detailErrList.add(detailRs);
							continue;*/
							//3.13 讨论后更新业务 
							//对于提前清贷账单，或者普通账单，以 业务编码,期数 为复合唯一主键，没有做插入，存在做更新。
							data.setLoanCutSequence(loanCut.getLoanCutSequence());
							d = bizDataManager.dataImportAfterLoan(data);
						} else if (VbsBillType.usual_bill.getCode().equals(loanCut.getBillType())) {
							/*// 2015.03.02 普通账单也不能做重复更新，但需要给VBS返回提示：该普通账单已被处理过，不需要重复推送
							errCount++;
							detailRs.setLoanBusinessId(data.getInvestmentBusinessCode());
							detailRs.setLoanPeriod(data.getLoanPeriod());
							detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_816);
							detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_816);
							detailErrList.add(detailRs);
							continue;*/
							//3.13 讨论后更新业务 
							//对于提前清贷账单，或者普通账单，以 业务编码,期数 为复合唯一主键，没有做插入，存在做更新。
							data.setLoanCutSequence(loanCut.getLoanCutSequence());
							d = bizDataManager.dataImportAfterLoan(data);
						}
					} else {
						//新增
						d = bizDataManager.dataImportAfterLoan(data);
					}
					success++;
					detailRs.setLoanBusinessId(d.getInvestmentBusinessCode());
					detailRs.setLoanPeriod(d.getLoanPeriod());
					detailSuccessList.add(detailRs);
					conDatas.add(d);
				} catch (Exception e) {
					errCount++;
					detailRs.setLoanBusinessId(data.getInvestmentBusinessCode());
					detailRs.setLoanPeriod(data.getLoanPeriod());
					detailRs.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_822);
					detailRs.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_822 + e.getMessage());
					detailErrList.add(detailRs);
					continue;
				}
			}
			successCount = success;
		} catch (Exception e) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_830,
					ResponseConstants.CommonMessage.RESPONSE_MSG_830 + e.getMessage(), 0, 0, 0, null);
		}

		//返回结果至VBS
		/*
		 * rs.setErrCount(errCount); rs.setTotalCount(totalCount);
		 * rs.setSuccessCount(successCount);
		 */
		rs.setDetailErrList(detailErrList);
		rs.setDetailSuccessList(detailSuccessList);

		// 发布event begin
		eventDto.setLoanCutList(conDatas);
		logger.info("bizdata推送 贷后导入数据发布event,条数："+conDatas.size());
		asyncEventMessageGateway.publishEvent(eventDto);
		// 发布event end
		
		logger.info("bizdata推送 贷后导入数据发布event end");
		if (successCount == totalCount) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_800, ResponseConstants.CommonMessage.RESPONSE_MSG_800, totalCount,
					successCount, errCount, rs);
		} else if (successCount == 0) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_820, ResponseConstants.CommonMessage.RESPONSE_MSG_820, totalCount,
					successCount, errCount, rs);
		} else {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_810, ResponseConstants.CommonMessage.RESPONSE_MSG_810, totalCount,
					successCount, errCount, rs);
		}
		
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/beforeloanQuery")
	public VbsResponse beforeloanQuery(Map<String, Object> paramMap) {
		ObjectMapper objectMapper = new ObjectMapper();
		int totalCount = 0; //数据库返回数据，去重后的总数
		int errCount = 0;
		int successCount = 0;
		HashMap<String, Object> conMap = new HashMap<String, Object>();
		List<VbsQueryDto> lendResults;
		List<VbsQueryDto> filterResults = new ArrayList<VbsQueryDto>();
		VbsQueryResult rs = new VbsQueryResult();
		ArrayList<VbsQueryRsDetail> detailErrList = new ArrayList<VbsQueryRsDetail>();
		ArrayList<VbsQueryRsDetail> detailSuccessList = new ArrayList<VbsQueryRsDetail>();
		VbsQueryRsDetail detail = new VbsQueryRsDetail();
		String[] strArr;
		HashMap<String, Integer> duplicateBizIdMap = new HashMap<String, Integer>(); //返回数据中含有重复业务id 的数据
//		HashMap<String, Object> noBizIdMap = new HashMap<String, Object>();// 返回数据中不包含 查询条件中业务id 的数据
		boolean isPattern = true;
		
		try {
			String dtoListJson;
			List<LoanQueryInfo> dtoBeans;
			try {
				ArrayList<LoanQueryInfo> dtoList = (ArrayList<LoanQueryInfo>) paramMap.get(ConstantsUtil.SyncParam.LOAN_LIST);
				dtoListJson = objectMapper.writeValueAsString(dtoList);
				dtoBeans = P2pUtil.getBeansFromJson(dtoListJson, LoanQueryInfo.class);
				strArr = new String[dtoBeans.size()];
				for (int i = 0; i < dtoBeans.size(); i++) {
					strArr[i] = dtoBeans.get(i).getLoanbId();
					conMap.put(strArr[i], "");
				}

			} catch (Exception e) {
				e.printStackTrace();
				return parseJsonBeforeLoanErrorResponse(rs);
			}
			
			lendResults = bizDataManager.getVbsQueryList(strArr);
			//过滤重复的返回数据
			//判定查询数据 和 p2p返回数据 是否 逐一匹配 
			for(VbsQueryDto dto : lendResults){
				String bId = dto.getInvestmentBusinessCode();
				if(conMap.containsKey(bId)){ //返回结果中的业务id 在查询条件map内 存在 
					/*if(duplicateBizIdMap.containsKey(bId)){ //存在多次
						duplicateBizIdMap.put(bId, duplicateBizIdMap.get(bId)+1);
						isPattern = false;
					}else{ //存在一次
						duplicateBizIdMap.put(bId, 1);
					}*/
					//数据库返回数据 按业务编码、项目创建日期（倒序），只取重复的第1条
					if(!duplicateBizIdMap.containsKey(bId)){
						duplicateBizIdMap.put(bId, 1);
						filterResults.add(dto);
					}else{
						duplicateBizIdMap.put(bId, duplicateBizIdMap.get(bId)+1);
					}
					
				}else{ //返回结果中 出现不存在 条件集合中的情况,理论不可能
//					noBizIdMap.put(dto.getInvestmentBusinessCode(), "");
//					isPattern = false;
				}
			}
			
			//查询条件中有业务编号id，返回结果中没有返回相对匹配的数据
			Iterator iterator2 = conMap.keySet().iterator();
			while(iterator2.hasNext()) {
				String key = iterator2.next().toString();
				if(!duplicateBizIdMap.containsKey(key)){
					isPattern = false;
				}
			}
			//
			
			
			totalCount = filterResults.size();
			for(VbsQueryDto dto : filterResults){
				detail = new VbsQueryRsDetail();
				BeanUtils.copyProperties(detail, dto);
				
				//数据未处理
				if(StringUtils.isEmpty(dto.getInvestmentNumber())){
					detail.setInvestmentStatus(BizQueryStatusEnum.PROCESS_FAIL.getCode());
					detail.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_852);
					detail.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_852);
					detailErrList.add(detail);
					errCount++;
					continue;
				}
				
				//数据已处理
				if(null != dto.getInvestmentStartDate()){
					detail.setInvestmentStartDateStr(DateFormatUtil.dateToString(dto.getInvestmentStartDate(), DateFormatUtil.VBS_DATE_FMT));
				}
				if(null != dto.getInvestmentCreditDate()){
					detail.setInvestmentCreditDateStr(DateFormatUtil.dateToString(dto.getInvestmentCreditDate(), DateFormatUtil.VBS_DATE_FMT));
				}
				if(null != dto.getInvestmentFillDate()){
					detail.setInvestmentFillDateStr(DateFormatUtil.dateToString(dto.getInvestmentFillDate(), DateFormatUtil.VBS_DATE_FMT));
				}
				if(null != dto.getInvestmentLostDate()){
					detail.setInvestmentLostDateStr(DateFormatUtil.dateToString(dto.getInvestmentLostDate(), DateFormatUtil.VBS_DATE_FMT));
				}
				
				if(detail.getRecordStatus().equals(LoanDataRecordStatusEnum.RIGHT.getCode())){
					//数据处理正常
					if(ConstantsUtil.INV2VBS_STATUS1_MAP.containsKey(detail.getInvestmentStatus().toString())){
						detail.setInvestmentStatus(BizQueryStatusEnum.RELEASE_WAIT.getCode()); //待发布 1
					}else if(ConstantsUtil.INV2VBS_STATUS2_MAP.containsKey(detail.getInvestmentStatus().toString())){
						detail.setInvestmentStatus(BizQueryStatusEnum.ON_LINE.getCode()); //招标中 2
					}else if(ConstantsUtil.INV2VBS_STATUS3_MAP.containsKey(detail.getInvestmentStatus().toString())){
						detail.setInvestmentStatus(BizQueryStatusEnum.TENDER_FAIL.getCode()); //已流标 3
					}else if(ConstantsUtil.INV2VBS_STATUS4_MAP.containsKey(detail.getInvestmentStatus().toString())){
						detail.setInvestmentStatus(BizQueryStatusEnum.TENDER_FINISH.getCode()); //已满标 4
					}else if(ConstantsUtil.INV2VBS_STATUS5_MAP.containsKey(detail.getInvestmentStatus().toString())){
						detail.setInvestmentStatus(BizQueryStatusEnum.RELEASE_CASH_FINISH.getCode()); //已放款 5
					}
					detailSuccessList.add(detail);
					successCount++;
				}else if(detail.getRecordStatus().equals(LoanDataRecordStatusEnum.ERROR.getCode())){
					//数据处理异常
					detail.setInvestmentStatus(BizQueryStatusEnum.RELEASE_CASH_FINISH.getCode());
					detail.setParseErrCode(ResponseConstants.CommonCode.RESPONSE_CODE_853);
					detail.setParseErrMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_853);
					detailErrList.add(detail);
					errCount++;
				}
			}
			
			
		} catch (Exception e) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_830,
					ResponseConstants.CommonMessage.RESPONSE_MSG_830 + e.getMessage(), 0, 0, 0, null);
		}
		
		rs.setDetailErrList(detailErrList);
		rs.setDetailSuccessList(detailSuccessList);

		if (successCount == totalCount) {
			if(isPattern){
				return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_801, ResponseConstants.CommonMessage.RESPONSE_MSG_801, totalCount,
						successCount, errCount, rs);	
			}else{
				return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_804, getNotPatternMsg(duplicateBizIdMap,conMap), totalCount,
						successCount, errCount, rs);	
			}
			
		} else if (successCount == 0) {
			return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_802, ResponseConstants.CommonMessage.RESPONSE_MSG_802, totalCount,
					successCount, errCount, rs);
		} else {
			if(isPattern){
				return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_803, ResponseConstants.CommonMessage.RESPONSE_MSG_803, totalCount,
						successCount, errCount, rs);
			}else{
				return VbsResponse.response(ResponseConstants.CommonCode.RESPONSE_CODE_804, getNotPatternMsg(duplicateBizIdMap,conMap), totalCount,
						successCount, errCount, rs);	
			}
		}
		
	}
	
	private String getNotPatternMsg(HashMap<String, Integer> duplicateBizIdMap,HashMap<String, Object> conMap){
		//获取返回结果中的业务id 在查询条件map内 存在多次
		String rtnMsg = "";
		rtnMsg += "查询业务编码数量："+conMap.size();
		
		// vbs不需要知道 p2p这边的重复数据情况
/*		String dupString = "";
		Iterator iterator = duplicateBizIdMap.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next().toString();
			Integer value = duplicateBizIdMap.get(key);
			if(value > 1){
				dupString += key + " ";
			}
		}
		rtnMsg += "; 返回结果中存在重复多次的业务id："+dupString;*/
		
		/*//返回结果中的业务id 在查询条件map内不存在
		String noBizString = "";
		Iterator it = noBizIdMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next().toString();
			noBizString += key + " ";
		}
		rtnMsg += " ；返回结果在查询条件中不存在的业务id："+noBizString;*/
		
		//查询条件中有，返回结果中没有
		String noConBizIdStr = "";
		Iterator iterator2 = conMap.keySet().iterator();
		while(iterator2.hasNext()) {
			String key = iterator2.next().toString();
			if(!duplicateBizIdMap.containsKey(key)){
				noConBizIdStr += key + " ";
			}
		}
		rtnMsg += "; 查询条件中有、返回结果中没有的业务id："+noConBizIdStr;
		
		return rtnMsg;
	}

}
