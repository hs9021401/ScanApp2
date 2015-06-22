package com.foxlinkimage.fit.ShareUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.foxlinkimage.fit.scanapp.FileUtils;
import com.foxlinkimage.fit.scanapp.PreferenceHelper;
import com.foxlinkimage.fit.scanapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Alex on 2015/6/12.
 */
public class DropboxShare {
    Context mContext;

    public static final String ACCOUNT_PREFS_NAME = "prefs";
    public static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    public static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final String APP_KEY = "cu11x6j7gvm8nyy";
    private static final String APP_SECRET = "nvwjjms26gnoaib";

    DropboxAPI<AndroidAuthSession> mApi;
    String mContentType;
    ArrayList<String> mContent;
    AndroidAuthSession session;
    Notification.Builder builder;
    NotificationManager notificationManager;


    public DropboxShare(Context context, ArrayList<String> content, String contenttype) {
        mContext = context;
        mContent = content;
        mContentType = contenttype;

        // We create a new AuthSession so that we can use the Dropbox API.
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        checkAppKeySetup();
        Boolean b = mApi.getSession().isLinked();
        Log.d("TAG", "LOGGED is " + b);
    }

    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs =   mContext.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = mContext.getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            Log.d("TAG", "URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
        }
    }

    public void Authenticate() {
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();
                storeAuth(session);
            } catch (IllegalStateException e) {
                Log.d("TAG", "Error authenticating", e);
            }
        } else {
            mApi.getSession().startOAuth2Authentication(mContext);
        }
    }

    private void storeAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = mContext.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.apply();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = mContext.getSharedPreferences(DropboxShare.ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(DropboxShare.ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(DropboxShare.ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.apply();
        }
    }

    public void Share() {
        if (session.authenticationSuccessful()) {
            Log.d("TAG", "authenticate pass");
            UploadContentTask uploadContentTask = new UploadContentTask();
            uploadContentTask.execute();
        } else {
            Log.d("TAG", "authenticate fail");
        }
    }

    public class UploadContentTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new Notification.Builder(mContext);
//            PendingIntent contentIndent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, GalleryActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("分享至Dropbox")
                    .setContentText("上傳檔案中....")
                    .setProgress(100, 0, false);
//            .setContentIntent(contentIndent)
            Notification notification = builder.build();
            notificationManager.notify(111, notification);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            builder.setProgress(100, values[0], false);
            notificationManager.notify(111, builder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mContentType.equals("FOLDER")) {
                for (int i = 0; i < mContent.size(); i++) {
                    ArrayList<File> files = FileUtils.getFiles(mContent.get(i).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath));
                    for (int j = 0; j < files.size(); j++) {
                        uploadFile(files.get(j));
                        double d = (double) (j + 1) / (double) files.size() * 100;
                        publishProgress((int) d);
                    }
                }

            } else if (mContentType.equals("FILE")) {
                for (int i = 0; i < mContent.size(); i++) {
                    uploadFile(new File(mContent.get(i).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath)));
                    double d = (double) (i + 1) / (double) mContent.size() * 100;
                    publishProgress((int) d);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            builder.setContentText("上傳完畢")
                    .setContentTitle("分享至Dropbox完畢");
            Notification notification = builder.build();
            notificationManager.notify(111, notification);
            super.onPostExecute(aVoid);
        }
    }

    private void uploadFile(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            String path = mContentType.equals("FOLDER") ? "/" + file.getParentFile().getName() + "/" + file.getName()
                    : "/" + file.getName();
            Log.d("TAG", path);
            DropboxAPI.Entry response = mApi.putFile(path, inputStream, file.length(), null, null);
            Log.d("TAG", "The uploaded file's rev is: " + response.rev);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    public DropboxAPI<AndroidAuthSession> getmApi() {
        return mApi;
    }

}
