package org.wjchen.textbook.web.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import org.wjchen.textbook.utils.TextbookUtil;

public class IsbnValidator implements IValidator<String> {

	private static final long serialVersionUID = 1L;

	@Override
	public void validate(IValidatable<String> validatable) {
		String isbn = validatable.getValue();				
		if(!TextbookUtil.validISBN(isbn)) {
			ValidationError error = new ValidationError();
			error.addKey("ISBN_Alert");
			validatable.error(error);
		}

	}

}
