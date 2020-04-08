package com.ssja.lms.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssja.lms.util.InteractionType;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiCommonRequest {

	private Integer interactionId;

	Map<String, String> requestParameters;

	@JsonIgnore
	InteractionType interactionType;

	/**
	 * @return the interactionId
	 */
	public Integer getInteractionId() {
		return interactionId;
	}

	/**
	 * @param interactionId
	 *            the interactionId to set
	 */
	public void setInteractionId(Integer interactionId) {
		this.interactionId = interactionId;
	}

	/**
	 * @return the requestParameters
	 */
	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	/**
	 * @param requestParameters
	 *            the requestParameters to set
	 */
	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public InteractionType getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(InteractionType interactionType) {
		this.interactionType = interactionType;
	}

}
