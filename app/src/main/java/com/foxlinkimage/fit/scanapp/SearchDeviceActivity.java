package com.foxlinkimage.fit.scanapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class SearchDeviceActivity extends Activity {
    Button btnSelDevice_Cancel, btnSelDevice_OK;
    NsdHelper mNsdHelper;
    PreferenceHelper mPreferenceHelper;
    static ListView lvDiscovereDevicesList;
    String strSelectedIP;
    static DevicesAdapter myDevicesAdapter;
    static ArrayList<String[]> mCandidateDeviceList;
    static String Hostname, IP, Port;
    static Boolean bDiplicateDeviceFound;
    private final MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);
        setupComponent();
        setupDiscovery();
    }

    void setupComponent() {
        btnSelDevice_Cancel = (Button) findViewById(R.id.selDev_Cancel);
        btnSelDevice_OK = (Button) findViewById(R.id.selDev_OK);
        lvDiscovereDevicesList = (ListView) findViewById(R.id.discovereDevicesList);
        mPreferenceHelper = new PreferenceHelper(this);
        mCandidateDeviceList = new ArrayList<>();
        myDevicesAdapter = new DevicesAdapter(getApplicationContext(), mCandidateDeviceList);
        lvDiscovereDevicesList.setAdapter(myDevicesAdapter);

        lvDiscovereDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View vPreSelectedItem;
            int select_item = -1;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if ((select_item == -1) || (select_item == position)) {
                    view.setBackgroundColor(Color.YELLOW);
                } else {
                    vPreSelectedItem.setBackgroundDrawable(null);
                    view.setBackgroundColor(Color.YELLOW);
                }
                vPreSelectedItem = view;
                select_item = position;

                strSelectedIP = mCandidateDeviceList.get(position)[1];
            }
        });

        btnSelDevice_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSelDevice_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_IP, strSelectedIP);
                finish();
            }
        });

    }

    private static class MyHandler extends Handler {
        private final WeakReference<SearchDeviceActivity> mActivity;

        public MyHandler(SearchDeviceActivity activity) {
            mActivity = new WeakReference<SearchDeviceActivity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            SearchDeviceActivity searchDeviceActivity = mActivity.get();
            if (searchDeviceActivity != null) {
                Hostname = msg.getData().getString("HOSTNAME");


//                if (Hostname != null) {
//                    if (Hostname.contains("\\032")) {
//                        Hostname.replace("\\032", " ");
//                    }
//                }

                IP = msg.getData().getString("IP");
                Port = msg.getData().getString("PORT");
                String[] strTuple = new String[3];
                strTuple[0] = Hostname;
                strTuple[1] = IP;
                strTuple[2] = Port;

                addToCandidateDeviceList(strTuple);

                myDevicesAdapter.notifyDataSetChanged();
            }


        }
    }

    static void addToCandidateDeviceList(String[] data) {
        if (!mCandidateDeviceList.isEmpty()) {
            bDiplicateDeviceFound = false;
            for (int i = 0; i < mCandidateDeviceList.size(); i++) {
                if (mCandidateDeviceList.get(i)[1].equals(data[1])) {
                    bDiplicateDeviceFound = true;
                    break;
                }
            }
            if (!bDiplicateDeviceFound)
                mCandidateDeviceList.add(data);
        } else {
            mCandidateDeviceList.add(data);
        }
    }

    void setupDiscovery() {
        //找尋可用機台
        mNsdHelper = new NsdHelper(this, handler);
        mNsdHelper.initializeNsd();
        mNsdHelper.discoverServices();
    }

    @Override
    protected void onDestroy() {
        mNsdHelper.stopDiscovery();
        super.onDestroy();
    }

}
