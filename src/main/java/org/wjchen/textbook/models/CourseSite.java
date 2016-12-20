/**
* 
* CourseSite
* 
**/
package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"siteId"})
@Entity
@Table(name = "SAKAI_SITE")
public class CourseSite implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SITE_ID")
	private String siteId;

	@Column(name="DESCRIPTION")
	private String description;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "site", cascade = CascadeType.ALL)
	private CourseDetail detail;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "site", cascade = CascadeType.ALL)
	private ColumbiaCourse cuCourse;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "site", cascade = CascadeType.ALL)
	private ColumbiaCourseTitle cuTitle;
	
	@OneToMany(mappedBy = "site")
	private List<CourseUser> courseusers;
	
}
