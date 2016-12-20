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
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseDetail;
import org.wjchen.textbook.models.CourseSite;
import org.wjchen.textbook.models.SakaiUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class UserDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UserDAO dao;
	private Search search = new Search(SakaiUser.class);

	@Before
	public void setUp() {
		Filter restrict = Filter.equal("userNm", "dlj2122");
		search.addFilter(restrict);
	}

	@Test
	public void testCount() {
		Assert.assertEquals("Find Book Ids: ", 1, dao.count(search));		
	}

	@Test
	public void testUser() {
		List<SakaiUser> users = dao.search(search);
		SakaiUser user = users.get(0);
		Assert.assertEquals("User USER_ID: ", "0386507d-cc8d-4dc4-9b32-2414e364bd6e", user.getUserId());
//		List<CourseSite> courses = user.getCourses();
//		Assert.assertEquals("Courses count: ", 33, courses.size());
//		Set<CourseBook> courseBooks = new HashSet<CourseBook>();
//		for(CourseSite course : courses) {
//			CourseDetail detail = course.getDetail();
//			if(detail != null) {
//				Set<CourseBook> cbooks = detail.getCourseBooks();
//				courseBooks.addAll(cbooks);
//			}
//		}
//		Assert.assertEquals("Course Books count: ", 44, courseBooks.size());
//		Set<Textbook> books = new HashSet<Textbook>();
//		for(CourseBook courseBook : courseBooks) {
//			Textbook book = courseBook.getBook();
//			books.add(book);
//		}
//		Assert.assertEquals("Books count: ", 34, books.size());
//		for(Textbook book : books) {
//			System.out.println(book.getTitle());
//		}
	}

}
