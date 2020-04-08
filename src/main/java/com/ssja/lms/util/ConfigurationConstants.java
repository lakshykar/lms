package com.ssja.lms.util;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum ConfigurationConstants {

	REQUIRED(1,"REQUIRED"),
	NOT_REQUIRED(0,"NOT REQUIRED"),
	SUCCESS(0,"Success"),
	FAIL(1,"Fail"),
	ERROR(2,"Error"),
	ACTIVE(1,"Active"),
	INACTIVE(0,"Inactive"),
	YES(1,"Yes"),
	NO(0,"No"),
	;
	
	private int id;

	private String description;

	private ConfigurationConstants(int id, String description) {
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

}

