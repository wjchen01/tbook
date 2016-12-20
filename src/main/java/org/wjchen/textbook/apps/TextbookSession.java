package org.wjchen.textbook.apps;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.imsglobal.basiclti.provider.api.BasicLtiContext;
import org.imsglobal.basiclti.provider.servlet.util.BasicLTIContextWebUtil;

import org.wjchen.textbook.web.models.BreadCrumbViewList;
import org.wjchen.textbook.web.models.CourseBookViewList;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
public class TextbookSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;

//	@SpringBean(name = "org.wjchen.textbook.logics.UserService")
//	private UserService userService;
//	
//	@SpringBean(name = "org.wjchen.textbook.logics.CourseBookService")
//	private CourseBookService coursebookService;
	
	private String userNm;
	private String siteNm;
	private List<String> roleNms;
	
	private BreadCrumbViewList crumbs = new BreadCrumbViewList();
	private CourseBookViewList cbooks = new CourseBookViewList();
	
	public TextbookSession(Request request) {
		super(request);

		refresh(request);
	}

    public static TextbookSession get() {
		return (TextbookSession) Session.get();
	}

	public void refresh(Request request) {
		HttpSession session = ((HttpServletRequest) request.getContainerRequest()).getSession();
		BasicLtiContext ltiContext = BasicLTIContextWebUtil.getBasicLtiContext(session);
		
		userNm = null;
		siteNm = null;
		
		if(ltiContext != null) {
			userNm = ltiContext.getLisPerson().getSourcedId();
			siteNm = ltiContext.getLisCourseOffering().getSourcedId();			
			if(siteNm == null) {
				siteNm = ltiContext.getContext().getId();
			}
					
			roleNms = ltiContext.getUser().getRoles();
			
			signIn(userNm, siteNm);
			
			log.info("Session - User, " + userNm + " sign in Course, " + siteNm + " as Role, " + roleNms);
		}
	}

	@Override
	public boolean authenticate(String userNm, String siteNm) {
		boolean authenticated = false;
		
		if(userNm != null && siteNm != null) {
			authenticated = true;
		}
		
		return authenticated;
	}
	
	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		
		if(isSignedIn()) {
			roles.add("SIGNED_IN");
		
			boolean adminable = false;
			for(String roleNm : roleNms) {
				if(roleNm.matches("(?i:.*instructor.*)")) {
					adminable = true;
				}
				else if(roleNm.matches("(?i:.*teacher.*)")) {
					adminable = true;
				}
				else if(roleNm.matches("(?i:.*teaching assistant.*)")) {
					adminable = true;
				}
				else if(roleNm.matches("(?i:.*administrator.*)")) {
					adminable = true;
				}
			}
			if(adminable) {
				roles.add(Roles.ADMIN);
			}
			else {
				roles.add(Roles.USER);
			}
		}
		
		return roles;
	}
	
}
