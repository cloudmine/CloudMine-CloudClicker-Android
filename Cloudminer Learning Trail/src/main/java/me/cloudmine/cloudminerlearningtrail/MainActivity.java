package me.cloudmine.cloudminerlearningtrail;

import android.os.Bundle;
import android.util.Log;

import me.cloudmine.cloudminerlearningtrail.core.CMLTActivity;
import me.cloudmine.cloudminerlearningtrail.fragments.LoginFragment;
import me.cloudmine.cloudminerlearningtrail.R;


public class MainActivity extends CMLTActivity {

    /**
     * Launches the main Fragment. The fragments handle 90% of th
     * @param savedInstanceState
     */
    @Override
    protected void onCloudmineCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        switchFragment(LoginFragment.class);
        Log.d("CLOUDMINE_APPLICATION", "Started Activity");
    }
}
