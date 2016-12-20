package org.wjchen.textbook.daos;

import java.util.List;

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

import org.wjchen.textbook.models.Realm;
import org.wjchen.textbook.models.RealmRoleFunction;
import org.wjchen.textbook.models.RealmRoleUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:textbook-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("textbook")
public class RealmDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private RealmDAO dao;
	private Search search = new Search(Realm.class);

	@Before
	public void setUp() {
		Filter restrict = Filter.equal("realmKey", 764L);
		search.addFilter(restrict);
	}

	@Test
	public void testCount() {
		Assert.assertEquals("Find Book Ids: ", 1, dao.count(search));		
	}

	@Test
	public void testUser() {
		List<Realm> realms = dao.search(search);
		Assert.assertEquals("Realm Role Function count: ", 1, realms.size());		
		Realm realm = realms.get(0);
		Assert.assertEquals("Realm Realm_ID: ", "/site/b344734f-5b3e-4159-002f-4e5372639b13", realm.getRealmId());
		List<RealmRoleFunction> realmRoleFunctions = realm.getRealmRoleFunctions();
		Assert.assertEquals("Realm RealmRoleFunction count: ", 186, realmRoleFunctions.size());
		List<RealmRoleUser> realmRoleUsers = realm.getRealmRoleUsers();
		Assert.assertEquals("Realm RealmRoleFunction count: ", 33, realmRoleUsers.size());
	}

}
