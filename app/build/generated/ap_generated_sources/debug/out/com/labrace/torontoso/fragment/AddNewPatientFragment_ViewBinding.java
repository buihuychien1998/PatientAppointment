// Generated code from Butter Knife. Do not modify!
package com.labrace.torontoso.fragment;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.labrace.torontoso.R;
import fr.ganfra.materialspinner.MaterialSpinner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddNewPatientFragment_ViewBinding implements Unbinder {
  private AddNewPatientFragment target;

  private View view7f090054;

  private View view7f090034;

  private View view7f090024;

  @UiThread
  @SuppressLint("ClickableViewAccessibility")
  public AddNewPatientFragment_ViewBinding(final AddNewPatientFragment target, View source) {
    this.target = target;

    View view;
    target.firstName = Utils.findRequiredViewAsType(source, R.id.firstName, "field 'firstName'", EditText.class);
    target.lastName = Utils.findRequiredViewAsType(source, R.id.lastName, "field 'lastName'", EditText.class);
    view = Utils.findRequiredView(source, R.id.dob, "field 'dob', method 'onItemSelectedDob', and method 'onTouchDob'");
    target.dob = Utils.castView(view, R.id.dob, "field 'dob'", MaterialSpinner.class);
    view7f090054 = view;
    ((AdapterView<?>) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> p0, View p1, int p2, long p3) {
        target.onItemSelectedDob(p0, p1, p2, p3);
      }

      @Override
      public void onNothingSelected(AdapterView<?> p0) {
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View p0, MotionEvent p1) {
        return target.onTouchDob(p0, p1);
      }
    });
    target.sex = Utils.findRequiredViewAsType(source, R.id.sex, "field 'sex'", MaterialSpinner.class);
    target.race = Utils.findRequiredViewAsType(source, R.id.race, "field 'race'", MaterialSpinner.class);
    target.service = Utils.findRequiredViewAsType(source, R.id.service, "field 'service'", MaterialSpinner.class);
    target.braceClass = Utils.findRequiredViewAsType(source, R.id.braceClass, "field 'braceClass'", MaterialSpinner.class);
    target.hyphoKypho = Utils.findRequiredViewAsType(source, R.id.hyphoKypho, "field 'hyphoKypho'", MaterialSpinner.class);
    view = Utils.findRequiredView(source, R.id.cancel, "field 'cancel' and method 'onClickCancel'");
    target.cancel = Utils.castView(view, R.id.cancel, "field 'cancel'", Button.class);
    view7f090034 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickCancel();
      }
    });
    view = Utils.findRequiredView(source, R.id.add, "field 'add' and method 'onClickAdd'");
    target.add = Utils.castView(view, R.id.add, "field 'add'", Button.class);
    view7f090024 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickAdd();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddNewPatientFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.firstName = null;
    target.lastName = null;
    target.dob = null;
    target.sex = null;
    target.race = null;
    target.service = null;
    target.braceClass = null;
    target.hyphoKypho = null;
    target.cancel = null;
    target.add = null;

    ((AdapterView<?>) view7f090054).setOnItemSelectedListener(null);
    view7f090054.setOnTouchListener(null);
    view7f090054 = null;
    view7f090034.setOnClickListener(null);
    view7f090034 = null;
    view7f090024.setOnClickListener(null);
    view7f090024 = null;
  }
}
