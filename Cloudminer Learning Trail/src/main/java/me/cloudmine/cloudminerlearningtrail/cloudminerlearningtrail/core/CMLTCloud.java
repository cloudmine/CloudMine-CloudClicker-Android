package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core;

import com.cloudmine.api.db.LocallySavableCMObject;

/**
 * Created by brandon on 6/30/14.
 */
public class CMLTCloud extends LocallySavableCMObject {

    public static final String CLASS_NAME = "CMLTCloud";

    private int mClicks;
    private String mCMLTID;

    public CMLTCloud() {
        super();
    }

    public CMLTCloud(String cmltID) {
        this(cmltID, 0);
    }

    public CMLTCloud(String cmltID, int clicks) {
        this();
        this.mCMLTID = cmltID;
        this.mClicks = clicks;
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    public String getCMLTID() {
        return mCMLTID;
    }

    public void setCMLTID(String mCMLTID) {
        this.mCMLTID = mCMLTID;
    }

    public int getClicks() {
        return mClicks;
    }

    public void setClicks(int mClicks) {
        this.mClicks = mClicks;
    }

    public void click() {
        mClicks++;
    }
}
