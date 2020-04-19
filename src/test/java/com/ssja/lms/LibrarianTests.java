package com.ssja.lms;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssja.lms.dao.RoleRepository;
import com.ssja.lms.dao.UserRepository;
import com.ssja.lms.dao.UserRoleMappingRepository;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.model.Role;
import com.ssja.lms.model.User;
import com.ssja.lms.model.UserRoleMapping;
import com.ssja.lms.service.RequestProcessor;
import com.ssja.lms.util.ConfigurationConstants;
import com.ssja.lms.util.RoleConstants;
import com.ssja.lms.util.UserTypeConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Librarian Test")
class LibrarianTests {

	ObjectMapper mapper = new ObjectMapper();
	Map<String, String> requestParameters;
	ApiCommonRequest commonRequest;
	TestReporter reporter;

	@MockBean
	UserRepository userRepository;

	@MockBean
	RoleRepository roleRepository;

	@MockBean
	UserRoleMappingRepository userRoleMappingRepository;

	@BeforeEach
	void init(TestReporter reporter) {
		this.reporter = reporter;
		requestParameters = new HashMap<>();
		commonRequest = new ApiCommonRequest();
	}

	@Autowired
	RequestProcessor requestProcessor;

	
	@Test
	@DisplayName("Get Librarian Test")
	void getLibrarianTest() {
		commonRequest.setRequestParameters(requestParameters);
		commonRequest.setInteractionId(1001);

		when(userRepository.findByUserType(UserTypeConstants.LIBRARIAN.getUserType())).thenReturn(Stream.of(
				new User("Rohit", "9300489836", "abc@gmail.com", UserTypeConstants.LIBRARIAN.getUserType(), 1),
				new User("Rohit1", "9300489836", "abc@gmail.com", UserTypeConstants.LIBRARIAN.getUserType(), 1))
				.collect(Collectors.toList()));

		ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

		try {
			reporter.publishEntry(mapper.writeValueAsString(response));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertAll(() -> assertEquals(0, response.getStatus(), "Status should be 0"),
				() -> assertEquals("OK", response.getResponseCodeDesc(), "Response code desc should be OK"));

	}
	
	@Nested
	@DisplayName("Add Librarian Tests")
	class AddLibrarianTest {

		

		@Test
		@DisplayName("Add Librarian Test with missing parameters")
		void addLibrarianMissingParametersTest() {

			requestParameters.put("name", "rohit_lb4");
			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(1000);

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(1, response.getStatus(), () -> "Status should be failed"),
					() -> assertEquals("400", response.getResponseCode(), () -> "Status should be bad request 400"));

		}

		@Test
		@DisplayName("Add Librarian With duplicate username")
		void addLibrarianDuplicationUsernameTest() {

			requestParameters.put("name", "rohit_lb4");
			requestParameters.put("mobile", "9988777700");
			requestParameters.put("email", "xyz@gmail.com");
			requestParameters.put("username", "rohit_lb4");
			requestParameters.put("password", "rohit_lb4");
			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(1000);

			when(userRepository.findByUsername(requestParameters.get("username"))).thenReturn(
					new User("rohit_lb4", "9988777700", "xyz@gmail.com", UserTypeConstants.LIBRARIAN.getUserType(), 1));

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(1, response.getStatus(), () -> "Status should be failed"),
					() -> assertEquals("601", response.getResponseCode(), () -> "Status should be bad request 400"));

		}

		@Test
		@DisplayName("Add Librarian With Correct Details")
		void addLibrarianTest() {

			requestParameters.put("name", "rohit_lb4");
			requestParameters.put("mobile", "9988777700");
			requestParameters.put("email", "xyz@gmail.com");
			requestParameters.put("username", "rohit_lb4");
			requestParameters.put("password", "rohit_lb4");

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(1000);

			User user = new User();
			user.setId(1);
			user.setName("rohit_lb4");

			when(userRepository.findByUsername("rohit_lb4")).thenReturn(null);
			when(roleRepository.findById(RoleConstants.ROLE_LIBRARIAN.getRole())).thenReturn(Optional.of(new Role()));
			when(userRepository.save(user)).thenReturn(user);
			when(userRoleMappingRepository.save(new UserRoleMapping())).thenReturn(new UserRoleMapping());

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(0, response.getStatus(), () -> "Status should be Success"),
					() -> assertEquals("602", response.getResponseCode(), () -> "Status should be bad request OK"));

		}

	}
	
	@Test
	@DisplayName("Delete Librarian")
	void deleteLibrarianTest() {

		requestParameters.put("user_id", "3");

		commonRequest.setRequestParameters(requestParameters);
		commonRequest.setInteractionId(1002);

		User user = new User("rohit_ibl3", "749857395", "abc@gmail.com",
				UserTypeConstants.LIBRARIAN.getUserType(), 1);
		when(userRepository.findByIdAndUserTypeAndStatus(3, UserTypeConstants.LIBRARIAN.getUserType(),
				ConfigurationConstants.ACTIVE.getId()))
						.thenReturn(user);

		when(userRepository.save(user)).thenReturn(user);
		
		when(userRoleMappingRepository.findByUserId(user)).thenReturn(Stream.of(new UserRoleMapping()).collect(Collectors.toList()));
		
		when(userRoleMappingRepository.save(new UserRoleMapping())).thenReturn(new UserRoleMapping());

		ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

		try {
			reporter.publishEntry(mapper.writeValueAsString(response));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertAll(() -> assertEquals(0, response.getStatus(), () -> "Status should be Success"),
				() -> assertEquals("200", response.getResponseCode(), () -> "Status should be bad request OK"));

	}

}
