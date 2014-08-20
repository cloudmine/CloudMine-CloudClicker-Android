package me.cloudmine.cloudminerlearningtrail.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import me.cloudmine.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.core.CMLTFragment;


public class PlayFragment extends CMLTFragment {

    /**
     * Initializing action bar and setting the layout
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setTitle(R.drawable.lg_cloudmine);
        setPositiveButton("", false);
        setNegativeButton("< Back", true);
        setLayout(R.layout.fragment_play);
        registerOnClickView(R.id.playBtn);
        registerOnClickView(R.id.topScoresBtn);
        registerOnClickView(R.id.shareBtn);
    }

    /**
     * Update the users total clicks view. This is ran onResume
     * because the layout is not inflated until pre-onResume
     */
    @Override
    public void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.playTotalClicks)).setText("Your total Clicks: " + getUser().getClicks());
    }

    /**
     * Play, Top Scores and, Share button are all handled
     * here
     * @param view
     */
    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.playBtn:
                switchFragment(GameFragment.class);
                break;
            case R.id.topScoresBtn:
                switchFragment(TopScoresFragment.class);
                break;
            case R.id.shareBtn:
                View inflate = getActivity().getLayoutInflater().inflate(R.layout.share_banner, null);
                inflate.setDrawingCacheEnabled(true);
                Bitmap bitmap = inflate.getDrawingCache();
                File file = new File(getActivity().getExternalCacheDir(), "cmltshare.png");
                FileOutputStream fos;
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    shareIntent.setType("image/PNG");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.negativeButton:
                previousFragment();
                break;
        }
    }
}
