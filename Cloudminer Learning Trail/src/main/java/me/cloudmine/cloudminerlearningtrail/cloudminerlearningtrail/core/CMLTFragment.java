package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandon on 7/11/14.
 */
public abstract class CMLTFragment extends Fragment implements CMLTSwap {

    public final static String TAG = "CLOUDMINE_FRAGMENT";

    private CMLTActivity mActivity;
    private int mLayout, mDrawableTitle;
    private String mPositiveValue, mNegativeValue;
    private boolean mPositiveBold, mNegativeBold;
    private List<Integer> mRegisterViews;

    public CMLTFragment() {
        this.mLayout = -1;
        this.mPositiveValue = "";
        this.mNegativeValue = "";
        this.mPositiveBold = false;
        this.mNegativeBold = false;
        this.mRegisterViews = new ArrayList<Integer>();
    }

    public abstract boolean onViewClick(View view);

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.setNegativeButton(mNegativeValue, mNegativeBold);
        mActivity.setPositiveButton(mPositiveValue, mPositiveBold);
        mActivity.setTitle(mDrawableTitle);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick(view);
            }
        };
        for (int viewID : mRegisterViews) {
            View view = findViewById(viewID);
            if (view == null) {
                Log.w("CLOUDMINEFRAGMENT", "Unable to register view: " + viewID);
                continue;
            }
            view.setOnClickListener(listener);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof CMLTActivity)) {
            Log.w(TAG, "Attempted to attach a non cloudmine activity to fragment!");
            throw new ClassCastException("Class is not a cloudmine mActivity");
        }
        this.mActivity = (CMLTActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(mLayout, container, false);
    }

    @Override
    public void switchFragment(Class<? extends CMLTFragment> fragment) {
        mActivity.switchFragment(fragment);
    }

    @Override
    public CMLTUser getUser() {
        return mActivity.getUser();
    }

    @Override
    public void setUser(CMLTUser user) {
        mActivity.setUser(user);
    }

    @Override
    public void setTitle(int drawable) {
        mDrawableTitle = drawable;
        mActivity.setTitle(drawable);
    }

    @Override
    public void setPositiveButton(String name, boolean bold) {
        mPositiveValue = name;
        mPositiveBold = bold;
        mActivity.setPositiveButton(mPositiveValue, mPositiveBold);
    }

    @Override
    public void setNegativeButton(String name, boolean bold) {
       mNegativeValue = name;
       mNegativeBold = bold;
       mActivity.setNegativeButton(mNegativeValue, mNegativeBold);
    }

    @Override
    public void previousFragment() {
        mActivity.previousFragment();
    }

    public void registerOnClickView(int id) {
       mRegisterViews.add(id);
    }

    protected void setLayout(int layout) {
        this.mLayout = layout;
    }

    public CMLTActivity getCloudmine() {
        return mActivity;
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

}
