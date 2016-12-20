package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.web.models.CourseBookViewList;
import org.wjchen.textbook.web.pages.MyTextbookPage;

public class BookItemPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseBookService coursebookService;
	
	public BookItemPanel(String id, CompoundPropertyModel<Textbook> model) {
		super(id, model);
		
		setOutputMarkupId(true);
		
		final Textbook book = model.getObject();
		
		add(new Label("label-book-isbn", new ResourceModel("textbook.result.isbn")));
		add(new Label("label-book-author", new ResourceModel("textbook.result.authors")));
		add(new Label("label-book-publisher", new ResourceModel("textbook.result.publisher")));
		add(new Label("label-book-copyright", new ResourceModel("textbook.result.copyright")));

		add(new Label("item-book-title", book.getTitle()));
		add(new Label("item-book-isbn", book.getIsbn13()));
		add(new Label("item-book-author", book.getAuthors()));
		add(new Label("item-book-publisher", book.getPublisher()));
		add(new Label("item-book-copyright", book.getYear()));
		
		Link<Void> required_link = new Link<Void>("add-as-required-link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				TextbookSession session = TextbookSession.get();
				String siteNm = session.getSiteNm();
				String userNm = session.getUserNm();
				String isbn = book.getIsbn13();
				coursebookService.addCourseBook(siteNm, isbn, userNm, true);		
				CourseBookViewList list = session.getCbooks();
				list.getBooks().clear();
				list.addBooks(coursebookService.findCourseBooks(siteNm));
				getRequestCycle().setResponsePage(MyTextbookPage.class);
			}
			
		};
		required_link.add(new Label("add-as-required-label", new Model<String>("Add as Required")));
		add(required_link);
		
		Link<Void> recommanded_link = new Link<Void>("add-as-recommanded-link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				TextbookSession session = TextbookSession.get();
				String siteNm = session.getSiteNm();
				String userNm = session.getUserNm();
				String isbn = book.getIsbn13();
				coursebookService.addCourseBook(siteNm, isbn, userNm, false);
				CourseBookViewList list = session.getCbooks();
				list.getBooks().clear();
				list.addBooks(coursebookService.findCourseBooks(siteNm));
				getRequestCycle().setResponsePage(MyTextbookPage.class);
			}
			
		};
		recommanded_link.add(new Label("add-as-recommanded-label", new Model<String>("Add as Recommanded")));
		add(recommanded_link);		
	}
	
}
