package org.wjchen.textbook.web.panels;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import org.wjchen.textbook.web.pages.AddTextbookPage;
import org.wjchen.textbook.web.pages.ImportTextbookPage;

@AuthorizeAction(action = "RENDER", roles = {"ADMIN"})
public class CourseBookNavBarPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public CourseBookNavBarPanel(String id) {
		super(id);
		
		setOutputMarkupId(true);
		
		BookmarkablePageLink<Void> add_book = new BookmarkablePageLink<Void>("add-book", AddTextbookPage.class);
		add_book.add(new Label("label-add-book", new StringResourceModel("admin.bar.action.add", this, null)));
		add(add_book);
		
		BookmarkablePageLink<Void> import_book = new BookmarkablePageLink<Void>("import-book", ImportTextbookPage.class);
		import_book.add(new Label("label-import-book", new StringResourceModel("admin.bar.action.import", this, null)));
		add(import_book);		
	}
	
}