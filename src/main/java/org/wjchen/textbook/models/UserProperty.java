package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "SAKAI_USER_PROPERTY")
public class UserProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId 
	private UserPropertyId id;
	
	@Column(name = "VALUE")
	private String value;

	@ManyToOne
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private SakaiUser user;

}
