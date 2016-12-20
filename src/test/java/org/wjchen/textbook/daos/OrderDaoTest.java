package org.wjchen.textbook.daos;

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

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.textbook.models.BookOrder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class OrderDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private BookOrderDAO dao;
	private Search search = new Search(BookOrder.class);


	@Test
	public void testCourse() {
		search.addFilter(Filter.equal("courseId", "ACTUK4821_001_2016_1"));
		search.addFilter(Filter.equal("isbn", "9781439082539"));
		List<BookOrder> orders = dao.search(search);
		Assert.assertEquals("Order count: ",0, orders.size());
	}

}
