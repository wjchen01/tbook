package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

public class UserInstructionPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public UserInstructionPanel(String id, ResourceModel title_model) {
		super(id);

		setOutputMarkupId(true);
		
		add(new Label("textbook-instruction-heading", title_model));
	}

}