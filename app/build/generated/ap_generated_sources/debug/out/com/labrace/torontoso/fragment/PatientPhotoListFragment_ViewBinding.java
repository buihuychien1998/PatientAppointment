// Generated code from Butter Knife. Do not modify!
package com.labrace.torontoso.fragment;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.labrace.torontoso.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PatientPhotoListFragment_ViewBinding implements Unbinder {
  private PatientPhotoListFragment target;

  @UiThread
  public PatientPhotoListFragment_ViewBinding(PatientPhotoListFragment target, View source) {
    this.target = target;

    target.imageListView = Utils.findRequiredViewAsType(source, R.id.imageList, "field 'imageListView'", RecyclerView.class);
    target.textEmpty = Utils.findRequiredViewAsType(source, R.id.textEmpty, "field 'textEmpty'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PatientPhotoListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageListView = null;
    target.textEmpty = null;
  }
}
