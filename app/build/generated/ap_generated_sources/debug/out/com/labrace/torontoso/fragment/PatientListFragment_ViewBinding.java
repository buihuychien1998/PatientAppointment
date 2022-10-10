// Generated code from Butter Knife. Do not modify!
package com.labrace.torontoso.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labrace.torontoso.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class PatientListFragment_ViewBinding implements Unbinder {
  private PatientListFragment target;

  private View view7f090025;

  @UiThread
  public PatientListFragment_ViewBinding(final PatientListFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.addPatient, "field 'addPatient' and method 'onClickAddPatient'");
    target.addPatient = Utils.castView(view, R.id.addPatient, "field 'addPatient'", FloatingActionButton.class);
    view7f090025 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickAddPatient();
      }
    });
    target.patientListView = Utils.findRequiredViewAsType(source, R.id.patientList, "field 'patientListView'", RecyclerView.class);
    target.patientScroller = Utils.findRequiredViewAsType(source, R.id.patientScroller, "field 'patientScroller'", VerticalRecyclerViewFastScroller.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PatientListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.addPatient = null;
    target.patientListView = null;
    target.patientScroller = null;

    view7f090025.setOnClickListener(null);
    view7f090025 = null;
  }
}
