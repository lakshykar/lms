package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

public enum StringConstants {

	EXCEPTION("Exception"),
	RUNTIMEEXCEPTION("RuntimeException"),
	SOURCE("source"),
	REQUEST("Request"),
	RESPONSE("Response"),
	REQUIRED("Required"),
	INVALID_PARAMETER_VALUE("Invalid value of parameter"),
;
	private String name;

	private StringConstants(String name) {
		this.name = name;
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

	public static StringConstants getFromString(String name) throws InvalidValueException {

		for (StringConstants candidate : StringConstants.values()) {

			if (candidate.getName().equalsIgnoreCase(name)) {
				return candidate;
			}
		}

		throw new InvalidValueException("No String Constant With Name :" + name);
	}

}
