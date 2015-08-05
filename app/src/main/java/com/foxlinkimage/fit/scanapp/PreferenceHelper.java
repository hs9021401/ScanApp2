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

    public final static String key_AUTOCROP_ONOFF = "AUTOCROP_ONOFF";
    public final static String key_AUTOCROP_THRESHOLD = "AUTOCROP_THRESHOLD";
    public final static String key_AUTOCROP_AUTOEXPOSURE = "AUTOCROP_AUTOEXPOSURE";

    public final static String key_BLANKPAGE_DETECTION_ONOFF = "BLANKPAGE_DETECTION";
    public final static String key_BLANKPAGE_SENSITIVITY = "BLANKPAGE_SENSITIVITY";

    public final static String key_AUTO_COLOR_ONOFF = "AUTO_COLORONOFF";
    public final static String key_AUTO_COLOR_DETECTION_MODE = "AUTO_COLOR_DETECTION_MODE";
    public final static String key_AUTO_COLOR_SENSITIVITY = "AUTO_COLOR_SENSITIVITY";


//    public final static String key_COMPRESS_RATE = "SHARE_COMPRESS_RATE";
    public static final String key_DROPBOX_ACCESS_KEY_NAME = "ACCESS_KEY";
    public static final String key_DROPBOX_ACCESS_SECRET_NAME = "ACCESS_SECRET_NAME";

    public final static String key_FIRST_LAUNCH = "FIRST_LAUNCH";

    public final static String strDefaultSaveFolderPath = Environment.getExternalStorageDirectory().getPath() + "/FIT";
    public final static String strDefaultSaveFolderThumbnailsPath= Environment.getExternalStorageDirectory().getPath() + "/FIT_thumbnails";
    private Context context;

    private SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context)
    {
        this.context = context;
    }

    public String getPreferenceString(String key)
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
                strDefaultValue = "Feeder";
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

    public Integer getPreferenceInteger(String key)
    {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        return sharedPreferences.getInt(key, 0);
    }

    public Integer getPreferenceInteger(String key, Integer iniValue)
    {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, iniValue);
        return sharedPreferences.getInt(key, iniValue);
    }

    public Boolean getPreferenceBoolean(String key)
    {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        return sharedPreferences.getBoolean(key, true);
    }

    public Boolean getPreferenceBoolean(String key, Boolean iniValue)
    {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        return sharedPreferences.getBoolean(key, iniValue);
    }

    public void setPreferenceString(String key, String value) {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void setPreferenceBoolean(String key, Boolean value)
    {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void setPreferenceInteger(String key, Integer value) {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void clearPreference(String key)
    {
        sharedPreferences = context.getSharedPreferences(strPreferenceListName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.clear();
        editor.apply();
    }

}
