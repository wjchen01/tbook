package org.wjchen.textbook.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import org.wjchen.textbook.web.panels.ImportFromCoursePanel;
import org.wjchen.textbook.web.panels.ImportFromFilePanel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AuthorizeInstantiation("SIGNED_IN")
@AuthorizeAction(action = "ENABLE", roles = {"ADMIN"})
public class ImportTextbookPage extends BasePage {

	private static final long serialVersionUID = 1L;

	public ImportTextbookPage() {
		super();
		
		log.info("init");
		
		setOutputMarkupId(true);
		
		this.setPageTitle("Import");
		initBreadCrumbs(this);
				
		List<ITab> tabs = new ArrayList<ITab>();

		tabs.add(new AbstractTab(new Model<String>("Import From Course")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new ImportFromCoursePanel(panelId);
			}
		});			

		tabs.add(new AbstractTab(new Model<String>("Import From File")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new ImportFromFilePanel(panelId);
			}
		});			
		
		BootstrapTabbedPanel<ITab> importPanel = new BootstrapTabbedPanel<ITab>("import-book-tabs", tabs);
		importPanel.setSelectedTab(0);
		
		add(importPanel);
	}

	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "ImportTextbookPage.css")));
	}

}
