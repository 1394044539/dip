package com.wpy.dips.util;

import java.util.HashMap;
import java.util.Map;

//处理结果
public class ProcResult {

	private int code;//200-处理成功，300-处理失败
	private String msg;
	private Map<String,Object> data = new HashMap<String,Object>();
	
	public ProcResult(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public static ProcResult success() {
		return new ProcResult(200,"执行成功！");
	}
	
	public static ProcResult fail() {
		return new ProcResult(300,"执行失败！");
	}
	
	//绑定额外数据
	public ProcResult bind(String key,Object value) {
		this.data.put(key, value);
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
}
