package com.ssja.lms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Author Rohit Lakshykar 23-Apr-2018 4:03:37 PM
 *
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Interactions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private Integer interactionTypeId;

	private String description;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;


	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	/**
	 * @return the interactionTypeId
	 */
	public Integer getInteractionTypeId() {
		return interactionTypeId;
	}

	/**
	 * @param interactionTypeId
	 *            the interactionTypeId to set
	 */
	public void setInteractionTypeId(Integer interactionTypeId) {
		this.interactionTypeId = interactionTypeId;
	}

	public Date getCreatedAt() {

		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {

		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {

		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {

		this.updatedAt = updatedAt;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

}
