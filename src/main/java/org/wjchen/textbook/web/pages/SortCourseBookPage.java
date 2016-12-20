package org.wjchen.textbook.web.pages;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable;
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable.HashListView;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.web.models.CourseBookViewList;
import edu.emory.mathcs.backport.java.util.Collections;

public class SortCourseBookPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private static CourseBookService coursebookService;
	
	private boolean required = true;
	
	public SortCourseBookPage(final PageParameters p) {
		super();
		
		String cbook_type = p.get("status").toString();
		if(!cbook_type.equalsIgnoreCase("required")) {
			required = false;
		}
		this.setPageTitle("Textbook Order");
		initBreadCrumbs(this);

		CourseBookViewList viewList = TextbookSession.get().getCbooks();
		final List<CourseBook> books;
		if(required) {
			books = viewList.getRequiredBookList();
		}
		else {
			books = viewList.getRecommendedBookList();
		}
		Collections.sort(books, CourseBook.SequenceComparator);
				
		// FeedbackPanel //
		final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
		this.add(feedback.setOutputMarkupId(true));

		final Sortable<CourseBook> sortable = new Sortable<CourseBook>("coursebook-sortable", books) {

			private static final long serialVersionUID = 1L;

			@Override
			protected HashListView<CourseBook> newListView(IModel<List<CourseBook>> model)
			{
				return SortCourseBookPage.newListView("coursebook-sortable-items", model);
			}

			@Override
			public void onUpdate(AjaxRequestTarget target, CourseBook item, int index)
			{
				super.onUpdate(target, item, index);

				target.add(feedback);
			}

		};
		add(sortable);
		
		Link<Void> save_link = new Link<Void>("coursebook-sortable-save") {

			private static final long serialVersionUID = 1L;
			 
			@Override
			public void onClick() {
				for(int i = 0; i < books.size(); i++) {
					CourseBook book = books.get(i);
					book.setSequence(new Long(i));
				}
				coursebookService.updateAll(books);
				
				getRequestCycle().setResponsePage(MyTextbookPage.class);				
			}
		};
		add(save_link);

		Link<Void> cancel_link = new Link<Void>("coursebook-sortable-cancel") {

			private static final long serialVersionUID = 1L;
			 
			@Override
			public void onClick() {
				for(int i = 0; i < books.size(); i++) {
					CourseBook book = books.get(i);
					book.setSequence(new Long(i));
				}
				getRequestCycle().setResponsePage(MyTextbookPage.class);				
			}
		};
		add(cancel_link);
		
		Link<Void> reset_link = new Link<Void>("coursebook-sortable-reset") {

			private static final long serialVersionUID = 1L;
			 
			@Override
			public void onClick() {
				getRequestCycle().setResponsePage(SortCourseBookPage.class, p);				
			}
		};
		add(reset_link);
	}
	
	protected static HashListView<CourseBook> newListView(String id, IModel<List<CourseBook>> model)
	{
		return new HashListView<CourseBook>(id, model) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CourseBook> item) {
				CourseBook book = item.getModelObject();
				item.add(new Label("coursebook-isbn", book.getIsbn()));
				item.add(new Label("coursebook-title", coursebookService.getTitle(book)));
				item.add(AttributeModifier.append("class", "ui-state-default"));				
			}
		};
	}

	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "SortCourseBookPage.css")));
	}

}
