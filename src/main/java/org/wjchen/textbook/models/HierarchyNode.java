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
@EqualsAndHashCode(of={"id"})
@Entity
@Table(name = "HIERARCHY_NODE")
public class HierarchyNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5840303013421563825L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "PARENTIDS")
	private String parentIds;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "node", cascade = CascadeType.ALL)
	private HierarchyMeta meta;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "node", cascade = CascadeType.ALL)
	private Set<HierarchyPerms> permissions = new HashSet<HierarchyPerms>(0);
	
	public boolean isGlobal() {
		return numberOfParents() == 0;
	}
	
	public boolean isSchool() {
		
		return numberOfParents() == 1;
	}
	
	public boolean isDepartment() {
		return numberOfParents() == 2;
	}
	
	public boolean isSubject() {
		return numberOfParents() == 3;
	}
	
	private int numberOfParents() {
		if(parentIds == null || parentIds.isEmpty()) {
			return 0;
		}
		
		String[] parents = parentIds.split(":");
		List<String> pids = new ArrayList<String>(parents.length);
		for(String parent : parents) {
			if(parent != null && !parent.isEmpty()) {
				pids.add(parent);
			}
		}

		return pids.size();
	}
	
}
