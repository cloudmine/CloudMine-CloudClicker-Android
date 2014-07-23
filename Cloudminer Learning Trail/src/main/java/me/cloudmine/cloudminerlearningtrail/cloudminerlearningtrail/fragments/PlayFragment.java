package me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.R;
import me.cloudmine.cloudminerlearningtrail.cloudminerlearningtrail.core.CMLTFragment;


public class PlayFragment extends CMLTFragment {

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

    @Override
    public void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.playTotalClicks)).setText("Your total Clicks: " + getUser().getClicks());
    }

    @Override
    public boolean onViewClick(View view) {
        switch (view.getId()) {
            case R.id.playBtn:
                switchFragment(GameFragment.class);
                return true;
            case R.id.topScoresBtn:
                switchFragment(TopScoresFragment.class);
                return true;
            case R.id.shareBtn:
                View inflate = getActivity().getLayoutInflater().inflate(R.layout.share_banner, null);
                inflate.setDrawingCacheEnabled(true);
                Bitmap b = inflate.getDrawingCache();
                File file = new File(getActivity().getExternalCacheDir(), "cmltshare.png");
                FileOutputStream fos;
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();

                    System.out.println(file.exists() + ":" + file.length());

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    shareIntent.setType("image/PNG");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.negativeButton:
                previousFragment();
                return true;
        }
        return false;
    }
}
