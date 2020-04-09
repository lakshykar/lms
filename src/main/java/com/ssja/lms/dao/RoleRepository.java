package com.ssja.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
