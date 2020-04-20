package com.ssja.lms;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssja.lms.dao.BookRepository;
import com.ssja.lms.dao.UserRepository;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.model.Book;
import com.ssja.lms.model.User;
import com.ssja.lms.service.RequestProcessor;
import com.ssja.lms.util.InteractionType;
import com.ssja.lms.util.ParameterConstants;
import com.ssja.lms.util.UserTypeConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Book Tests")
class BookTests {

	ObjectMapper mapper = new ObjectMapper();
	Map<String, String> requestParameters;
	ApiCommonRequest commonRequest;
	TestReporter reporter;
	UserDetails applicationUser;

	@MockBean
	UserRepository userRepository;

	@MockBean
	BookRepository bookRepository;

	@BeforeEach
	void init(TestReporter reporter) {
		this.reporter = reporter;
		requestParameters = new HashMap<>();
		commonRequest = new ApiCommonRequest();

		this.applicationUser = mock(UserDetails.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Autowired
	RequestProcessor requestProcessor;

	@Nested
	@DisplayName("Add Book Tests")
	class AddBookTest{
		
		@Test
		@DisplayName("Add book Test with missing parameters")
		void addBookWithMissingParametersTest() {

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.ADD_BOOK.getInteractionId());

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
		@DisplayName("Add book Test with duplicate ISBN")
		void addBookWithDuplicateISBNParametersTest() {

			requestParameters.put("isbn", "1000110012");
			requestParameters.put("author", "rohit");
			requestParameters.put("title", "math");
			requestParameters.put("publisher", "122dfe");
			requestParameters.put("location", "self 20");
			requestParameters.put("number_of_copies", "20");

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.ADD_BOOK.getInteractionId());

			when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
			when(applicationUser.getUsername()).thenReturn("rohit");

			User user = new User("rohit", "9300489836", "abc@gmail.com", UserTypeConstants.LIBRARIAN.getUserType(), 1);

			Book book = new Book();
			book.setIsbn("1000110012");
			book.setAuthor("Rohit");
			book.setLocation("abc self");
			book.setNumberOfCopies(10);
			book.setPublisher("Pub");

			when(userRepository.findByUsername("rohit")).thenReturn(user);

			when(bookRepository.findByIsbn("1000110012")).thenReturn(book);
			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(1, response.getStatus(), () -> "Status should be failed"),
					() -> assertEquals("603", response.getResponseCode(), () -> "Status should be bad request 603"));

		}

		@Test
		@DisplayName("Add book Wih Correct parameters")
		void addBookTest() {

			requestParameters.put("isbn", "1000110012");
			requestParameters.put("author", "rohit");
			requestParameters.put("title", "math");
			requestParameters.put("publisher", "122dfe");
			requestParameters.put("location", "self 20");
			requestParameters.put("number_of_copies", "20");

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.ADD_BOOK.getInteractionId());

			when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
			when(applicationUser.getUsername()).thenReturn("rohit");

			User user = new User("Rohit", "9300489836", "abc@gmail.com", UserTypeConstants.LIBRARIAN.getUserType(), 1);

			Book book = new Book();
			book.setIsbn("1000110012");
			book.setAuthor("Rohit");
			book.setLocation("abc self");
			book.setNumberOfCopies(10);
			book.setPublisher("Pub");

			when(userRepository.findByUsername("rohit")).thenReturn(user);
			when(bookRepository.findByIsbn("1000110012")).thenReturn(null);
			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(0, response.getStatus(), () -> "Status should be success"),
					() -> assertEquals("200", response.getResponseCode(), () -> "Status should be bad request 200"));

		}
	}
	

	@Nested
	@DisplayName("Get Book Test")
	class GetBookTest {
		@Test
		@DisplayName("Get Book Test by ISBN")
		void getBookByISBNTest() {

			requestParameters.put(ParameterConstants.ISBN.getName(), "1000110012");
			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.VIEW_BOOKS.getInteractionId());

			Book book = new Book();
			book.setIsbn("1000110012");
			book.setAuthor("Rohit");
			book.setLocation("abc self");
			book.setNumberOfCopies(10);
			book.setPublisher("Pub");
			when(bookRepository.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName())))
					.thenReturn(book);

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			assertAll(() -> assertEquals(0, response.getStatus(), "Status should be 0"),
					() -> assertEquals("200", response.getResponseCode(), "Response code desc should be OK"));

		}

		@Test
		@DisplayName("Get Book Title")
		void getBookByTitleTest() {

			requestParameters.put(ParameterConstants.TITLE.getName(), "math");
			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.VIEW_BOOKS.getInteractionId());

			Book book = new Book();
			book.setIsbn("1000110012");
			book.setTitle("math");
			book.setAuthor("Rohit");
			book.setLocation("abc self");
			book.setNumberOfCopies(10);
			book.setPublisher("Pub");
			when(bookRepository
					.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.TITLE.getName())))
							.thenReturn(book);

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			assertAll(() -> assertEquals(0, response.getStatus(), "Status should be 0"),
					() -> assertEquals("200", response.getResponseCode(), "Response code desc should be OK"));

		}

		@Test
		@DisplayName("Get ALl book")
		void getAllBookTest() {

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.VIEW_BOOKS.getInteractionId());

			Book book = new Book();
			book.setIsbn("1000110012");
			book.setTitle("math");
			book.setAuthor("Rohit");
			book.setLocation("abc self");
			book.setNumberOfCopies(10);
			book.setPublisher("Pub");
			when(bookRepository.findAll()).thenReturn(Stream.of(book).collect(Collectors.toList()));

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			assertAll(() -> assertEquals(0, response.getStatus(), "Status should be 0"),
					() -> assertEquals("200", response.getResponseCode(), "Response code desc should be OK"));

		}

	}

}
