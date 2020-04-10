package com.ssja.lms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ssja.lms.dao.BookRepository;
import com.ssja.lms.dao.IssueBookRepository;
import com.ssja.lms.dao.UserRepository;
import com.ssja.lms.dto.ApiCommonRequest;
import com.ssja.lms.dto.ApiCommonResponse;
import com.ssja.lms.dto.BookDto;
import com.ssja.lms.dto.IssuedBookDto;
import com.ssja.lms.model.Book;
import com.ssja.lms.model.IssuedBook;
import com.ssja.lms.model.User;
import com.ssja.lms.util.HttpResponseCode;
import com.ssja.lms.util.IssuedBookStatusConstants;
import com.ssja.lms.util.MasterLocks;
import com.ssja.lms.util.ParameterConstants;
import com.ssja.lms.util.ResponseCodeConstants;
import com.ssja.lms.util.StatusConstants;

@Service
public class BookService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(BookService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	IssueBookRepository issueBookRepository;

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

		boolean acquired = false;
		ReentrantLock lock = null;
		String lockKey = null;

		switch (commonRequest.getInteractionType()) {

		case ADD_BOOK:
			addBook(commonRequest, commonResponse);
			break;
		case UPDATE_BOOK:
			updateBook(commonRequest, commonResponse);
			break;
		case VIEW_BOOKS:
			viewBooks(commonRequest, commonResponse);
			break;
		case ISSUE_BOOK:
			lockKey = commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName());
			try {
				MasterLocks.bookLock.putIfAbsent(lockKey, new ReentrantLock(true));
				lock = MasterLocks.bookLock.get(lockKey);
				acquired = lock.tryLock(10000, TimeUnit.MILLISECONDS);
				if (acquired) {
					issueBook(commonRequest, commonResponse);
				} else {
					commonResponse.getResponseParameter().put("message",
							"Another request in progress, try after sometime");
					commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
					commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
				}
			} catch (Exception e) {
				logger.error("Exception", e);
				commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
			} finally {
				if (acquired) {
					lock.unlock();
					MasterLocks.bookLock.remove(lockKey);
				}
			}

			break;
		case VIEW_ISSUED_BOOK:
			viewIssueBook(commonRequest, commonResponse);
			break;
		case RETURN_ISSUED_BOOK:

			lockKey = commonRequest.getRequestParameters().get(ParameterConstants.ISSUED_BOOK_ID.getName());
			try {
				logger.info("lock key {} ",lockKey);
				MasterLocks.issuedBookLock.putIfAbsent(lockKey, new ReentrantLock(true));
				lock = MasterLocks.issuedBookLock.get(lockKey);
				acquired = lock.tryLock(10000, TimeUnit.MILLISECONDS);
				if (acquired) {
					returnIssueBook(commonRequest, commonResponse);
				} else {
					commonResponse.getResponseParameter().put("message",
							"Another request in progress, try after sometime");
					commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
					commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
				}
			} catch (Exception e) {
				logger.error("Exception", e);
				commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
			} finally {
				if (acquired) {
					lock.unlock();
					MasterLocks.issuedBookLock.remove(lockKey);
				}
			}

			break;

		default:
			break;

		}

		return commonResponse;
	}

	private void returnIssueBook(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		try {
			String username;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}

			logger.info("User name :{}", username);

			User revciver = userRepository.findByUsername(username);
			if (revciver == null || revciver.getStatus() != StatusConstants.ACTIVE.getStatus()) {
				commonResponse.getResponseParameter().put("message", "Invalid issuer/inactive");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			IssuedBook issuedBook = issueBookRepository.findByIdAndStatus(
					Long.parseLong(
							commonRequest.getRequestParameters().get(ParameterConstants.ISSUED_BOOK_ID.getName())),
					IssuedBookStatusConstants.ISSUED.getStatus());

			if (issuedBook == null) {
				commonResponse.getResponseParameter().put("message", "Invalid issued book id");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			issuedBook.setStatus(IssuedBookStatusConstants.RETURNED.getStatus());
			issuedBook.setReceivedBy(revciver);
			issuedBook.setReturnDate(new Date());

			Book book = issuedBook.getBookId();
			book.setNumberOfCopies(book.getNumberOfCopies() + 1);

			em.persist(issuedBook);
			em.persist(book);

			commonResponse.getResponseParameter().put(ParameterConstants.ISBN.getName(), book.getIsbn());
			commonResponse.getResponseParameter().put(ParameterConstants.TITLE.getName(), book.getTitle());
			commonResponse.getResponseParameter().put(ParameterConstants.ICARD_NUMBER.getName(),
					issuedBook.getUserId().getIdCardNumber());
			commonResponse.getResponseParameter().put("message", "Book returned");
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}

	}

	private void viewIssueBook(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		List<IssuedBookDto> issuedBookList = new ArrayList<>();

		try {
			if (commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()) != null) {
				Book book = bookRepository
						.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()));

				if (book != null) {
					List<IssuedBook> issuedBooks = issueBookRepository.findByBookIdAndStatus(book,
							IssuedBookStatusConstants.ISSUED.getStatus());
					issuedBooks.forEach(issuedBook -> issuedBookList.add(new IssuedBookDto(issuedBook)));
				}
			} else if (commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName()) != null) {
				User user = userRepository.findByIdCardNumber(
						commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName()));

				if (user != null) {
					List<IssuedBook> issuedBooks = issueBookRepository.findByUserIdAndStatus(user,
							IssuedBookStatusConstants.ISSUED.getStatus());
					issuedBooks.forEach(issuedBook -> issuedBookList.add(new IssuedBookDto(issuedBook)));
				}

			} else {
				List<IssuedBook> issuedBooks = issueBookRepository
						.findByStatus(IssuedBookStatusConstants.ISSUED.getStatus());
				issuedBooks.forEach(issuedBook -> issuedBookList.add(new IssuedBookDto(issuedBook)));
			}

			commonResponse.getResponseParameter().put("book_list", issuedBookList);
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}

	}

	@Transactional
	private void issueBook(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		try {
			SimpleDateFormat returDateFormate = new SimpleDateFormat("yyyy-MM-dd");
			String username;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}

			logger.info("User name :{}", username);

			User issuer = userRepository.findByUsername(username);
			if (issuer == null || issuer.getStatus() != StatusConstants.ACTIVE.getStatus()) {
				commonResponse.getResponseParameter().put("message", "Invalid issuer/inactive");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			User user = userRepository.findByIdCardNumber(
					commonRequest.getRequestParameters().get(ParameterConstants.ICARD_NUMBER.getName()));

			if (user == null || issuer.getStatus() != StatusConstants.ACTIVE.getStatus()) {
				commonResponse.getResponseParameter().put("message", "Invalid/inactive icard number");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			Book book = bookRepository
					.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()));

			if (book == null) {
				commonResponse.getResponseParameter().put("message", "Invalid isbn");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			if (book.getNumberOfCopies() == 0) {
				commonResponse.getResponseParameter().put("message", "Book not available");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			book.setNumberOfCopies(book.getNumberOfCopies() - 1);
			IssuedBook issuedBook = new IssuedBook();
			issuedBook.setBookId(book);
			issuedBook.setIssuedBy(issuer);
			issuedBook.setUserId(user);
			issuedBook.setIssuedTill(returDateFormate
					.parse(commonRequest.getRequestParameters().get(ParameterConstants.RETURN_DATE.getName())));
			issuedBook.setStatus(IssuedBookStatusConstants.ISSUED.getStatus());

			em.persist(issuedBook);
			em.persist(book);
			commonResponse.getResponseParameter().put(ParameterConstants.ISBN.getName(), book.getIsbn());
			commonResponse.getResponseParameter().put(ParameterConstants.TITLE.getName(), book.getTitle());
			commonResponse.getResponseParameter().put(ParameterConstants.RETURN_DATE.getName(),
					returDateFormate.format(issuedBook.getIssuedTill()));
			commonResponse.getResponseParameter().put(ParameterConstants.ICARD_NUMBER.getName(),
					user.getIdCardNumber());
			commonResponse.getResponseParameter().put("message", "Book issued");
			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}

	}

	private void updateBook(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		try {
			String username;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}

			logger.info("User name :{}", username);

			User user = userRepository.findByUsername(username);
			if (user == null) {
				commonResponse.getResponseParameter().put("message", "Invalid user");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			Book book = bookRepository
					.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()));

			if (book == null) {
				commonResponse.getResponseParameter().put("message", "no book found with provided isbn");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			if (book.getNumberOfCopies() + Integer.parseInt(
					commonRequest.getRequestParameters().get(ParameterConstants.NUMBER_OF_COPIES.getName())) < 0) {
				commonResponse.getResponseParameter().put("message", "Number of copies can't be less than 0");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			book.setNumberOfCopies(book.getNumberOfCopies() + Integer
					.parseInt(commonRequest.getRequestParameters().get(ParameterConstants.NUMBER_OF_COPIES.getName())));

			em.persist(book);

			commonResponse.getResponseParameter().put(ParameterConstants.TITLE.getName(), book.getTitle());
			commonResponse.getResponseParameter().put(ParameterConstants.AUTHOR.getName(), book.getAuthor());
			commonResponse.getResponseParameter().put(ParameterConstants.ISBN.getName(), book.getIsbn());
			commonResponse.getResponseParameter().put(ParameterConstants.LOCATION.getName(), book.getLocation());
			commonResponse.getResponseParameter().put(ParameterConstants.PUBLISHER.getName(), book.getPublisher());
			commonResponse.getResponseParameter().put(ParameterConstants.NUMBER_OF_COPIES.getName(),
					book.getNumberOfCopies());
			commonResponse.getResponseParameter().put("message", "Number of copies updated successfully");

			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());
		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}

	}

	private void viewBooks(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {

		List<BookDto> bookList = new ArrayList<>();

		if (commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()) != null) {
			Book book = bookRepository
					.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()));

			if (book != null) {
				bookList.add(new BookDto(book));
			}
		} else if (commonRequest.getRequestParameters().get(ParameterConstants.TITLE.getName()) != null) {
			Book book = bookRepository
					.findByTitle(commonRequest.getRequestParameters().get(ParameterConstants.TITLE.getName()));

			if (book != null) {
				bookList.add(new BookDto(book));
			}
		} else {
			List<Book> books = bookRepository.findAll();
			if (!books.isEmpty()) {
				books.forEach(book -> bookList.add(new BookDto(book)));
			}
		}
		commonResponse.getResponseParameter().put("book_list", bookList);
		commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
		commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());

	}

	@Transactional
	private void addBook(ApiCommonRequest commonRequest, ApiCommonResponse commonResponse) {
		logger.info("Inside addLibrarian");
		try {

			String username;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}

			logger.info("User name :{}", username);

			User user = userRepository.findByUsername(username);
			if (user == null) {
				commonResponse.getResponseParameter().put("message", "Invalid user");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			if (Integer.parseInt(
					commonRequest.getRequestParameters().get(ParameterConstants.NUMBER_OF_COPIES.getName())) < 0) {
				commonResponse.getResponseParameter().put("message", "Number of copies can't be less than 0");
				commonResponse.setResponseCode(ResponseCodeConstants.BAD_REQUEST.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.BAD_REQUEST.getHttpCode());
				return;
			}

			Book book = bookRepository
					.findByIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()));

			if (book != null) {
				commonResponse.getResponseParameter().put("message", "isbn already added in the library");
				commonResponse.setResponseCode(ResponseCodeConstants.DUPLICATE_ISBN.getResponseCode());
				commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
				return;
			}

			book = new Book();
			book.setCreatedBy(user);
			book.setTitle(commonRequest.getRequestParameters().get(ParameterConstants.TITLE.getName()));
			book.setAuthor(commonRequest.getRequestParameters().get(ParameterConstants.AUTHOR.getName()));
			book.setIsbn(commonRequest.getRequestParameters().get(ParameterConstants.ISBN.getName()));
			book.setLocation(commonRequest.getRequestParameters().get(ParameterConstants.LOCATION.getName()));
			book.setNumberOfCopies(Integer
					.parseInt(commonRequest.getRequestParameters().get(ParameterConstants.NUMBER_OF_COPIES.getName())));
			book.setPublisher(commonRequest.getRequestParameters().get(ParameterConstants.PUBLISHER.getName()));
			em.persist(book);

			commonResponse.getResponseParameter().put(ParameterConstants.TITLE.getName(), book.getTitle());
			commonResponse.getResponseParameter().put(ParameterConstants.AUTHOR.getName(), book.getAuthor());
			commonResponse.getResponseParameter().put(ParameterConstants.ISBN.getName(), book.getIsbn());
			commonResponse.getResponseParameter().put(ParameterConstants.LOCATION.getName(), book.getLocation());
			commonResponse.getResponseParameter().put(ParameterConstants.PUBLISHER.getName(), book.getPublisher());
			commonResponse.getResponseParameter().put(ParameterConstants.NUMBER_OF_COPIES.getName(),
					book.getNumberOfCopies());
			commonResponse.getResponseParameter().put("message", "Book added successfully");

			commonResponse.setHttpStatusCode(HttpResponseCode.OK.getHttpCode());
			commonResponse.setResponseCode(ResponseCodeConstants.OK.getResponseCode());

		} catch (Exception e) {
			logger.error("Exception", e);
			commonResponse.setResponseCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR.getResponseCode());
			commonResponse.setHttpStatusCode(HttpResponseCode.INTERNAL_SERVER_ERROR.getHttpCode());
		}
	}

}
