package com.vcredit.jdev.p2p.base;


public class Response {
	private String status;
	private String msg;
	private Object data;
	
	
	public Response(String status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	/**
	 * 获取数据成功
	 * **/
	public static Response successResponse(Object data){
		return new Response(ResponseConstants.CommonCode.SUCCESS_CODE,ResponseConstants.CommonMessage.SUCCESS_MESSAGE,data);
	}
	
	public static Response successResponse(){
		return new Response(ResponseConstants.CommonCode.SUCCESS_CODE,ResponseConstants.CommonMessage.SUCCESS_MESSAGE,"");
	}
	
	/**
	 * 用户未登录
	 * **/
	public static Response noLoginResponse(){
		return new Response(ResponseConstants.CommonCode.NO_LOGIN,ResponseConstants.CommonMessage.NO_LOGIN_MESSAGE,"");
	}

	
	public static Response response(String status, String msg, Object data){
		return new Response(status, msg, data);
	}
	
	public static Response response(String status){
		return new Response(status, "", "");
	}

	public static Response response(String status, String msg){
		return new Response(status, msg,"");
	}



}
