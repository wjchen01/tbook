package org.wjchen.textbook.daos;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

import org.wjchen.textbook.models.CourseSite;

@Repository("org.wjchen.textbook.daos.CourseDAO")
public class CourseDAOImpl extends GenericDAOImpl<CourseSite, String> implements CourseDAO {

    @Autowired
    @Qualifier("org.wjchen.textbook.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

}
