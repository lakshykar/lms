package com.ssja.lms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.User;
import com.ssja.lms.model.UserRoleMapping;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping,Integer> {

	List<UserRoleMapping> findByUserId(User user);
}
