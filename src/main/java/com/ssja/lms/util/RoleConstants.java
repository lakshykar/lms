package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum RoleConstants {

	ROLE_ADMIN(100, "Admin Role"),
	ROLE_LIBRARIAN(200, "Librarian Role"),
	;
	
	private int role;

	private String description;

	private RoleConstants(int role, String description) {
		this.role = role;
		this.description = description;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static RoleConstants getFromInt(int role) throws InvalidValueException {
		for (RoleConstants candidate : RoleConstants.values()) {
			if (candidate.getRole() == role) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid role Id : " + role);
	}

}
