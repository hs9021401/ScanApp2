package com.foxlinkimage.fit.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity{
    Button btnScan,btnFolder, btnSettings;
    ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponent();

        //加入廣告框架
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("14C59F6EE6CCFF60843C98AA5042B041").build();
//        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void setupComponent()
    {
        imgBackground = (ImageView)findViewById(R.id.imageWelcome);
        imgBackground.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.main_logo));

        btnScan = (Button)findViewById(R.id.btnScan);
        btnFolder = (Button)findViewById(R.id.btnFolder);
        btnSettings = (Button)findViewById(R.id.btnSetting);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(it);
            }
        });

        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, FolderActivity.class);
                startActivity(it);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(it);
            }
        });
    }

}
