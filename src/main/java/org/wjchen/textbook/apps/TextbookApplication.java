package org.wjchen.textbook.apps;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.less.BootstrapLess;
import org.wjchen.textbook.web.pages.AddNewTextbookPage;
import org.wjchen.textbook.web.pages.AddTextbookPage;
import org.wjchen.textbook.web.pages.AppSignInPage;
import org.wjchen.textbook.web.pages.ImportTextbookPage;
import org.wjchen.textbook.web.pages.MyTextbookPage;
import org.wjchen.textbook.web.pages.SortCourseBookPage;
import org.wjchen.textbook.web.pages.TextbookInternalErrorPage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "textbookApplication")
public class TextbookApplication extends AuthenticatedWebApplication {

	private static String JQUERY_JS = "js/jquery-1.11.3.min.js";
	
	protected void init() {
		log.info("init");
		super.init();
		
//		getRequestCycleListeners().add(new SessionExpiredListener());
		
		// Runtime error handle
		getRequestCycleListeners().add(new AbstractRequestCycleListener() { 
            @Override 
            public IRequestHandler onException(RequestCycle cycle, Exception ex) {             	
            	if (ex instanceof WicketRuntimeException) {
            		log.error(ex.getMessage());
            		// print stack trace
                    StringWriter sw = new StringWriter(1024);
                    PrintWriter pw = new PrintWriter(sw);
            		ex.printStackTrace(pw);
            		pw.flush();
            		log.error(sw.toString());
            		pw.close();
            		try {
						sw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		
            		return new RenderPageRequestHandler(new PageProvider(TextbookInternalErrorPage.class)); 
            	}
            	
				return super.onException(cycle, ex);
            } 
		}); 
		
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));

        configureBootstrap();
		// Javascript
		JavaScriptResourceReference jquery_js_ref = new JavaScriptResourceReference(this.getClass(), JQUERY_JS);
		getResourceBundles().addJavaScriptBundle(this.getClass(), "javascript", jquery_js_ref);

		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		getResourceSettings().setThrowExceptionOnMissingResource(true);
		getMarkupSettings().setStripWicketTags(true);
		
        mountPage("myTextbook", MyTextbookPage.class);
        mountPage("addTextbook", AddTextbookPage.class);
        mountPage("addNewTextbook", AddNewTextbookPage.class);
        mountPage("importTextbook", ImportTextbookPage.class);
        mountPage("sortTextbook", SortCourseBookPage.class);
        mountPage("accessTextbook", AppSignInPage.class);

	}

	@Override
	public Class<? extends Page> getHomePage() {
		return MyTextbookPage.class;
	}

	//expose Application itself
	public static TextbookApplication get() {
		return (TextbookApplication) Application.get();
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new TextbookSession(request);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return AppSignInPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return TextbookSession.class;
	}

    private void configureBootstrap() {
        
        final IBootstrapSettings settings = new BootstrapSettings();
        settings.useCdnResources(true);
        
        Bootstrap.install(this, settings);
        BootstrapLess.install(this);
    }

 }
