package com.ssja.lms.service;

import java.util.ArrayList;
import java.util.List;

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
public class UserService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

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
		case ADD_STUDENT:
			addStudent(commonRequest, commonResponse);
			break;
		case GET_STUDENT:
			getStudent(commonRequest, commonResponse);
			break;
		default:
			break;

		}

		return commonResponse;
	}

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
				userRepository.save(user);
				//em.persist(user);

				Role role = roleRepository.findById(RoleConstants.ROLE_LIBRARIAN.getRole()).get();

				UserRoleMapping userRoleMapping = new UserRoleMapping();
				userRoleMapping.setUserId(user);
				userRoleMapping.setRoleId(role);
				userRoleMapping.setStatus(ConfigurationConstants.ACTIVE.getId());
				userRoleMappingRepository.save(userRoleMapping);

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

	private void deleteLibrarian(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		User user = userRepository.findByIdAndUserTypeAndStatus(
				Integer.parseInt(commonRequest.getRequestParameters().get(ParameterConstants.USER_ID.getName())),
				UserTypeConstants.LIBRARIAN.getUserType(), ConfigurationConstants.ACTIVE.getId());

		if (user != null) {

			user.setStatus(ConfigurationConstants.INACTIVE.getId());

			userRepository.save(user);

			List<UserRoleMapping> userRoles = userRoleMappingRepository.findByUserId(user);

			if (userRoles != null && !userRoles.isEmpty()) {
				userRoles.forEach(userRole -> {
					userRole.setStatus(ConfigurationConstants.INACTIVE.getId());
					userRoleMappingRepository.save(userRole);
				});
			}
			commonResponse.getResponseParameter().put(ParameterConstants.USER_ID.getName(), user.getId());
			commonResponse.getResponseParameter().put(ParameterConstants.USERNAME.getName(), user.getUsername());
			commonResponse.getResponseParameter().put("message", "user has been deleted");
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
		} else {
			commonResponse.getResponseParameter().put("message", "No Active librarian found with provided user id");
			commonResponse.setResponseCode(ResponseCodeConstants.USER_NOT_FOUND.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
		}

	}

	private void getLibrarian(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		try {
			List<User> users = new ArrayList<>();

			if (commonRequest.getRequestParameters().get(ParameterConstants.USER_ID.getName()) != null) {
				User user = userRepository.findByIdAndUserType(
						Integer.parseInt(
								commonRequest.getRequestParameters().get(ParameterConstants.USER_ID.getName())),
						UserTypeConstants.LIBRARIAN.getUserType());

				users.add(user);
			} else {
				users = userRepository.findByUserType(UserTypeConstants.LIBRARIAN.getUserType());
			}

			List<UserDto> userList = new ArrayList<>();

			if (users != null && !users.isEmpty()) {
				users.forEach(user -> userList.add(new UserDto(user)));
			}

			if (userList.isEmpty()) {
				commonResponse.getResponseParameter().put("meessage", "No librarian found");
			} else {
				commonResponse.getResponseParameter().put("user_list", userList);
			}
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());

		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}

	}

	private void getStudent(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		try {
			List<User> users = new ArrayList<>();

			if (commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName()) != null) {
				User user = userRepository.findByIdCardNumberAndUserType(
						commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName()),
						UserTypeConstants.STUDENT.getUserType());

				users.add(user);
			} else {
				users = userRepository.findByUserType(UserTypeConstants.STUDENT.getUserType());
			}

			List<UserDto> userList = new ArrayList<>();

			if (users != null && !users.isEmpty()) {
				users.forEach(user -> userList.add(new UserDto(user)));
			}

			if (userList.isEmpty()) {
				commonResponse.getResponseParameter().put("meessage", "No User found");
			} else {
				commonResponse.getResponseParameter().put("user_list", userList);
			}
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());

		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}

	}

	private void addStudent(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		logger.info("Inside addLibrarian");
		try {
			User user = userRepository
					.findByUsername(commonRequest.getRequestParameters().get(ParameterConstants.USERNAME.getName()));
			if (user == null) {
				user = new User();
				user.setUserType(UserTypeConstants.STUDENT.getUserType());
				user.setName(commonRequest.getRequestParameters().get(ParameterConstants.NAME.getName()));
				user.setMobile(commonRequest.getRequestParameters().get(ParameterConstants.MOBILE.getName()));
				user.setUsername(commonRequest.getRequestParameters().get(ParameterConstants.USERNAME.getName()));
				user.setEmail(commonRequest.getRequestParameters().get(ParameterConstants.EMAIL.getName()));
				user.setPassword(new BCryptPasswordEncoder()
						.encode(commonRequest.getRequestParameters().get(ParameterConstants.PASSWORD.getName())));
				user.setStatus(ConfigurationConstants.ACTIVE.getId());
				userRepository.save(user);
				user.setIdCardNumber("STUD00" + user.getId());
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
