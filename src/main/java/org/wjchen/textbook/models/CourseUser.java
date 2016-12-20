package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="SAKAI_SITE_USER")
public class CourseUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CourseUserId id;
	
	@Column(name="PERMISSION")
	@Enumerated(EnumType.ORDINAL)
	private int permission;
	
	@ManyToOne
	@JoinColumn(name="SITE_ID", insertable=false, updatable=false)
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	private CourseSite site;
	
	@ManyToOne
	@JoinColumn(name="USER_ID", insertable=false, updatable=false)
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	private SakaiUser user;
	
}
