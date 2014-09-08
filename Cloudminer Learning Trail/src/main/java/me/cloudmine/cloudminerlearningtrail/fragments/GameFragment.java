package me.cloudmine.cloudminerlearningtrail.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.CreationResponse;

import java.util.ArrayList;
import java.util.List;

import me.cloudmine.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.core.CMLTCloud;
import me.cloudmine.cloudminerlearningtrail.core.CMLTFragment;


public class GameFragment extends CMLTFragment {

    //List of cloud click listeners
    private List<OnCloudClickListener> mClickListenerList;

    /**
     * Initializing action bar and setting the layout
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setTitle(R.drawable.lg_clickthecloud);
        setPositiveButton("", false);
        setNegativeButton("< Back", true);
        setLayout(R.layout.fragment_game);
    }

    /**
     * After the view has been create this adds clouds onto the
     * horizontal list view and starts a Object query request to match
     * each database object to an object in a list view
     * The query searches for only a CMLTCloud class type
     */
    @Override
    public void onStart() {
        super.onStart();
        mClickListenerList = new ArrayList<OnCloudClickListener>();
        LinearLayout linearLayoutCloudView = (LinearLayout) findViewById(R.id.linearLayoutCloudView);
        linearLayoutCloudView.removeAllViews();
        addCloud(linearLayoutCloudView, CloudType.BLUE_CLOUD);
        addCloud(linearLayoutCloudView, CloudType.RED_CLOUD);

        LocallySavableCMObject.searchObjects(getActivity(), SearchQuery.filter(CMLTCloud.class).searchQuery(), new Response.Listener<CMObjectResponse>() {
            @Override
            public void onResponse(CMObjectResponse response) {
                List<CMLTCloud> clouds = response.getObjects(CMLTCloud.class);
                if (response.hasError() || clouds.size() < 1) {
                    Log.e(TAG, "No Cloud objects returned");
                    return;
                }
                for (CMLTCloud cmltCloud : clouds) {
                    for (OnCloudClickListener cloudClickListener : mClickListenerList) {
                        if (!cloudClickListener.cmltCloud.getCmid().equalsIgnoreCase(cmltCloud.getCmid())) {
                            continue;
                        }
                        synchronized (cloudClickListener.cmltCloud) {
                            cloudClickListener.cmltCloud = cmltCloud;
                        }
                        cloudClickListener.updateClicks();
                    }
                }
            }
        });
    }


    /**
     * Updates the users total clicks along with
     * all of the clouds total clicks.
     */
    @Override
    public void onStop() {
        for (OnCloudClickListener listener : mClickListenerList) {
            listener.cmltCloud.save(getActivity());
        }
        getUser().saveProfile(getActivity(), new Response.Listener<CreationResponse>() {
            @Override
            public void onResponse(CreationResponse creationResponse) {
                Log.d(TAG, "User has been successfully logged in");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "", volleyError);
            }
        });
        super.onStop();
    }

    /**
     * Sends a user to the previous fragment if the negative button is clicked
     * @param view
     */
    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.negativeButton:
                previousFragment();
                break;
            default:
                break;
        }
    }

    /**
     * Adds a cloud to the horizontal list group
     * It also adds the OnCloudClickListener so when a
     * cloud is click it increments the cloud and the user
     * @param group
     * @param type
     */
    private void addCloud(ViewGroup group, CloudType type) {
        final View inflate = getActivity().getLayoutInflater().inflate(R.layout.cloudview, null);
        assert inflate != null;
        OnCloudClickListener clickListener = new OnCloudClickListener(type.id, inflate);
        mClickListenerList.add(clickListener);
        inflate.setOnClickListener(clickListener);
        ((ImageView) inflate.findViewById(R.id.cloudView)).setImageDrawable(getResources().getDrawable(type.drawable));
        group.addView(inflate);
    }

    /**
     * Private class for increasing the users and clouds total
     */
    private class OnCloudClickListener implements View.OnClickListener {

        //The Cloudmine LocallySavable to send the total to the database later
        protected CMLTCloud cmltCloud;
        //Instance of the view to update the total visually
        private View view;

        public OnCloudClickListener(String id, View view) {
            this.view = view;
            this.cmltCloud = new CMLTCloud(id);
        }

        /**
         * When the cloud is clicked the phone will vibrate for 50 milliseconds,
         * increments the cloud total and increments the users total then finally
         * logs the updated value
         * @param view
         */
        @Override
        public void onClick(View view) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            assert animation != null;
            view.findViewById(R.id.cloudView).startAnimation(animation);
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(50);
            cmltCloud.click();
            if (cmltCloud.getCmid().equals(CloudType.RED_CLOUD.id)) {
                getUser().clickRed();
            } else if (cmltCloud.getCmid().equals(CloudType.BLUE_CLOUD.id)) {
                getUser().clickBlue();
            }
            updateClicks();
            Log.d(TAG, "Cloud: " + cmltCloud.getCmid() + " Clicks: " + cmltCloud.getClicks());
        }

        /**
         * Updates the total click amount and is synchronized because there is a very
         * small chance where cloudmine sends the callback and updates the click the exact
         * same time the user is finishing clicking the cloud
         */
        protected void updateClicks() {
            if (view == null) {
                return;
            }
            ((TextView)view.findViewById(R.id.clicksCounter)).setText("Clicks: " + cmltCloud.getClicks());
            ((TextView)findViewById(R.id.totalClicks)).setText("Your total Clicks: " + getUser().getClicks());
        }
    }

    private enum CloudType {
        RED_CLOUD("redcloud", R.drawable.cloud_red),
        BLUE_CLOUD("bluecloud", R.drawable.cloud_blue);

        public final String id;
        public final int drawable;

        CloudType(String id, int drawable) {
            this.id = id;
            this.drawable = drawable;
        }
    }
}
