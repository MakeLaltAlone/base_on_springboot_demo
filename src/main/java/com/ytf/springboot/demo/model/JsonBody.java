package com.ytf.springboot.demo.model;

public class JsonBody {
	private int status;
	private Object data = "";
	private String errMsg = "";
	private int total = 0;
	public JsonBody(int status, Object data) {
		this.status = status;
		this.data = data;
	}
	
	public JsonBody(){}
	
	public JsonBody(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public static JsonBody success(Object data){
		return new JsonBody(0, data);
	}

	public static JsonBody success(Object data, int total){
		JsonBody body = new JsonBody();
		body.setStatus(0);
		body.setData(data);
		body.setTotal(total);
		return body;
	}

	public static JsonBody fail(String retMsg){
		JsonBody body = new JsonBody(retMsg);
		body.setStatus(-1);
		return body;
	}
}
