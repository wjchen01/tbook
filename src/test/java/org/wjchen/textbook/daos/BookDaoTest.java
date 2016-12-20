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

import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseDetail;
//import org.wjchen.textbook.models.CourseSite;
import org.wjchen.textbook.models.Textbook;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class BookDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private BookDAO dao;
	private Search search = new Search(CourseBook.class);

	@Test
	public void testBook() {
//		Filter isbnRestrict = Filter.equal("isbn", "9781439082539");
//		Filter typeRestrict = Filter.equal("types", Textbook.Types.manual);
//		search.clearFilters();
//		search.addFilter(isbnRestrict);
//		search.addFilter(typeRestrict);
//		search.setDisjunction(false);
//		List<CourseBook> cbooks = dao.search(search);
//		Assert.assertEquals("Book count from DB: ", 32, cbooks.size());
//		
//		Set<CourseDetail> details = new HashSet<CourseDetail>();
//		for(CourseBook cbook : cbooks) {
//			CourseDetail detail = cbook.getCourse();
//			details.add(detail);
//		}
//		Assert.assertEquals("Book count Courses: ", 32, details.size());			
	}

	@Test
	public void testCourse() {
//		String siteId = "PEDSM7201_001_2014_1";
//		Filter siteRestrict = Filter.equal("course.course.siteId", siteId);
//		search.clearFilters();
//		search.addFilter(siteRestrict);
//		List<CourseBook> cbooks = dao.search(search);
//		Assert.assertEquals("Course Book count: ", 32, cbooks.size());	
		
		Long id = 47784L;
		Assert.assertTrue("Remove entry 47784", dao.removeById(id));
	}

}
