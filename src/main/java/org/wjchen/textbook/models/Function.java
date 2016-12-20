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
@Table(name = "SAKAI_REALM_FUNCTION")
public class Function implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FUNCTION_KEY")
	private Long key;

	@Column(name = "FUNCTION_NAME")
	private String name;
	
	@OneToMany(mappedBy="function")
	private List<RealmRoleFunction> realmRoleFunctions;

}
