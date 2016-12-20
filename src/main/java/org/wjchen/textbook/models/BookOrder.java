package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "TEXTBOOK_BOOKORDER")
public class BookOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookOrderIdSequence") 
	@SequenceGenerator(name = "bookOrderIdSequence", sequenceName = "TEXTBOOK_BOOKORDER_ID_SEQ", allocationSize=1)
	@Column(name = "ID")
	private Long id;	

	@Column(name = "COURSE_ID")
	private String courseId;
	
	@Column(name = "ISBN")
	private String isbn;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "AUTHOR")
	private String author;
	
	@Column(name = "PUBLISHER")
	private String publisher;
	
	@Column(name = "YEAR")
	private String year;
	
	@Column(name = "REQUIRED")
	@Enumerated(EnumType.STRING)
	private CourseBookStatus required;
	
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "CREATED_DATE")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date createdOn;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "PROCESSED_DATE")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date processedDate;
	
	@Column(name = "INSERTED_DATE")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date insertedDate;
	
	@Column(name = "BOOKORDER")
	private boolean bookOrder;
		
}
