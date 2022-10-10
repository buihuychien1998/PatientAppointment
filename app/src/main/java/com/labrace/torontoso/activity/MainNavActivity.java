package com.labrace.torontoso.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.labrace.torontoso.R;
import com.labrace.torontoso.common.Constants;
import com.labrace.torontoso.fragment.PatientListFragment;
import com.labrace.torontoso.service.TorsoCADService;
import com.labrace.torontoso.utils.PreferencesUtils;
import com.labrace.torontoso.utils.TorsoCADUncaughtExceptionHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dung on 4/18/2016.
 */
public class MainNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE = 9999;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private ActionBarDrawerToggle toggle;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView txtUserName = (TextView) header.findViewById(R.id.txtUserName);
        txtUserName.setText(PreferencesUtils.loadData(this, Constants.USER_NAME));

        openPage(Constants.SCREEN_PATIENT_LIST);

        checkAndRequestPermissions();

        Thread.setDefaultUncaughtExceptionHandler(new TorsoCADUncaughtExceptionHandler(this));
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(this, getString(R.string.permissions_granted), Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, getString(R.string.permissions_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null && fragment.isVisible() && fragment instanceof PatientListFragment) {
            finish();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_new_patient:
                openPage(Constants.SCREEN_NEW_PATIENT);
                break;
            case R.id.nav_patient_list:
                openPage(Constants.SCREEN_PATIENT_LIST);
                break;
            case R.id.nav_sign_out:
                signOut();
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void signOut() {
        TorsoCADService.logOut(MainNavActivity.this);
        Intent intent = new Intent(MainNavActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void openPage(String fName, Bundle... bundle) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        if (bundle != null && bundle.length > 0) {
            tx.replace(R.id.main, Fragment.instantiate(MainNavActivity.this, fName, bundle[0]));
        } else {
            tx.replace(R.id.main, Fragment.instantiate(MainNavActivity.this, fName));
        }
        tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tx.addToBackStack(fName);
        tx.commit();
    }

    public void invalidateToolbarForOtherPage() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(getDrawerToggleDelegate().getThemeUpIndicator());
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggle.isDrawerIndicatorEnabled()) {
                    onBackPressed();
                }
            }
        });
    }

    public void showProgress(int messageId) {
        mProgress = ProgressDialog.show(this, null, getString(messageId), true);
    }

    public void hideProgress() {
        if(mProgress != null) {
            mProgress.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
