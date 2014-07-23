package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMAndroidSocial;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.CMSocial;
import com.cloudmine.api.rest.callbacks.Callback;
import com.cloudmine.api.rest.response.CMResponse;
import com.cloudmine.api.rest.response.CMSocialLoginResponse;
import com.cloudmine.api.rest.response.LoginResponse;

import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTUser;
import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTFragment;


public class LoginFragment extends CMLTFragment {

    private ProgressDialog mProgressDialog;
    private Long mDelay = -1l;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setTitle(R.drawable.lg_cloudmine);
        setPositiveButton("Sign Up >", true);
        setNegativeButton("Forgot Password", false);
        setLayout(R.layout.fragment_login);
        registerOnClickView(R.id.emailLogin);
        registerOnClickView(R.id.facebookLogin);
        registerOnClickView(R.id.googlePlusLogin);
        if (savedInstance != null && savedInstance.containsKey("mDelay")) {
            mDelay = savedInstance.getLong("mDelay");
            Log.d(TAG, "Delay updated");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("mDelay", mDelay);
    }

    @Override
    public boolean onViewClick(final View view) {
        switch (view.getId()) {
            case R.id.emailLogin:
                EditText loginView = (EditText) findViewById(R.id.loginEmail);
                EditText passwordView = (EditText) findViewById(R.id.loginPassword);
                String email = loginView.getText().toString();
                String password = passwordView.getText().toString();

                boolean hadError = false;
                if (email.length() <= 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    loginView.setError("Invalid email address");
                    hadError = true;
                }
                if (password.length() <= 0) {
                    passwordView.setError("Invalid password");
                    hadError = true;
                }
                if (hadError) {
                    return true;
                }

                mProgressDialog = new ProgressDialog(getCloudmine());
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                final CMLTUser user = new CMLTUser(email, password);
                user.login(getCloudmine(), new Response.Listener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse loginResponse) {
                        InputMethodManager imm = (InputMethodManager) getCloudmine().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        onReponseUser(user);
                        Log.d(TAG, "Successfully logged in!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onFailed(volleyError.networkResponse != null ? volleyError.networkResponse.statusCode : 0);
                        Log.e(TAG, "Failed to login", volleyError);
                    }
                });
                return true;
            case R.id.facebookLogin:
                mProgressDialog = new ProgressDialog(getCloudmine());
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
                loginSocial();
                return true;
            case R.id.googlePlusLogin:
                mProgressDialog = new ProgressDialog(getCloudmine());
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
                loginSocial();
                return true;
            case R.id.postiveButton:
                switchFragment(RegisterFragment.class);
                return true;
            case R.id.negativeButton:
                EditText lView = (EditText) findViewById(R.id.loginEmail);
                String lEmail = lView.getText().toString();
                if (lEmail.length() <= 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(lEmail).matches()) {
                    lView.setError("Invalid email address");
                    Log.d(TAG, "Invalid email address");
                    return true;
                }
                if (mDelay >= System.currentTimeMillis()) {
                    Toast.makeText(getActivity(), "Please wait " + (((mDelay - System.currentTimeMillis()) / 1000) / 60)
                            + "minutes (s) before resetting your email!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Sent reset email request");
                    return true;
                }
                mDelay = System.currentTimeMillis() + 1800000;
                new CMUser(lEmail, "").resetPasswordRequest(new Callback<CMResponse>() {
                    @Override
                    public void onCompletion(CMResponse cmResponse) {
                        Toast.makeText(getActivity(), "Password reset email sent!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Reset password worked!");
                    }

                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        Toast.makeText(getActivity(), "Password reset failed!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Reset password failed", throwable);
                    }

                    @Override
                    public void setStartTime(long l) {

                    }

                    @Override
                    public long getStartTime() {
                        return 0;
                    }
                });
                return true;
            default:
                return false;
        }
    }

    private void loginSocial() {
        CMAndroidSocial.loginThroughSocial(CMSocial.Service.FACEBOOK, getActivity(), new Callback<CMSocialLoginResponse>() {
            @Override
            public void onCompletion(CMSocialLoginResponse cmSocialLoginResponse) {
                onReponseUser((CMLTUser) cmSocialLoginResponse.getUser());
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                throwable.printStackTrace();
                onFailed(999);
            }

            @Override
            public void setStartTime(long l) {

            }

            @Override
            public long getStartTime() {
                return 20;
            }
        });
    }

    private void onReponseUser(CMLTUser user) {
        mProgressDialog.dismiss();
        setUser(user);
        switchFragment(PlayFragment.class);
    }

    private void onFailed(int error) {
        mProgressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getCloudmine())
                .setTitle("LoginFragment Failed!")
                .setPositiveButton("Ok", null);
        if (error == 401) {
            builder.setMessage("Invalid credentials.");
        } else {
            builder.setMessage("Unable to log you in.");
        }
        builder.show();
    }
}
