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
@EqualsAndHashCode(of={"realmKey"})
@Entity
@Table(name = "SAKAI_REALM")
public class Realm implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REALM_KEY")
	private Long realmKey;
	
	@Column(name = "REALM_ID")
	private String realmId;
	
	@Column(name = "MAINTAIN_ROLE")
	private Long maintain;
	
	@OneToMany(mappedBy="realm")
	private List<RealmRoleFunction> realmRoleFunctions;

	@OneToMany(mappedBy="realm")
	private List<RealmRoleUser> realmRoleUsers;

}
