package com.ssja.lms.model;

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssja.lms.dto.ApiCommonResponse;

/**
 * @Author Rohit Lakshykar
 *
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Parameters implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String name;

	@ManyToOne
	@JoinColumn(name = "parameter_type_id")
	private ParameterTypes parameterTypeId;

	private String refName;

	private Integer minLength;
	private Integer maxLength;

	@Column(name = "minValue", columnDefinition = "decimal", precision = 12, scale = 3)
	private Double minValue;

	@Column(name = "maxValue", columnDefinition = "decimal", precision = 12, scale = 3)
	private Double maxValue;

	private String regex;

	private String description;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parameterTypeId
	 */
	public ParameterTypes getParameterTypeId() {
		return parameterTypeId;
	}

	/**
	 * @param parameterTypeId
	 *            the parameterTypeId to set
	 */
	public void setParameterTypeId(ParameterTypes parameterTypeId) {
		this.parameterTypeId = parameterTypeId;
	}

	/**
	 * @return the refName
	 */
	public String getRefName() {
		return refName;
	}

	/**
	 * @param refName
	 *            the refName to set
	 */
	public void setRefName(String refName) {
		this.refName = refName;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isValidParameter(String value, ApiCommonResponse commonResponse, Integer parameterType) {

		boolean res = true;

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
