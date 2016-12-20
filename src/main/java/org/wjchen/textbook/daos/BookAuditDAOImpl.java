package org.wjchen.textbook.daos;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

import org.wjchen.textbook.models.BookAudit;

@Repository("org.wjchen.textbook.daos.BookAuditDAO")
public class BookAuditDAOImpl extends GenericDAOImpl<BookAudit, Long>implements BookAuditDAO {

    @Autowired
    @Qualifier("org.wjchen.textbook.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
    	super.setSessionFactory(sessionFactory);
    }

}
