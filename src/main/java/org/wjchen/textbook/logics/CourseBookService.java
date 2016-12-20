package org.wjchen.textbook.logics;

import java.io.InputStream;
import java.util.List;

import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseBookStatus;
import org.wjchen.textbook.models.CourseDetail;
import org.wjchen.textbook.models.CourseSite;
import org.wjchen.textbook.models.SakaiUser;
import org.wjchen.textbook.models.Textbook;

public interface CourseBookService {

	public List<CourseSite> findImportableCourses(String userNm, String siteNm);
	public List<CourseBook> findCourseBooks(String siteNm);
	public List<Textbook> findTextBookByISBN(String isbn);
	public List<Textbook> findTextbook(String isbn, String copyright);
	public void addCourseBook(String siteNm, String isbn, String userNm, boolean required);
	public void addCourseBook(String siteNm, Textbook book, String userNm, boolean required);
	public void addCourseBook(CourseDetail course, Textbook book, SakaiUser requester, CourseBookStatus status);
	public void removeCourseBook(String siteNm, String isbn, String userNm);
	public void removeCourseBook(CourseBook book, String userNm);	
	public void removeCourseBook(CourseBook book, SakaiUser requester);	
	public void importCourseBooks(String siteNm, String userNm, List<CourseSite> sites);
	public boolean isAdminable(String siteNm, String userNm);
	public String getTitle(CourseBook book);
	public void updateAll(List<CourseBook> books);
	public boolean loadCourseBooks(String siteNm, String userNm, InputStream xlsxStream);
	public Textbook findTextbook(CourseBook cbook);
	
}
