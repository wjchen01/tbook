package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import org.wjchen.textbook.web.models.CourseBookDataProvider;


public class CourseBookViewPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public CourseBookViewPanel(String id) {
		super(id);
		
		setOutputMarkupId(true);

		CourseBookDataProvider data_required = new CourseBookDataProvider(true);
		if(data_required.size() > 0) {
			TextbookViewPanel required_panel = new TextbookViewPanel("textbook-required-panel", true);
			add(required_panel);
		}
		else {
			ResourceModel model = new ResourceModel("user.textbook.infomation.nobook");
			MessagePanel required_panel = new MessagePanel("textbook-required-panel", model);
			add(required_panel);
		}
				
		CourseBookDataProvider data_recommented = new CourseBookDataProvider(false);
		if(data_recommented.size() > 0) {
			TextbookViewPanel recommended_panel = new TextbookViewPanel("textbook-recommended-panel", false);
			add(recommended_panel);
		}
		else {
			ResourceModel model = new ResourceModel("user.textbook.infomation.nobook");
			MessagePanel recommended_panel = new MessagePanel("textbook-recommended-panel", model);
			add(recommended_panel);			
		}
	}

}
