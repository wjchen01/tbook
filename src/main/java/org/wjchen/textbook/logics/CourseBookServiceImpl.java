package org.wjchen.textbook.logics;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.textbook.daos.BookDAO;
import org.wjchen.textbook.daos.BookOrderDAO;
import org.wjchen.textbook.daos.CourseDAO;
import org.wjchen.textbook.daos.TextbookDAO;
import org.wjchen.textbook.daos.UserDAO;
import org.wjchen.textbook.models.BookOrder;
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseBookStatus;
import org.wjchen.textbook.models.CourseDetail;
import org.wjchen.textbook.models.CourseSite;
import org.wjchen.textbook.models.CourseUser;
import org.wjchen.textbook.models.SakaiUser;
import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.models.UserProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.textbook.logics.CourseBookService")
@Transactional("textbook")
public class CourseBookServiceImpl implements CourseBookService, Serializable {

	private static final long serialVersionUID = 1L;

	public enum Table_Heading {
		ISBN13("ISBN 13"), 
		Title("Title"), 
		Author("Author"), 
		Publisher("Publisher"),
		Year("Copyright Date"), 
		Status("Required/Recommended"); 
		
		private String messagekey;
		
		private Table_Heading(String messagekey) {
			this.messagekey = messagekey;
		}
		
		public String messagekey() {
			return this.messagekey;
		}
	}
	
	@Autowired
	private UserDAO userDao;

	@Autowired
	private CourseDAO courseDao;

	@Autowired
	private TextbookDAO textbookDao;

	@Autowired
	private BookDAO coursebookDao;

	@Autowired
	private BookOrderDAO orderDao;
	
	@PostConstruct
	public void init() {
		log.info("init");
	}
	
	@Override
	public List<CourseBook> findCourseBooks(String siteNm) {
		Search search = new Search(CourseSite.class);
		search.clearFilters();
		Filter restrict = Filter.equal("siteId", siteNm);
		search.addFilter(restrict);
	
		List<CourseBook> books = new ArrayList<CourseBook>();
		
		List<CourseSite> courses = courseDao.search(search);
		for(CourseSite course : courses) {
			CourseDetail detail = course.getDetail();
			if(detail != null) {
				Set<CourseBook> cbooks = detail.getCourseBooks();
				books.addAll(cbooks);
			}
		}
		
		return books;
	}

	@Override
	public List<Textbook> findTextBookByISBN(String isbn) {
		
		List<Textbook> list = new ArrayList<Textbook>();

		Search search = new Search(Textbook.class);
		search.clearFilters();
		Filter restrict = null;
		if(isbn.length() == 13) {
			restrict = Filter.equal("isbn13", isbn);
		}
		else if(isbn.length() == 10) {
			restrict = Filter.equal("isbn10", isbn);			
		}
		
		if(restrict == null) {
			return list;
		}
		
		search.addFilter(restrict);
		List<Textbook> books = textbookDao.search(search); 
		Set<Textbook> set = new HashSet<Textbook>();
		set.addAll(books);
		
		list.addAll(set);
		
		return list;
	}

	@Override
	public void addCourseBook(String siteNm, String isbn, String userNm, boolean required) {	
		if(isbn != null) {
			Search searchBook = new Search(Textbook.class);
			searchBook.clearFilters();
			if(isbn.length() == 10) {
				Filter restrictBook = Filter.equal("isbn10", isbn);
				searchBook.addFilter(restrictBook);
			}
			else {
				Filter restrictBook = Filter.equal("isbn13", isbn);
				searchBook.addFilter(restrictBook);				
			}
			List<Textbook>books = textbookDao.search(searchBook); 
			if(books.size() > 0) {
				Textbook book = books.get(0);
		
				addCourseBook(siteNm, book, userNm, required);
			}
			else {
				log.warn("ISBN is not valid, {}", isbn);
			}
		}
		else {
			log.warn("ISBN is null");
		}
	}

	@Override
	public void addCourseBook(String siteNm, Textbook book, String userNm, boolean required) {
		Textbook addable;
		if(book.getBookId() == null) {
			List<Textbook> tbooks = findTextbook(book.getIsbn13(), book.getYear());
			if(tbooks.size() > 0) {
				addable = tbooks.get(0);
			}
			else {
				addable = book;
				boolean added = textbookDao.save(addable);
				if(!added) {
					log.warn("Save failed: add textbook, {} by User, {}", addable.getIsbn13(), userNm);
					return;
				}
				else {
					if(addable.getIsbn13() == null && addable.getIsbn10() == null) {
						String isbn = String.format("xxx%d", addable.getBookId());
						addable.setIsbn10(isbn);
						addable.setIsbn13(isbn);
						log.info("Update ISBN: {}", textbookDao.save(addable));
					}
					log.info("Save success: add textbook, {} by User, {}", addable.getIsbn13(), userNm);
				}
			}
		}
		else {
			addable = book;
		}

		CourseSite site = courseDao.find(siteNm);
		if(site == null) {
			log.warn("No site for {}", siteNm);
			return;
		}
		
		CourseDetail course = site.getDetail();
		if(course == null) {
			course = new CourseDetail(site);
			site.setDetail(course);
			log.info("Save: course site, {}: {}", site.getSiteId(), courseDao.save(site));
		}

		String isbn = book.getIsbn13();
		
		boolean repeat = false;
		for(CourseBook cbook : course.getCourseBooks()) {
			String cbook_isbn = cbook.getIsbn();
			if(cbook_isbn.startsWith("xxx")) {
				continue;
			}
			
			if(cbook.getIsbn().equals(isbn)) {
				repeat = true;
				break;
			}
		}
		if(repeat) {
			log.warn("The book, {} has been assigned to this course {}", isbn, siteNm);
			return;
		}
		
		SakaiUser requester = this.findUserByUserNm(userNm);
		if(requester == null) {
			log.warn("The requester is valid: {}", userNm);
			return;
		}
		
		CourseBookStatus status = CourseBookStatus.required;
		if(!required) {
			status = CourseBookStatus.recommended;
		}

		
		this.addCourseBook(course, addable, requester, status);		
	}
	
	@Override
	public void addCourseBook(CourseDetail course, Textbook book, SakaiUser requester, CourseBookStatus status) {
		CourseBook cbook = new CourseBook(course, book, requester, status);	
		course.setWithoutBooks(false);
		if(coursebookDao.save(cbook)) {
			log.info("Save success: Add Textbook, " + cbook.getIsbn() + 
					 " to Course Site, " + cbook.getCourse().getSite().getSiteId() +  
					 " by User, " + requester.getUserNm());
		}
		else {
			log.warn("Save failed: Add Textbook, " + cbook.getIsbn() + 
					 " to Course Site, " + cbook.getCourse().getSite().getSiteId() +  
					 " by User, " + requester.getUserNm());			
		}
		
		Search search = new Search(BookOrder.class);
		String courseId = course.getSite().getSiteId();
		Filter r_courseId = Filter.equal("courseId", courseId);
		String isbn = cbook.getIsbn();
		Filter r_isbn = Filter.equal("isbn", isbn);
		search.addFilter(r_courseId);
		search.addFilter(r_isbn);
		List<BookOrder> orders = orderDao.search(search);
		
		if(orders.size() == 0) {
			Date insertedDate = new Date();
			BookOrder order = new BookOrder();
			order.setCourseId(courseId);
			order.setIsbn(isbn);
			order.setTitle(book.getTitle());
			order.setAuthor(book.getAuthors());
			order.setPublisher(book.getPublisher());
			order.setYear(book.getYear());
			order.setRequired(cbook.getStatus());
			order.setAction("addbook");
			order.setCreatedOn(insertedDate);
			order.setCreatedBy(requester.getUserId());
			saveBookOrder(order,"deletebook");
		}
	}

	@Override
	public void removeCourseBook(String siteNm, String isbn, String userNm) {
		CourseSite site = courseDao.find(siteNm);
		if(site == null) {
			return;
		}
		CourseDetail course = site.getDetail();
		if(course == null) {
			return;
		}
		
		Set<CourseBook> coursebooks = new HashSet<CourseBook>(0);
		coursebooks.addAll(course.getCourseBooks());				
		for(CourseBook cbook : coursebooks) {
			if(cbook.getIsbn().equals(isbn)) {
				this.removeCourseBook(cbook, userNm);
			}
		}		
	}

	@Override
	public void removeCourseBook(CourseBook cbook, String userNm) {
		SakaiUser requester = this.findUserByUserNm(userNm);	
		if(requester == null) {
			return;
		}
		
		this.removeCourseBook(cbook, requester);
	}

	@Override
	public void removeCourseBook(CourseBook cbook, SakaiUser requester) {
		CourseDetail course = cbook.getCourse();
		Set<CourseBook> coursebooks = course.getCourseBooks();
		coursebooks.remove(cbook);

		if(coursebookDao.remove(cbook)) {
			log.info("Delete success: Remove Textbook, " + cbook.getIsbn() + 
					 " from Course Site, " + cbook.getCourse().getSite().getSiteId() +  
					 " by User, " + requester.getUserNm());
		}
		else {
			log.warn("Delete failed: Remove Textbook, " + cbook.getIsbn() + 
					 " from Course Site, " + cbook.getCourse().getSite().getSiteId() +  
					 " by User, " + requester.getUserNm());			
		}
	}

	@Override
	public List<CourseSite> findImportableCourses(String userNm, String siteNm) {
		SakaiUser user = this.findUserByUserNm(userNm);
		
		List<CourseSite> sites = new ArrayList<CourseSite>();
		
		List<CourseUser> courseusers = user.getCourseusers();
		for(CourseUser courseuser : courseusers) {
			if(courseuser.getPermission() == -1) {
				CourseSite site = courseuser.getSite();
				if(site.getSiteId().equals(siteNm)) {
					continue;
				}
				CourseDetail course = site.getDetail();
				if(course == null) {
					continue;
				}
				if(course.getCourseBooks().size() > 0) {
					sites.add(courseuser.getSite());
				}
			}
		}
		
		return sites;
	}

	@Override
	public void importCourseBooks(String siteNm, String userNm, List<CourseSite> sites) {
		CourseSite thisSite = courseDao.find(siteNm);
		CourseDetail thisCourse = thisSite.getDetail();
		if(thisCourse == null) {
			thisCourse = new CourseDetail(thisSite);
			thisSite.setDetail(thisCourse);
			log.info("Save: course site, {}: {}", thisSite.getSiteId(), courseDao.save(thisSite));			
		}
		
		SakaiUser requester = this.findUserByUserNm(userNm);
		
		for(CourseSite site : sites) {
			CourseDetail otherCourse = site.getDetail();
			if(otherCourse == null) {
				continue;
			}
			Set<CourseBook> mbooks = otherCourse.getCourseBooks();
			for(CourseBook mbook : mbooks) {
				String m_isbn = mbook.getIsbn();
				
				Textbook m_tbook = this.findTextbook(mbook);
				if(m_tbook == null) {
					continue;
				}
//				String m_copyright = mbook.getBook().getYear();
				String m_copyright = m_tbook.getYear();
				
				Set<CourseBook> nbooks = thisCourse.getCourseBooks();
				boolean found = false;
				for(CourseBook nbook : nbooks) {
					String n_isbn = nbook.getIsbn();
//					String n_copyright = nbook.getBook().getYear();
					Textbook n_tbook = this.findTextbook(nbook);
					if(n_tbook == null) {
						continue;
					}
					String n_copyright = n_tbook.getYear();
					if(n_isbn.equalsIgnoreCase(m_isbn) && n_copyright.equalsIgnoreCase(m_copyright)) {
						found = true;
						break;
					}
				}
				
				if(!found) {
//					CourseBook nbook = new CourseBook(thisCourse, mbook.getBook(), requester, mbook.getStatus());
					CourseBook nbook = new CourseBook(thisCourse, m_tbook, requester, mbook.getStatus());
					nbook.setRetailPrice(mbook.getRetailPrice());
					if(coursebookDao.save(nbook)) {
						log.info("Save success: Import Textbook, " + nbook.getIsbn() + 
								 " to Course Site, " + nbook.getCourse().getSite().getSiteId() +  
								 " by User, " + requester.getUserNm());
					}
					else {
						log.warn("Save failed: Import Textbook, " + nbook.getIsbn() + 
								 " to Course Site, " + nbook.getCourse().getSite().getSiteId() +  
								 " by User, " + requester.getUserNm());			
					}
				}
			}
		}
	}

	@Override
	public List<Textbook> findTextbook(String isbn, String copyright) {
		List<Textbook> books = new ArrayList<Textbook>();
		if(isbn != null) {
			Search search = new Search(Textbook.class);
			search.clearFilters();
			Filter isbnRestrict = Filter.equal("isbn13", isbn);
			search.addFilter(isbnRestrict);
		
			if(copyright != null) {
				Filter copyrightRestrict = Filter.equal("year", copyright);
				search.addFilter(copyrightRestrict);
			}
			books = textbookDao.search(search);
		}
		
		return books;  		
	}

	@Override
	public boolean isAdminable(String siteNm, String userNm) {
		boolean adminable = false;

		SakaiUser user = this.findUserByUserNm(userNm);
		List<CourseUser> courseusers = user.getCourseusers();
		for(CourseUser courseuser : courseusers) {
			CourseSite site = courseuser.getSite();
			if(site.getSiteId().equals(siteNm)) {
				if(courseuser.getPermission() == -1) {
					adminable = true;
				}
			}
		}
		
		return adminable;
	}

	private SakaiUser findUserByUserNm(String userNm) {
		Search search = new Search(SakaiUser.class);
		Filter filter = Filter.equal("userNm", userNm);
		search.addFilter(filter);

		List<SakaiUser> users = userDao.search(search);
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle(CourseBook book) {
		String title = null;
		
		String isbn = book.getIsbn();
		List<Textbook> tbooks = this.findTextBookByISBN(isbn);
		if(tbooks.size() > 0) {
			title = tbooks.get(0).getTitle();
		}
		
		return title;
//		return book.getBook().getTitle();
	}

	@Override
	public void updateAll(List<CourseBook> books) {
		for(CourseBook book : books) {
			coursebookDao.save(book);
		}
	}
	
	private void saveBookOrder(BookOrder bookOrder, String preAction) {
		if (bookOrder != null) {
			
			// check the term of the course
			if (isValidTermOfCourse(bookOrder.getCourseId())) {
				
				Search search = new Search(BookOrder.class);
				if ("bookorder".equals(preAction)) {					
					// check if there is an existing record
					search.clear();
					search.addFilter(Filter.equal("courseId", bookOrder.getCourseId()));
					search.addFilter(Filter.equal("isbn", bookOrder.getIsbn()));
					search.addFilter(Filter.equal("action", "addbook"));
					List<BookOrder> orders = orderDao.search(search);
					if(orders.size() == 0) {
						search.clear();
						search.addFilter(Filter.equal("courseId", bookOrder.getCourseId()));
						search.addFilter(Filter.equal("isbn", bookOrder.getIsbn()));
						search.addFilter(Filter.equal("action", "deletebook"));
						orders.clear();
						orders = orderDao.search(search);
						if(orders.size() == 0) {
							if(orderDao.save(bookOrder)) {
								log.info("Save success: Add Book Order, " + bookOrder.getId().toString() +
										 ": Textbook, " + bookOrder.getIsbn() +
										 " by User, " + bookOrder.getCreatedBy());
							}
							else {
								log.warn("Save failed: Add Book Order, " + bookOrder.getId().toString() +
										 ": Textbook, " + bookOrder.getIsbn() +
										 " by User, " + bookOrder.getCreatedBy());								
							}
						} else {
							BookOrder existingBO = orders.get(0);
							existingBO.setRequired(bookOrder.getRequired());
							existingBO.setAction(bookOrder.getAction());
							existingBO.setCreatedOn(bookOrder.getCreatedOn());
							existingBO.setCreatedBy(bookOrder.getCreatedBy());
							existingBO.setInsertedDate(bookOrder.getInsertedDate());
							if(orderDao.save(existingBO)) {
								log.info("Save success: Add Book Order, " + existingBO.getId().toString() +
										 ": Textbook, " + existingBO.getIsbn() +
										 " by User, " + existingBO.getCreatedBy());
							}
							else {
								log.warn("Save failed: Add Book Order, " + existingBO.getId().toString() +
										 ": Textbook, " + existingBO.getIsbn() +
										 " by User, " + existingBO.getCreatedBy());								
							}
						}
					} else {
						BookOrder existingBO = orders.get(0);
						existingBO.setRequired(bookOrder.getRequired());
						existingBO.setAction(bookOrder.getAction());
						existingBO.setCreatedOn(bookOrder.getCreatedOn());
						existingBO.setCreatedBy(bookOrder.getCreatedBy());
						existingBO.setInsertedDate(bookOrder.getInsertedDate());
						if(orderDao.save(existingBO)) {
							log.info("Save success: Add Book Order, " + existingBO.getId().toString() +
									 ": Textbook, " + existingBO.getIsbn() +
									 " by User, " + existingBO.getCreatedBy());
						}
						else {
							log.warn("Save failed: Add Book Order, " + existingBO.getId().toString() +
									 ": Textbook, " + existingBO.getIsbn() +
									 " by User, " + existingBO.getCreatedBy());								
						}
					}
				} else {
					// check if there is an existing record
					search.clear();
					search.addFilter(Filter.equal("courseId", bookOrder.getCourseId()));
					search.addFilter(Filter.equal("isbn", bookOrder.getIsbn()));
					search.addFilter(Filter.equal("action", preAction));
					List<BookOrder> orders = orderDao.search(search);
					if(orders.size() == 0) {
						if(orderDao.save(bookOrder)) {
							log.info("Save success: Add Book Order, " + bookOrder.getId().toString() +
									 ": Textbook, " + bookOrder.getIsbn() +
									 " by User, " + bookOrder.getCreatedBy());
						}
						else {
							log.warn("Save failed: Add Book Order, " + bookOrder.getId().toString() +
									 ": Textbook, " + bookOrder.getIsbn() +
									 " by User, " + bookOrder.getCreatedBy());								
						}
					} else {
						BookOrder existingBO = orders.get(0);
						existingBO.setRequired(bookOrder.getRequired());
						existingBO.setAction(bookOrder.getAction());
						existingBO.setCreatedOn(bookOrder.getCreatedOn());
						existingBO.setCreatedBy(bookOrder.getCreatedBy());
						existingBO.setInsertedDate(bookOrder.getInsertedDate());
						if(orderDao.save(existingBO)) {
							log.info("Save success: Add Book Order, " + existingBO.getId().toString() +
									 ": Textbook, " + existingBO.getIsbn() +
									 " by User, " + existingBO.getCreatedBy());
						}
						else {
							log.warn("Save failed: Add Book Order, " + existingBO.getId().toString() +
									 ": Textbook, " + existingBO.getIsbn() +
									 " by User, " + existingBO.getCreatedBy());								
						}
					}
				}				
			}
		}
	}

	private boolean isValidTermOfCourse(String courseId) {
		boolean isCurrent = false;
		
		String[] cfTerms = getCurrentFutureTerms();
		
		CourseSite site = courseDao.find(courseId);
		if(site == null) {
			return false;
		}
		String sTerm = site.getCuCourse().getTerm();
		
		for (String term : cfTerms) {
			if (term.equals(sTerm)) {
				isCurrent = true;
				break;
			}
		}
		
		return isCurrent;
	}

	private String[] getCurrentFutureTerms() {
		String[] terms = new String[3];
		
		String userNm = "sistime0";
		Search search = new Search(SakaiUser.class);
		search.addFilter(Filter.equal("userNm", userNm));
		List<SakaiUser> users = userDao.search(search);
		SakaiUser term = users.get(0);
		for(UserProperty property : term.getProperties()) {
			if(property.getId().getName().equalsIgnoreCase("columbia.current.semester")) {
				terms[0] = property.getValue();
			}
			else if(property.getId().getName().equalsIgnoreCase("columbia.next.semester1")) {
				terms[1] = property.getValue();
			}
			else if(property.getId().getName().equalsIgnoreCase("columbia.next.semester2")) {
				terms[2] = property.getValue();
			}
		}
		
		return terms;		
	}

	@Override
	public boolean loadCourseBooks(String siteNm, String userNm, InputStream excelStream) {
		boolean result = false;
		try {
			Workbook workbook = WorkbookFactory.create(excelStream);
			Sheet sheet = workbook.getSheetAt(0);
			
			for(Row row : sheet) {
				Cell c_isbn = row.getCell(Table_Heading.ISBN13.ordinal());
				Cell c_title = row.getCell(Table_Heading.Title.ordinal());
				Cell c_author = row.getCell(Table_Heading.Author.ordinal());
				Cell c_publisher = row.getCell(Table_Heading.Publisher.ordinal());
				Cell c_year = row.getCell(Table_Heading.Year.ordinal());
				Cell c_status = row.getCell(Table_Heading.Status.ordinal());
				
				String isbn = c_isbn.getStringCellValue();
				String title = c_title.getStringCellValue();
				String author = c_author.getStringCellValue();
				String publisher = c_publisher.getStringCellValue();
				String year = c_year.getStringCellValue();
				String status = c_status.getStringCellValue();
				
				if(row.getRowNum() == 0) {
					if(isbn.equalsIgnoreCase(Table_Heading.ISBN13.messagekey()) &&
						title.equalsIgnoreCase(Table_Heading.Title.messagekey()) &&
						author.equalsIgnoreCase(Table_Heading.Author.messagekey()) &&
						publisher.equalsIgnoreCase(Table_Heading.Publisher.messagekey()) &&
						year.equalsIgnoreCase(Table_Heading.Year.messagekey()) &&
						status.equalsIgnoreCase(Table_Heading.Status.messagekey())) {
						result = true;
					}
					else {
						break;
					}
				}
				else {
					boolean required = false;
					if(status.equalsIgnoreCase("required")) {
						required = true;
					}
					addCourseBook(siteNm, isbn, userNm, required);
				}
			}
		} catch (EncryptedDocumentException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return result;
	}
	
	@Override
	public Textbook findTextbook(CourseBook cbook) {
		Textbook tbook = null;
		
		Long bookId = cbook.getBookId();
		
		if(bookId != null && bookId != 0) {
			tbook = textbookDao.find(bookId);
		}
		
		if(tbook == null) {
			String isbn = cbook.getIsbn();
			List<Textbook> tbooks = this.findTextBookByISBN(isbn);
			if(tbooks.size() > 0) {
				tbook = tbooks.get(0);
			}
		}
		
		return tbook;
	}
}
