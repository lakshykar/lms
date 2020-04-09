package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum UserTypeConstants {

	ADMIN(1, "Admin"),
	LIBRARIAN(2, "Librarian"),
	STUDENT(3, "Student"),
	;
	
	private int userType;

	private String description;

	private UserTypeConstants(int userType, String description) {
		this.userType = userType;
		this.description = description;
	}

	

	public int getUserType() {
		return userType;
	}



	public void setUserType(int userType) {
		this.userType = userType;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public static UserTypeConstants getFromInt(int userType) throws InvalidValueException {
		for (UserTypeConstants candidate : UserTypeConstants.values()) {
			if (candidate.getUserType() == userType) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid user Id : " + userType);
	}

}
