// Generated code from Butter Knife. Do not modify!
package com.cpsteam.torontoso.activity;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cpsteam.torontoso.R;
import com.dd.processbutton.iml.ActionProcessButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.mUserNameView = Utils.findRequiredViewAsType(source, R.id.username, "field 'mUserNameView'", AutoCompleteTextView.class);
    target.mPasswordView = Utils.findRequiredViewAsType(source, R.id.password, "field 'mPasswordView'", EditText.class);
    target.mProgressView = Utils.findRequiredView(source, R.id.login_progress, "field 'mProgressView'");
    target.mLoginFormView = Utils.findRequiredView(source, R.id.login_form, "field 'mLoginFormView'");
    target.mSignInButton = Utils.findRequiredViewAsType(source, R.id.btn_login, "field 'mSignInButton'", ActionProcessButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mUserNameView = null;
    target.mPasswordView = null;
    target.mProgressView = null;
    target.mLoginFormView = null;
    target.mSignInButton = null;
  }
}
