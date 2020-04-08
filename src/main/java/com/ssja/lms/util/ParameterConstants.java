package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

public enum ParameterConstants {

	 INITIATOR (1,"initiator"),
	 DEVICETID (2,"deviceTID"),
	;
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
