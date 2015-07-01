package com.foxlinkimage.fit.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.foxlinkimage.fit.ShareUtils.DropboxShare;


public class SettingsActivity extends AppCompatActivity {
    EditText edtIP;
    ToggleButton tgAdvancedOption, tgConnectDropbox, tgConnectEvernote;
    Button btnSearchDevice;
    Spinner spnDocumentFormat, spnInputSource, spnColorMode, spnColorSpace, spnCcdChannel, spnBinaryRendering, spnDuplex, spnDiscreteResolution;
    PreferenceHelper mPreferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupComponent();
    }

    @Override
    protected void onDestroy() {
        String tmp_IP = mPreferenceHelper.getPreferenceString(PreferenceHelper.key_IP);
        if (!tmp_IP.equals(edtIP.getText().toString())) {
            mPreferenceHelper.setPreferenceString(PreferenceHelper.key_IP, edtIP.getText().toString());
        }
        super.onDestroy();
    }

    void setupComponent() {
        mPreferenceHelper = new PreferenceHelper(this);
        edtIP = (EditText) findViewById(R.id.IP);
        btnSearchDevice = (Button) findViewById(R.id.search_device);
        tgAdvancedOption = (ToggleButton) findViewById(R.id.advance_option);
        tgConnectEvernote = (ToggleButton)findViewById(R.id.connect_evernote);
        tgConnectDropbox = (ToggleButton)findViewById(R.id.connect_dropbox);
        spnDocumentFormat = (Spinner) findViewById(R.id.document_format);
        spnInputSource = (Spinner) findViewById(R.id.input_source);
        spnColorMode = (Spinner) findViewById(R.id.color_mode);
        spnColorSpace = (Spinner) findViewById(R.id.color_space);
        spnCcdChannel = (Spinner) findViewById(R.id.ccd_channel);
        spnBinaryRendering = (Spinner) findViewById(R.id.binary_rendering);
        spnDuplex = (Spinner) findViewById(R.id.duplex);
        spnDiscreteResolution = (Spinner) findViewById(R.id.discrete_resolution);


        btnSearchDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, SearchDeviceActivity.class);
                startActivity(it);
            }
        });

        tgAdvancedOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //顯示進階設訂區塊的動畫
                    //第1,2個參數是變化之前的X座標
                    //第3,4個參數是變化之後的X座標
                    //第5,6個參數是變化之前的Y座標
                    //第7,8個參數是變化之後的Y座標
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);
                    findViewById(R.id.layout_scan_params).startAnimation(mShowAction);
                    findViewById(R.id.layout_scan_params).setVisibility(View.VISIBLE);
                }else{
                    //隱藏進階設訂區塊的動畫
                    TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            -1.0f);
                    mHiddenAction.setDuration(500);
                    findViewById(R.id.layout_scan_params).setAnimation(mHiddenAction);
                    findViewById(R.id.layout_scan_params).setVisibility(View.GONE);
                }
            }
        });

        tgConnectDropbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DropboxShare mDropboxShare = new DropboxShare(getApplicationContext(),null,null);
                if(isChecked)
                {
                    //連結dropbox
                    mDropboxShare.Resume();
                    Toast.makeText(SettingsActivity.this,"Dropbox connected", Toast.LENGTH_SHORT).show();
                }else
                {
                    mDropboxShare.UnConnect();
                    Toast.makeText(SettingsActivity.this,"Dropbox is disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tgConnectEvernote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });


        //IP
        edtIP.setText(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_IP));


        //Document Format
        ArrayAdapter<CharSequence> adapFormatList = ArrayAdapter.createFromResource(this, R.array.document_format, R.layout.support_simple_spinner_dropdown_item);
        spnDocumentFormat.setAdapter(adapFormatList);
        spnDocumentFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_DOCUMENT_FORMAT, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Input Source
        ArrayAdapter<CharSequence> adapSourceList = ArrayAdapter.createFromResource(this, R.array.input_source, R.layout.support_simple_spinner_dropdown_item);
        spnInputSource.setAdapter(adapSourceList);
        spnInputSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_INPUT_SOURCE, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Color Mode
        ArrayAdapter<CharSequence> adapModeList = ArrayAdapter.createFromResource(this, R.array.color_mode, R.layout.support_simple_spinner_dropdown_item);
        spnColorMode.setAdapter(adapModeList);

        spnColorMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_COLOR_MODE, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Color Space
        ArrayAdapter<CharSequence> adapSpaceList = ArrayAdapter.createFromResource(this, R.array.color_space, R.layout.support_simple_spinner_dropdown_item);
        spnColorSpace.setAdapter(adapSpaceList);

        spnColorSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_COLOR_SPACE, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //CCD Channel
        ArrayAdapter<CharSequence> adapCcdChannelList = ArrayAdapter.createFromResource(this, R.array.ccd_channel, R.layout.support_simple_spinner_dropdown_item);
        spnCcdChannel.setAdapter(adapCcdChannelList);

        spnCcdChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_CCD_CHANNEL, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Binary Rendering
        ArrayAdapter<CharSequence> adapBinaryRenderingList = ArrayAdapter.createFromResource(this, R.array.binary_rendering, R.layout.support_simple_spinner_dropdown_item);
        spnBinaryRendering.setAdapter(adapBinaryRenderingList);

        spnBinaryRendering.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_BINARY_RENDERING, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //Duplex
        ArrayAdapter<CharSequence> adapDuplexList = ArrayAdapter.createFromResource(this, R.array.duplex, R.layout.support_simple_spinner_dropdown_item);
        spnDuplex.setAdapter(adapDuplexList);

        spnDuplex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_DUPLEX, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //discrete resolution (dpi)
        ArrayAdapter<CharSequence> adapDpiList = ArrayAdapter.createFromResource(this, R.array.discrete_resolution, R.layout.support_simple_spinner_dropdown_item);
        spnDiscreteResolution.setAdapter(adapDpiList);
        spnDiscreteResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_DISCRETE_RESOLUTION, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }


    void loadPreference() {
        edtIP.setText(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_IP));

        String[] mode = getResources().getStringArray(R.array.color_mode);
        for (int i = 0; i < mode.length; i++) {
            if (mode[i].equals(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_COLOR_MODE))) {
                spnColorMode.setSelection(i);
                break;
            }
        }

        String[] source = getResources().getStringArray(R.array.input_source);
        for (int i = 0; i < source.length; i++) {
            if (source[i].equals(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_INPUT_SOURCE))) {
                spnInputSource.setSelection(i);
                break;
            }
        }

        String[] dpi = getResources().getStringArray(R.array.discrete_resolution);
        for (int i = 0; i < dpi.length; i++) {
            if (dpi[i].equals(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_DISCRETE_RESOLUTION))) {
                spnDiscreteResolution.setSelection(i);
                break;
            }
        }

        String[] format = getResources().getStringArray(R.array.document_format);
        for (int i = 0; i < format.length; i++) {
            if (format[i].equals(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_DOCUMENT_FORMAT))) {
                spnDocumentFormat.setSelection(i);
                break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreference();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
