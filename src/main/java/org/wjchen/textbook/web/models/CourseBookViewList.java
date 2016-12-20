package org.wjchen.textbook.web.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseBookStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseBookViewList implements Serializable {

	private static final long serialVersionUID = 1L;

	List<CourseBook> books;
	
	public CourseBookViewList() {
		books = new ArrayList<CourseBook>();
	}
	
	public void addBook(CourseBook additional) {
		books.add(additional);
	}
	
	public void addBooks(Collection<CourseBook> additionals) {
		books.addAll(additionals);
	}
	
	public List<CourseBook> getRequiredBookList() {
		List<CourseBook> list = new ArrayList<CourseBook>();
		for(CourseBook book : books) {
			if(book.getStatus() == CourseBookStatus.required) {
				list.add(book);
			}
		}
		
		return list;
	}

	public List<CourseBook> getRecommendedBookList() {
		List<CourseBook> list = new ArrayList<CourseBook>();
		for(CourseBook book : books) {
			if(book.getStatus() == CourseBookStatus.recommended) {
				list.add(book);
			}
		}
		
		return list;
	}

}
