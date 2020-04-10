package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum IssuedBookStatusConstants {

	ISSUED(1, "Issued"), 
	RETURNED(2, "Returned"),
	;

	private int status;

	private String description;

	private IssuedBookStatusConstants(int status, String description) {
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

	public static IssuedBookStatusConstants getFromInt(int status) throws InvalidValueException {
		for (IssuedBookStatusConstants candidate : IssuedBookStatusConstants.values()) {
			if (candidate.getStatus() == status) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid status Id : " + status);
	}

}
