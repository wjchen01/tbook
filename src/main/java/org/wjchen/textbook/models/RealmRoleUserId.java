package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"realmKey", "roleKey", "userId"})
@Embeddable
public class RealmRoleUserId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="REALM_KEY")
	private Long realmKey;	
	
	@Column(name="ROLE_KEY")
	private Long roleKey;
	
	@Column(name="USER_ID")
	private String userId;
	
}
