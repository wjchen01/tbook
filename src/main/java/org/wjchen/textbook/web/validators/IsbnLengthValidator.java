package org.wjchen.textbook.web.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class IsbnLengthValidator implements IValidator<String> {

	private static final long serialVersionUID = 1L;

	@Override
	public void validate(IValidatable<String> validatable) {
		String isbn = validatable.getValue();				
		if(!(isbn.length() == 10 || isbn.length() == 13)) {
			ValidationError error = new ValidationError();
			error.addKey("ISBN_Alert");
			validatable.error(error);
		}
	}

}
