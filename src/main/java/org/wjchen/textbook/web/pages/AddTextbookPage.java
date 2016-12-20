package org.wjchen.textbook.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.logics.TextbookService;
import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.web.models.TextbookViewList;
import org.wjchen.textbook.web.panels.AdminInstructionPanel;
import org.wjchen.textbook.web.panels.MatchTextbookPanel;
import org.wjchen.textbook.web.validators.IsbnValidator;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
@AuthorizeInstantiation("SIGNED_IN")
@AuthorizeAction(action = "ENABLE", roles = {"ADMIN"})
public class AddTextbookPage extends BasePage implements IHeaderContributor {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private TextbookService bookService;

	private MatchBookContainer match_container;
	
	private class InstructionContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;

		public InstructionContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
			ResourceModel title_model = new ResourceModel("textbook.add.existing.heading");
			add(new AdminInstructionPanel("instruction-panel", title_model));			
		}
		
	}
	
	private final class SearchTextbookForm extends Form<TextbookViewList> {

		private static final long serialVersionUID = 1L;

		private String isbn;
			
		public SearchTextbookForm(String id, CompoundPropertyModel<TextbookViewList> book_model) {
			super(id, book_model);
				
			setOutputMarkupId(true);
				
			final TextbookViewList view_list = book_model.getObject();

			final FeedbackPanel feedback = new FeedbackPanel("textbook-search-feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);
				
			add(new Label("textbook-search-heading", new ResourceModel("textbook.search.heading")));

			add(new Label("isbn-input-description", new ResourceModel("textbook.search.field")));
			add(new Label("isbn-not-found-description", new ResourceModel("textbook.search.instruction.unavailable")));
			add(new BookmarkablePageLink<Void>("add-new-textbook", AddNewTextbookPage.class));

			PropertyModel<String> isbn_model = new PropertyModel<String>(this, "isbn");
			TextField<String> isbnField = new TextField<String>("isbn-input-field", isbn_model);
			isbnField.setRequired(true);
			isbnField.add(new IsbnValidator());		
			add(isbnField);
				
			// Submit Button
			IndicatingAjaxButton button = new IndicatingAjaxButton("isbn-action", this) {

				private static final long serialVersionUID = 1L;
					
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					view_list.getBooks().clear();
					error("No result!");
					target.add(match_container);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					view_list.getBooks().clear();						
					Textbook textbook = bookService.getExactMatchBook(isbn);
					if(textbook != null) {
						view_list.addBook(textbook);
					}
					else {
						error("No result!");
					}
					target.add(match_container);
					target.add(feedback);
				}
			};
			button.add(new Label("submit-button-text", new ResourceModel("textbook.search.submit.button")));
			add(button);
			
			Label footer = new Label("textbook-search-footing", new ResourceModel("textbook.search.note"));
			footer.setEscapeModelStrings(false);
			add(footer);
		}
	}
		
	public class MatchBookContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public MatchBookContainer(String id, CompoundPropertyModel<TextbookViewList> model) {
			super(id);

			setOutputMarkupId(true);
						
			MatchTextbookPanel matchPanel = new MatchTextbookPanel("match-book-panel", model);
			add(matchPanel);
		}
		
	}
	
	public AddTextbookPage() {
		super();
		
		log.info("init");
		
		setOutputMarkupId(true);
		
		this.setPageTitle("Search");
		initBreadCrumbs(this);

		TextbookViewList textbook_list = new TextbookViewList();
		CompoundPropertyModel<TextbookViewList> book_model = new CompoundPropertyModel<TextbookViewList>(textbook_list);
		
		add(new InstructionContainer("instruction-container"));
		
		add(new SearchTextbookForm("textbook-search-form", book_model));
		
		match_container = new MatchBookContainer("match-book-container", book_model);
		match_container.setOutputMarkupId(true);
		add(match_container);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "AddTextbookPage.css")));
	}

}
