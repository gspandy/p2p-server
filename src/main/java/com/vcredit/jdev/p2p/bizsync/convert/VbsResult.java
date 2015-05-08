package com.vcredit.jdev.p2p.bizsync.convert;

import java.util.ArrayList;

public class VbsResult {
	
/*	*//** 请求的总条数  *//*
	private int totalCount;
	*//** 成功处理条数  *//*
	private int successCount;
	*//** 失败条数 *//*
	private int errCount;*/
	/** 逐条错误信息列表 */
	private ArrayList<VbsDetailRs> detailErrList;
	/** 逐条成功信息列表 */
	private ArrayList<VbsDetailRs> detailSuccessList;

	public ArrayList<VbsDetailRs> getDetailErrList() {
		return detailErrList;
	}
	public void setDetailErrList(ArrayList<VbsDetailRs> detailErrList) {
		this.detailErrList = detailErrList;
	}
	public ArrayList<VbsDetailRs> getDetailSuccessList() {
		return detailSuccessList;
	}
	public void setDetailSuccessList(ArrayList<VbsDetailRs> detailSuccessList) {
		this.detailSuccessList = detailSuccessList;
	}
	/*public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getErrCount() {
		return errCount;
	}
	public void setErrCount(int errCount) {
		this.errCount = errCount;
	}*/
	
}
