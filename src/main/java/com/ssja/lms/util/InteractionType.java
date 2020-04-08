package com.ssja.lms.util;

import com.ssja.lms.exception.InvalidValueException;

/**
 * @Author Rohit Lakshykar 3-Sep-2019
 *
 */

public enum InteractionType {

	ADD_LIBRARIAN(1000, "Add Librarian"),
	GET_LIBRARIAN(1002, "Get Librarian"),
	DELETE_LIBRARIAN(1003, "Delete Librarian"),
	;
	
	private int interactionId;

	private String description;

	private InteractionType(int interactionId, String description) {
		this.interactionId = interactionId;
		this.description = description;
	}

	/**
	 * @return the interactionId
	 */
	public int getInteractionId() {
		return interactionId;
	}

	/**
	 * @param interactionId
	 *            the interactionId to set
	 */
	void setInteractionId(int interactionId) {
		this.interactionId = interactionId;
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

	public static InteractionType getFromInt(int interactionId) throws InvalidValueException {
		for (InteractionType candidate : InteractionType.values()) {
			if (candidate.getInteractionId() == interactionId) {
				return candidate;
			}
		}
		throw new InvalidValueException("Invalid Interaction Id : " + interactionId);
	}

}