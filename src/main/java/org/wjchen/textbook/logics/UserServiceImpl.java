package org.wjchen.textbook.logics;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.textbook.daos.UserDAO;
import org.wjchen.textbook.models.SakaiUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.textbook.logics.UserService")
@Transactional("textbook")
public class UserServiceImpl implements UserService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UserDAO dao;

	@PostConstruct
	public void init() {
		log.info("init");
	}

	@Override
	public SakaiUser findByUserId(String userId) {
		return dao.find(userId);
	}

	@Override
	public SakaiUser findByUserNm(String userNm) {
		Search search = new Search(SakaiUser.class);
		Filter filter = Filter.equal("userNm", userNm);
		search.addFilter(filter);

		List<SakaiUser> users = dao.search(search);
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

}
