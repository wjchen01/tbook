package org.wjchen.textbook.utils;

public class TextbookUtil 
{
	/**
	 * @param isbn
	 * @return
	 */
	public static String normalizeISBN(String isbn)
    {
		if(isbn != null && ! isbn.trim().equals(""))
		{
			isbn = isbn.trim().replaceAll("\\p{Punct}", "").replaceAll("\\s", "");
		}
	    return isbn;
    }

	public static boolean formMatchesISBN(String string)
	{
		boolean matches = false;
		
		if(string != null)
		{
			string = normalizeISBN(string);
			matches = string.matches("[0-9]{13}|[0-9]{10}|[0-9]{9}X");
		}
		
		return matches;
	}
	
	/**
	 * @param isbn13
	 * @return
	 */
	public static String calculateISBN10(String isbn13)
	{
		String isbn = normalizeISBN(isbn13);
		isbn = isbn.substring(3, 12);
		isbn += calculateChecksum10(isbn);
		return isbn;
	}
	
	/**
	 * @param countryCode
	 * @param isbn10
	 * @return
	 */
	public static String calculateISBN13(String countryCode, String isbn10)
	{
		String isbn = normalizeISBN(isbn10);
		isbn = countryCode + isbn.substring(0, 9);
		isbn += calculateChecksum13(isbn);
		return isbn;
	}

	/**
	 * @param isbn
	 * @return
	 */
	protected static char calculateChecksum13(String isbn) 
	{
		int checksum = 0;
		for(int i = 0; i < 6; i++)
		{
			char c0 = isbn.charAt(i * 2);
			char c1 = isbn.charAt(i * 2 + 1);
			checksum += (c0 - '0') * 1 + (c1 - '0') * 3;
		}
		checksum = 10 - (checksum % 10);
		char cx = '0';
		if(checksum < 10)
		{
			cx = (char)('0' + checksum);
		}
		return cx;
	}

	/**
	 * @param isbn
	 * @return
	 */
	protected static char calculateChecksum10(String isbn) 
	{
		int checksum = 0;
		int weight = 10;
		char cx;

		for(int i = 0; i < 9; i++)
		{
			char c = isbn.charAt(i);
			checksum += (int)(c - '0') * weight;
			weight--;
		}

		checksum = 11 - (checksum % 11);
		if (checksum == 10)
			cx = 'x';
		else if (checksum == 11)
			cx = '0';
		else
			cx = (char)('0' + checksum);
		return cx;
	}

	public static boolean validISBN(String isbn) 
	{
		boolean valid = false; 
		isbn = isbn.toLowerCase();
		if(isbn.length() == 10)
		{
			char cx = calculateChecksum10(isbn);
			valid = (isbn.charAt(9) == cx);
		}
		else if(isbn.length() == 13)
		{
			char cx = calculateChecksum13(isbn);
			valid = (isbn.charAt(12) == cx);
		}
		return valid;
	}

}
