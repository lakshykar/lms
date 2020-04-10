package com.ssja.lms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssja.lms.exception.InvalidValueException;
import com.ssja.lms.model.User;
import com.ssja.lms.util.StatusConstants;
import com.ssja.lms.util.UserTypeConstants;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

	String name;

	String email;

	String username;

	@JsonProperty("user_type")
	String userType;

	@JsonProperty("icard_number")
	String icardNumber;

	int status;

	@JsonProperty("status_desc")
	String statusDesc;

	public UserDto(User user) {
		super();
		this.name = user.getName();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.icardNumber = user.getIdCardNumber();
		try {
			this.userType = UserTypeConstants.getFromInt(user.getUserType()).getDescription();
		} catch (InvalidValueException e) {
		}
		this.status = user.getStatus();

		try {
			this.statusDesc = StatusConstants.getFromInt(user.getStatus()).getDescription();
		} catch (InvalidValueException e) {
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public String getIcardNumber() {
		return icardNumber;
	}

	public void setIcardNumber(String icardNumber) {
		this.icardNumber = icardNumber;
	}

}