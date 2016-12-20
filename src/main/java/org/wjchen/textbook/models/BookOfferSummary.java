package org.wjchen.textbook.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class BookOfferSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long bookId;
	protected String isbn13;
	protected String title;
	protected String authors;
	protected String edition;
	protected String publisher;
	protected int offerCount = 0;
	protected BigDecimal minPrice = new BigDecimal("0.00");
	protected BigDecimal maxPrice = new BigDecimal("0.00");
	protected boolean removed = false;
	protected boolean expired = false;
	
	public BookOfferSummary()
	{
		
	}
	
	public BookOfferSummary(long bookId, String isbn13, String title, 
				String authors, String edition, String publisher, int offerCount, 
				BigDecimal minPrice, BigDecimal maxPrice, boolean removed, 
				boolean expired)
	{
		set(bookId, isbn13, title, authors, edition, publisher, offerCount,
				minPrice, maxPrice, removed, expired);
	}

	public BookOfferSummary(long bookId, String isbn13, String title, 
			String authors, String edition, String publisher, int offerCount, 
			String minPriceStr, String maxPriceStr, boolean removed, 
			boolean expired)
	{
		set(bookId, isbn13, title, authors, edition, publisher, offerCount,
				new BigDecimal(minPriceStr), new BigDecimal(maxPriceStr), removed, expired);
		
	}
	
	public BookOfferSummary(long bookId, String isbn13, String title, 
			String authors, String edition, String publisher, int offerCount, 
			String minPriceStr, String maxPriceStr, boolean removed, 
			String expiredStr)
	{
		set(bookId, isbn13, title, authors, edition, publisher, offerCount,
				new BigDecimal(minPriceStr), new BigDecimal(maxPriceStr), removed, 
				Boolean.valueOf(expiredStr));
		
	}
	
	public BookOfferSummary(String bookIdStr, String isbn13, String title, 
			String authors, String edition, String publisher, String offerCountStr, 
			String minPriceStr, String maxPriceStr, String removedStr, 
			String expiredStr)
	{
		set(Long.valueOf(bookIdStr), isbn13, title, authors, edition, publisher, Integer.valueOf(offerCountStr),
				new BigDecimal(minPriceStr), new BigDecimal(maxPriceStr), Boolean.valueOf(removedStr), 
				Boolean.valueOf(expiredStr));
		
	}
	
	protected void set(long bookId, String isbn13, String title,
			String authors, String edition, String publisher, int offerCount,
			BigDecimal minPrice, BigDecimal maxPrice, boolean removed,
			boolean expired) 
	{
		this.bookId = bookId;
		this.isbn13 = isbn13;
		this.title = title;
		this.authors = authors;
		this.edition = edition;
		this.publisher = publisher;
		this.offerCount = offerCount;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.removed = removed;
		this.expired = expired;
	}
	
	
	/**
	 * @return the isbn
	 */
	public String getIsbn() 
	{
		return isbn13;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) 
	{
		this.isbn13 = isbn;
	}

	/**
	 * @return the offerCount
	 */
	public int getOfferCount() 
	{
		return offerCount;
	}
	
	/**
	 * @param offerCount the offerCount to set
	 */
	public void setOfferCount(int offerCount) 
	{
		this.offerCount = offerCount;
	}
	
	/**
	 * @return the minPrice
	 */
	public BigDecimal getMinPrice() 
	{
		return minPrice;
	}
	
	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(BigDecimal minPrice) 
	{
		this.minPrice = minPrice;
	}
	
	/**
	 * @return the maxPrice
	 */
	public BigDecimal getMaxPrice() 
	{
		return maxPrice;
	}
	
	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(BigDecimal maxPrice) 
	{
		this.maxPrice = maxPrice;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() 
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * @return the edition
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * @param edition the edition to set
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * @return the isbn13
	 */
	public String getIsbn13() {
		return isbn13;
	}

	/**
	 * @param isbn13 the isbn13 to set
	 */
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Long getBookId() 
	{
		// TODO Auto-generated method stub
		return this.bookId;
	}

	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	/**
	 * @return the removed
	 */
	public boolean isRemoved() 
	{
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(boolean removed) 
	{
		this.removed = removed;
	}

	/**
	 * @return the expired
	 */
	public boolean isExpired() 
	{
		return expired;
	}

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(boolean expired) 
	{
		this.expired = expired;
	}
	
}
