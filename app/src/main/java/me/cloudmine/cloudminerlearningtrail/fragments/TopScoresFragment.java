package me.cloudmine.cloudminerlearningtrail.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.response.CMObjectResponse;

import java.util.Collections;
import java.util.List;

import me.cloudmine.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.core.CMLTAdapter;
import me.cloudmine.cloudminerlearningtrail.core.CMLTFragment;
import me.cloudmine.cloudminerlearningtrail.core.CMLTUser;


public class TopScoresFragment extends CMLTFragment {


    /**
     * Initializing action bar and setting the layout
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setTitle(R.drawable.lg_top_clicks);
        setPositiveButton("", false);
        setNegativeButton("< Back", true);
        setLayout(R.layout.fragment_top_scores);
    }

    /**
     * Pulls all of the users from the database sorts them
     * and then displays it in a SortedList
     */
    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog mProgressDialog  = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        CMUser.loadAllUserProfiles(getActivity(), new Response.Listener<CMObjectResponse>() {
            @Override
            public void onResponse(CMObjectResponse objectResponse) {
                mProgressDialog.dismiss();
                List<CMLTUser> objects = objectResponse.getObjects(CMLTUser.class);
                Collections.sort(objects);
                Collections.reverse(objects);
                CMLTAdapter cmltAdapter = new CMLTAdapter(getActivity(), R.layout.row_basic_text, objects);
                ((ListView) findViewById(R.id.clickerListView)).setAdapter(cmltAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Failed to grab top scores", volleyError);
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * Handles a back button
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


}
