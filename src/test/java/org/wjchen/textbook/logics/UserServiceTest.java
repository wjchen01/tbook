package org.wjchen.textbook.logics;

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

import org.wjchen.textbook.logics.UserService;
import org.wjchen.textbook.models.SakaiUser;
import lombok.Getter;
import lombok.Setter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	@Getter @Setter
	private UserService service;

	@Test
	public void testUser() {
		String uni = "wc2442";
		SakaiUser user = service.findByUserNm(uni);
		Assert.assertEquals("User USER_ID: ", "77bed6ec-60ec-4535-9f53-37eba6898a45", user.getUserId());
		Assert.assertEquals("UserDetail LAST_NAME: ", "Chen", user.getDetail().getLastName());
	}
	
}
