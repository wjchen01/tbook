package org.wjchen.textbook.web.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.models.CourseDetail;
import org.wjchen.textbook.models.CourseSite;
import org.wjchen.textbook.web.pages.MyTextbookPage;

public class ImportFromCoursePanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private CourseBookService coursebookService;
	
	public ImportFromCoursePanel(String id) {
		super(id);
		
		setOutputMarkupId(true);

		final CheckGroup<CourseSite> group = new CheckGroup<CourseSite>("import-course-group", new ArrayList<CourseSite>());
		
		Form<CourseSite> form = new Form<CourseSite>("import-course-select-form");
		
		Button btn_import = new Button("button-import")
		{
			 
			private static final long serialVersionUID = 1L;

			@Override
	        public void onSubmit() {
				 final List<CourseSite> sites = (List<CourseSite>) group.getModelObject();
				 
				 TextbookSession session = TextbookSession.get();
				 String siteNm = session.getSiteNm();
				 String userNm = session.getUserNm();
				 
				 coursebookService.importCourseBooks(siteNm, userNm, sites);
				 
				 getRequestCycle().setResponsePage(MyTextbookPage.class);
	         }
		};
		group.add(btn_import);
		
		Button btn_cancel = new Button("button-cancel")
		{
			 
			private static final long serialVersionUID = 1L;

			@Override
	        public void onSubmit() {
				 getRequestCycle().setResponsePage(this.getPage());
	         }
		};
		group.add(btn_cancel);
		
		add(form);
		form.add(group);
		group.add(new CheckGroupSelector("import-course-selectall"));
		
		Label l_course_number = new Label("import-course-number-label", 
				new Model<String>("Course Number"));				  
		group.add(l_course_number);		
		Label l_course_title = new Label("ç", 
				new Model<String>("Course Title"));				  
		group.add(l_course_title);
		Label l_select_all = new Label("import-course-selectall-label", 
				new Model<String>("Select All"));				  
		group.add(l_select_all);

		TextbookSession session = TextbookSession.get();
		String siteNm = session.getSiteNm();
		String userNm = session.getUserNm();
		final List<CourseSite> sites = coursebookService.findImportableCourses(userNm, siteNm);
		
		ListView<CourseSite> courseView = new ListView<CourseSite>("list-importable-courses", sites) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CourseSite> item) {
				final CourseSite site = item.getModelObject();
				CourseDetail course = site.getDetail();
				String siteId = site.getSiteId();
				String title = course.getTitle();
				item.add(new Check<CourseSite>("import-course-select", item.getModel()));
	            item.add(new Label("import-course-id", new Model<String>(siteId)));
	            item.add(new Label("import-course-title", new Model<String>(title)));				
			}
			
		};
		group.add(courseView);
		courseView.setReuseItems(true);
	}

}
