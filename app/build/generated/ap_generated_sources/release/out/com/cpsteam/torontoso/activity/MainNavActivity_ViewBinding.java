// Generated code from Butter Knife. Do not modify!
package com.cpsteam.torontoso.activity;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cpsteam.torontoso.R;
import com.google.android.material.navigation.NavigationView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainNavActivity_ViewBinding implements Unbinder {
  private MainNavActivity target;

  @UiThread
  public MainNavActivity_ViewBinding(MainNavActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainNavActivity_ViewBinding(MainNavActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.navigationView = Utils.findRequiredViewAsType(source, R.id.nav_view, "field 'navigationView'", NavigationView.class);
    target.drawer = Utils.findRequiredViewAsType(source, R.id.drawer_layout, "field 'drawer'", DrawerLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainNavActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.navigationView = null;
    target.drawer = null;
  }
}
