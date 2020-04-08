package com.ssja.lms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "status", "status_desc", "response_code", "response_code_desc", "response_status_id", "data",
		"error" })
public class ApiCommonResponse {

	private int status;

	@JsonProperty("status_desc")
	private String statusDesc;

	@JsonProperty("response_code")
	private String responseCode;

	@JsonProperty("response_code_desc")
	private String responseCodeDesc;

	@JsonProperty("response_status_id")
	private int responseStatusId;

	private Object data;

	private ErrorResponse error;

	@JsonIgnore
	private Integer httpStatusCode;
	@JsonIgnore
	private String ValidationErrorMessage;

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

	public int getResponseStatusId() {
		return responseStatusId;
	}

	public void setResponseStatusId(int responseStatusId) {
		this.responseStatusId = responseStatusId;
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
		return ValidationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		ValidationErrorMessage = validationErrorMessage;
	}

}
