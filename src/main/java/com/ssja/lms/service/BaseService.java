package com.ssja.lms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssja.lms.dao.InteractionsRepository;
import com.ssja.lms.dao.RequestStructureRepository;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.dto.ErrorResponse;
import com.ssja.lms.model.Interactions;
import com.ssja.lms.model.RequestStructure;
import com.ssja.lms.util.HttpResponseCode;
import com.ssja.lms.util.ResponseCodeConstants;
import com.ssja.lms.util.StringConstants;

@Service
public class BaseService implements BaseInterface {

	private static Logger logger = LoggerFactory.getLogger(BaseService.class);

	@Autowired
	RequestStructureRepository requestStructureRepository;

	@Autowired
	InteractionsRepository interactionsRepository;
	
	@Override
	public boolean validate(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		return validateParameters(commonRequest, commonResponse);
	}

	@Override
	public ApiCommonResponse processRequest(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		return null;
	}

	@Override
	public boolean validateParameters(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		logger.info("Inside validateParameters");

		boolean validationResult = true;

		Map<String, String> requestParameters = commonRequest.getRequestParameters();

		Map<String, String> invalidParams = new HashMap<>();

		Interactions interaction = interactionsRepository.findById(commonRequest.getInteractionId()).get();

		List<RequestStructure> requestStructureList = requestStructureRepository.findByInteractionId(interaction);

		if (requestStructureList == null) {
			return true;
		}

		for (RequestStructure requestStructure : requestStructureList) {

			String parameterName = requestStructure.getParameterId().getName();

			String parameterValue = requestParameters.get(parameterName);

			logger.debug(" Validating Parameter Name:{} Value:{}", parameterName, parameterValue);

			if ((parameterValue != null && parameterValue.trim().length() > 0) || requestStructure.isMandatory()) {

				if (parameterValue == null || parameterValue.trim().length() == 0) {

					invalidParams.put(requestStructure.getParameterId().getRefName(),
							StringConstants.REQUIRED.getName());

					validationResult = false;

				} else if (!requestStructure.isValidParameter(parameterValue, commonResponse,
						requestStructure.getParameterId().getParameterTypeId().getId())) {

					invalidParams.put(requestStructure.getParameterId().getRefName(),
							StringConstants.INVALID_PARAMETER_VALUE.getName() + " "
									+ requestStructure.getParameterId().getRefName()
									+ commonResponse.getValidationErrorMessage());

					validationResult = false;
				}

			}

		}

		if (!validationResult) {
			ErrorResponse error = new ErrorResponse();
			error.setInvalidParams(invalidParams);
			commonResponse.setResponseCode(String.valueOf(ResponseCodeConstants.BAD_REQUEST.getResponseCode()));
			commonResponse.setResponseCodeDesc(ResponseCodeConstants.BAD_REQUEST.getDescription());
			commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
			commonResponse.setError(error);
		}

		return validationResult;
	}

}
