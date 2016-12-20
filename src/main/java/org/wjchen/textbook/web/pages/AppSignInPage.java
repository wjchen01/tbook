package org.wjchen.textbook.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;

import org.wjchen.textbook.apps.TextbookSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppSignInPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public AppSignInPage() {
		super();
		
		TextbookSession session = TextbookSession.get();
		String sid = session.getId();
		session.invalidateNow();
		
		log.debug("Logout expired session: {}", sid);
		
		add(new Label("textbook-signin-instruction", new ResourceModel("textbook.app.signin")));
	}

}
