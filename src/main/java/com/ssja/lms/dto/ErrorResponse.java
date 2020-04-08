package com.ssja.lms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	@JsonProperty("invalid_params")
	private Object invalidParams;

	private String reason;

	
	public ErrorResponse() {
		super();
	}

	/**
	 * @return the invalidParams
	 */
	public Object getInvalidParams() {
		return invalidParams;
	}

	/**
	 * @param invalidParams
	 *            the invalidParams to set
	 */
	public void setInvalidParams(Object invalidParams) {
		this.invalidParams = invalidParams;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
