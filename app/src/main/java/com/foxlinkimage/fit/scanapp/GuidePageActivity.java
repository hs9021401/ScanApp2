package com.foxlinkimage.fit.scanapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuidePageActivity extends FragmentActivity {
    private static final int NUM_PAGES = 7;
    PreferenceHelper preferenceHelper;
    ImageView[] dots;
    private int currentIndex;


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("離開教學")
                .setMessage("確定要離開教學說明?")
                .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructment);
        preferenceHelper = new PreferenceHelper(this);

        boolean first_launch = preferenceHelper.getPreferenceBoolean(PreferenceHelper.key_FIRST_LAUNCH);
        if (first_launch) {
            initDots();
            ViewPager mPager = (ViewPager) findViewById(R.id.pager);
            PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setPageTransformer(true, new GuidePageZoomOutTransformer());
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Log.d("TAG", "position is " + position);
                    dots[currentIndex].setEnabled(true);
                    dots[position].setEnabled(false);
                    currentIndex = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            preferenceHelper.setPreferenceBoolean(PreferenceHelper.key_FIRST_LAUNCH, false);

        } else {
            finish();
            Intent it = new Intent(GuidePageActivity.this, MainActivity.class);
            startActivity(it);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            GuidePageSlidesFrag newFragment = new GuidePageSlidesFrag(GuidePageActivity.this);
            Bundle bundle = new Bundle();
            bundle.putInt("page_num", i);
            newFragment.setArguments(bundle);
            return newFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void initDots() {
        LinearLayout dotslayout = (LinearLayout) findViewById(R.id.dotslayout);

        dots = new ImageView[7];
        for (int i = 0; i < 7; i++) {
            dots[i] = (ImageView) dotslayout.getChildAt(i);
            dots[i].setEnabled(true);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

}
