package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.Comparator;
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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TEXTBOOK_BOOKS")
public class Textbook implements Serializable, Comparable<Textbook> {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sequence", sequenceName = "TEXTBOOK_BOOKS_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "BOOK_ID")
	private Long bookId;

	@Column(name = "AUTHORS")
	private String authors;

	@Column(name = "EDITION")
	private String edition;

	@Column(name = "FORMAT")
	private String format;

	@Column(name = "ISBN10")
	private String isbn10;

	@Column(name = "ISBN13")
	private String isbn13;

	@Column(name = "LANGUAGE")
	private String language;

	@Column(name = "PUBLISHER")
	private String publisher;

	@Column(name = "RELATEDBOOKS")
	private String relatedBooks;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "YEAR")
	private String year;

	@Column(name = "KEYWORDS")
	private String keywords;

	@Column(name = "RETRIEVEDDATE")
	private Date retrievedDate;

	@Column(name = "VERSION")
	private int version;

	@Column(name = "TYPES")
	@Enumerated(EnumType.STRING)
	private Type type;

//	@OneToMany(mappedBy = "book", orphanRemoval = true)
//	private Set<CourseBook> books = new HashSet<CourseBook>(0);

	// @OneToOne(mappedBy = "book", fetch = FetchType.LAZY, cascade =
	// CascadeType.ALL)
	// private BookOffer offer;

	@Override
	public int compareTo(Textbook other) {
		// sort by year (unless the years are the same)
		String year0 = this.getYear();
		String year1 = other.getYear();
		if (year0 == null && year1 == null) {
			// use isbn instead of year
			String isbn0 = this.getIsbn13();
			String isbn1 = other.getIsbn13();
			if (isbn0 == null && isbn1 == null) {
				// use isbn10 instead of isbn13
				String isbn100 = this.getIsbn10();
				String isbn101 = other.getIsbn10();
				if (isbn100 == null && isbn101 == null) {
					// give up -- the books are equal
					return 0;
				} else if (isbn100 == null) {
					// book0 is less than book1
					return 1;
				} else if (isbn101 == null) {
					// book0 is less than book1
					return -1;
				}

				// use the isbn10's
				return isbn100.compareTo(isbn101);
			} else if (isbn0 == null) {
				// book0 is less than book1
				return 1;
			} else if (isbn1 == null) {
				// book0 is less than book1
				return -1;
			}

			// use the isbn's
			return isbn0.compareTo(isbn1);
		} else if (year0 == null) {
			// book0 is less than book1
			return 1;
		}
		if (year1 == null) {
			// book0 is greater than book1
			return -1;
		} else if (year0.equals(year1)) {
			// break tie with isbn
			return this.getIsbn13().compareTo(other.getIsbn13());
		}

		// neither year is null, and the years are not equal
		// so sort in descending order by year
		return year1.compareTo(year0);

	}

	public boolean copy(Textbook other) {
		boolean changes = false;
		try {
			// this.id = other.id;
			if ((this.authors == null && other.authors != null)
					|| (this.authors != null && !this.authors.equals(other.authors))) {
				this.authors = other.authors;
				changes = true;
			}
			if ((this.edition == null && other.edition != null)
					|| (this.edition != null && !this.edition.equals(other.edition))) {
				this.edition = other.edition;
				changes = true;
			}
			if ((this.isbn10 == null && other.isbn10 != null)
					|| (this.isbn10 != null && !this.isbn10.equals(other.isbn10))) {
				this.isbn10 = other.isbn10;
				changes = true;
			}
			if ((this.isbn13 == null && other.isbn13 != null)
					|| (this.isbn13 != null && !this.isbn13.equals(other.isbn13))) {
				this.isbn13 = other.isbn13;
				changes = true;
			}
			if ((this.format == null && other.format != null) 
					|| (this.format != null && !this.format.equals(other.format))){
				this.format = other.format;
					changes = true;
			}
			if ((this.language == null && other.language != null)
					|| (this.language != null && !this.language.equals(other.language))) {
				this.language = other.language;
				changes = true;
			}
			if ((this.publisher == null && other.publisher != null)
					|| (this.publisher != null && !this.publisher.equals(other.publisher))) {
				this.publisher = other.publisher;
				changes = true;
			}
			if ((this.relatedBooks == null && other.relatedBooks != null)
					|| (this.relatedBooks != null && !this.relatedBooks.equals(other.relatedBooks))) {
				this.relatedBooks = other.relatedBooks;
				changes = true;
			}
			if ((this.title == null && other.title != null)
					|| (this.title != null && !this.title.equals(other.title))) {
				this.title = other.title;
				changes = true;
			}
			if ((this.year == null && other.year != null) || (this.year != null && !this.title.equals(other.title))) {
				this.year = other.year;
				changes = true;
			}
			if (changes && other.retrievedDate != null) {
				this.retrievedDate = new Date(other.retrievedDate.getTime());
			} else {
				this.retrievedDate = new Date();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// we always need to update the retrievedDate
		changes = true;

		return changes;
	}

	public boolean isExpired(long freshnessPeriod) {
		boolean expired = true;
		if (this.retrievedDate != null) {
			long now = new Date().getTime();
			expired = this.retrievedDate.getTime() + freshnessPeriod < now;
		}
		return expired;
	}

	public static Comparator<Textbook> TextbookComparator = new Comparator<Textbook>() {

		@Override
		public int compare(Textbook book0, Textbook book1) {
			return book0.compareTo(book1);
		}

	};

	public enum Type {
		manual("textbook.types.manual"), search("textbook.types.search");

		private String messagekey;

		private Type(String msgkey) {
			this.messagekey = msgkey;
		}

		public String messagekey() {
			return this.messagekey;
		}

	}

}
