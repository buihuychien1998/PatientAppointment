package com.labrace.torontoso.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dd.processbutton.iml.ActionProcessButton;
import com.labrace.torontoso.R;
import com.labrace.torontoso.common.LoginStatus;
import com.labrace.torontoso.service.TorsoCADService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "dung:123456", "GezB@123456"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    @BindView(R.id.username)
    AutoCompleteTextView mUserNameView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.btn_login)
    ActionProcessButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSignInButton.setMode(ActionProcessButton.Mode.ENDLESS);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(), 0);

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mSignInButton.setProgress(1);
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, LoginStatus> {

        private final String mUserName;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected LoginStatus doInBackground(Void... params) {
            LoginStatus loginStatus = TorsoCADService.logIn(LoginActivity.this, mUserName, mPassword);

            return loginStatus;
        }

        @Override
        protected void onPostExecute(LoginStatus loginStatus) {
            mAuthTask = null;
            loginStatus = LoginStatus.SUCCESS;
            switch (loginStatus) {
                case EXCEPTION:
                    Toast.makeText(LoginActivity.this, getString(R.string.error_exception), Toast.LENGTH_SHORT).show();
                    break;
                case ACCOUNT_DISABLED:
                    mSignInButton.setProgress(-1);
                    mPasswordView.setError(getString(R.string.error_account_disabled));
                    mPasswordView.requestFocus();
                    break;
                case INVALID_USERNAME_OR_PASSWORD:
                    mSignInButton.setProgress(-1);
                    mPasswordView.setError(getString(R.string.error_incorrect_credential));
                    mUserNameView.setError(getString(R.string.error_incorrect_credential));
                    mPasswordView.requestFocus();
                    break;
                case SUCCESS:
                    mSignInButton.setProgress(100);
                    Intent intent = new Intent(LoginActivity.this, MainNavActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

