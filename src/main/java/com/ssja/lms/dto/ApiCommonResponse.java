package com.ssja.lms.dto;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "status", "status_desc", "response_code", "response_code_desc", "data", "error" })
public class ApiCommonResponse {

	private int status;

	@JsonProperty("status_desc")
	private String statusDesc;

	@JsonProperty("response_code")
	private String responseCode;

	@JsonProperty("response_code_desc")
	private String responseCodeDesc;

	private Object data;

	private ErrorResponse error;

	@JsonIgnore
	private Integer httpStatusCode;
	@JsonIgnore
	private String validationErrorMessage;
	
	@JsonIgnore
	Map<String, Object> responseParameter = new HashMap<>();

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

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseCodeDesc() {
		return responseCodeDesc;
	}

	public void setResponseCodeDesc(String responseCodeDesc) {
		this.responseCodeDesc = responseCodeDesc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ErrorResponse getError() {
		return error;
	}

	public void setError(ErrorResponse error) {
		this.error = error;
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}

	public Map<String, Object> getResponseParameter() {
		return responseParameter;
	}

	public void setResponseParameter(Map<String, Object> responseParameter) {
		this.responseParameter = responseParameter;
	}
	
	

}
