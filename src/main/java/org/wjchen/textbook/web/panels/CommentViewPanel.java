package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseService;
import org.wjchen.textbook.web.pages.EditCommentPage;

public class CommentViewPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseService courseService;
	
	public CommentViewPanel(String id) {
		super(id);
		
		String siteId = TextbookSession.get().getSiteNm();
		String comment = courseService.retrieveComments(siteId);
		
		BookmarkablePageLink<Void> comment_link = new BookmarkablePageLink<Void>("textbook-comment-link", EditCommentPage.class);
		add(comment_link);
		comment_link.setVisible(TextbookSession.get().getRoles().hasRole("ADMIN"));
		
		MultiLineLabel comment_area = new MultiLineLabel("textbook-comments", Model.of(comment));
		comment_area.setEscapeModelStrings(false);
		add(comment_area);
	}

}
