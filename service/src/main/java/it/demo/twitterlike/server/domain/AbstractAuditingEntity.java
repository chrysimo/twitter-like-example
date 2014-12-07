package it.demo.twitterlike.server.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base abstract class for entities which will hold definitions for created,
 * last modified by and created, last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity<PK extends Serializable>
		implements Persistable<PK> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@CreatedBy
	// @NotNull
	@Column(name = "created_by", nullable = true, length = 50, updatable = false)
	private String createdBy;

	@CreatedDate
//	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@NotNull
	 @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "created_date", nullable = false)
	private DateTime createdDate = DateTime.now();

	@LastModifiedBy
	@Column(name = "last_modified_by", length = 50)
	private String lastModifiedBy;

	@LastModifiedDate
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "last_modified_date")
//	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private DateTime lastModifiedDate = DateTime.now();

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return getId() == null;
	}
}
