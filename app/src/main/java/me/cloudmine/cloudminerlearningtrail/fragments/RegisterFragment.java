package me.cloudmine.cloudminerlearningtrail.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.rest.response.CreationResponse;

import me.cloudmine.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.core.CMLTUser;
import me.cloudmine.cloudminerlearningtrail.core.CMLTFragment;


public class RegisterFragment extends CMLTFragment {

    /**
     * Initializing action bar and setting the layout
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setTitle(R.drawable.lg_cloudmine);
        setPositiveButton("", false);
        setNegativeButton("< Back", true);
        setLayout(R.layout.fragment_register);
        registerOnClickView(R.id.registerButton);
    }

    /**
     * Register button and Back button are handled here
     *
     * @param view
     */
    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                EditText name = (EditText) findViewById(R.id.registerName);
                EditText email = (EditText) findViewById(R.id.registerEmail);
                EditText password = (EditText) findViewById(R.id.registerPassword);
                EditText passwordRetype = (EditText) findViewById(R.id.registerPasswordRepeat);

                boolean failed = false;
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Invalid email address!");
                    failed = true;
                }
                if (!password.getText().toString().equals(passwordRetype.getText().toString())) {
                    password.setError("Passwords do not match");
                    passwordRetype.setError("Passwords do not match");
                    failed = true;
                }

                if (failed || hasValue(name, email, password, passwordRetype)) {
                    return;
                }

                final ProgressDialog pdia = new ProgressDialog(getCloudmineActivity());
                pdia.setMessage("Loading...");
                pdia.show();

                final CMLTUser user = new CMLTUser(email.getText().toString(), name.getText().toString(), password.getText().toString());
                user.create(getCloudmineActivity(), new Response.Listener<CreationResponse>() {
                    @Override
                    public void onResponse(CreationResponse creationResponse) {
                        pdia.dismiss();
                        setUser(user);
                        switchFragment(PlayFragment.class);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pdia.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getCloudmineActivity())
                                .setTitle("Registration Failed!")
                                .setPositiveButton("Ok", null);
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                            builder.setMessage("This account already exists.");
                        } else {
                            builder.setMessage("Unable register your account.");
                        }
                        builder.show();
                        Log.e(TAG, "Failed to register user", volleyError);
                    }
                });
                break;
            case R.id.negativeButton:
                previousFragment();
                break;
            default:
                break;
        }
    }

    /**
     * Do any of the register views have a empty value
     * @param textViews
     * @return
     */
    private boolean hasValue(TextView... textViews) {
        boolean failed = false;
        for (TextView textView : textViews) {
            if (textView.length() <= 0) {
                textView.setError("Required Field!");
                failed = true;
            }
        }
        return failed;
    }
}
