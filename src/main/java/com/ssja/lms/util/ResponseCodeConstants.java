package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

public enum ResponseCodeConstants {
	OK("200","OK",0,"Success"),
	CREATED("201","CREATED",0,"Success"),
	BAD_REQUEST("400","BAD_REQUEST",1,"Fail"),
	UNAUTHORIZED("401","UNAUTHORIZED",1,"Fail"),
	FORBIDDEN("403","FORBIDDEN",1,"Fail"),
	NOT_FOUND("404","NOT_FOUND",1,"Fail"),
	INTERNAL_SERVER_ERROR("500","INTERNAL_SERVER_ERROR",2,"Error"),
	USER_NOT_FOUND("600","User Not Found",1,"Fail"),
	USER_NAME_TAKEN("601","Username alredy taken",1,"Fail"),
	USER_ADDED("602","User added",0,"Success"),
	DUPLICATE_ISBN("603","Duplicate isbn",1,"Fail"),
	;

	private String responseCode;

	private String description;

	private int status;

	private String statusDesc;

	private ResponseCodeConstants(String responseCode, String description, int status, String statusDesc) {
		this.responseCode = responseCode;
		this.description = description;
		this.status = status;
		this.statusDesc = statusDesc;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public static ResponseCodeConstants getFromResposneCode(String responseCode) throws InvalidValueException {
		for (ResponseCodeConstants candidate : ResponseCodeConstants.values()) {
			if (candidate.getResponseCode().equals(responseCode)) {
				return candidate;
			}
		}
		throw new InvalidValueException("No Response Code :" + responseCode);
	}

}
