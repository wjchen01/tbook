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

import org.wjchen.textbook.models.Textbook;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class WorldCatBookFinderTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private TextbookFinder service;

	@Test
	public void testFindBookByISBN() {
		String isbn = "9780495112587";
		List<Textbook> books = service.getBooks(isbn);
		Assert.assertEquals("9780495112587 first book isbn", "9780495112587", books.get(0).getIsbn13());		
	}

}
