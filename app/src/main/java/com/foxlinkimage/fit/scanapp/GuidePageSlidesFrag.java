package com.foxlinkimage.fit.scanapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class GuidePageSlidesFrag extends Fragment {
    Button btnNaviToMain;
    int page_num;
    Context mContext;

    public GuidePageSlidesFrag() {
        super();
    }

    public GuidePageSlidesFrag(Context context) {
        super();
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        page_num = bundle.getInt("page_num");
        Log.d("TAG", "page number: " + page_num);

        int res = 0;
        switch (page_num) {
            case 0:
                res = R.layout.fragment_screen_slide_main;
                break;
            case 1:
                res = R.layout.fragment_screen_slide_setting;
                break;
            case 2:
                res = R.layout.fragment_screen_slide_device_search;
                break;
            case 3:
                res = R.layout.fragment_screen_slide_setting_detail;
                break;
            case 4:
                res = R.layout.fragment_screen_slide_folder;
                break;
            case 5:
                res = R.layout.fragment_screen_slide_edit;
                break;
            case 6:
                res = R.layout.fragment_screen_slide_share;
                break;
        }

        View rootView = inflater.inflate(res, container, false);

        if (res == R.layout.fragment_screen_slide_share) {
            btnNaviToMain = (Button) rootView.findViewById(R.id.navitomain);

            btnNaviToMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    Intent it = new Intent(mContext, MainActivity.class);
                    startActivity(it);
                }
            });
        }

        return rootView;
    }
}
