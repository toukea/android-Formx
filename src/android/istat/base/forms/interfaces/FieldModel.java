package android.istat.base.forms.interfaces;

import android.istat.base.forms.Form;
import android.view.View;

public interface FieldModel {
	public boolean onModelling(Form form, String fieldname, View view);
}