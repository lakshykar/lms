package com.ssja.lms.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.service.RequestProcessor;
import com.ssja.lms.util.InteractionType;
import com.ssja.lms.util.ParameterConstants;
import com.ssja.lms.util.ResponseCodeConstants;

/**
 * @Author Rohit Lakshykar 24-Apr-2018 10:44:13 AM
 *
 */

@RestController
@RequestMapping("api/v1")
public class ApiRequestController {

	static final Logger logger = LoggerFactory.getLogger(ApiRequestController.class);

	public static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	RequestProcessor requestProcessor;

	@GetMapping(value = "/health")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> test(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = new ApiCommonResponse();
		response.setStatus(0);
		response.setResponseCode(ResponseCodeConstants.OK.getResponseCode());

		return ResponseEntity.ok().body(response);
	}

	@GetMapping(value = "/librarian")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> getLibrarian(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.GET_LIBRARIAN.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}

	@PostMapping(value = "/librarian")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> addLibrarian(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.ADD_LIBRARIAN.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	@PostMapping(value = "/student")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> addStudent(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.ADD_STUDENT.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	@GetMapping(value = "/student")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> getStudent(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.GET_STUDENT.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}

	@PutMapping(value = "/librarian/{user_id}/delete")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> deleteLibrarian(@RequestParam Map<String, String> requestParameters,
			@PathVariable("user_id") String userId, HttpServletRequest httpServletRequest) {
		requestParameters.put(ParameterConstants.USER_ID.getName(), userId);

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.DELETE_LIBRARIAN.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	@GetMapping(value = "/book")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> viewBooks(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.VIEW_BOOKS.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}

	@PostMapping(value = "/book")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> addBook(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.ADD_BOOK.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	@PutMapping(value = "/book/{isbn}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> addBook(@RequestParam Map<String, String> requestParameters,
			@PathVariable("isbn") String isbn,HttpServletRequest httpServletRequest) {
		
		requestParameters.put(ParameterConstants.ISBN.getName(), isbn);
		
		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.UPDATE_BOOK.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}

	
	@PostMapping(value = "/book/issue")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> addIssue(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.ISSUE_BOOK.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	@GetMapping(value = "/book/issue")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> viewIssueBook(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.VIEW_ISSUED_BOOK.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	@PutMapping(value = "/book/return")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ApiCommonResponse> returnIssuedBook(@RequestParam Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest) {

		ApiCommonResponse response = consumeAndProcessRequest(requestParameters, httpServletRequest,
				InteractionType.RETURN_ISSUED_BOOK.getInteractionId());

		return ResponseEntity.status(response.getHttpStatusCode()).body(response);
	}
	
	private ApiCommonResponse consumeAndProcessRequest(Map<String, String> requestParameters,
			HttpServletRequest httpServletRequest, int interactionId) {

		ApiCommonRequest request = new ApiCommonRequest();

		request.setRequestParameters(requestParameters);

		printJson(request, "Request");

		request.setInteractionId(interactionId);

		ApiCommonResponse response = requestProcessor.processRequest(request);

		printJson(response, "Response");
		return response;
	}

	private void printJson(Object obj, String type) {

		try {
			logger.info("{}: {}", type, mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}
	}

}
