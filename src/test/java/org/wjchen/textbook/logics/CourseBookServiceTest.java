package org.wjchen.textbook.logics;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseSite;
import org.wjchen.textbook.models.Textbook;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class CourseBookServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private CourseBookService service;

//	@Test
//	public void testBook() {
//		String siteId = "PEDSM7201_001_2014_1";
//		List<CourseBook> books = service.findCourseBooks(siteId);
//		Assert.assertEquals("PEDSM7201_001_2014_1 book count: ", 3, books.size());
//		
//		siteId = "ANTHX3872_001_2015_1";
//		books = service.findCourseBooks(siteId);
//		Assert.assertEquals("ANTHX3872_001_2015_1 book count: ", 5, books.size());
//	}
	
//	@Test
//	public void testAddCourseBook() {
//		String siteId = "PEDSM7201_001_2014_1";
//		String userId = "77bed6ec-60ec-4535-9f53-37eba6898a45";
//		String isbn = "9780495112587";
//		
//		service.addCourseBook(siteId, isbn, userId, true);
//		
//		List<CourseBook> books = service.findCourseBooks(siteId);
//		Assert.assertEquals("PEDSM7201_001_2014_1 book count: ", 4, books.size());			
//	}

//	@Test
//	public void testRemoveCourseBook() {
//		String siteId = "PEDSM7201_001_2014_1";
//		String userId = "77bed6ec-60ec-4535-9f53-37eba6898a45";
//		String isbn = "9780495112587";
//		
//		service.addCourseBook(siteId, isbn, userId, true);
//	
//		service.removeCourseBook(siteId, isbn, userId);
//		
//		List<CourseBook> books = service.findCourseBooks(siteId);
//		Assert.assertEquals("PEDSM7201_001_2014_1 book count: ", 3, books.size());			
//	}

//	@Test
//	public void testImportableCourseBooks() {
//		String siteId = "ANTHX3872_001_2015_1";
//		String userId = "77bed6ec-60ec-4535-9f53-37eba6898a45";
//		
//		List<CourseSite> sites = service.findImportableCourses(userId, siteId);
//		Assert.assertEquals("Importable book count: ", 1, sites.size());			
//	}

	@Test
	public void testAddCourseBook() {
		String siteId = "TLT_TEST_2013_SITE";
		String userId = "wc2442";
		boolean required = false;
		
		String isbn13 = "xxx14892";
		String authors = "Test author";
		String publisher = "Test publisher";
		String copyright = "c2015";
		String title = "Test title";
//		Textbook.Type type = Textbook.Type.manual;
		
		Textbook book = new Textbook();
		book.setAuthors(authors);
		book.setIsbn13(isbn13);
		book.setPublisher(publisher);
		book.setYear(copyright);
		book.setTitle(title);
//		book.setType(type);
		
//		String isbn = "9780138053260";
//		List<Textbook> tbooks = service.findTextBookByISBN(isbn);
//		if(tbooks.size() > 0) {
//			Textbook book = tbooks.get(0);
			service.addCourseBook(siteId, book, userId, required);
//		}
		List<CourseBook> books = service.findCourseBooks(siteId);
		Assert.assertEquals("Importable book count: ", 2, books.size());			
	}

}
