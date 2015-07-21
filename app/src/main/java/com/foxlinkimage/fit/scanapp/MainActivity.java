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

        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("14C59F6EE6CCFF60843C98AA5042B041").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //(可刪除)取得FB app的hash key
//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("com.foxlinkimage.fit.scanapp", PackageManager.GET_SIGNATURES);
//            for(Signature signature : info.signatures)
//            {
//                MessageDigest md;
//                md =MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String KeyResult =new String(Base64.encode(md.digest(), 0));//String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", KeyResult);
//                Toast.makeText(MainActivity.this, "My FB Key is \n" + KeyResult, Toast.LENGTH_LONG).show();
//            }
//        }catch(PackageManager.NameNotFoundException e1){Log.e("name not found", e1.toString());
//        }catch(NoSuchAlgorithmException e){Log.e("no such an algorithm", e.toString());
//        }catch(Exception e){Log.e("exception", e.toString());}

    }

    void setupComponent()
    {
        imgBackground = (ImageView)findViewById(R.id.imageWelcome);
        //imgBackground.setImageDrawable(getResources().getDrawable(R.drawable.main_logo));
        imgBackground.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.main_logo));


        btnScan = (Button)findViewById(R.id.btnScan);
        btnFolder = (Button)findViewById(R.id.btnFolder);
        btnSettings = (Button)findViewById(R.id.btnSetting);

//        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/w2.ttc");    //使用其他字體
//        btnScan.setTypeface(font);
//        btnScan.setTextSize(24.0F);
//        btnFolder.setTypeface(font);
//        btnFolder.setTextSize(24.0F);
//        btnSettings.setTypeface(font);
//        btnSettings.setTextSize(24.0F);


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
