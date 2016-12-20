package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.StringResourceModel;

public class TextbookInstructionPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public TextbookInstructionPanel(String id) {
		super(id);

		setOutputMarkupId(true);
		
		Label l_description = new Label("textbook-instruction-description", 
				new StringResourceModel("textbook.instruction.line0", this, null));
		add(l_description);
		
		RepeatingView listItems = new RepeatingView("textbook-instruction-items");
		for(int i = 1; i <=3; i++) {
			String resourceKey = String.format("textbook.instruction.line%d", i);
			Label instruction = new Label(listItems.newChildId(), new StringResourceModel(resourceKey, this, null));
			listItems.add(instruction);
		}
		add(listItems);
	}

}
