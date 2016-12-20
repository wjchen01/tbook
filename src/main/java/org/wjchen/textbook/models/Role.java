package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"key"})
@Entity
@Table(name = "SAKAI_REALM_ROLE")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ROLE_KEY")
	private Long key;
	
	@Column(name = "ROLE_NAME")
	private String name;
	
	@OneToMany(mappedBy="role")
	private List<RealmRoleFunction> realmRoleFunctions;

	@OneToMany(mappedBy="role")
	private List<RealmRoleUser> realmRoleUsers;

}
