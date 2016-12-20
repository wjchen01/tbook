/**
 * $URL: https://source.sakaiproject.org/contrib/textbook/tbook/branches/2.9.x/api/src/java/org/sakaiproject/tbook/model/Course.java $
 * $Id: Course.java 54217 2008-10-22 23:58:23Z jimeng@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "TEXTBOOK_COURSE")
public class CourseDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "sequence", sequenceName = "TEXTBOOK_COURSE_ID_SEQ", allocationSize=1) 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence") 
	@Column(name = "ID")
	private Long id;

	@OneToOne(optional=false)
	@JoinColumn(name = "COURSEID")
	private CourseSite site;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "MODIFIEDDATE")
	private Date modifiedDate;

	@Column(name = "NOTEXTBOOKS")
	private boolean withoutBooks = false;

	@Column(name = "COMMENTS")
	private String comments;
	
	@Column(name = "CONTEXT")
	private String context;
	
	@Column(name = "URL")
	private String url;
	
	@OneToMany(mappedBy = "course", orphanRemoval=true)
	private Set<CourseBook> courseBooks = new HashSet<CourseBook>(0);
	
	public CourseDetail() {}
	
	public CourseDetail(CourseSite site) {
		this.site = site;
		
		ColumbiaCourseTitle cuTitle = site.getCuTitle();
		if(cuTitle != null) {
			this.setTitle(cuTitle.getNewTitle());
		}
		else {
			ColumbiaCourse cuCourse = site.getCuCourse();
			if(cuCourse != null) {
				this.setTitle(cuCourse.getTitle());
			}
			else {
				this.setTitle(site.getSiteId());
			}
		}
		this.setContext(site.getSiteId());
		this.setUrl("/portal/site/" + site.getSiteId());
	}
	
}
