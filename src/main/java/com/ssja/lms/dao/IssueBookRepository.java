package com.ssja.lms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.Book;
import com.ssja.lms.model.IssuedBook;
import com.ssja.lms.model.User;

@Repository
public interface IssueBookRepository extends JpaRepository<IssuedBook, Long> {

	List<IssuedBook> findByBookIdAndStatus(Book book, int status);

	List<IssuedBook> findByUserIdAndStatus(User user, int status);

	List<IssuedBook> findByStatus(int status);

	IssuedBook findByIdAndStatus(long id, int status);
	
}
