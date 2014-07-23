package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTUser;

/**
 * Created by brandon on 7/18/14.
 */
public class CMLTAdapter extends ArrayAdapter<CMLTUser> {

    private int mResource;
    private Context mContext;

    public CMLTAdapter(Activity context, int resource, List<CMLTUser> entries) {
        super(context, resource, entries);
        this.mResource = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            v = inflater.inflate(mResource, parent, false);
        }

        CMLTUser item = getItem(position);

        if (item != null) {
            TextView rank = (TextView) v.findViewById(R.id.rankValue);
            TextView name = (TextView) v.findViewById(R.id.nameValue);
            TextView clicks = (TextView) v.findViewById(R.id.clicksValue);

            if (rank != null) {
                rank.setText(String.valueOf(position + 1));
            }
            if (name != null) {
                name.setText(item.getName());
            }
            if (clicks != null) {
                clicks.setText(item.getClicks() + " Clicks");
            }
        }
        return v;
    }
}
