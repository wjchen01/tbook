package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "CU_COURSE_TITLE")
public class ColumbiaCourseTitle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SITE_ID")
	private String siteId;
	
	@Column(name="OLD_TITLE")
	private String oldTitle;

	@Column(name="NEW_TITLE")
	private String newTitle;
	
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private CourseSite site;
	
}
