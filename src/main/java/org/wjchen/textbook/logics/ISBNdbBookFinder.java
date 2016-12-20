package org.wjchen.textbook.logics;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.wjchen.textbook.models.Textbook;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
//@Service("org.wjchen.textbook.logics.ISBNdbBookFinder")
public class ISBNdbBookFinder implements TextbookFinder {

	private static final int MAX_BOOKS = 24;

	private static String ISBNDB_URL = "http://isbndb.com/api/books.xml?access_key=";
	private static String ISBN_VALUE = "&index1=isbn&value1=";
	private static String TITLE_VALUE = "&index1=title&value1=";
	private static String RESULTS_DETAILS = "&results=details";
	private static String PAGE_NUMBER = "&results=details&page_number=";
	
	private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

	protected String accessKey;
	
	@Override
	public List<Textbook> getBooks(String isbn) {
		List<Textbook> books = new ArrayList<Textbook>();
		
		if(isbn == null || isbn.trim().equals(""))
		{
			log.debug("unable to get title for null isbn");
			return books;
		}
		else
		{
			// Search initial books through ISBN
			String isbn_url = ISBNDB_URL + accessKey + ISBN_VALUE + isbn.trim() + RESULTS_DETAILS; 
			Document doc = getDocumentFromURL(isbn_url);
			if(doc == null) {
				return books;
			}
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("BookData");				
			extractBooksFromNodes(books, nodes);
				
			if(! books.isEmpty())
			{
				Textbook book = books.get(0);
				String keywords = extractKeywords(book.getTitle());
				String encode_keywords = null;
				try {
					encode_keywords = URLEncoder.encode(keywords.trim(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					log.warn("Invalid characters: {}", e.getMessage());
					e.printStackTrace();
				}
				
				if(encode_keywords == null) {
					return books;
				}
				// Search additional books through the first book title
				String keyword_url = ISBNDB_URL + accessKey + TITLE_VALUE + encode_keywords + RESULTS_DETAILS; 
				doc = getDocumentFromURL(keyword_url);
				root = doc.getDocumentElement();
				nodes = root.getElementsByTagName("BookData");
				extractBooksFromNodes(books, nodes);

				while(books.size() < MAX_BOOKS)
				{
					String total_results_str = getValue(root, "BookList", "total_results");
					String page_size_str = getValue(root, "BookList", "page_size");
					String page_number_str = getValue(root, "BookList", "page_number");
						
					int total_results = Integer.parseInt(total_results_str);
					int page_size = Integer.parseInt(page_size_str);
					int page_number = Integer.parseInt(page_number_str);
						
					if(total_results < page_size * page_number) {
						encode_keywords = null;
						try {
							encode_keywords = URLEncoder.encode(keywords.trim(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							log.warn("Invalid characters: {}", e.getMessage());
							e.printStackTrace();
						}
						if(encode_keywords == null) {
							break;
						}
						
						String page_url = ISBNDB_URL + accessKey + TITLE_VALUE + encode_keywords + PAGE_NUMBER + (page_number + 1);
						doc = getDocumentFromURL(page_url);
						root = doc.getDocumentElement();
						nodes = root.getElementsByTagName("BookData");
						extractBooksFromNodes(books, nodes);
					}
				}	
			}
		}
		
		return books;
	}

	@Override
	public void verifyIsbn(Textbook item) {
		// TODO Auto-generated method stub

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
				
			doc = dBuilder.parse(stream);
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
	
	private void extractBooksFromNodes(List<Textbook> books, NodeList nodes)
    {
		Date now = new Date();

	    for(int i = 0; i < nodes.getLength(); i++)
	    {
	    	Element element = (Element) nodes.item(i);
	    	String nodeISBN = element.getAttribute("isbn").trim();
	    	if(nodeISBN != null)
	    	{
	    		nodeISBN = nodeISBN.replaceAll("\\p{Punct}", "").replaceAll("\\s", "");

	    		String title = getValue(element, "Title");
	    		String author = getValue(element, "AuthorsText");
	    		String publisher = getValue(element, "PublisherText");
	    		String ed = getValue(element, "Details", "edition_info");
	    		String lang = getValue(element, "Details", "language");
	    		//String form = element.getAttribute("form");
	    		//String year = element.getAttribute("year");
	    		
	    		Textbook book = new Textbook();
	    		if(nodeISBN.length() == 10) {
	    			book.setIsbn10(nodeISBN);
	    		}
	    		else if(nodeISBN.length() == 13) {
	    			book.setIsbn13(nodeISBN);
	    		}
	    		book.setTitle(title);
	    		book.setEdition(ed);
	    		book.setAuthors(author);
	    		book.setPublisher(publisher);
	    		book.setRetrievedDate(now);
	    		
	    		//book.setYear(year);
	    		//book.setForms(form.split(" "));
	    		
	    		// lang: The language field uses three-character MARC Code List for Languages.
	    		book.setLanguage(lang);
	    		
	    		books.add(book);
	    	}
	    }		    
    }
	
	private String extractKeywords(String phrase)
    {
	    String keywords = " " + phrase.toLowerCase().trim() + " ";
	    keywords = keywords.replaceAll("\\p{Punct}", " ").replaceAll("\\s+", " ");
	    
	    keywords = keywords.replaceAll(" the ", " ");
	    keywords = keywords.replaceAll(" a ", " ");
	    keywords = keywords.replaceAll(" an ", " ");
	    keywords = keywords.replaceAll(" these ", " ");
	    keywords = keywords.replaceAll(" this ", " ");
	    keywords = keywords.replaceAll(" those ", " ");
	    keywords = keywords.replaceAll(" that ", " ");
	    keywords = keywords.replaceAll(" there ", " ");
	    keywords = keywords.replaceAll(" my ", " ");
	    keywords = keywords.replaceAll(" our ", " ");
	    keywords = keywords.replaceAll(" your ", " ");
	    keywords = keywords.replaceAll(" you ", " ");
	    keywords = keywords.replaceAll(" his ", " ");
	    keywords = keywords.replaceAll(" her ", " ");
	    keywords = keywords.replaceAll(" their ", " ");
	    keywords = keywords.replaceAll(" its ", " ");
	    keywords = keywords.replaceAll(" any ", " ");
	    keywords = keywords.replaceAll(" all ", " ");
	    keywords = keywords.replaceAll(" some ", " ");
	    keywords = keywords.replaceAll(" few ", " ");

	    keywords = keywords.replaceAll(" for ", " ");
	    keywords = keywords.replaceAll(" and ", " ");
	    keywords = keywords.replaceAll(" nor ", " ");
	    keywords = keywords.replaceAll(" but ", " ");
	    keywords = keywords.replaceAll(" or ", " ");
	    keywords = keywords.replaceAll(" yet ", " ");
	    keywords = keywords.replaceAll(" so ", " ");
	    keywords = keywords.replaceAll(" both ", " ");
	    keywords = keywords.replaceAll(" neither ", " ");
	    keywords = keywords.replaceAll(" nor ", " ");
	    keywords = keywords.replaceAll(" only ", " ");
	    keywords = keywords.replaceAll(" either ", " ");
	    
	    keywords = keywords.replaceAll(" from ", " ");
	    keywords = keywords.replaceAll(" in ", " ");
	    keywords = keywords.replaceAll(" of ", " ");
	    keywords = keywords.replaceAll(" after ", " ");
	    keywords = keywords.replaceAll(" before ", " ");
	    keywords = keywords.replaceAll(" although ", " ");
	    keywords = keywords.replaceAll(" unless ", " ");
	    keywords = keywords.replaceAll(" because ", " ");
	    keywords = keywords.replaceAll(" if ", " ");
	    keywords = keywords.replaceAll(" to ", " ");
	    keywords = keywords.replaceAll(" with ", " ");
	    keywords = keywords.replaceAll(" is ", " ");
	    keywords = keywords.replaceAll(" are ", " ");
	    keywords = keywords.replaceAll(" be ", " ");
	    keywords = keywords.replaceAll(" was ", " ");
	    keywords = keywords.replaceAll(" except ", " ");
	    keywords = keywords.replaceAll(" until ", " ");
	    keywords = keywords.replaceAll(" when ", " ");
	    keywords = keywords.replaceAll(" where ", " ");
	    keywords = keywords.replaceAll(" who ", " ");
	    keywords = keywords.replaceAll(" what ", " ");
	    keywords = keywords.replaceAll(" how ", " ");
	    keywords = keywords.replaceAll(" why ", " ");

	    // 	    keywords = keywords.replaceAll(" to ", " ");

	    return keywords;
    }

	private String getValue(Element element, String tagName, String attributeName)
    {
		String value = null;
		NodeList nodes = element.getElementsByTagName(tagName);
		
		Stack<Node> stack = new Stack<Node>();
	    for(int i = 0; i < nodes.getLength(); i++)
	    {
	    	stack.push(nodes.item(i));
	    }

		while(value == null && ! stack.isEmpty())
		{
			Node node = stack.pop();
			if(node.hasChildNodes())
			{
				NodeList moreChildren = node.getChildNodes();
				for(int j = 0; j < moreChildren.getLength(); j++)
				{
					stack.push(moreChildren.item(j));
				}
			}
			if(node instanceof Element)
			{
				Element el = (Element) node;
				value = el.getAttribute(attributeName);
	    	}
	    }
    	return value;
    }

	private String getValue(Element element, String tagName)
    {
		String value = null;
		NodeList nodes = element.getElementsByTagName(tagName);
		
	    for(int i = 0; i < nodes.getLength(); i++)
	    {
	    	Element el = (Element) nodes.item(i);
	
	    	value = el.getNodeValue();
	    	if(value == null && el.hasChildNodes())
	    	{
	    		Stack<Node> stack = new Stack<Node>();
	    		NodeList children = el.getChildNodes();
	    		for(int j = 0; j < children.getLength(); j++)
	    		{
	    			stack.push(children.item(j));
	    		}
	    		while(value == null && ! stack.isEmpty())
	    		{
	    			Node node = stack.pop();
	    			if(node.hasChildNodes())
	    			{
	    				NodeList moreChildren = node.getChildNodes();
	    				for(int j = 0; j < moreChildren.getLength(); j++)
	    				{
	    					stack.push(moreChildren.item(j));
	    				}
	    			}
	    			
	    			switch(node.getNodeType())
	    			{
	    				case Node.TEXT_NODE:
	    				case Node.CDATA_SECTION_NODE:
	    					value = node.getNodeValue();
	    					break;
	    				default:
	    					break;
	    			}
	    		}
	    	}
	    }
	    
    	return value;
    }

}
