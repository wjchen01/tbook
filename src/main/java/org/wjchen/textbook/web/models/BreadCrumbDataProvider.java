package org.wjchen.textbook.web.models;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import org.wjchen.textbook.apps.TextbookSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BreadCrumbDataProvider implements IDataProvider<BreadCrumbItem> {

	private static final long serialVersionUID = 1L;

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends BreadCrumbItem> iterator(long first, long count) {
		TextbookSession session = TextbookSession.get();
		BreadCrumbViewList viewList = session.getCrumbs();
		
		List<BreadCrumbItem> list = viewList.getCrumbs();
		int lFirst = (int) first;
		int lCount = (int) count;
		
		List<BreadCrumbItem> subList = list.subList(lFirst, lFirst + lCount);

		return subList.iterator();
	}

	@Override
	public long size() {
		TextbookSession session = TextbookSession.get();
		BreadCrumbViewList viewList = session.getCrumbs();
		
		List<BreadCrumbItem> list = viewList.getCrumbs();
		log.debug("size -- {}", list.size());
		return list.size();
	}

	@Override
	public IModel<BreadCrumbItem> model(BreadCrumbItem object) {
		return new CompoundPropertyModel<BreadCrumbItem>(object);
	}

}
