package com.ssja.lms;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import com.ssja.lms.dao.IssueBookRepository;
import com.ssja.lms.dao.UserRepository;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.model.Book;
import com.ssja.lms.model.IssuedBook;
import com.ssja.lms.model.User;
import com.ssja.lms.service.RequestProcessor;
import com.ssja.lms.util.InteractionType;
import com.ssja.lms.util.IssuedBookStatusConstants;
import com.ssja.lms.util.ParameterConstants;
import com.ssja.lms.util.UserTypeConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Book Issue Tests")
class BookIssueTests {

	ObjectMapper mapper = new ObjectMapper();
	Map<String, String> requestParameters;
	ApiCommonRequest commonRequest;
	TestReporter reporter;
	UserDetails applicationUser;
	User librarian;
	User student;
	Book book;
	IssuedBook issuedBook;

	@MockBean
	UserRepository userRepository;

	@MockBean
	BookRepository bookRepository;

	@MockBean
	IssueBookRepository issueBookRepository;

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

		User librarian = new User("rohit_lb4", "9988777700", "xyz@gmail.com", UserTypeConstants.LIBRARIAN.getUserType(),
				1);
		User student = new User("stud_001", "9988777700", "xyz@gmail.com", UserTypeConstants.STUDENT.getUserType(), 1);
		student.setIdCardNumber("STUD00001");
		student.setId(10);
		librarian.setId(2);
		this.librarian = librarian;
		this.student = student;

		Book book = new Book();
		book.setId(1001L);
		book.setIsbn("1000110012");
		book.setAuthor("Rohit");
		book.setLocation("abc self");
		book.setNumberOfCopies(10);
		book.setPublisher("Pub");

		this.book = book;

		IssuedBook issuedBook = new IssuedBook();
		issuedBook.setBookId(book);
		issuedBook.setIssuedBy(librarian);
		issuedBook.setCreatedAt(new Date());
		issuedBook.setIssuedTill(new Date());
		issuedBook.setUserId(student);
		this.issuedBook = issuedBook;

	}

	@Autowired
	RequestProcessor requestProcessor;

	@Nested
	@DisplayName("Issu Book Tests")
	class IssueBookTests {

		@Test
		@DisplayName("Issued book")
		void issueBookTest() {

			requestParameters.put(ParameterConstants.ICARD_NUMBER.getName(), "STUD00001");
			requestParameters.put(ParameterConstants.ISBN.getName(), "1000110012");
			requestParameters.put(ParameterConstants.RETURN_DATE.getName(), "2020-12-12");

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.ISSUE_BOOK.getInteractionId());

			when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
			when(applicationUser.getUsername()).thenReturn("rohit_lb4");
			when(userRepository.findByUsername("rohit_lb4")).thenReturn(librarian);
			when(bookRepository.findByIsbn(requestParameters.get(ParameterConstants.ISBN.getName()))).thenReturn(book);

			when(userRepository.findByIdCardNumber(
					commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName())))
							.thenReturn(student);

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(0, response.getStatus(), () -> "Status should be success"),
					() -> assertEquals("200", response.getResponseCode(), () -> "Status should be bad request 200"));

		}

		@Test
		@DisplayName("Issued book when book unavailable")
		void issueBookWhenBookUnavailableTest() {

			requestParameters.put(ParameterConstants.ICARD_NUMBER.getName(), "STUD00001");
			requestParameters.put(ParameterConstants.ISBN.getName(), "1000110012");
			requestParameters.put(ParameterConstants.RETURN_DATE.getName(), "2020-12-12");

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.ISSUE_BOOK.getInteractionId());

			book.setNumberOfCopies(0);
			when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

			when(applicationUser.getUsername()).thenReturn("rohit_lb4");

			when(userRepository.findByUsername("rohit_lb4")).thenReturn(librarian);

			when(bookRepository.findByIsbn(requestParameters.get(ParameterConstants.ISBN.getName()))).thenReturn(book);

			when(issueBookRepository.findByStatus(IssuedBookStatusConstants.ISSUED.getStatus()))
					.thenReturn(Stream.of(issuedBook).collect(Collectors.toList()));

			when(userRepository.findByIdCardNumber(
					commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName())))
							.thenReturn(student);

			when(bookRepository.save(book)).thenReturn(book);
			when(issueBookRepository.save(issuedBook)).thenReturn(issuedBook);

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(1, response.getStatus(), () -> "Status should be Fail"),
					() -> assertEquals("400", response.getResponseCode(), () -> "Status should be bad request 400"));

		}
	}

	@Test
	@DisplayName("Return Issued book")
	void returBookTest() {

		requestParameters.put(ParameterConstants.ISSUED_BOOK_ID.getName(), "1");
		commonRequest.setRequestParameters(requestParameters);
		commonRequest.setInteractionId(InteractionType.RETURN_ISSUED_BOOK.getInteractionId());

		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

		when(applicationUser.getUsername()).thenReturn("rohit_lb4");

		when(userRepository.findByUsername("rohit_lb4")).thenReturn(librarian);

		when(issueBookRepository.findByIdAndStatus(1, IssuedBookStatusConstants.ISSUED.getStatus()))
				.thenReturn(issuedBook);

		when(bookRepository.save(book)).thenReturn(book);
		when(issueBookRepository.save(issuedBook)).thenReturn(issuedBook);

		ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

		try {
			reporter.publishEntry(mapper.writeValueAsString(response));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		verify(bookRepository, times(1)).save(book);

		assertAll(() -> verify(bookRepository, times(1)).save(book),
				() -> verify(issueBookRepository, times(1)).save(issuedBook),
				() -> assertEquals(0, response.getStatus(), () -> "Status should be success"),
				() -> assertEquals("200", response.getResponseCode(), () -> "Status should be bad request 200"));

	}

	@Nested
	@DisplayName("View Issed Book Tessts")
	class ViewIssuedBook {
		@Test
		@DisplayName("View Issued book")
		void viewIssueBookTest() {

			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.VIEW_ISSUED_BOOK.getInteractionId());

			when(issueBookRepository.findByStatus(IssuedBookStatusConstants.ISSUED.getStatus()))
					.thenReturn(Stream.of(issuedBook).collect(Collectors.toList()));
			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(0, response.getStatus(), () -> "Status should be success"),
					() -> assertEquals("200", response.getResponseCode(), () -> "Status should be bad request 200"));

		}

		@Test
		@DisplayName("View Issued book with ISBN")
		void viewIssueBookWithISBNTest() {

			requestParameters.put(ParameterConstants.ISBN.getName(), "1000110012");
			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.VIEW_ISSUED_BOOK.getInteractionId());

			when(bookRepository.findByIsbn(requestParameters.get(ParameterConstants.ISBN.getName()))).thenReturn(book);
			when(issueBookRepository.findByBookIdAndStatus(book, IssuedBookStatusConstants.ISSUED.getStatus()))
					.thenReturn(Stream.of(issuedBook).collect(Collectors.toList()));

			ApiCommonResponse response = requestProcessor.processRequest(commonRequest);

			try {
				reporter.publishEntry(mapper.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			assertAll(() -> assertEquals(0, response.getStatus(), () -> "Status should be success"),
					() -> assertEquals("200", response.getResponseCode(), () -> "Status should be bad request 200"));

		}

		@Test
		@DisplayName("View Issued book with Id Card Number")
		void viewIssueBookWithIdCardNumberTest() {

			requestParameters.put(ParameterConstants.ISSUED_BOOK_ID.getName(), "STUD00001");
			commonRequest.setRequestParameters(requestParameters);
			commonRequest.setInteractionId(InteractionType.VIEW_ISSUED_BOOK.getInteractionId());

			when(userRepository.findByIdCardNumber(
					commonRequest.getRequestParameters().get(ParameterConstants.ISSUED_BOOK_ID.getName())))
							.thenReturn(student);

			when(issueBookRepository.findByUserIdAndStatus(student, IssuedBookStatusConstants.ISSUED.getStatus()))
					.thenReturn(Stream.of(issuedBook).collect(Collectors.toList()));

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

}
