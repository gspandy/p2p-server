package com.vcredit.jdev.p2p.bizsync.convert;

import java.util.ArrayList;

public class VbsQueryResult {
	
	/** 逐条错误信息列表 */
	private ArrayList<VbsQueryRsDetail> detailErrList;
	/** 逐条成功信息列表 */
	private ArrayList<VbsQueryRsDetail> detailSuccessList;

	public ArrayList<VbsQueryRsDetail> getDetailErrList() {
		return detailErrList;
	}
	public void setDetailErrList(ArrayList<VbsQueryRsDetail> detailErrList) {
		this.detailErrList = detailErrList;
	}
	public ArrayList<VbsQueryRsDetail> getDetailSuccessList() {
		return detailSuccessList;
	}
	public void setDetailSuccessList(ArrayList<VbsQueryRsDetail> detailSuccessList) {
		this.detailSuccessList = detailSuccessList;
	}
	
}
