package org.wjchen.textbook.web.pages;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.Bootstrap;
import org.wjchen.textbook.apps.TextbookApplication;
//import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.logics.CourseBookService;
import org.wjchen.textbook.logics.UserService;
import org.wjchen.textbook.models.CourseBook;
import org.wjchen.textbook.web.models.BreadCrumbViewList;
import org.wjchen.textbook.web.panels.BreadCrumbBarPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
@RequireHttps
@AuthorizeInstantiation("SIGNED_IN")
@AuthorizeAction(action = "ENABLE", roles = {"USER", "ADMIN"})
public abstract class BasePage extends WebPage implements IHeaderContributor {

	private static final long serialVersionUID = 6130209537998455547L;
	
	@SpringBean
	protected UserService userService;
	
	@SpringBean
	protected CourseBookService courseBookService;
	
	protected String siteId;
	protected String pageTitle;
	

	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);

        Bootstrap.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));

	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "BasePage.css")));

	    //Meta Head
		MetaDataHeaderItem meta = new MetaDataHeaderItem("meta");
		meta.addTagAttribute("http-equiv", "Content-Type");
		meta.addTagAttribute("content", "text/html");
		meta.addTagAttribute("charset", "UTF-8");
		response.render(meta);		
	}
	
	public BasePage() {
//		super();
		
		log.info("init");

		TextbookSession session = TextbookSession.get();
		if(session.isSessionInvalidated()) {
			session = (TextbookSession) TextbookApplication.get().newSession(this.getRequest(), this.getResponse());
		}
		session.bind();
		session.refresh(this.getRequest());			

		siteId = session.getSiteNm();
		List<CourseBook> books = courseBookService.findCourseBooks(siteId);
		session.getCbooks().getBooks().clear();
		session.getCbooks().addBooks(books);;
	}

	public void initBreadCrumbs(BasePage page) {
		
		TextbookSession session = TextbookSession.get();
		BreadCrumbViewList breadCrumbs = session.getCrumbs();
		
		breadCrumbs.refresh(page);		

		CompoundPropertyModel<BreadCrumbViewList> model = new CompoundPropertyModel<BreadCrumbViewList>(breadCrumbs);		
		add(new BreadCrumbBarPanel("bread-crumb-panel", model));
	}

	public IModel<String> getTitle() {
		return new Model<String>(BasePage.this.getPageTitle());
	}

}
