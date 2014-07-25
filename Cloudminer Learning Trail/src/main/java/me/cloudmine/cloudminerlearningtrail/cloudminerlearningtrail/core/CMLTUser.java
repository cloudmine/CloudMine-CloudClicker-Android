package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core;

import com.cloudmine.api.CMUser;

/**
 * Created by brandon on 6/27/14.
 */
public class CMLTUser extends CMUser implements Comparable<CMLTUser> {

    //The name of the user
    private String mName;
    //How many times has the user clicked
    private int mClicks;

    /**
     * THIS IS REQUIRED!!!!!
     * You need a public default constructor
     * otherwise de-serialization will fail
     */
    public CMLTUser() {
        this(null, null);
    }

    public CMLTUser(String email, String password) {
        this(null, email, password);
    }

    public CMLTUser(String name, String email, String password) {
        this(name, email, password, 0);

    }
    public CMLTUser(String name, String email, String password, int clicks) {
        super(email, password);
        this.mName = name;
        this.mClicks = clicks;
    }

    /**
     * Get the users name and is required for serialization to
     * function correctly. Being public is important to serialization
     * @return
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the users name and is required for de-serialization to
     * function correctly. Being public is important to de-serialization
     */
    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * Get the clicks and is required for serialization to
     * function correctly. Being public is important to serialization
     * @return
     */
    public Integer getClicks() {return mClicks;}

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
        return Integer.compare(mClicks, cmltUser.getClicks());
    }
}
