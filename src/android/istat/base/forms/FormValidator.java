package android.istat.base.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.view.View;

/**
 * @author istat
 */
public final class FormValidator {
    HashMap<String, List<FieldValidator>> validationCondition = new HashMap<String, List<FieldValidator>>();
    ValidationListener validationListener;

    public final void setValidationListener(ValidationListener validationListener) {
        this.validationListener = validationListener;
    }

    public final static FormState validate(Form form,
                                           HashMap<String, List<FieldValidator>> constraints) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        View nullView = null;
        return validator.validate(form, nullView);
    }

    public final static FormState validate(Form form, View formView,
                                           HashMap<String, List<FieldValidator>> constraints) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        return validator.validate(form, formView);
    }

    public final static FormState validate(Form form, View formView,
                                           HashMap<String, List<FieldValidator>> constraints, ValidationListener listener) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        validator.setValidationListener(listener);
        return validator.validate(form, formView);
    }

    public final void setConstraints(
            HashMap<String, List<FieldValidator>> validationDirective) {
        if (validationDirective == null) {
            this.validationCondition.clear();
            return;
        }
        this.validationCondition = validationDirective;
    }

    public final FormValidator addConstraint(String field, FieldValidator validator) {
        List<FieldValidator> validators = validationCondition.get(field);
        if (validators == null) {
            validators = new ArrayList<FieldValidator>();
        }
        validators.add(validator);
        validationCondition.put(field, validators);
        return this;
    }

    public final FormState validate(Form form, View formView) {
        FormState state = new FormState();
        proceedCheckup(form, state, formView);
        return state;
    }

    private void proceedCheckup(Form form, FormState state, View formView) {
        if (validationListener != null) {
            validationListener.onValidationStarting(form, formView);
        }
        Iterator<String> iterator = form.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<FieldValidator> validators = validationCondition.get(key);
            Object objValue = form.get(key);
            FormFieldError error = null;
            for (FieldValidator validator : validators) {
                boolean isValidated = validator.validate(form, key, objValue);
                if (!isValidated) {
                    if (error == null) {
                        error = new FormFieldError(key, objValue);
                    }
                    if (formView != null) {
                        error.viewCause = formView.findViewWithTag(key);
                    }
                    error.addFailedValidators(validator);
                    state.addError(error);
                    if (validationListener != null) {
                        validationListener.onValidateField(form, key, objValue,
                                formView, validator, isValidated);
                    }
                    if (validator.hasBreakValidationIfErrorEnable()) {
                        break;
                    }
                }
            }
            if (error != null) {
                state.addError(error);
            }
        }
        if (validationListener != null) {
            validationListener.onValidationCompleted(form, formView, state);
        }
    }
//
//	public static class ValidationDirective extends
//			HashMap<String, List<FieldValidator>> {
//
//		/**
//		 *
//		 */
//		private static final long serialVersionUID = 1L;
//
//	}

    public static abstract class FieldValidator {
        final String FIELD_BREAK_IF_ERROR = "breakValidationIfError";
        final String FIELD_ERROR_MESSAGE = "errorMessage";
        boolean breakValidationIfError = false;

        public void setBreakValidationIfError(boolean breakIfError) {
            this.breakValidationIfError = breakIfError;
        }

        public boolean hasBreakValidationIfErrorEnable() {
            return breakValidationIfError;
        }

        public abstract String getErrorMessage();

        public final boolean validate(Form form, String fieldName, Object value) {
            return onValidate(form, fieldName, value);
        }

        protected abstract boolean onValidate(Form form, String fieldName,
                                              Object value);

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put(FIELD_BREAK_IF_ERROR, breakValidationIfError);
                json.put(FIELD_ERROR_MESSAGE, getErrorMessage());
            } catch (Exception e) {

            }
            return json;
        }

        public FieldValidator fillFrom(JSONObject json) {
            this.breakValidationIfError = json.optBoolean(FIELD_BREAK_IF_ERROR);
            return this;
        }
    }

    public static interface ValidationListener {
        public void onValidationStarting(Form form, View formView);

        public void onValidateField(Form form, String FieldName, Object value,
                                    View viewCause, FormValidator.FieldValidator validator,
                                    boolean validated);

        public void onValidationCompleted(Form form, View formView,
                                          FormState state);
    }
}
