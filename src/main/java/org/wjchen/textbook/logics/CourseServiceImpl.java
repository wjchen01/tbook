package org.wjchen.textbook.logics;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.wjchen.textbook.daos.CourseDAO;
import org.wjchen.textbook.models.CourseDetail;
import org.wjchen.textbook.models.CourseSite;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.textbook.logics.CourseService")
@Transactional("textbook")
public class CourseServiceImpl implements CourseService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CourseDAO courseDao;

	@Override
	public void updateComments(String siteId, String comment) {
		CourseSite site = courseDao.find(siteId);
		CourseDetail course = site.getDetail();
		if(course == null) {
			course = new CourseDetail(site);
			site.setDetail(course);
		}
		course.setComments(comment);
		
		log.info("Save: course site, {}, update comment: {}", siteId, courseDao.save(site));
	}

	@Override
	public String retrieveComments(String siteId) {
		String comment = null;

		CourseSite site = courseDao.find(siteId);
		CourseDetail course = site.getDetail();
		if(course != null) {
			comment = course.getComments();
		}
		
		return comment;
	}

	@Override
	public Long getCourseNm(String siteId) {
		CourseSite site = courseDao.find(siteId);
		CourseDetail course = site.getDetail();
		if(course == null) {
			course = new CourseDetail(site);
			site.setDetail(course);
			log.info("Save: course site, {}, get course id: {}", site.getSiteId(), courseDao.save(site));
		}
		
		return course.getId();
	}

	@Override
	public void updateNoBook(String siteId, boolean nobook) {
		CourseSite site = courseDao.find(siteId);
		CourseDetail course = site.getDetail();
		if(course == null) {
			course = new CourseDetail(site);
			site.setDetail(course);
		}
		course.setWithoutBooks(nobook);		
		
		log.info("Save: course site, {}, update no book: {}", site.getSiteId(), courseDao.save(site));
	}

	@Override
	public boolean noBookSetting(String siteId) {
		boolean nobook = false;
		
		CourseSite site = courseDao.find(siteId);
		CourseDetail course = site.getDetail();
		if(course != null) {
			nobook = course.isWithoutBooks();
		}
		
		return nobook;
	}

}
