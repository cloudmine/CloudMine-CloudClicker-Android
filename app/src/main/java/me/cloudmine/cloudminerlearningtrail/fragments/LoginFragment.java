package me.cloudmine.cloudminerlearningtrail.fragments;

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

import me.cloudmine.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.core.CMLTUser;
import me.cloudmine.cloudminerlearningtrail.core.CMLTFragment;


public class LoginFragment extends CMLTFragment {

    private static final int DEFAULT_DELAY = 1800000;
    private Long mDelay = -1l;

    /**
     * Initializing action bar and setting the layout
     */
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

    /**
     * Save the forgot password value to cache
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("mDelay", mDelay);
    }

    /**
     * Handles the emailLogin, negative action bar, positive action bar, facebook and google login
     * button clicks
     * @param view
     */
    @Override
    public void onViewClick(final View view) {
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
                    return;
                }
                final ProgressDialog mProgressDialog = getDefaultProgressDialog();
                mProgressDialog.show();
                final CMLTUser user = new CMLTUser(email, password);
                user.login(getCloudmineActivity(), new Response.Listener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse loginResponse) {
                        InputMethodManager imm = (InputMethodManager) getCloudmineActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        onReponseUser(user, mProgressDialog);
                        Log.d(TAG, "Successfully logged in!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onFailed(volleyError.networkResponse != null ? volleyError.networkResponse.statusCode : 0, mProgressDialog);
                        Log.e(TAG, "Failed to login", volleyError);
                    }
                });
                break;
            case R.id.facebookLogin:
                loginSocial();
                break;
            case R.id.googlePlusLogin:
                loginSocial();
                break;
            case R.id.postiveButton:
                switchFragment(RegisterFragment.class);
                break;
            case R.id.negativeButton:
                EditText loginEmailAddress = (EditText) findViewById(R.id.loginEmail);
                String lEmail = loginEmailAddress.getText().toString();
                if (lEmail.length() <= 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(lEmail).matches()) {
                    loginEmailAddress.setError("Invalid email address");
                    Log.d(TAG, "Invalid email address");
                    break;
                }
                if (mDelay >= System.currentTimeMillis()) {
                    Toast.makeText(getActivity(), "Please wait " + (((mDelay - System.currentTimeMillis()) / 1000) / 60)
                            + "minutes (s) before resetting your email!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Sent reset email request");
                    break;
                }
                mDelay = System.currentTimeMillis() + DEFAULT_DELAY;
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
                break;
            default:
                break;
        }
    }

    /**
     * Cloudmine function to clear the pervious session cookies
     * and log into a social media network. Facebook in this scenario
     */
    private void loginSocial() {
        final ProgressDialog mProgressDialog = getDefaultProgressDialog();
        mProgressDialog.show();
        CMAndroidSocial.clearSessionCookies(getActivity());
        CMAndroidSocial.loginThroughSocial(CMSocial.Service.FACEBOOK, getActivity(), new Callback<CMSocialLoginResponse>() {
            @Override
            public void onCompletion(CMSocialLoginResponse cmSocialLoginResponse) {
                onReponseUser((CMLTUser) cmSocialLoginResponse.getUser(), mProgressDialog);
                Log.d(TAG, "Successfully logged through social media");
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                throwable.printStackTrace();
                onFailed(999, mProgressDialog);
                Log.e(TAG, "Failed to login through social media", throwable);
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

    /**
     * When a request returns a user lets handle it here
     * @param user
     */
    private void onReponseUser(CMLTUser user, ProgressDialog mProgressDialog) {
        mProgressDialog.dismiss();
        setUser(user);
        switchFragment(PlayFragment.class);
    }

    /**
     * When a user attempts to login but it fails
     * we handle it here. Reducing code redundancy
     * @param error
     */
    private void onFailed(int error, ProgressDialog mProgressDialog) {
        mProgressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getCloudmineActivity())
                .setTitle("LoginFragment Failed!")
                .setPositiveButton("Ok", null);
        if (error == 401) {
            builder.setMessage("Invalid credentials.");
        } else {
            builder.setMessage("Unable to log you in.");
        }
        builder.show();
    }

    private ProgressDialog getDefaultProgressDialog() {
        ProgressDialog mProgressDialog = new ProgressDialog(getCloudmineActivity());
        mProgressDialog.setMessage("Loading...");
        return mProgressDialog;
    }
}
