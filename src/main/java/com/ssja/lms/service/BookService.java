package com.ssja.lms.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssja.lms.dao.RoleRepository;
import com.ssja.lms.dao.UserRepository;
import com.ssja.lms.dao.UserRoleMappingRepository;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.dto.UserDto;
import com.ssja.lms.model.Role;
import com.ssja.lms.model.User;
import com.ssja.lms.model.UserRoleMapping;
import com.ssja.lms.util.ConfigurationConstants;
import com.ssja.lms.util.HttpResponseCode;
import com.ssja.lms.util.ParameterConstants;
import com.ssja.lms.util.ResponseCodeConstants;
import com.ssja.lms.util.RoleConstants;
import com.ssja.lms.util.UserTypeConstants;

@Service
public class BookService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(BookService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Override
	public boolean validate(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		logger.info("Inside validate of {},", getClass().getName());
		return super.validate(commonRequest, commonResponse);
	}

	@Override
	@Transactional
	public ApiCommonResponse processRequest(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		logger.info("Inside processRequest of {},", getClass().getName());

		if (!validate(commonRequest, commonResponse)) {
			return commonResponse;
		}

		switch (commonRequest.getInteractionType()) {

		case ADD_BOOK:
			break;
		case VIEW_BOOKS:
			break;
		case ISSUE_BOOK:
			break;
		case VIEW_ISSUED_BOOK:
			break;
		case RETURN_ISSUED_BOOK:
			break;

		}

		return commonResponse;
	}

	@Transactional
	private void addLibrarian(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		logger.info("Inside addLibrarian");
		try {
			User user = userRepository
					.findByUsername(commonRequest.getRequestParameters().get(ParameterConstants.USERNAME.getName()));
			if (user == null) {
				user = new User();
				user.setUserType(UserTypeConstants.LIBRARIAN.getUserType());
				user.setName(commonRequest.getRequestParameters().get(ParameterConstants.NAME.getName()));
				user.setMobile(commonRequest.getRequestParameters().get(ParameterConstants.MOBILE.getName()));
				user.setUsername(commonRequest.getRequestParameters().get(ParameterConstants.USERNAME.getName()));
				user.setEmail(commonRequest.getRequestParameters().get(ParameterConstants.EMAIL.getName()));
				user.setPassword(new BCryptPasswordEncoder()
						.encode(commonRequest.getRequestParameters().get(ParameterConstants.PASSWORD.getName())));
				user.setStatus(ConfigurationConstants.ACTIVE.getId());
				em.persist(user);

				Role role = roleRepository.findById(RoleConstants.ROLE_LIBRARIAN.getRole()).get();

				UserRoleMapping userRoleMapping = new UserRoleMapping();
				userRoleMapping.setUserId(user);
				userRoleMapping.setRoleId(role);
				userRoleMapping.setStatus(ConfigurationConstants.ACTIVE.getId());
				em.persist(userRoleMapping);

				commonResponse.getResponseParameter().put(ParameterConstants.USER_ID.getName(), user.getId());
				commonResponse.getResponseParameter().put(ParameterConstants.USERNAME.getName(), user.getUsername());
				commonResponse.getResponseParameter().put("message", "User added");

				commonResponse.setResponseCode(ResponseCodeConstants.USER_ADDED.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			} else {
				logger.info("Username already taken");
				commonResponse.getResponseParameter().put("message",
						ResponseCodeConstants.USER_NAME_TAKEN.getDescription());
				commonResponse.setResponseCode(ResponseCodeConstants.USER_NAME_TAKEN.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}
	}

}
