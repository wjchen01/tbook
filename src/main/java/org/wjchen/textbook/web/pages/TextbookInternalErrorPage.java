package org.wjchen.textbook.web.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.model.ResourceModel;

import org.wjchen.textbook.apps.TextbookSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextbookInternalErrorPage extends InternalErrorPage {

	private static final long serialVersionUID = 1L;

	public TextbookInternalErrorPage() {
		super();
		
		TextbookSession session = TextbookSession.get();
		String sid = session.getId();
		session.invalidateNow();
		
		log.debug("Logout internal error: {}", sid);
		
		add(new Label("textbook-internal-error", new ResourceModel("textbook.app.internalerror")));
	}

}
