package me.cloudmine.cloudminerlearningtrail.core;

import android.os.Build;

import com.cloudmine.api.CMUser;

/**
 * Created by brandon on 6/27/14.
 */
public class CMLTUser extends CMUser implements Comparable<CMLTUser> {
    //Class name in the cloudmine database. This variable is Registered in LearningTrailApplication
    public static final String CLASS_NAME = "CMLTUser";

    //How many times has the user clicked and the user specific cloud clicks
    private int mClicks, mRedClicks, mBlueClicks;
    //Chaching the user name to be public
    private String name;

    /**
     * THIS IS REQUIRED!!!!!
     * You need a public default constructor
     * otherwise de-serialization will fail
     */
    public CMLTUser() {
        this(null, null, null);
    }

    public CMLTUser(String email, String password) {
        this(email, null, password, 0);
    }

    public CMLTUser(String email, String username, String password) {
        this(email, username, password, 0);

    }
    public CMLTUser(String email, String username, String password, int clicks) {
        super(email, username, password);
        this.mClicks = clicks;
        this.name = username;
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
     * Get the clicks and is required for serialization to
     * function correctly. Being public is important to serialization
     * @return
     */
    public Integer getClicks() {return mClicks;}

    public Integer getRed() {
        return mRedClicks;
    }

    public void setRed(int clicks) {
        this.mRedClicks = clicks;
    }

    public void clickRed() {
        mRedClicks++;
        click();
    }

    public Integer getBlue() {
        return mBlueClicks;
    }

    public void setBlue(int clicks) {
        this.mBlueClicks = clicks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public void clickBlue() {
        mBlueClicks++;
        click();
    }


    /**
     * Sets the users clicks and is required for de-serialization to
     * function correctly. Being public is important to de-serialization
     */
    public void setClicks(Integer clicks) {this.mClicks = clicks;}

    /**
     * Increments the clicks by one
     */
    public void click() {
        this.mClicks++;
    }

    @Override
    public int compareTo(CMLTUser cmltUser) {
        if (Build.VERSION.SDK_INT < 11) {
            return Integer.compare(mClicks, cmltUser.getClicks());
        } else {
            return (mClicks<cmltUser.getClicks() ? -1 : (mClicks==cmltUser.getClicks() ? 0 : 1));
        }
    }
}
