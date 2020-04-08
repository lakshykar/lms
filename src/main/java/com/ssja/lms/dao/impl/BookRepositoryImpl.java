package com.ssja.lms.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssja.lms.dao.BookRepositoryCustom;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepositoryCustom {

	@PersistenceContext
	protected EntityManager em;
}
