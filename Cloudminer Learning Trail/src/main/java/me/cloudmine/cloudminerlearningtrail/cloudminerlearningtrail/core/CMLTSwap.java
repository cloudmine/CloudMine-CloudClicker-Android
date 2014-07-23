package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core;

/**
 * Created by brandon on 7/18/14.
 */
public interface CMLTSwap {

    public void switchFragment(Class<? extends CMLTFragment> fragment);

    public CMLTUser getUser();

    public void setUser(CMLTUser user);

    public void setTitle(int drawable);

    public void setNegativeButton(String name, boolean bold);

    public void setPositiveButton(String name, boolean bold);

    public void previousFragment();
}
