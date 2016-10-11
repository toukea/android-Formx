package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.interfaces.FieldHandler;
import android.istat.base.forms.utils.ViewUtil;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFiller extends FormGetSetter {

    FormFiller(Form form) {
        super(form);
    }

    public static FormFiller fillFromView(Form form, View view) {
        return fillFromView(form, view, false);
    }

    public static FormFiller fillFromView(Form form, View view,
                                          boolean editableOnly) {
        return fillFromView(form, view, editableOnly, new FieldValueGetter[0]);
    }

    public static FormFiller fillFromView(Form form, View view,
                                          boolean editableOnly, FieldValueGetter... handlers) {
        return fillFromView(form, view, editableOnly, handlers != null
                && handlers.length > 0 ? Arrays.asList(handlers) : null);
    }

    public static FormFiller fillFromView(Form form, View view,
                                          FieldValueGetter... handlers) {
        return fillFromView(form, view, false, handlers != null
                && handlers.length > 0 ? Arrays.asList(handlers) : null);
    }

    public static FormFiller fillFromView(Form form, View view,
                                          boolean editableOnly, List<FieldValueGetter> fieldHandlers) {
        FormFiller filler = new FormFiller(form);
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            List<FieldHandler> handlers = new ArrayList<FieldHandler>();
            handlers.addAll(fieldHandlers);
            filler.addAllFieldHandlers(handlers);
        }
        filler.setModifyEditableOnly(editableOnly);
        filler.mutateView(view);
        return filler;
    }

    public static abstract class FieldValueGetter<T, V extends View> extends FieldValueGetSetter {
        public abstract T getValue(V v);

        @Override
        public final boolean onHandle(Form form, String fieldName, View view) {
            Class clazzView = getViewTypeClass();
            if (view.getClass().isAssignableFrom(clazzView)) {
                form.put(fieldName, getValue((V) view));
            }
            return super.onHandle(form, fieldName, view);
        }
    }

    public final static class TextViewValueGeter extends FormFiller.FieldValueGetter<String, TextView> {

        @Override
        public String getValue(TextView textView) {
            return null;
        }
    }

    public final static class SpinnerValueGeter extends FormFiller.FieldValueGetter<String, TextView> {

        @Override
        public String getValue(TextView textView) {
            return null;
        }
    }

    public final static class RadioButtonValueGeter extends FormFiller.FieldValueGetter<String, TextView> {

        @Override
        public String getValue(TextView textView) {
            return null;
        }
    }

    public final static class RadioGroupValueGeter extends FormFiller.FieldValueGetter<String, TextView> {

        @Override
        public String getValue(TextView textView) {
            return null;
        }
    }

    public final static class CheckBoxValueGeter extends FormFiller.FieldValueGetter<String, TextView> {

        @Override
        public String getValue(TextView textView) {
            return null;
        }
    }


}
