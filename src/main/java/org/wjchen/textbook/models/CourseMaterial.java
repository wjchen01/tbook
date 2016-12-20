package org.wjchen.textbook.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TEXTBOOK_COURSE_MATERIALS")
public class CourseMaterial implements Serializable {

	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "sequence", sequenceName = "TEXTBOOK_COURSE_ITEM_ID_SEQ") 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence") 
	@Column(name = "ID")
	@Getter @Setter
	private Long id;

	@Column(name = "DESCRIPTION")
	@Getter @Setter
	private String description;
	
	@Column(name = "NOTES")
	@Getter @Setter
	private String notes;
	
	@Column(name = "LATEUSE")
	@Getter @Setter
	private boolean lateUse;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID", insertable = false, updatable = false)
	@Getter @Setter
	private CourseDetail course;

		
}
