package org.wjchen.textbook.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "TEXTBOOK_COURSE_BOOK")
public class CourseBook implements Serializable {

	private static final long serialVersionUID = 1L;	
	 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseBookIdSequence") 
	@SequenceGenerator(name = "courseBookIdSequence", sequenceName = "TEXTBOOK_COURSE_ITEM_ID_SEQ", allocationSize=1)
	@Column(name = "ID")
	private Long id;	
	
	@Column(name = "ISBN")
	private String isbn;

	@Column(name = "NOTES")
	private String notes = null;

	@Column(name ="LATEUSE")
	private boolean lateUse = false;
	
	@Column (name = "LIBRARYRESERVEREQUESTED")
	private boolean libraryReserveRequested = false;

	@Column (name = "REQUIRED")
	@Enumerated(EnumType.STRING)
	private CourseBookStatus status = CourseBookStatus.required;
	
	@Column (name = "SEQUENCE")
	private Long sequence;
	
	@Column (name ="RETAILPRICE")
	private BigDecimal retailPrice = new BigDecimal(-0.01);
	
	@Column (name = "INSERTEDDATE")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date insertedDate;
	
	@Column (name = "BOOK_ID")
	private Long bookId;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COURSE", referencedColumnName="ID")
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	private CourseDetail course;
	
//	@NotFound(action=NotFoundAction.IGNORE)
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="BOOK_ID", referencedColumnName="BOOK_ID")
//	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
//	private Textbook book;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INSERTEDBY", referencedColumnName="USER_ID")
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	private SakaiUser requester;

	public CourseBook() {}
	
	public CourseBook(CourseDetail course, Textbook book, SakaiUser requester, CourseBookStatus status) {
//		course.getCourseBooks().add(this);
		this.setCourse(course);
//		book.getBooks().add(this);
//		this.setBook(book);
//		requester.getBooks().add(this);
		this.setRequester(requester);
		
		this.setStatus(status);
		
		this.updateISBN(course, book);
		
		this.updateSequence(status);
		
		this.setBookId(book.getBookId());
		
		this.setInsertedDate(new Date());
	}
	
	private void updateISBN(CourseDetail course, Textbook book) {
		if(book.getIsbn13() != null && ! book.getIsbn13().isEmpty()) {
			this.setIsbn(book.getIsbn13());
		}
		else if(book.getIsbn10() != null && ! book.getIsbn10().isEmpty()) {
			this.setIsbn(book.getIsbn10());
		}
		else {
			String isbn = String.format("xxx%d", course.getId());
			this.setIsbn(isbn); 
		}
	}
	
	private void updateSequence(CourseBookStatus status) {
		Long sequence = 0L;
		for(CourseBook cbook : course.getCourseBooks()) {
			if(cbook.status == status) {
				sequence++;
			}
		}
		
		this.setSequence(sequence);		
	}
	
	public static Comparator<CourseBook> SequenceComparator = new Comparator<CourseBook>() {

		@Override
		public int compare(CourseBook book1, CourseBook book2) {
			return book1.getSequence().compareTo(book2.getSequence());
		}
		
	};
}
