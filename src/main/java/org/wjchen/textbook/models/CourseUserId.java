package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class CourseUserId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "SITE_ID")
	private String siteId;
	
	@Column(name = "USER_ID")
	private String userId;
}
