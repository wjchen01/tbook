package org.wjchen.textbook.logics;

import java.util.List;

import org.wjchen.textbook.models.Textbook;

public interface TextbookService {

	public Textbook getExactMatchBook(String isbn);
	public Textbook getBookFromDB(String isbn);
	public List<Textbook> getBooksByFinder(String isbn);
	public List<Textbook> getBooksByTitle(String title);
	public Textbook getItemById(Long id);
	
}
