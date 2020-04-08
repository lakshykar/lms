package com.ssja.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssja.lms.util.InteractionType;

@Service
public class BaseFactory {

	@Autowired
	BaseService baseService;
	
	@Autowired
	UserService userService;

	public BaseService getService(InteractionType interactionType) {

		switch (interactionType) {

		case ADD_LIBRARIAN:
		case GET_LIBRARIAN:
		case DELETE_LIBRARIAN:
			return userService;

		default:
			return baseService;

		}

	}
}
