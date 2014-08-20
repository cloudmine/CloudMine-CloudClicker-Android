package me.cloudmine.cloudminerlearningtrail.core;

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

    //Tag used for logging in the cloudmine fragment
    public final static String TAG = "CLOUDMINE_FRAGMENT";

    //Variables used to remove a large amount of redundant android code.
    private CMLTActivity mActivity;
    private int mLayout, mDrawableTitle;
    private String mPositiveValue, mNegativeValue;
    private boolean mPositiveBold, mNegativeBold;
    private List<Integer> mRegisterViews;

    /**
     * Public default constructor used to set the default values of
     * the fragment class
     */
    public CMLTFragment() {
        this.mLayout = -1;
        this.mPositiveValue = "";
        this.mNegativeValue = "";
        this.mPositiveBold = false;
        this.mNegativeBold = false;
        this.mRegisterViews = new ArrayList<Integer>();
    }

    /**
     * Centralizes all of the click events so that they are handled
     * in one functions and chosen via a switch statement
     * @param view
     */
    public abstract void onViewClick(View view);

    /**
     * Enables the class to be saved when the fragment
     * goes to the backstack
     * @param savedInstance
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    /**
     * Initialize the action bar and set
     * the registered views to refer to the onViewClick function
     */
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

    /**
     * Attaches the activity whether it is visible or hidden
     * Hidden fragments do not have an attached fragment
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof CMLTActivity)) {
            Log.w(TAG, "Attempted to attach a non cloudmine activity to fragment!");
            throw new ClassCastException("Class is not a cloudmine mActivity");
        }
        this.mActivity = (CMLTActivity) activity;
    }

    /**
     * Detaches the activity whether it is visible or hidden
     * Hidden fragments do not have an attached fragment
     */
    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    /**
     * This removes a redundant onCreate and was replaced by a function that enables
     * you to specify your layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

    /**
     * Set what views will refer to the onViewClick
     * Method
     * @param id
     */
    public void registerOnClickView(int id) {
       mRegisterViews.add(id);
    }

    /**
     * Set the layout inflated and returned onViewCreate
     * Set this on or before the onCreate function
     * @param layout
     */
    protected void setLayout(int layout) {
        this.mLayout = layout;
    }

    /**
     * Get the cloudmine activity attached to the fragment
     * it will be null if there is no view attached or it does
     * not inherit from CMLTActivity
     * @return
     */
    public CMLTActivity getCloudmineActivity() {
        return mActivity;
    }

    /**
     * Why is not already a thing?
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

}
