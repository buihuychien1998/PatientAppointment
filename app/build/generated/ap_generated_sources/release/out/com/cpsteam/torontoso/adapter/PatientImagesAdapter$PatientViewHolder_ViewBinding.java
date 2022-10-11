// Generated code from Butter Knife. Do not modify!
package com.cpsteam.torontoso.adapter;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cpsteam.torontoso.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PatientImagesAdapter$PatientViewHolder_ViewBinding implements Unbinder {
  private PatientImagesAdapter.PatientViewHolder target;

  @UiThread
  public PatientImagesAdapter$PatientViewHolder_ViewBinding(
      PatientImagesAdapter.PatientViewHolder target, View source) {
    this.target = target;

    target.patientImage = Utils.findOptionalViewAsType(source, R.id.patientImage, "field 'patientImage'", ImageView.class);
    target.mask = source.findViewById(R.id.mask);
    target.delete = Utils.findOptionalViewAsType(source, R.id.delete, "field 'delete'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PatientImagesAdapter.PatientViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.patientImage = null;
    target.mask = null;
    target.delete = null;
  }
}
