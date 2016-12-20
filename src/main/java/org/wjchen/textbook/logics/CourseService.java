package org.wjchen.textbook.logics;

public interface CourseService {

	public Long getCourseNm(String siteId);
	public void updateComments(String siteId, String comment);
	public String retrieveComments(String siteId);
	public void updateNoBook(String siteId, boolean nobook);
	public boolean noBookSetting(String siteId);
	
}
