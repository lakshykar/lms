package com.ssja.lms.service;

import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;

public interface BaseInterface {

	boolean validate(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse);

	ApiCommonResponse processRequest(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse);

	boolean validateParameters(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse);

}
