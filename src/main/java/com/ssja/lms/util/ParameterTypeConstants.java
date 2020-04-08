package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum ParameterTypeConstants {

	NUMERIC(1,"NUMERIC"),
	TEXT(2,"TEXT"),
	LIST(3,"LIST"),
	;
	
	private int id;

	private String description;

	private ParameterTypeConstants(int id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	void setId(int id) {
		this.id = id;
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
	void setDescription(String description) {
		this.description = description;
	}

	public static ParameterTypeConstants getFromInt(int id) throws InvalidValueException {
		for (ParameterTypeConstants candidate : ParameterTypeConstants.values()) {
			if (candidate.getId() == id) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid Parameter Type: "+id);
	}

	public static ParameterTypeConstants getFromString(String des) throws InvalidValueException {

		for (ParameterTypeConstants candidate : ParameterTypeConstants.values()) {
			if (candidate.getDescription().equalsIgnoreCase(des)) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid Parameter Name: "+des);
	}
}

