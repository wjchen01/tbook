package org.wjchen.textbook.web.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.wjchen.textbook.web.pages.BasePage;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class BreadCrumbViewList implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient List<BreadCrumbItem> crumbs;
	
	public void refresh(BasePage page) {
		log.debug("Before Search Pages: {}", crumbs);
		
		BreadCrumbItem item = new BreadCrumbItem(page);
		if(crumbs == null) {
			crumbs = new ArrayList<BreadCrumbItem>();
		}
		int index = crumbs.indexOf(item);
		if(index < 0) {
			crumbs.add(item);
		}
		else {
			log.debug("Sub Pages [0, {}]: {}", index+1, crumbs.subList(0, index + 1));
			crumbs = crumbs.subList(0, index + 1);
		}
		
		log.debug("After Search Pages: {}", crumbs);
	}
	
}
