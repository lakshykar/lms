package com.ssja.lms.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.exception.InvalidValueException;
import com.ssja.lms.util.ConfigurationConstants;
import com.ssja.lms.util.HttpResponseCode;
import com.ssja.lms.util.InteractionType;
import com.ssja.lms.util.ResponseCodeConstants;

@Service
public class RequestProcessor {

	public static final ObjectMapper mapper = new ObjectMapper();

	protected static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);

	@Autowired
	BaseFactory baseFactory;

	@PersistenceContext
	EntityManager em;

	public ApiCommonResponse processRequest(ApiCommonRequest commonRequest) {

		ApiCommonResponse commonResponse = new ApiCommonResponse();

		logger.info("Inside processRequest");
		try {

			InteractionType interactionType = InteractionType.getFromInt(commonRequest.getInteractionId());
			commonRequest.setInteractionType(interactionType);

		} catch (InvalidValueException e) {
			logger.error("InvalidValueException", e);

			commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
			commonResponse.setResponseCodeDesc("Invalid Interaction");

			return commonResponse;
		} catch (Exception e) {
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setResponseCodeDesc(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getDescription());
			return commonResponse;
		}

		try {

			BaseService service = baseFactory.getService(commonRequest.getInteractionType());

			commonResponse = service.processRequest(commonRequest, commonResponse);

		} catch (Exception e) {
			logger.error("Exception", e);

			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			buildResponse(commonRequest, commonResponse);
		}

		return commonResponse;
	}

	private void buildResponse(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		try {
			ResponseCodeConstants responseCode = ResponseCodeConstants
					.getFromResposneCode(commonResponse.getResponseCode());
			commonResponse.setResponseCodeDesc(responseCode.getDescription());
			commonResponse.setStatus(responseCode.getStatus());
			commonResponse.setStatusDesc(responseCode.getStatusDesc());

		} catch (InvalidValueException e) {
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setResponseCodeDesc(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getDescription());
			commonResponse.setStatus(ConfigurationConstants.ERROR.getId());
			commonResponse.setStatusDesc(ConfigurationConstants.ERROR.getDescription());
		}

	}

}
