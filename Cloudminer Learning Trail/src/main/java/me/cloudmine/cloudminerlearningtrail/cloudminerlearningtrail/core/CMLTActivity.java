package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.LearningTrailApplication;
import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.R;

/**
 * This class removes the need to constantly initialize your keys in each activity.
 * If your keys change per activity remove the static final from each variable and
 * re-initialize the keys onCloudmineCreate().
 */
public abstract class CMLTActivity extends ActionBarActivity implements CMLTSwap {

    public final static String TAG = "CLOUDMINE_ACTIVITIY";

    private Map<Class<? extends CMLTFragment>, CMLTFragment> mFragmentCache;
    private ActionBar.LayoutParams mParams;

    protected CMLTActivity() {
        mFragmentCache = new HashMap<Class<? extends CMLTFragment>, CMLTFragment>();
        mParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }

    protected abstract void onCloudmineCreate(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCloudmineCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        View customNav = LayoutInflater.from(this).inflate(R.layout.cmlt_actionbar, null);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("main_view");
                if (fragment == null || !(fragment instanceof CMLTFragment)) {
                    Log.w(TAG, "Unable to pass actionbar click event to fragment");
                    return;
                }
                ((CMLTFragment) fragment).onViewClick(view);
            }
        };
        customNav.findViewById(R.id.negativeButton).setOnClickListener(listener);
        customNav.findViewById(R.id.postiveButton).setOnClickListener(listener);
        actionBar.setCustomView(customNav, mParams);
    }

    @Override
    public void switchFragment(Class<? extends CMLTFragment> fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            CMLTFragment cloudmineFragment;
            if (mFragmentCache.containsKey(fragment)) {
                cloudmineFragment = mFragmentCache.get(fragment);
            } else {
                cloudmineFragment = fragment.newInstance();
                mFragmentCache.put(fragment, cloudmineFragment);
            }
            transaction.replace(R.id.coreContent, cloudmineFragment, "main_view");
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Unknown error when switching fragments", e);
            throw new NullPointerException("Null fragment instance");
        }
    }

    @Override
    public void setUser(CMLTUser user) {
        ((LearningTrailApplication) getApplication()).setUser(user);
    }

    @Override
    public CMLTUser getUser() {
        return ((LearningTrailApplication) getApplication()).getUser();
    }

    @Override
    public void setTitle(int drawable) {
        ((ImageView) findViewById(R.id.coreTitle)).setImageDrawable(getResources().getDrawable(drawable));
    }

    @Override
    public void setPositiveButton(String name, boolean bold) {
        setButtonText(getActionBar().getCustomView().findViewById(R.id.postiveButton), name, bold);
    }

    @Override
    public void setNegativeButton(String name, boolean bold) {
        setButtonText(getActionBar().getCustomView().findViewById(R.id.negativeButton), name, bold);
    }

    @Override
    public void previousFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
    }

    private void setButtonText(View view, String name, boolean bold) {
        TextView tView = (TextView) view;
        tView.setText(name);
        if (bold) {
            tView.setTypeface(null, Typeface.BOLD);
        } else {
            tView.setTypeface(null, Typeface.NORMAL);
        }
    }
}
