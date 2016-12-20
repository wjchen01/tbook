package org.wjchen.textbook.logics;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.utils.TextbookUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.textbook.logics.TextbookFinder")
@Transactional("textbook")
public class WorldCatBookFinder implements TextbookFinder {

	private static final Map<String,String> onixFormCodes = new HashMap<String, String>();
	
	private static final Map<String, String> ENCODINGS = new HashMap<String, String>();
	
	static
	{
		ENCODINGS.put("&quot;",	"\"");
		ENCODINGS.put("&amp;",	"\u0026");
		ENCODINGS.put("&lt;",	"\u003C");
		ENCODINGS.put("&gt;",	"\u003E");
		ENCODINGS.put("&nbsp;",	"\u00A0");		
	}

	static
	{
		onixFormCodes.put("AA", "Audio");
		onixFormCodes.put("BA", "Book");
		onixFormCodes.put("BB", "Hardcover");
		onixFormCodes.put("BC", "Paperback");
		onixFormCodes.put("DA", "Digital");
		onixFormCodes.put("FA", "Film or transparency");
		onixFormCodes.put("MA", "Microform");
		onixFormCodes.put("VA", "Video");
	}

	@Autowired
	private BookFinderAuditor bookFinderAuditor;
	
	private String countryCode = "978";
	
	private String simpleWSUrl = "http://www.worldcat.org/webservices/catalog/content/isbn/";
	private String wsKey = "?wskey=yV5Lm4vV8glm2lvN4gr7fB6p2XkiZkPGSXvFzg5SBzyWvGCp7HSoe7ItWEpC6uObyUDmVsJLAitdkjF4";

	private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

	@Override
	public List<Textbook> getBooks(String isbn) {
		List<Textbook> books = new ArrayList<Textbook>();
		
		boolean succeeded = false;
		String result = "No books retrieved: ";

		String urlStr = this.simpleWSUrl;
		if(TextbookUtil.validISBN(isbn)) {
			urlStr += isbn.trim() + this.wsKey;
			Document doc = getDocumentFromURL(urlStr);
			Element root = doc.getDocumentElement();
			if (!"diagnostics".equalsIgnoreCase(root.getNodeName())) {
				succeeded = extractBooksFromMarcXML(isbn, books, root);
			}
		}
		else
		{
			result = "ISBN was not valid. No request to xISBN: " + isbn;
		}

		// Move the exact book to the beginning of the list
		if(succeeded && books != null && ! books.isEmpty())
		{
			// sort(isbn, books);
			
			result = "retrieved " + books.size() + " books";
			
			StringBuilder relatedBooksBuilder = new StringBuilder();
			for(Textbook book : books)
			{
				String book_isbn = book.getIsbn13();
				if(book_isbn == null || book_isbn.trim().equals(""))
				{
					book_isbn = book.getIsbn10();
				}
				relatedBooksBuilder.append(' ');
				relatedBooksBuilder.append(book_isbn);
			}
			
			for(Textbook book : books)
			{
				String relatedBooks = relatedBooksBuilder.toString();
				String book_isbn = book.getIsbn13();
				if(book_isbn == null || book_isbn.trim().equals(""))
				{
					book_isbn = book.getIsbn10();
				}
				if(relatedBooks != null && book_isbn != null && ! book_isbn.trim().equals(""))
				{
					relatedBooks = relatedBooks.replace(book_isbn, "").replaceAll("\\s\\s", " ");
					book.setRelatedBooks(relatedBooks);
				}
			}
			
			report(urlStr, result);
			
			if(! succeeded)
			{
				throw new RuntimeException("BookFinder failed");
			}
		}

		return books;
	}

	@Override
	public void verifyIsbn(Textbook item) {
		String isbn13 = item.getIsbn13();
		String isbn10 = item.getIsbn10();
		if(isbn13 == null && isbn10 != null)
		{
			String new_isbn = TextbookUtil.calculateISBN13(countryCode, isbn10);
			if(new_isbn != null && new_isbn.length() == 13)
			{
				item.setIsbn13(new_isbn);
			}
		}
		else if(isbn10 == null && isbn13 != null)
		{
			String new_isbn = TextbookUtil.calculateISBN10(isbn13);
			if(new_isbn != null && new_isbn.length() == 10)
			{
				item.setIsbn10(new_isbn);
			}
		}
	}

	private Document getDocumentFromURL(String s_url) {
	    URL url = null;
		HttpURLConnection conn = null;
		InputStream stream = null;

		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			url = new URL(s_url);
			conn = (HttpURLConnection) url.openConnection();
			stream = conn.getInputStream();
			InputSource inputSource = new InputSource(stream);	
			doc = dBuilder.parse(inputSource);
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if(stream != null) {
					stream.close();
				}
				if(conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
			
		return doc;
	}

	private String decode(String text)
	{
		StringBuilder buf = new StringBuilder();
		while(text != null && text.length() > 0)
		{
			// find index of '&'
			int ampIndex = text.indexOf('&');
			if(ampIndex > 0)
			{
				String preAmp = text.substring(0, ampIndex);
				buf.append(preAmp);
				
				text = text.substring(ampIndex);
				// text now starts with '&'
				
				int scIndex = text.indexOf(';');
				if(scIndex > 0)
				{
					String code = text.substring(0, scIndex + 1);
					if(ENCODINGS.containsKey(code))
					{
						buf.append(ENCODINGS.get(code));
						if(text.length() > scIndex)
						{
							text = text.substring(scIndex + 1);
						}
						else
						{
							text = "";
						}
					}
					else
					{
						buf.append('&');
						if(text.length() > 1)
						{
							text = text.substring(1);
						}
						else
						{
							text = "";
						}
					}
				}
				else
				{
					buf.append(text);
					text = "";
				}
			}
			if(text != null && text.length() > 0 && ! text.contains("&"))
			{
				buf.append(text);
				text = "";
			}
		}
		return buf.toString();
	}

	private void report(String url, String result)
	{
		if(this.bookFinderAuditor != null)
		{
			this.bookFinderAuditor.report(url, result);
		}
	}

	private boolean extractBooksFromMarcXML(String isbn, List<Textbook> books, Element root) {		
		String blank = " ";
		String separator = " ; ";
		String title = "";
		String author = "";
		String authorAlter = "";
		String publisher = "";
		String year = "";
		Date now = new Date();
		
		// check nodes
		NodeList nodes = root.getElementsByTagName("datafield");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = (Element)nodes.item(i);
			String tag = element.getAttribute("tag");
			if ("100".equalsIgnoreCase(tag)) { // author
				NodeList children = element.getElementsByTagName("subfield");
				for (int k = 0; k < children.getLength() ; k++) {
					Element child = (Element)children.item(k);
					if ("a".equalsIgnoreCase(child.getAttribute("code"))) {
						author += separator+child.getTextContent();
					}
				}
			} else if ("245".equalsIgnoreCase(tag)) { // title
				NodeList children = element.getElementsByTagName("subfield");
				for (int k = 0; k < children.getLength() ; k++) {
					Element child = (Element)children.item(k);
					if ("a".equalsIgnoreCase(child.getAttribute("code"))) {
						title += blank+child.getTextContent();
					} else if ("b".equalsIgnoreCase(child.getAttribute("code"))) {
						title += blank+child.getTextContent();
					}
				}
			} else if ("260".equalsIgnoreCase(tag)) { // publisher and year
				NodeList children = element.getElementsByTagName("subfield");
				for (int k = 0; k < children.getLength() ; k++) {
					Element child = (Element)children.item(k);
					if ("b".equalsIgnoreCase(child.getAttribute("code"))) {
						if (publisher.isEmpty()) publisher += blank+child.getTextContent();
					} else if ("c".equalsIgnoreCase(child.getAttribute("code"))) {
						if (year.isEmpty()) year += blank+child.getTextContent();
					}
				}
			} else if ("264".equalsIgnoreCase(tag)) { // publisher and year
				NodeList children = element.getElementsByTagName("subfield");
				for (int k = 0; k < children.getLength() ; k++) {
					Element child = (Element)children.item(k);
					if ("b".equalsIgnoreCase(child.getAttribute("code"))) {
						if (publisher.isEmpty()) publisher += blank+child.getTextContent();
					} else if ("c".equalsIgnoreCase(child.getAttribute("code"))) {
						if (year.isEmpty()) year += blank+child.getTextContent();
					}
				}
			} else if ("700".equalsIgnoreCase(tag)) { // author
				NodeList children = element.getElementsByTagName("subfield");
				for (int k = 0; k < children.getLength() ; k++) {
					Element child = (Element)children.item(k);
					if ("a".equalsIgnoreCase(child.getAttribute("code"))) {
						authorAlter += separator+child.getTextContent();
					}
				}
			} else if ("720".equalsIgnoreCase(tag)) { // author
				NodeList children = element.getElementsByTagName("subfield");
				for (int k = 0; k < children.getLength() ; k++) {
					Element child = (Element)children.item(k);
					if ("a".equalsIgnoreCase(child.getAttribute("code"))) {
						authorAlter += separator+child.getTextContent();
					}
				}
			}
	    }
	    
		//
		if (title.length() > 0) {
			Textbook book = new Textbook();
			if(isbn.length()==10) {
				book.setIsbn10(isbn);
			}
			else {
				book.setIsbn13(isbn);
			}
			book.setTitle(decode(title.replaceAll("/", "").trim()));
			String authorUsed = author.isEmpty()?authorAlter.substring(3).replaceAll("/", "").trim():author.substring(3).replaceAll("/", "").trim();
			book.setAuthors(decode(authorUsed));
			book.setPublisher(decode(publisher.replaceAll("/", "").trim()));
			book.setYear(year.trim());
			book.setRetrievedDate(now);
			try {
				verifyIsbn(book);
			} catch (Exception e) {
				log.warn("BookFinder unavailable while verifying ISBN: " + isbn);
			}
			books.add(book);
		}
		
	    //
		boolean success = true;
	    return success;
	}
	
}
