package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail;

import android.app.Application;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.persistance.ClassNameRegistry;

import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTCloud;
import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTUser;

/**
 * Created by brandon on 7/11/14.
 */
public class LearningTrailApplication extends Application {
    //This is your KEY available on the dashboard
    public static final String KEY =  "b94ba211089b4acb8b61a902cf64cc19";
    //This is your id also available on the dashboard
    public static final String KEY_ID = "a9abfaa749ed4122affb3661112ea888";

    private CMLTUser mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing the keys in Cloudmine
        System.out.println("LAUNCHED APPLICATION");
        CMApiCredentials.initialize(KEY_ID, KEY, getApplicationContext());
        ClassNameRegistry.register(CMLTCloud.CLASS_NAME, CMLTCloud.class);

    }


    public CMLTUser getUser() {
        return mUser;
    }

    public void setUser(CMLTUser user) {
        this.mUser = user;
    }
}
