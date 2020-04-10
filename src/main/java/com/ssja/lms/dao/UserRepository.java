package com.ssja.lms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	List<User> findByUserTypeAndStatus(int userType, int status);

	User findByIdAndUserTypeAndStatus(int id, int userType, int status) ;

	User findByUsername(String username);

	User findByIdAndUserType(int parseInt, int userType);

	List<User> findByUserType(int userType);

	User findByIdCardNumberAndUserType(String icardNumber, int userType);

	User findByIdCardNumber(String icardNumber);
	
}
