package me.cloudmine.cloudminerlearningtrail.core;

import com.cloudmine.api.db.LocallySavableCMObject;

/**
 * Created by brandon on 6/30/14.
 */
public class CMLTCloud extends LocallySavableCMObject {

    //Class name in the cloudmine database. This variable is Registered in LearningTrailApplication
    public static final String CLASS_NAME = "CMLTCloud";
    //Number of times use clouds has been clicked
    private int mClicks;
    //Unique cloud ID
    private String mCMLTID;

    /**
     * THIS IS REQUIRED!!!!!
     * You need a public default constructor
     * otherwise deserialization will fail
     */
    public CMLTCloud() {
        super();
    }

    public CMLTCloud(String id) {
        this(id, 0);
    }

    public CMLTCloud(String id, int clicks) {
        this();
        this.mCMLTID = id;
        this.mClicks = clicks;
    }

    /**
     * This is required for serialization and serialization
     * to function correctly. Class name can be whatever you want
     * but cannot be the same name of another Cloudmine object and
     * cannot change otherwise you will loose all prior cloudmine objects
     *
     * @return
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    /**
     * Get the clouds id and is required for serialization to
     * function correctly. Being public is important to serialization
     * @return
     */
    public String getCmid() {
        return mCMLTID;
    }

    /**
     * Sets the clouds UUID and is required for de-serialization to
     * function correctly. Being public is important to de-serialization
     * @param mCMLTID
     */
    public void setCmid(String mCMLTID) {
        this.mCMLTID = mCMLTID;
    }

    /**
     * Gets the clouds current clicks and is required for serialization to
     * function correctly. Being public is important to serialization
     * @return
     */
    public int getClicks() {
        return mClicks;
    }

    /**
     * Sets the clouds total clicks and is required for de-serialization to
     * function correctly. Being public is important to de-serialization
     * @param mClicks
     */
    public void setClicks(int mClicks) {
        this.mClicks = mClicks;
    }

    /**
     * Increments the total clicks
     */
    public void click() {
        mClicks++;
    }
}
