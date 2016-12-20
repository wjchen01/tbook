package org.wjchen.textbook.web.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.web.models.BreadCrumbDataProvider;
import org.wjchen.textbook.web.models.BreadCrumbItem;
import org.wjchen.textbook.web.models.BreadCrumbViewList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BreadCrumbBarPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public BreadCrumbBarPanel(String id, CompoundPropertyModel<BreadCrumbViewList> model) {
		super(id, model);
		this.setDefaultModel(model);
		
		setOutputMarkupId(true);
		
		final BreadCrumbDataProvider dataProvider = new BreadCrumbDataProvider();
		DataView<BreadCrumbItem> dataView = new DataView<BreadCrumbItem>("textbook-bread-crumbs", dataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<BreadCrumbItem> item) {
				
				final BreadCrumbItem pageItem = item.getModelObject();
				log.debug("Item Page: {}", pageItem);
								
				if(item.getIndex() < (int) (dataProvider.size() - 1)) {
					Link<Void> link = new Link<Void>("bread-crumb-item") {

						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							TextbookSession session = TextbookSession.get();
							BreadCrumbViewList crumbs = session.getCrumbs();
							crumbs.refresh(pageItem.getPage());
						
							getRequestCycle().setResponsePage(pageItem.getPage().getPageClass());
						}					
					};
					link.add(new Label("bread-crumb-title", new Model<String>(pageItem.getTitle())));
					item.add(link);		
				}
				else {
					WebMarkupContainer container = new WebMarkupContainer("bread-crumb-item");
					container.add(new Label("bread-crumb-title", new Model<String>(pageItem.getTitle())));
					item.add(container);
					item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {

						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return "active";
						}						
					}));
					
				}
			}

		};
		add(dataView);
	}
	
}
