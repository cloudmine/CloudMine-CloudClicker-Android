package me.cloudmine.cloudminerlearningtrail;

import android.app.Application;
import android.util.Log;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.persistance.ClassNameRegistry;

import me.cloudmine.cloudminerlearningtrail.core.CMLTCloud;
import me.cloudmine.cloudminerlearningtrail.core.CMLTUser;

/**
 * Created by brandon on 7/11/14.
 */
public class LearningTrailApplication extends Application {
    //This is your KEY available on the dashboard
    public static final String KEY =  "aa6aeecb10e2407b8e1b2ea866b418ef";
    //This is your id also available on the dashboard
    public static final String KEY_ID = "33a972520cb447bda768b5d93ceb266f";

    private CMLTUser mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing the keys in Cloudmine
        Log.d("CLOUDMINE_APPLICATION", "Started Application");
        CMApiCredentials.initialize(KEY_ID, KEY, getApplicationContext());
        ClassNameRegistry.register(CMLTUser.CLASS_NAME, CMLTUser.class);
        ClassNameRegistry.register(CMLTCloud.CLASS_NAME, CMLTCloud.class);
    }

    /**
     * Grabs the global CMLTUser
     * @return
     */
    public CMLTUser getUser() {
        return mUser;
    }

    /**
     * Sets the global CMLTUser
     * @param user
     */
    public void setUser(CMLTUser user) {
        this.mUser = user;
    }
}
