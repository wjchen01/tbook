package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"userId"})
@Entity
@Table(name = "SAKAI_USER_ID_MAP")
public class SakaiUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "EID")
	private String userNm;	
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	private UserDetail detail;

	@OneToMany(mappedBy="user")
	private List<RealmRoleUser> realmRoleUsers;

	@OneToMany(mappedBy = "user")
	private List<CourseUser> courseusers = new ArrayList<CourseUser>();
	
	@OneToMany(mappedBy="requester", orphanRemoval=true)
	private Set<CourseBook> books = new HashSet<CourseBook>(0);
	
	@OneToMany(mappedBy="user")
	private Set<UserProperty> properties = new HashSet<UserProperty>(0);
	
}
