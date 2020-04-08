package com.ssja.lms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.RoleUrlMapping;

@Repository
public interface RoleUrlMappingRepository extends JpaRepository<RoleUrlMapping, Integer> {
	
	List<RoleUrlMapping> findByStatus(int status);
}
