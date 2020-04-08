package com.ssja.lms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;

@Service
public class UserService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Override
	public boolean validate(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		logger.info("Inside validate of {},", getClass().getName());
		return super.validate(commonRequest, commonResponse);
	}

	@Override
	public ApiCommonResponse processRequest(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		logger.info("Inside processRequest of {},", getClass().getName());

		if (!validate(commonRequest, commonResponse)) {
			return commonResponse;
		}

		switch (commonRequest.getInteractionType()) {

		case ADD_LIBRARIAN:
			addLibrarian(commonRequest, commonResponse);
			break;
		case GET_LIBRARIAN:
			getLibrarian(commonRequest, commonResponse);
			break;
		case DELETE_LIBRARIAN:
			deleteLibrarian(commonRequest, commonResponse);
			break;

		}

		return commonResponse;
	}

	private void deleteLibrarian(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

	}

	private void getLibrarian(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

	}

	private void addLibrarian(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

	}

}
