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
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;


public class SettingsActivity extends AppCompatActivity {
    EditText edtIP, edtAutoCropThreshold, edtBlankPageSensitivity, edtAutoColorSensitivity;
    ToggleButton tgAdvancedOption;
    Switch swchAutoCrop, swchAutoExpo, swchBlankPageDetect, swchAutoColor;
    Button btnSearchDevice;
    Spinner spnDocumentFormat, spnInputSource, spnColorMode, spnColorSpace, spnCcdChannel, spnBinaryRendering, spnDuplex, spnDiscreteResolution, spnAutoColorDetectionMode;
    PreferenceHelper mPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupComponent();
    }

    @Override
    public void onBackPressed() {
        int error_count = 0;

        //檢查IP是否一樣, 不一樣就存入Preference
        String tmp_IP = mPreferenceHelper.getPreferenceString(PreferenceHelper.key_IP);
        if (!tmp_IP.equals(edtIP.getText().toString())) {
            mPreferenceHelper.setPreferenceString(PreferenceHelper.key_IP, edtIP.getText().toString());
        }

        //AutoCrop on/off狀態
        mPreferenceHelper.setPreferenceBoolean(PreferenceHelper.key_AUTOCROP_ONOFF, swchAutoCrop.isChecked());

        //AutoExposure on/off狀態
        mPreferenceHelper.setPreferenceBoolean(PreferenceHelper.key_AUTOCROP_AUTOEXPOSURE, swchAutoExpo.isChecked());

        //AutoColor on/off狀態
        mPreferenceHelper.setPreferenceBoolean(PreferenceHelper.key_AUTO_COLOR_ONOFF, swchAutoColor.isChecked());

        //BlankPage on/off狀態
        mPreferenceHelper.setPreferenceBoolean(PreferenceHelper.key_BLANKPAGE_DETECTION_ONOFF, swchBlankPageDetect.isChecked());

        //檢查AutoCrop threahold值是否介於0~255
        int tmp_threshold = Integer.valueOf(edtAutoCropThreshold.getText().toString());
        if (tmp_threshold >= 0 && tmp_threshold <= 255) {
            mPreferenceHelper.setPreferenceInteger(PreferenceHelper.key_AUTOCROP_THRESHOLD, tmp_threshold);
        }else
        {
            error_count ++;
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.thresholdwarning), Toast.LENGTH_SHORT).show();
        }

        //檢查BlankPageSensitivity值是否介於0~255
        tmp_threshold = Integer.valueOf(edtBlankPageSensitivity.getText().toString());
        if (tmp_threshold >= 0 && tmp_threshold <= 255) {
            mPreferenceHelper.setPreferenceInteger(PreferenceHelper.key_BLANKPAGE_SENSITIVITY, tmp_threshold);
        }else
        {
            error_count ++;
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.thresholdwarning), Toast.LENGTH_SHORT).show();
        }

        //檢查AutoColorSensitivity值是否介於0~255
        tmp_threshold = Integer.valueOf(edtAutoColorSensitivity.getText().toString());
        if (tmp_threshold >= 0 && tmp_threshold <= 255) {
            mPreferenceHelper.setPreferenceInteger(PreferenceHelper.key_AUTO_COLOR_SENSITIVITY, tmp_threshold);
        }else
        {
            error_count ++;
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.thresholdwarning), Toast.LENGTH_SHORT).show();
        }

        if(error_count == 0)
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void setupComponent() {
        mPreferenceHelper = new PreferenceHelper(this);
        edtIP = (EditText) findViewById(R.id.IP);
        edtAutoCropThreshold = (EditText) findViewById(R.id.autocrop_threshold);
        edtBlankPageSensitivity = (EditText)findViewById(R.id.blankpage_sensitivity);
        edtAutoColorSensitivity = (EditText)findViewById(R.id.autocolor_sensitivity);
        btnSearchDevice = (Button) findViewById(R.id.search_device);
        tgAdvancedOption = (ToggleButton) findViewById(R.id.advance_option);
        swchAutoCrop = (Switch) findViewById(R.id.autocrop);
        swchAutoExpo = (Switch) findViewById(R.id.autocrop_autoexpo);
        swchBlankPageDetect = (Switch)findViewById(R.id.blankpagedetection);
        swchAutoColor = (Switch)findViewById(R.id.autocolor);
        spnDocumentFormat = (Spinner) findViewById(R.id.document_format);
        spnInputSource = (Spinner) findViewById(R.id.input_source);
        spnColorMode = (Spinner) findViewById(R.id.color_mode);
        spnColorSpace = (Spinner) findViewById(R.id.color_space);
        spnCcdChannel = (Spinner) findViewById(R.id.ccd_channel);
        spnBinaryRendering = (Spinner) findViewById(R.id.binary_rendering);
        spnDuplex = (Spinner) findViewById(R.id.duplex);
        spnDiscreteResolution = (Spinner) findViewById(R.id.discrete_resolution);
        spnAutoColorDetectionMode = (Spinner)findViewById(R.id.auto_color_detection_mode);

        findViewById(R.id.layout_scan_params).setVisibility(View.GONE);

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
                } else {
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

        //AutoCrop
        swchAutoCrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.autocrop_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.autocrop_layout).setVisibility(View.GONE);
                }
            }
        });

        //BlankPageDetect
        swchBlankPageDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.blankpagedetect_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.blankpagedetect_layout).setVisibility(View.GONE);
                }
            }
        });

       //AutoColor
        //BlankPageDetect
        swchAutoColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.autocolor_layout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.autocolor_layout).setVisibility(View.GONE);
                }
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

        //AutoColorDetectionMode
        ArrayAdapter<CharSequence> adapColorDetectionModeList = ArrayAdapter.createFromResource(this, R.array.autocolor_detection_mode, R.layout.support_simple_spinner_dropdown_item);
        spnAutoColorDetectionMode.setAdapter(adapColorDetectionModeList);
        spnAutoColorDetectionMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (initializedAdapter != adapterView.getAdapter()) {
                    initializedAdapter = adapterView.getAdapter();
                    return;
                }
                mPreferenceHelper.setPreferenceString(PreferenceHelper.key_AUTO_COLOR_DETECTION_MODE, adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    void loadPreference() {
        edtIP.setText(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_IP));
        edtAutoCropThreshold.setText(String.valueOf(mPreferenceHelper.getPreferenceInteger(PreferenceHelper.key_AUTOCROP_THRESHOLD, 128)));
        edtBlankPageSensitivity.setText(String.valueOf(mPreferenceHelper.getPreferenceInteger(PreferenceHelper.key_BLANKPAGE_SENSITIVITY, 255)));
        edtAutoColorSensitivity.setText(String.valueOf(mPreferenceHelper.getPreferenceInteger(PreferenceHelper.key_AUTO_COLOR_SENSITIVITY, 145)));

        //AutoCrop預設關閉
        swchAutoCrop.setChecked(mPreferenceHelper.getPreferenceBoolean(PreferenceHelper.key_AUTOCROP_ONOFF, false));
        // Autoexposure 預設關閉
        swchAutoExpo.setChecked(mPreferenceHelper.getPreferenceBoolean(PreferenceHelper.key_AUTOCROP_AUTOEXPOSURE, false));
        //BlankPageDetection預設關閉
        swchBlankPageDetect.setChecked(mPreferenceHelper.getPreferenceBoolean(PreferenceHelper.key_BLANKPAGE_DETECTION_ONOFF, false));
        //AutoColor預設關閉
        swchAutoExpo.setChecked(mPreferenceHelper.getPreferenceBoolean(PreferenceHelper.key_AUTO_COLOR_ONOFF, false));


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

        String[] autocolordetectmode = getResources().getStringArray(R.array.autocolor_detection_mode);
        for (int i = 0; i < autocolordetectmode.length; i++) {
            if (autocolordetectmode[i].equals(mPreferenceHelper.getPreferenceString(PreferenceHelper.key_AUTO_COLOR_DETECTION_MODE))) {
                spnAutoColorDetectionMode.setSelection(i);
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
