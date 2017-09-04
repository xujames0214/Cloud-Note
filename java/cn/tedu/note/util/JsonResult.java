package cn.tedu.note.util;

import java.io.Serializable;

public class JsonResult implements Serializable {
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	
	private int state;
	private String message;
	private Object data;
	
	public JsonResult(){}

	public JsonResult(String error){
		state = ERROR;
		this.message = error;
	}
	
	public JsonResult(Object data){
		state = SUCCESS;
		this.data = data;
	}
	
	public JsonResult(Throwable e){
		state = ERROR;
		message = e.getMessage();
	}
	
	public JsonResult(int state, Throwable e){
		this.state = state;
		this.message = e.getMessage();
	}
	public JsonResult(boolean b){
		if(b){
			this.state = SUCCESS;
		}else{
			this.state = ERROR;
		}
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static int getSuccess() {
		return SUCCESS;
	}

	public static int getError() {
		return ERROR;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	
}
