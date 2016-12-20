package org.wjchen.textbook.logics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.wjchen.textbook.daos.BookAuditDAO;
import org.wjchen.textbook.models.BookAudit;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Service("org.wjchen.textbook.logics.BookFinderAuditor")
@Transactional("textbook")
public class BookFinderAuditorImpl implements BookFinderAuditor {

	@Autowired
	private BookAuditDAO dao;
	
	@Override
	public void report(String url, String result) {
		BookAudit report = new BookAudit(url, result);
		dao.save(report);
	}

}
