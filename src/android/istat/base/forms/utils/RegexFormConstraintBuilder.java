package android.istat.base.forms.utils;

import java.util.ArrayList;
import java.util.List;
import android.istat.base.forms.FormValidator.FieldValidator;

public final class RegexFormConstraintBuilder extends
		FormConstraintBuilder {
	public final static RegexFormConstraintBuilder newInstance() {
		return new RegexFormConstraintBuilder();
	}

	RegexFormConstraintBuilder() {

	}

	public RegexFormConstraintBuilder appendFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		List<FieldValidator> validators = conditionMap.get(fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

	public RegexFormConstraintBuilder applyFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		List<FieldValidator> validators = new ArrayList<FieldValidator>();
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}
}