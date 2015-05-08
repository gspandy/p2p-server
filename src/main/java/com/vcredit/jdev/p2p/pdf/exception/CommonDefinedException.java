package com.vcredit.jdev.p2p.pdf.exception;

import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;

/**
 * 错误码常量类
 * 
 * @author zhaopeijun
 *
 */
public class CommonDefinedException {
	public static CommonException SYSTEM_ERROR = new ErrorCodeException.CommonException("SYSTEM_ERROR", "系统内部错误");
	public static CommonException PDF_DATA_NOT_EXIST = new ErrorCodeException.CommonException("PDF_DATA_NOT_EXIST", "pdf合同数据不存在");
	public static CommonException PDF_CREATE_FAIL = new ErrorCodeException.CommonException("PDF_CREATE_FAIL", "pdf生成失败");
	public static CommonException PDF_UPLOAD_FAIL = new ErrorCodeException.CommonException("PDF_UPLOAD_FAIL", "pdf上传ftp失败");
	public static CommonException MAIL_SEND_FAIL = new ErrorCodeException.CommonException("MAIL_SEND_FAIL", "给借款人发送邮件失败");
	public static CommonException MSG_SEND_FAIL = new ErrorCodeException.CommonException("MSG_SEND_FAIL", "给借款人发送短信失败");
	public static CommonException MSG_SEND_FAIL_DETAIL1 = new ErrorCodeException.CommonException("MSG_SEND_FAIL_DETAIL1", "用户名不存在或邮箱为空");
	public static CommonException MSG_SEND_FAIL_DETAIL2 = new ErrorCodeException.CommonException("MSG_SEND_FAIL_DETAIL2", "借款人手机号不能为空");
}
