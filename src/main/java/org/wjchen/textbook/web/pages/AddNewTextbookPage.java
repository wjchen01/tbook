package org.wjchen.textbook.web.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import org.wjchen.textbook.web.panels.AdminInstructionPanel;
import org.wjchen.textbook.web.panels.NewTextbookPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
@AuthorizeInstantiation("SIGNED_IN")
@AuthorizeAction(action = "ENABLE", roles = {"ADMIN"})
public class AddNewTextbookPage extends BasePage implements IHeaderContributor {

	private static final long serialVersionUID = 1L;

//	private InstructionContainer instrContainer;
	private NewBookContainer bookContainer;
	
	private class InstructionContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;

		public InstructionContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
			ResourceModel title_model = new ResourceModel("textbook.add.new.heading");
			add(new AdminInstructionPanel("instruction-panel", title_model));			
		}
		
	}
	
	private class NewBookContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;

		
		public NewBookContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
			add(new NewTextbookPanel("new-book-panel"));
		}
		
	}
	
	public AddNewTextbookPage() {
		super();
		
		log.info("init");
		
		setOutputMarkupId(true);
		
		this.setPageTitle("Add");
		initBreadCrumbs(this);

		add(new InstructionContainer("instruction-container"));
		
		bookContainer = new NewBookContainer("new-book-container");
		add(bookContainer);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "AddNewTextbookPage.css")));
	}

}
