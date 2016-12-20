package org.wjchen.textbook.web.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.wjchen.textbook.models.Textbook;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TextbookViewList implements Serializable {

	private static final long serialVersionUID = 1L;

	List<Textbook> books;
	
	public TextbookViewList() {
		books = new ArrayList<Textbook>();
	}
	
	public void addBook(Textbook additional) {
		books.add(additional);
	}
	
	public void addBooks(Collection<Textbook> additionals) {
		books.addAll(additionals);
	}
	
}
