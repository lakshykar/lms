package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

public enum ParameterConstants {

	USER_ID(1,"user_id"),
	NAME(2,"name"),
	MOBILE(3,"mobile"),
	USERNAME(4,"username"),
	PASSWORD(5,"password"),
	USER_TYPE(6,"user_type"),
	STATUS(7,"status"),
	EMAIL(8,"email"),
	TITLE(9,"title"),
	ISBN(10,"isbn"),
	LOCATION(11,"location"),
	AUTHOR(12,"author"),
	NUMBER_OF_COPIES(13,"number_of_copies"),
	PUBLISHER(14,"publisher"),
	LIMIT(15,"limit"),
	OFFSET(16,"offset"),
	ICARD_NUMBER(17,"icard_number"),
	RETURN_DATE(18,"return_date"),
	ISSUED_BOOK_ID(19,"issued_book_id");
	

	private int parameterId;

	private String name;

	private ParameterConstants(int parameterId, String name) {
		this.parameterId = parameterId;
		this.name = name;
	}

	/**
	 * @return the parameterId
	 */
	public int getParameterId() {
		return parameterId;
	}

	/**
	 * @param parameterId
	 *            the parameterId to set
	 */
	void setParameterId(int parameterId) {
		this.parameterId = parameterId;
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
	void setName(String name) {
		this.name = name;
	}

	public static ParameterConstants getFromInt(int parameterId) throws InvalidValueException {
		for (ParameterConstants candidate : ParameterConstants.values()) {
			if (candidate.getParameterId() == parameterId) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid Parameter Id :" + parameterId);
	}

	public static ParameterConstants getFromString(String name) throws InvalidValueException {

		for (ParameterConstants candidate : ParameterConstants.values()) {
			if (candidate.getName().equalsIgnoreCase(name)) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid Parameter :" + name);
	}

}
