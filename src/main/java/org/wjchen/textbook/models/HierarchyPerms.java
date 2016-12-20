package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"id"})
@Entity
@Table(name = "HIERARCHY_PERMS")
public class HierarchyPerms implements Serializable {

	private static final long serialVersionUID = -1075342235066363872L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "PERMISSION")
	private String permission;

    @ManyToOne
    @JoinColumn(name = "USERID")
	private SakaiUser user;

    @ManyToOne
    @JoinColumn(name = "NODEID")
	private HierarchyNode node;
	
}
