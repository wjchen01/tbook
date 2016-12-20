package org.wjchen.textbook.web.pages;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.web.panels.UserInstructionPanel;
import org.wjchen.textbook.web.panels.AdminInstructionPanel;
import org.wjchen.textbook.web.panels.CommentViewPanel;
import org.wjchen.textbook.web.panels.CourseBookNavBarPanel;
import org.wjchen.textbook.web.panels.CourseBookViewPanel;
import org.wjchen.textbook.web.panels.NoCourseBookPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AuthorizeInstantiation("SIGNED_IN")
@AuthorizeAction(action = "ENABLE", roles = {"USER", "ADMIN"})
public class MyTextbookPage extends BasePage {

	private static final long serialVersionUID = 1L;

	public class NavbarContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public NavbarContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
			add(new CourseBookNavBarPanel("action-panel"));
		}

	}
	
	public class InstructionContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;

		public InstructionContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
			if(TextbookSession.get().getRoles().hasRole("ADMIN")) {
				ResourceModel title_model = new ResourceModel("admin.textbook.information.heading");
				add(new AdminInstructionPanel("instruction-panel", title_model));	
			}
			else {
				ResourceModel title_model = new ResourceModel("user.textbook.information.heading");
				add(new UserInstructionPanel("instruction-panel", title_model));					
			}
		}
		
	}
	
	public class NoCourseBookContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public NoCourseBookContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
			NoCourseBookPanel panel = new NoCourseBookPanel("nobook-panel");
			add(panel);
			List<CourseBook> books = courseBookService.findCourseBooks(siteId);
			if(books.size() > 0) {
				panel.setVisible(false);
			}
		}

	}
	
	public class TextbookContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public TextbookContainer(String id) {
			super(id);

			setOutputMarkupId(true);
			
//			CompoundPropertyModel<CourseBookViewList> model = new CompoundPropertyModel<CourseBookViewList>(list);	

			add(new CourseBookViewPanel("book-panel"));
		}
	}
	
	public class CommentContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public CommentContainer(String id) {
			super(id);
			
			add(new CommentViewPanel("comment-panel"));
		}
		
	}
	
	public class FooterContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public FooterContainer(String id) {
			super(id);
			
			ResourceModel foot_model;
			ResourceModel note_model;
			if(TextbookSession.get().getRoles().hasRole("ADMIN")) {
				foot_model = new ResourceModel("admin.textbook.information.footer");
				note_model = new ResourceModel("admin.textbook.information.note");
			}
			else {
				foot_model = new ResourceModel("user.textbook.information.footer");
				note_model = new ResourceModel("user.textbook.information.note");				
			}
			
			Label footer = new Label("textbook-information-footer", foot_model);
			footer.setEscapeModelStrings(false);
			add(footer);
			
			Label notes = new Label("textbook-information-notes", note_model);
			notes.setEscapeModelStrings(false);
			add(notes);
		}

	}
	
	public MyTextbookPage() {
		super();
		
		log.info("init");
		
		this.setPageTitle("Textbook Information");
		
		initBreadCrumbs(this);
		
		add(new NavbarContainer("action-container"));

		add(new InstructionContainer("instruction-container"));
		
		add(new NoCourseBookContainer("nobook-container"));
		
		TextbookContainer bookContainer = new TextbookContainer("book-container");
		add(bookContainer);		
		
		CommentContainer commentContainer = new CommentContainer("comment-container");
		add(commentContainer);

		FooterContainer footerContainer = new FooterContainer("footer-container");
		add(footerContainer);
}
	
	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(this.getClass(), "MyTextbookPage.js")));
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "MyTextbookPage.css")));
	}

}
