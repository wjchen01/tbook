package org.wjchen.textbook.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import org.wjchen.textbook.models.Textbook;
import org.wjchen.textbook.web.models.TextbookViewList;

public class MatchTextbookPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public MatchTextbookPanel(String id, CompoundPropertyModel<TextbookViewList> model) {
		super(id, model);
		
		setOutputMarkupId(true);
		
		add(new Label("match-book-title", new Model<String>("Exact Match")));
		
		final TextbookViewList view_list = model.getObject();
		DataView<Textbook> booksView = new DataView<Textbook>("textbook-view-list", 
																new ListDataProvider<Textbook>(view_list.getBooks())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Textbook> item) {
				Textbook book = item.getModelObject();
				CompoundPropertyModel<Textbook> model = new CompoundPropertyModel<Textbook>(book);
				item.add(new BookItemPanel("match-book-item-panel", model));
			}
			
		};
		add(booksView);		
	}

}
