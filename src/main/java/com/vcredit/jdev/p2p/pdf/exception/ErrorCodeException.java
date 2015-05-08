package com.vcredit.jdev.p2p.pdf.exception;

/**
 * 自定义异常
 * @author zhaopeijun
 *
 */
public class ErrorCodeException extends Exception {

	private static final long serialVersionUID = 4005358808809902012L;

	/** 异常码 */
	private String errorCode;
	/** 异常信息 */
	private String errorMsg;
	/** 异常详细 */
	private String detail;

	public ErrorCodeException(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public ErrorCodeException(String errorCode, String errorMsg, String detail) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.detail = detail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public static class CommonException extends ErrorCodeException {

		private static final long serialVersionUID = -3004891288021568474L;

		public CommonException(String errorCode, String errorMsg) {
			super(errorCode, errorMsg);
		}

	}

}
