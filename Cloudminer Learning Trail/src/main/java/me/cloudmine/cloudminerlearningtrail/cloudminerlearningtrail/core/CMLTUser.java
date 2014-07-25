package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core;

import com.cloudmine.api.CMUser;

/**
 * Created by brandon on 6/27/14.
 */
public class CMLTUser extends CMUser implements Comparable<CMLTUser> {

    private String mName;
    private int mClicks;

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

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Integer getClicks() {return mClicks;}

    public void setClicks(Integer clicks) {this.mClicks = clicks;}

    public void click() {
        this.mClicks++;
    }

    @Override
    public int compareTo(CMLTUser cmltUser) {
        return Integer.compare(mClicks, cmltUser.getClicks());
    }
}
