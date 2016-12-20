package org.wjchen.textbook.web.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.logics.CourseService;
import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.utils.TextbookUtil;
import org.wjchen.textbook.web.models.CourseBookViewList;
import org.wjchen.textbook.web.pages.MyTextbookPage;
import org.wjchen.textbook.web.validators.IsbnLengthValidator;
import lombok.Getter;
import lombok.Setter;

public class NewTextbookPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseBookService coursebookService;
	
	@SpringBean
	private CourseService courseService;

	private TextField<String> isbn_field;
	private TextField<String> title_field;
	private TextField<String> authors_field;
	private TextField<String> publisher_field;
	private TextField<String> copyright_field;
	
	@Getter @Setter
	private final class NewTextbookForm extends Form<String> {

		private static final long serialVersionUID = 1L;
		
		private String isbn;
		private String title;
		private String authors;
		private String publisher;
		private String copyright;
		
		public NewTextbookForm(String id) {
			super(id);
			
			setOutputMarkupId(true);
			
			final FeedbackPanel feedback = new FeedbackPanel("new-book-feedback");
			feedback.setOutputMarkupId(true);

			add(feedback);

			add(new Label("label-book-isbn", new ResourceModel("textbook.result.isbn")));
			add(new Label("label-book-title", new ResourceModel("textbook.result.title")));
			add(new Label("label-book-authors", new ResourceModel("textbook.result.authors")));
			add(new Label("label-book-publisher", new ResourceModel("textbook.result.publisher")));
			add(new Label("label-book-copyright", new ResourceModel("textbook.result.copyright")));

			add(new Label("label-book-isbn-sample", new ResourceModel("textbook.result.isbn.sample")));
			add(new Label("label-book-title-sample", new ResourceModel("textbook.result.title.sample")));
			add(new Label("label-book-authors-sample", new ResourceModel("textbook.result.authors.sample")));
			add(new Label("label-book-publisher-sample", new ResourceModel("textbook.result.publisher.sample")));
			add(new Label("label-book-copyright-sample", new ResourceModel("textbook.result.copyright.sample")));

			PropertyModel<String> isbn_model = new PropertyModel<String>(this, "isbn");
			PropertyModel<String> title_model = new PropertyModel<String>(this, "title");
			PropertyModel<String> authors_model = new PropertyModel<String>(this, "authors");
			PropertyModel<String> publisher_model = new PropertyModel<String>(this, "publisher");
			PropertyModel<String> copyright_model = new PropertyModel<String>(this, "copyright");

			isbn_field = new TextField<String>("book-isbn-input", isbn_model);
			isbn_field.add(new IsbnLengthValidator());
			add(isbn_field);
			
			title_field = new TextField<String>("book-title-input", title_model);	
			title_field.setRequired(true);
			title_field.setOutputMarkupId(true);
			add(title_field);
			
			authors_field = new TextField<String>("book-authors-input", authors_model);
			authors_field.setRequired(true);
			authors_field.setOutputMarkupId(true);
			add(authors_field);
			
			publisher_field = new TextField<String>("book-publisher-input", publisher_model);
			publisher_field.setRequired(true);
			publisher_field.setOutputMarkupId(true);
			add(publisher_field);
			
			copyright_field = new TextField<String>("book-copyright-input", copyright_model);
			copyright_field.setRequired(true);
			copyright_field.setOutputMarkupId(true);
			add(copyright_field);
			
			AjaxButton required_link = new AjaxButton("add-as-required", this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					TextbookSession session = TextbookSession.get();
					String siteNm = session.getSiteNm();
					String userNm = session.getUserNm();
					
					Textbook book = new Textbook();
					if(isbn == null) {
						isbn = String.format("xxx%d", courseService.getCourseNm(siteNm));
						book.setIsbn10(isbn);
						book.setIsbn13(isbn);
					}
					else {
						String isbn_10;
						String isbn_13;
						if(isbn.length() == 10) {
							isbn_10 = isbn;
							String countryCode = "978";
							isbn_13 = TextbookUtil.calculateISBN13(countryCode, isbn);
						}
						else {
							isbn_13 = isbn;
							isbn_10 = TextbookUtil.calculateISBN10(isbn);
						}
						book.setIsbn10(isbn_10);
						book.setIsbn13(isbn_13);
					}
					book.setTitle(title);
					book.setAuthors(authors);
					book.setPublisher(publisher);
					book.setYear(copyright);
					coursebookService.addCourseBook(siteNm, book, userNm, true);		
					CourseBookViewList list = session.getCbooks();
					list.getBooks().clear();
					list.addBooks(coursebookService.findCourseBooks(siteNm));
					getRequestCycle().setResponsePage(MyTextbookPage.class);					
				}
				
				@Override
		        protected void onError(AjaxRequestTarget target, Form<?> form)
		        {
		            target.add(feedback);
		        }
			};
			required_link.add(new Label("label-btn-required", "Add as Required"));
			add(required_link);
		
			AjaxButton recommendated_link = new AjaxButton("add-as-recommendated", this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					TextbookSession session = TextbookSession.get();
					String siteNm = session.getSiteNm();
					String userNm = session.getUserNm();

					Textbook book = new Textbook();
					if(isbn == null) {
						isbn = String.format("xxx%d", courseService.getCourseNm(siteNm));
						book.setIsbn10(isbn);
						book.setIsbn13(isbn);
					}
					else {
						String isbn_10;
						String isbn_13;
						if(isbn.length() == 10) {
							isbn_10 = isbn;
							String countryCode = "978";
							isbn_13 = TextbookUtil.calculateISBN13(countryCode, isbn);
						}
						else {
							isbn_13 = isbn;
							isbn_10 = TextbookUtil.calculateISBN10(isbn);
						}
						book.setIsbn10(isbn_10);
						book.setIsbn13(isbn_13);
					}
					book.setTitle(title);
					book.setAuthors(authors);
					book.setPublisher(publisher);
					book.setYear(copyright);
					coursebookService.addCourseBook(siteNm, book, userNm, false);		
					CourseBookViewList list = session.getCbooks();
					list.getBooks().clear();
					list.addBooks(coursebookService.findCourseBooks(siteNm));
					getRequestCycle().setResponsePage(MyTextbookPage.class);										
				}
				
				@Override
		        protected void onError(AjaxRequestTarget target, Form<?> form)
		        {
		            target.add(feedback);
		        }
			};
			recommendated_link.add(new Label("label-btn-recommendated", "Add as Recommendated"));
			add(recommendated_link);
		}
		
	}

	public NewTextbookPanel(String id) {
		super(id);
		
		setOutputMarkupId(true);
		
		Label description = new Label("new-book-description", new ResourceModel("textbook.add.new.description"));
		description.setEscapeModelStrings(false);
		add(description);
		
		NewTextbookForm form = new NewTextbookForm("new-book-form");
		add(form);
		
	}
	
}
