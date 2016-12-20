package org.wjchen.textbook.daos;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

import org.wjchen.textbook.models.Textbook;

@Repository("org.wjchen.textbook.daos.TextbookDAO")
public class TextbookDAOImpl extends GenericDAOImpl<Textbook, Long>implements TextbookDAO {

	@Autowired
    @Qualifier("org.wjchen.textbook.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

}
