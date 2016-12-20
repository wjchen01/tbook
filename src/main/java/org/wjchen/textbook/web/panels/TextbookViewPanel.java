package org.wjchen.textbook.web.panels;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.ModalCloseButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.models.CourseBookStatus;
import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.web.models.CourseBookDataProvider;
import org.wjchen.textbook.web.pages.MyTextbookPage;
import org.wjchen.textbook.web.pages.SortCourseBookPage;

public class TextbookViewPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseBookService coursebookService;
	
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();

	public TextbookViewPanel(String id, boolean required) {
		super(id);
		
		setOutputMarkupId(true);
		
		CourseBookDataProvider dataProvider = new CourseBookDataProvider(required);
		
		//
		// Header
		//
		final PageParameters params = new PageParameters();
		if(required) {
			params.add("status", CourseBookStatus.required.toString());
		}
		else {
			params.add("status", CourseBookStatus.recommended.toString());			
		}
		Link<Void> sort_link = new Link<Void>("textbook-sort-link") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				getRequestCycle().setResponsePage(SortCourseBookPage.class, params);
			}
			
			@Override
			public boolean isVisible() {
			    return TextbookSession.get().getRoles().hasRole("ADMIN");
			}

		};
		Label l_book_no = new Label("textbook-no-label", 
				new StringResourceModel("table.header.textbook.no", this, null));	
		sort_link.add(l_book_no);
		add(sort_link);	
		
		Label l_book_isbn = new Label("textbook-isbn-label", 
				new StringResourceModel("table.header.textbook.isbn", this, null));				  
		add(l_book_isbn);
		Label l_book_title = new Label("textbook-title-label", 
				new StringResourceModel("table.header.textbook.title", this, null));				  
		add(l_book_title);
		Label l_book_author = new Label("textbook-author-label", 
				new StringResourceModel("table.header.textbook.author", this, null));				  
		add(l_book_author);
		Label l_book_publisher = new Label("textbook-publisher-label", 
				new StringResourceModel("table.header.textbook.publisher", this, null));				  
		add(l_book_publisher);
		Label l_book_copyright = new Label("textbook-copyright-label", 
				new StringResourceModel("table.header.textbook.copyright", this, null));				  
		add(l_book_copyright);
		Label l_book_price = new Label("textbook-price-label", 
				new StringResourceModel("table.header.textbook.price", this, null));				  
		add(l_book_price);
		
		Label l_book_action = new Label("textbook-action-label", 
				new StringResourceModel("table.header.textbook.action", this, null));				  
		add(l_book_action);
		if(!TextbookSession.get().getRoles().hasRole("ADMIN")) {
			l_book_action.setVisible(false);
		}
		
		//
		// Textbook Items
		//
		final DataView<CourseBook> dataView = new DataView<CourseBook>("table-textbooks", dataProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<CourseBook> item) {
				final CourseBook book = item.getModelObject();
						
				String isbn = book.getIsbn();
				Textbook text = coursebookService.findTextbook(book);
				if(text == null) {
					return;
				}
				
				Integer index = new Integer(item.getIndex() + 1);
				item.add(new Label("textbook-no-item", index.toString()));
//				String isbn = book.getIsbn();
				if(isbn.startsWith("xxx")) {
					isbn = "";
				}
				item.add(new Label("textbook-isbn-item", isbn));
				item.add(new Label("textbook-title-item", text.getTitle()));
				item.add(new Label("textbook-author-item", text.getAuthors()));
				item.add(new Label("textbook-publisher-item", text.getPublisher()));
				item.add(new Label("textbook-copyright-item", text.getYear()));
				BigDecimal price = book.getRetailPrice();
				String s_price;
				if(price.floatValue() > 0.0) {
					s_price = formatter.format(price);
				}
				else {
					s_price = "*";
				}
				item.add(new Label("textbook-price-item", s_price));
								
				Model<String> confirm_model = new Model<String>("Are you sure you want to delete book " + text.getTitle() + "?");
				final TextContentModal dialog = new TextContentModal("textbook-delete-dialog", confirm_model);

				ModalCloseButton del_no = new ModalCloseButton(Model.of("Cancel"));
				dialog.addButton(del_no);
				
				ModalCloseButton del_ok = new ModalCloseButton(Model.of("OK")) {

					private static final long serialVersionUID = 1L;
					
					@Override
				    public void onClick(AjaxRequestTarget target) {
						TextbookSession session = TextbookSession.get();
						String userNm = session.getUserNm();

						coursebookService.removeCourseBook(book, userNm);							
						getRequestCycle().setResponsePage(MyTextbookPage.class);
				    }
				};
				dialog.addButton(del_ok);
				item.add(dialog); 
				
				AjaxLink<Void> del_link = new AjaxLink<Void>("textbook-delete-link") {

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
					    return TextbookSession.get().getRoles().hasRole("ADMIN");
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						dialog.show(target);						
					}
					
				};
				del_link.add(new Label("textbook-delete-label", new Model<String>("Delete")));
				item.add(del_link);
			}
		};
		add(dataView);		
	}

}
