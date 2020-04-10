package com.ssja.lms.dto;

import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssja.lms.model.IssuedBook;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssuedBookDto {

	@JsonProperty("issued_book_id")
	private Long issueBookId;

	private String title;

	private String isbn;

	@JsonProperty("icard_number")
	private String icardNumber;

	@JsonProperty("issued_date")
	private String issuedDate;

	@JsonProperty("return_date")
	private String returnDate;

	public IssuedBookDto(IssuedBook issuedBook) {
		super();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.title = issuedBook.getBookId().getTitle();
		this.isbn = issuedBook.getBookId().getIsbn();
		this.icardNumber = issuedBook.getUserId().getIdCardNumber();
		this.returnDate = sdf.format(issuedBook.getCreatedAt());
		this.issuedDate = sdf.format(issuedBook.getIssuedTill());
		this.issueBookId = issuedBook.getId();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIcardNumber() {
		return icardNumber;
	}

	public void setIcardNumber(String icardNumber) {
		this.icardNumber = icardNumber;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public Long getIssueBookId() {
		return issueBookId;
	}

	public void setIssueBookId(Long issueBookId) {
		this.issueBookId = issueBookId;
	}

}