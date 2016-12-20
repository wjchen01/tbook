package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"id"})
@Entity
@Table(name = "SAKAI_REALM_RL_GR")
public class RealmRoleUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId 
	private RealmRoleUserId id;
	
	@MapsId("realmKey")
	@ManyToOne
	@JoinColumn(name = "REALM_KEY")
	private Realm realm;
	
	@MapsId("roleKey")
	@ManyToOne
	@JoinColumn(name = "ROLE_KEY")
	private Role role;
	
	@MapsId("userId")
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private SakaiUser user;

	@Column(name = "ACTIVE")
	private boolean active;
	
	@Column(name = "PROVIDED")
	private boolean provided;

}
