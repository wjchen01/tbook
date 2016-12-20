package org.wjchen.textbook.web.panels;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseService;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AuthorizeAction(action = "RENDER", roles = {"ADMIN"})
public class NoCourseBookPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseService courseService;

	private boolean nobook;
	
	public NoCourseBookPanel(String id) {
		super(id);
		
		setOutputMarkupId(true);
		
		final String siteNm = TextbookSession.get().getSiteNm();
		nobook = courseService.noBookSetting(siteNm);
		
		Label description = new Label("no-course-book-descr", new ResourceModel("admin.textbook.information.nobook"));
		CheckBox chk_nobook = new CheckBox("no-course-book-checkbox", new PropertyModel<Boolean>(this, "nobook"));
		Form<Void> form = new Form<Void>("no-course-book-form") {

			private static final long serialVersionUID = 1L;
			
			@Override
	        public void onSubmit() {
				courseService.updateNoBook(siteNm, nobook);
			}
		};
		form.add(description);
		form.add(chk_nobook);
		
		add(form);
	}

}
