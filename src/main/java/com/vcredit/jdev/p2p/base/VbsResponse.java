package com.vcredit.jdev.p2p.base;

public class VbsResponse extends Response{
	
	/** 请求的总条数  */
	private int totalCount;
	/** 成功处理条数  */
	private int successCount;
	/** 失败条数 */
	private int errCount;
	
	public VbsResponse(String status, String msg, Object data, int totalCount, int successCount, int errCount) {
		super(status, msg, data);
		this.totalCount = totalCount;
		this.successCount = successCount;
		this.errCount = errCount;
	}

	public int getTotalCount() {
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
	}
	
	public static VbsResponse response(String status, String msg, int totalCount, int successCount, int errCount, Object data){
		return new VbsResponse(status, msg, data,totalCount,successCount,errCount);
	}
	

}
