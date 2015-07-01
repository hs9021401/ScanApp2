package com.foxlinkimage.fit.ShareUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.foxlinkimage.fit.scanapp.FileUtils;
import com.foxlinkimage.fit.scanapp.PreferenceHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2015/6/12.
 */
public class FacebookShare {
    Activity mContext;
    String mContentType;
    ArrayList<String> mContent;
    List<SharePhoto> listPhoto = new ArrayList<>();
    public static int FB_REQ_CODE = 64207;

    public FacebookShare(Activity context, ArrayList<String> content, String contenttype) {
        mContext = context;
        mContentType = contenttype;
        mContent = content;
    }

    public void Share() {
        collectSharePics();
        postToFacebook();
    }

    private void collectSharePics() {
        if (mContentType.equals("FOLDER")) {
            for (int i = 0; i < mContent.size(); i++) {
                ArrayList<File> files = FileUtils.getFiles(mContent.get(i).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath));
                for (int j = 0; j < (files != null ? files.size() : 0); j++) {
                    Bitmap bmp = BitmapFactory.decodeFile(files.get(j).getAbsolutePath());
                    SharePhoto photo = new SharePhoto.Builder().setBitmap(bmp).build();
                    listPhoto.add(photo);
                }
            }
        } else if (mContentType.equals("FILE")) {
            for (int i = 0; i < mContent.size(); i++) {
                String photo_path = mContent.get(i).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath);
                Bitmap bmp = BitmapFactory.decodeFile(photo_path);
                SharePhoto photo = new SharePhoto.Builder().setBitmap(bmp).build();
                listPhoto.add(photo);   //20150623 發現若插入過多圖片會導致OOM
            }
        }
    }

    private void postToFacebook()
    {
        SharePhotoContent content = new SharePhotoContent.Builder().addPhotos(listPhoto).build();
        ShareDialog shareDialog = new ShareDialog(mContext);
        shareDialog.show(content);
    }

}
