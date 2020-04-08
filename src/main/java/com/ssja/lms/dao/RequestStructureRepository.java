package com.ssja.lms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.Interactions;
import com.ssja.lms.model.RequestStructure;

@Repository
public interface RequestStructureRepository extends JpaRepository<RequestStructure, Integer> {
	
	List<RequestStructure> findByInteractionId(Interactions interaction);
}
