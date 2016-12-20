package org.wjchen.textbook.web.models;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import org.wjchen.textbook.apps.TextbookSession;
import org.wjchen.textbook.models.CourseBook;
import edu.emory.mathcs.backport.java.util.Collections;

public class CourseBookDataProvider implements IDataProvider<CourseBook> {

	private static final long serialVersionUID = 1L;

	private boolean required;
	
	public CourseBookDataProvider(boolean required) {
		this.required = required;
	}
	
	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends CourseBook> iterator(long first, long count) {
		TextbookSession session = TextbookSession.get();
		CourseBookViewList viewList = session.getCbooks();
		
		List<CourseBook> list;
		if(required) {
			list = viewList.getRequiredBookList();
		}
		else {
			list = viewList.getRecommendedBookList();
		}
		Collections.sort(list, CourseBook.SequenceComparator);

		int lFirst = (int) first;
		int lCount = (int) count;
		
		List<CourseBook> subList = list.subList(lFirst, lFirst + lCount);

		return subList.iterator();
	}

	@Override
	public long size() {
		TextbookSession session = TextbookSession.get();
		CourseBookViewList viewList = session.getCbooks();
		
		List<CourseBook> list;
		if(required) {
			list = viewList.getRequiredBookList();
		}
		else {
			list = viewList.getRecommendedBookList();
		}
		Collections.sort(list, CourseBook.SequenceComparator);

		return list.size();
	}

	@Override
	public IModel<CourseBook> model(CourseBook object) {
		return new CompoundPropertyModel<CourseBook>(object);
	}

}
