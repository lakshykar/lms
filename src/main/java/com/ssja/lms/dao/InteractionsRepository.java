package com.ssja.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssja.lms.model.Interactions;

@Repository
public interface InteractionsRepository extends JpaRepository<Interactions, Integer> {
	
}
