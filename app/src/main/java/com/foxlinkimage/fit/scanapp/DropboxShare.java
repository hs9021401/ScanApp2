package com.foxlinkimage.fit.scanapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Alex on 2015/6/12.
 */
public class DropboxShare {
    Context mContext;

    private static final String APP_KEY = "cu11x6j7gvm8nyy";
    private static final String APP_SECRET = "nvwjjms26gnoaib";

    DropboxAPI<AndroidAuthSession> mApi;
    String mContentType;
    ArrayList<String> mContent;
    AndroidAuthSession session;
    Notification.Builder builder;
    NotificationManager notificationManager;

    PreferenceHelper mPreferenceHelper;


//    public DropboxShare(Context context) {
//        new DropboxShare(context, null, null);
//    }

    public DropboxShare(Context context, ArrayList<String> content, String contenttype) {
        mContext = context;
        mContent = content;
        mContentType = contenttype;

        mPreferenceHelper = new PreferenceHelper(context);
        // We create a new AuthSession so that we can use the Dropbox API.
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
    }

    private void loadAuth(AndroidAuthSession session) {
        String key = mPreferenceHelper.getPreferenceString(PreferenceHelper.key_DROPBOX_ACCESS_KEY_NAME);
        String secret = mPreferenceHelper.getPreferenceString(PreferenceHelper.key_DROPBOX_ACCESS_SECRET_NAME);
        if (key.equals("DEFAULT_NULL") || secret.equals("DEFAULT_NULL") || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            session.setOAuth2AccessToken(secret);
        } else {
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    private void storeAuth(AndroidAuthSession session) {
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            mPreferenceHelper.setPreferenceString(PreferenceHelper.key_DROPBOX_ACCESS_KEY_NAME, "oauth2:");
            mPreferenceHelper.setPreferenceString(PreferenceHelper.key_DROPBOX_ACCESS_SECRET_NAME, oauth2AccessToken);
        }
    }


    public void Resume() {
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();
                storeAuth(session);
            } catch (IllegalStateException e) {
                Log.d("TAG", "Error authenticating", e);
            }
        } else {
            //20150624 修改原本的方式, 改採抓取已儲存的token進行認證
            mApi = new DropboxAPI<AndroidAuthSession>(session);
            String key = mPreferenceHelper.getPreferenceString(PreferenceHelper.key_DROPBOX_ACCESS_KEY_NAME);
            String secret = mPreferenceHelper.getPreferenceString(PreferenceHelper.key_DROPBOX_ACCESS_SECRET_NAME);

            if (key == null || secret == null || key.length() == 0 || secret.length() == 0) {
                mApi.getSession().startOAuth2Authentication(mContext);
            } else {
                session.setOAuth2AccessToken(secret);
            }
        }
    }

    public void UnConnect()
    {
        mApi = new DropboxAPI<>(session);
        mApi.getSession().unlink();
        mPreferenceHelper.clearPreference(PreferenceHelper.key_DROPBOX_ACCESS_KEY_NAME);
        mPreferenceHelper.clearPreference(PreferenceHelper.key_DROPBOX_ACCESS_SECRET_NAME);
    }

    public void Share() {
        UploadContentTask uploadContentTask = new UploadContentTask();
        uploadContentTask.execute();
    }

    public class UploadContentTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new Notification.Builder(mContext);
//            PendingIntent contentIndent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, FolderActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(mContext.getResources().getString(R.string.sharetodropbox))
                    .setContentText(mContext.getResources().getString(R.string.uploading))
                    .setProgress(100, 0, false);
//                            .setContentIntent(contentIndent);
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
            builder.setContentText(mContext.getResources().getString(R.string.returntofolder))
                    .setContentTitle(mContext.getResources().getString(R.string.uploadcomplete));
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

//    public DropboxAPI<AndroidAuthSession> getmApi() {
//        return mApi;
//    }

}
