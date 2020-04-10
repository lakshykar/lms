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
	
	@Autowired
	BookService bookService;
	

	public BaseService getService(InteractionType interactionType) {

		switch (interactionType) {

		case ADD_LIBRARIAN:
		case GET_LIBRARIAN:
		case DELETE_LIBRARIAN:
		case ADD_STUDENT:
		case GET_STUDENT:
			return userService;
		
		case ADD_BOOK:
		case VIEW_BOOKS:
		case VIEW_ISSUED_BOOK:
		case RETURN_ISSUED_BOOK:
		case ISSUE_BOOK:
		case UPDATE_BOOK:
			return bookService;

		default:
			return baseService;

		}

	}
}
