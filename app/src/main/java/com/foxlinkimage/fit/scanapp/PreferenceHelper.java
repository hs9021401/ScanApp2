package com.foxlinkimage.fit.scanapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * Created by Alex on 2015/4/27.
 */
public class PreferenceHelper {
    public final static String strPreferenceListName = "MY_PREFERENCE";
    public final static String key_IP = "SCAN_IP";
    public final static String key_DOCUMENT_FORMAT = "SCAN_DOCUMENT_FORMAT";
    public final static String key_INPUT_SOURCE = "SCAN_INPUT_SOURCE";
    public final static String key_COLOR_MODE = "SCAN_COLOR_MODE";
    public final static String key_COLOR_SPACE = "SCAN_COLOR_SPACE";
    public final static String key_CCD_CHANNEL = "SCAN_CCD_CHANNEL";
    public final static String key_BINARY_RENDERING = "SCAN_BINARY_RENDERING";
    public final static String key_DUPLEX = "SCAN_DUPLEX";
    public final static String key_DISCRETE_RESOLUTION = "SCAN_DISCRETE_RESOLUTION";

    public final static String strDefaultSaveFolderPath = Environment.getExternalStorageDirectory().getPath() + "/FIT";
    public final static String strDefaultSaveFolderThumbnailsPath= Environment.getExternalStorageDirectory().getPath() + "/FIT_thumbnails";
    private Context context;

    private SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context)
    {
        this.context = context;
    }

    public String getPreference(String key)
    {
        String strDefaultValue = "";
        switch(key)
        {
            case key_IP:
                strDefaultValue = "192.168.1.1";
                break;
            case key_DOCUMENT_FORMAT:
                strDefaultValue = "image/jpeg";
                break;
            case key_INPUT_SOURCE:
                strDefaultValue = "Platen";
                break;
            case key_COLOR_MODE:
                strDefaultValue = "RGB24";
                break;
            case key_COLOR_SPACE:
                strDefaultValue = "sRGB";
                break;
            case key_CCD_CHANNEL:
                strDefaultValue = "NTSC";
                break;
            case key_BINARY_RENDERING:
                strDefaultValue = "Threshold";
                break;
            case key_DUPLEX:
                strDefaultValue = "false";
                break;
            case key_DISCRETE_RESOLUTION:
                strDefaultValue = "300";
                break;
            default:
                strDefaultValue = "DEFAULT_NULL";
                break;
        }

        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        return sharedPreferences.getString(key, strDefaultValue);
    }

    public void setPreference(String key, String value) {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        sharedPreferences.edit().putString(key, value).apply();
    }

}
