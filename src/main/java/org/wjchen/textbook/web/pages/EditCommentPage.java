package org.wjchen.textbook.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseService;

@AuthorizeAction(action = "RENDER", roles = {"ADMIN"})
public class EditCommentPage extends BasePage {
	
	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseService courseService;
	
	private final class CommentEditForm extends Form<String> {

		private static final long serialVersionUID = 1L;

		String comment; 

		public CommentEditForm(String id) {
			super(id);
			
			final String siteId = TextbookSession.get().getSiteNm();		
			comment = courseService.retrieveComments(siteId);
			PropertyModel<String> comment_model = new PropertyModel<String>(this, "comment");
			final TextArea<String> textarea = new TextArea<String>("textbook-comments-textarea", comment_model);
			add(textarea);
			
			AjaxButton submit_link = new AjaxButton("textbook-comments-submit", this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					courseService.updateComments(siteId, comment);
					
					getRequestCycle().setResponsePage(MyTextbookPage.class);					
				}
				
			};
			add(submit_link);
			
			Link<Void> cancel_link = new Link<Void>("textbook-comments-cancel") {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					getRequestCycle().setResponsePage(MyTextbookPage.class);				
				}
				
			};
			add(cancel_link);			
		}
		
	}
	
	public EditCommentPage() {
		
		super();
		
		this.setPageTitle("Comments");
		initBreadCrumbs(this);
		
		CommentEditForm form = new CommentEditForm("textbook-comments-form");
		add(form);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forUrl("ckeditor/ckeditor.js"));
		response.render(JavaScriptHeaderItem.forUrl("ckeditor/adapters/jquery.js"));
	    response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(this.getClass(), "EditCommentPage.js")));
	}

}
