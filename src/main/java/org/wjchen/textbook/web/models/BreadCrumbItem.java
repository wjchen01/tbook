package org.wjchen.textbook.web.models;

import java.io.Serializable;

import org.wjchen.textbook.web.pages.BasePage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of={"title"})
@Getter @Setter
public class BreadCrumbItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private BasePage page;
	
	public BreadCrumbItem(BasePage page) {
		this.page = page;
		this.title = page.getPageTitle();
	}
	
}
