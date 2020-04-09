package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum StatusConstants {

	ACTIVE(1, "Active"), 
	INVACTIVE(0, "Inactive"),
	;

	private int status;

	private String description;

	private StatusConstants(int status, String description) {
		this.status = status;
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	private void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	public static StatusConstants getFromInt(int status) throws InvalidValueException {
		for (StatusConstants candidate : StatusConstants.values()) {
			if (candidate.getStatus() == status) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid status Id : " + status);
	}

}
