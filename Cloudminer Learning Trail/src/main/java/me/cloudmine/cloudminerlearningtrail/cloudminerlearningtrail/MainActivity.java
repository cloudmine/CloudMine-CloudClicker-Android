package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail;

import android.os.Bundle;
import android.util.Log;

import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTActivity;
import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.fragments.LoginFragment;


public class MainActivity extends CMLTActivity {

    @Override
    protected void onCloudmineCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        switchFragment(LoginFragment.class);
        Log.d("CLOUDMINE_APPLICATION", "Started Activity");
    }
}
