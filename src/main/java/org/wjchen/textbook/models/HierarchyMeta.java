package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "HIERARCHY_NODE_META")
public class HierarchyMeta implements Serializable {

	private static final long serialVersionUID = -1500286531321323694L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private HierarchyNode node;

}
