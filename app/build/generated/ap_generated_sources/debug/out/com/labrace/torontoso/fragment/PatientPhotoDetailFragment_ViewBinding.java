// Generated code from Butter Knife. Do not modify!
package com.labrace.torontoso.fragment;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.labrace.torontoso.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PatientPhotoDetailFragment_ViewBinding implements Unbinder {
  private PatientPhotoDetailFragment target;

  @UiThread
  public PatientPhotoDetailFragment_ViewBinding(PatientPhotoDetailFragment target, View source) {
    this.target = target;

    target.patientImage = Utils.findRequiredViewAsType(source, R.id.patientImage, "field 'patientImage'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PatientPhotoDetailFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.patientImage = null;
  }
}
