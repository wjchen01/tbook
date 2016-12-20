package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

public class MessagePanel extends Panel {

	private static final long serialVersionUID = 1L;

	public MessagePanel(String id, ResourceModel msg_model) {
		super(id, msg_model);

		add(new Label("textbook-message", msg_model));
	}

}
