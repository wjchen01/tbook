package org.wjchen.textbook.logics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.textbook.daos.TextbookDAO;
import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.utils.TextbookUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.textbook.logics.TextbookService")
@Transactional("textbook")
public class TextbookServiceImpl implements TextbookService {

	public static final long ONE_HOUR = 60L * 60L * 1000L;
	public static final long ONE_DAY = 24L * ONE_HOUR;

	@Autowired
	private TextbookDAO textbookDao;
	
	@Autowired
	private TextbookFinder bookFinder;
	
	protected long daysBeforeBookDataExpires = 30;
	
	@Override
	public Textbook getExactMatchBook(String isbn) {
		Textbook book = null;
		
		book = this.getBookFromDB(isbn);
		if(book == null) {
			List<Textbook> books = this.getBooksByFinder(isbn);
			if(books.size() > 0) {
				book = books.get(0);
			}
		}
		return book;
	}
	
	@Override
	public Textbook getBookFromDB(String isbn) {
		Textbook book = null;
		if(isbn == null)
		{
			return book;
		}
		
		isbn = TextbookUtil.normalizeISBN(isbn);
		Search search = new Search(Textbook.class);
		Filter filter = null;
		if(isbn.length() == 10) {
			filter = Filter.equal("isbn10", isbn);
		}
		else if(isbn.length() == 13) {
			filter = Filter.equal("isbn13", isbn);			
		}
		if(filter == null) {
			return book;
		}
		
		search.addFilter(filter);
		List<Textbook> books = textbookDao.search(search);
		if(books.size() > 0) {
			book = books.get(0);
		}
//		else {
//			books = getBooks(isbn);
//			if(books.size() > 0) {
//				book = books.get(0);
//			}
//		}
		
		return book;
	}

	@Override
	public List<Textbook> getBooksByFinder(String isbn) {
		List<Textbook> books = new ArrayList<Textbook>();
		
		isbn = TextbookUtil.normalizeISBN(isbn);
		
		Textbook exactMatch = this.getBookFromDB(isbn);
		// should return book even if expired
		
		if(exactMatch == null || exactMatch.isExpired(getFreshnessPeriod()))
		{
			try
			{
				List<Textbook> books_found = bookFinder.getBooks(isbn);
				if(books_found != null)
				{
					books.addAll(books_found);
				}
				else if(exactMatch != null)
				{
					books.add(exactMatch);
				}
			}
			catch(Exception e)
			{
				log.debug(this + ".getBooks(\"" + isbn + "\") + BookFinder failed: " + e.getMessage() + " " + e);
				if(exactMatch != null)
				{
					books.add(exactMatch);
				}
			}
			
		    // save books if they're not already in book store
		    for(Textbook book : books)
		    {
		    	Textbook savedBook = this.getBook(book);
		    	if(savedBook == null)
		    	{
		    		textbookDao.save(book);
		    	}
		    	else if(savedBook.isExpired(getFreshnessPeriod()))
		    	{
		    		replaceBook(savedBook, book);
		    	}
		    }
		}
		else
		{
			books.add(exactMatch);
		
			String relatedBooks = exactMatch.getRelatedBooks();
			if(relatedBooks != null && ! relatedBooks.trim().equals(""))
			{
				String[] relatedBookIsbns = relatedBooks.split(" ");
				for(String otherIsbn : relatedBookIsbns)
				{
					Textbook otherBook = this.getBookFromDB(otherIsbn);
					if(otherBook != null)
					{
						books.add(otherBook);
					}
				}
			}
		}
		
		sort(isbn, books);

	    return books;
 	}

	@Override
	public List<Textbook> getBooksByTitle(String title) {
		Search search = new Search(Textbook.class);
		Filter filter = Filter.equal("title", title);
		search.addFilter(filter);
		
		return textbookDao.search(search);
	}

	@Override
	public Textbook getItemById(Long id) {
		return textbookDao.find(id);
	}

    private void sort(String isbn, List<Textbook> books)
    {
    	if(isbn == null)
    	{
    		isbn = "";
    	}
    	else
    	{
    		isbn = isbn.trim().replaceAll("\\s", "").replaceAll("\\p{Punct}", "");
    	}
    	final String isbnX = isbn;
    	
	    if(! books.isEmpty())
		{
	    	Collections.sort(books, new Comparator<Textbook>() {

	    		// sort exact matches to front
	    		// sort by publication year
	    		// ties broken by ISBN-10
				public int compare(Textbook book0, Textbook book1)
                {
					// sort exact match(es) to front;
					boolean exactMatch0 = isbnX.equals(book0.getIsbn10()) || isbnX.equals(book0.getIsbn13());
					boolean exactMatch1 = isbnX.equals(book1.getIsbn10()) || isbnX.equals(book1.getIsbn13());
					if(exactMatch0 && ! exactMatch1)
					{
						return -1;
					}
					else if(! exactMatch0 && exactMatch1)
					{
						return 1;
					}
					else if(exactMatch0 && exactMatch1)
					{
						return 0;
					}
						
					// sort by year (unless the years are the same)
					String year0 = book0.getYear();
					String year1 = book1.getYear();
					if(year0 == null && year1 == null)
					{
						// use isbn instead of year
						String isbn0 = book0.getIsbn13();
						String isbn1 = book1.getIsbn13();
						if(isbn0 == null && isbn1 == null)
						{
							// use isbn10 instead of isbn13
							String isbn100 = book0.getIsbn10();
							String isbn101 = book1.getIsbn10();
							if(isbn100 == null && isbn101 == null)
							{
								// give up -- the books are equal
								return 0;
							}
							else if(isbn100 == null)
							{
								// book0 is less than book1
								return 1;
							}
							else if(isbn101 == null)
							{
								// book0 is less than book1
								return -1;
							}
							
							// use the isbn10's
							return isbn100.compareTo(isbn101);
						}
						else if(isbn0 == null)
						{
							// book0 is less than book1
							return 1;
						}
						else if(isbn1 == null)
						{
							// book0 is less than book1
							return -1;
						}
						
						// use the isbn's
						return isbn0.compareTo(isbn1);
					}
					else if(year0 == null)
					{
						// book0 is less than book1
						return 1;
					}
					if(year1 == null)
					{
						// book0 is greater than book1
						return -1;
					}
					else if(year0.equals(year1))
					{
						// break tie with isbn
						return book0.getIsbn13().compareTo(book1.getIsbn13());
					}
					
					// neither year is null, and the years are not equal
					// so sort in descending order by year
					return year1.compareTo(year0);
					
                }});
		}
    }

	protected Textbook getBook(Textbook book) 
	{
		Textbook savedBook = getBookFromDB(book.getIsbn13());
		if(savedBook == null)
		{
			savedBook = getBookFromDB(book.getIsbn10());
		}
		return savedBook;
	}

	protected void replaceBook(Textbook savedBook, Textbook book) 
	{
		boolean changes = false;
		if(savedBook != null && book != null)
		{
			changes = savedBook.copy(book);
		}
		if(changes) {
			textbookDao.save(savedBook);
		}
	}

	private long getFreshnessPeriod() 
	{
		return daysBeforeBookDataExpires * ONE_DAY;
	}

}
