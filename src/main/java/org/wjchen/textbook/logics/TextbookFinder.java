package org.wjchen.textbook.logics;

import java.util.List;

import org.wjchen.textbook.models.Textbook;

public interface TextbookFinder {

	public List<Textbook> getBooks(String isbn);
	public void verifyIsbn(Textbook item);
	
}
