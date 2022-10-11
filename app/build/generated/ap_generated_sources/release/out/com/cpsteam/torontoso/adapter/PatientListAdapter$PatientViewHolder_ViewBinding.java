// Generated code from Butter Knife. Do not modify!
package com.cpsteam.torontoso.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cpsteam.torontoso.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PatientListAdapter$PatientViewHolder_ViewBinding implements Unbinder {
  private PatientListAdapter.PatientViewHolder target;

  @UiThread
  public PatientListAdapter$PatientViewHolder_ViewBinding(
      PatientListAdapter.PatientViewHolder target, View source) {
    this.target = target;

    target.patientName = Utils.findOptionalViewAsType(source, R.id.patientName, "field 'patientName'", TextView.class);
    target.patientId = Utils.findOptionalViewAsType(source, R.id.patientId, "field 'patientId'", TextView.class);
    target.patientDob = Utils.findOptionalViewAsType(source, R.id.patientDob, "field 'patientDob'", TextView.class);
    target.patientGender = Utils.findOptionalViewAsType(source, R.id.patientGender, "field 'patientGender'", TextView.class);
    target.patientBraClass = Utils.findOptionalViewAsType(source, R.id.patientBraClass, "field 'patientBraClass'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PatientListAdapter.PatientViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.patientName = null;
    target.patientId = null;
    target.patientDob = null;
    target.patientGender = null;
    target.patientBraClass = null;
  }
}
