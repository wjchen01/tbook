package org.wjchen.textbook.daos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
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

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.models.ColumbiaCourse;
import org.wjchen.textbook.models.ColumbiaCourseTitle;
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseDetail;
import org.wjchen.textbook.models.CourseSite;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class CourseDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private CourseDAO dao;
	private Search search = new Search(CourseSite.class);

	@Before
	public void setUp() {
		Filter restrict = Filter.equal("siteId", "PEDSM7201_001_2014_1");
		search.addFilter(restrict);
	}

	@Test
	public void testCount() {
		Assert.assertEquals("Find Book Ids: ", 1, dao.count(search));		
	}

	@Test
	public void testCourse() {
//		List<CourseSite> courses = dao.search(search);
//		Assert.assertEquals("Courses count: ", 1, courses.size());
//		CourseSite course = courses.get(0);
//		Assert.assertNotNull(course);
//		CourseDetail detail = course.getDetail();
//		Assert.assertNotNull(detail);
//		Set<CourseBook> courseBooks = detail.getCourseBooks();
//		Assert.assertEquals("Course Books count: ", 3, courseBooks.size());
//		Set<Textbook> books = new HashSet<Textbook>();
//		for(CourseBook courseBook : courseBooks) {
//			Textbook book = courseBook.getBook();
//			books.add(book);
//		}
//		Assert.assertEquals("Books count: ", 3, books.size());
//		for(Textbook book : books) {
//			System.out.println(book.getTitle());
//		}
	}

	@Test
	public void testCourseTitle() {
//		List<CourseSite> courses = dao.search(search);
//		Assert.assertEquals("Courses count: ", 1, courses.size());
//		CourseSite course = courses.get(0);
//		ColumbiaCourse cuCourse = course.getCuCourse();
//		Assert.assertEquals("Columbia Courses Title: ", "PEDIATRICS CLERKSHIP", cuCourse.getTitle());
//		ColumbiaCourseTitle cuTitle = course.getCuTitle();
//		Assert.assertNull("CU Courses Title: ", cuTitle);
	}	
}
