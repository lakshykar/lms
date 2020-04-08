package com.ssja.lms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.util.ConfigurationConstants;
import com.ssja.lms.util.ParameterTypeConstants;

/**
 * @Author Rohit Lakshykar
 *
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class RequestStructure implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "interaction_id")
	private Interactions interactionId;

	@ManyToOne
	@JoinColumn(name = "parameter_id")
	private Parameters parameterId;

	private Integer minLength;
	private Integer maxLength;

	@Column(name = "minValue", columnDefinition = "decimal", precision = 12, scale = 3)
	private Double minValue;

	@Column(name = "maxValue", columnDefinition = "decimal", precision = 12, scale = 3)
	private Double maxValue;

	private String regex;

	private int isRequired;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the interactionId
	 */
	public Interactions getInteractionId() {
		return interactionId;
	}

	/**
	 * @param interactionId
	 *            the interactionId to set
	 */
	public void setInteractionId(Interactions interactionId) {
		this.interactionId = interactionId;
	}

	/**
	 * @return the parameterId
	 */
	public Parameters getParameterId() {
		return parameterId;
	}

	/**
	 * @param parameterId
	 *            the parameterId to set
	 */
	public void setParameterId(Parameters parameterId) {
		this.parameterId = parameterId;
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength
	 *            the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength
	 *            the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minValue
	 */
	public Double getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public Double getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex
	 *            the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @return the isRequired
	 */
	public int getIsRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired
	 *            the isRequired to set
	 */
	public void setIsRequired(int isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isMandatory() {

		return this.isRequired == ConfigurationConstants.REQUIRED.getId() ? true : false;
	}

	public boolean isValidParameter(String value, ApiCommonResponse commonResponse, Integer parameterTypeId) {
		boolean res = true;

		if (!getParameterId().isValidParameter(value, commonResponse, parameterTypeId)) {
			return false;
		}
		
		if ((getMinLength() != null && value.length() < getMinLength())
				|| (getMaxLength() != null && value.length() > getMaxLength())) {
			res = false;
			commonResponse.setValidationErrorMessage(" Min Length " + getMinLength() + " Max Length " + getMaxLength());
		} else if (getRegex() != null && !Pattern.matches(getRegex(), value)) {
			res = false;
			commonResponse.setValidationErrorMessage("Value should match with regex " + getRegex());
		} else if ((getMinValue() != null && Double.parseDouble(value) < getMinValue())
				|| (getMaxValue() != null && Double.parseDouble(value) > getMaxValue())) {
			res = false;
			commonResponse.setValidationErrorMessage(" Min Value " + getMinLength() + " Max Value " + getMaxLength());
		}

		return res;
	}

}
