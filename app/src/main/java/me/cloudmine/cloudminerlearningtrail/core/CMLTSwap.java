package me.cloudmine.cloudminerlearningtrail.core;

/**
 * Created by brandon on 7/18/14.
 */
public interface CMLTSwap {

    /**
     * Switches between diffrent CMMLTFragments and caches the instance for
     * reuse.
     */
    public void switchFragment(Class<? extends CMLTFragment> fragment);


    public CMLTUser getUser();

    /**
     * Set the current CMLTUser to be used globally across activities and
     * fragments
     *
     * @param user
     */
    public void setUser(CMLTUser user);

    /**
     * Set the title on the top of the current fragment
     * @param drawable
     */
    public void setTitle(int drawable);

    /**
     * Custom action bar button with changeable text and
     * the ability to make the text bold. This is the left side button
     *
     * @param name
     * @param bold
     */
    public void setNegativeButton(String name, boolean bold);

    /**
     * Custom action bar button with changeable text and
     * the ability to make the text bold. This is the right side button
     *
     * @param name
     * @param bold
     */
    public void setPositiveButton(String name, boolean bold);

    /**
     * Go the the last visible fragment. Similar to the android
     * back button.
     */
    public void previousFragment();
}
