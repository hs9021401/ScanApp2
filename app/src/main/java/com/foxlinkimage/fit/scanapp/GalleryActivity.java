package com.foxlinkimage.fit.scanapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.foxlinkimage.fit.ShareUtils.DropboxShare;
import com.foxlinkimage.fit.ShareUtils.FacebookShare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;


public class GalleryActivity extends ActionBarActivity {
    GridView gvGallery;
    GalleryAdapter mGalleryAdapter;
    static int screenWidth;
    String strProcessCropImgPath;
    DropboxShare dropboxShare;
    FacebookShare facebookShare;
    Boolean IsShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setupComponent();
        if (mGalleryAdapter != null) {
            dropboxShare = new DropboxShare(getApplicationContext(), mGalleryAdapter.getSelectedPics(), "FILE");
            facebookShare = new FacebookShare(GalleryActivity.this, mGalleryAdapter.getSelectedPics(), "FILE");
        }
    }


    void setupComponent() {
        gvGallery = (GridView) findViewById(R.id.gallery);

        Intent it = getIntent();
        String strFolderPath = it.getStringExtra("FolderPath");

        //取得手機螢幕寬度, 在adapter中計算縮圖比例時會使用到
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        final ArrayList<File> getSubFolderFile = FileUtils.getFiles(strFolderPath);
        if (getSubFolderFile == null) {
            gvGallery.setEmptyView(findViewById(R.id.empty2));
        } else {
            mGalleryAdapter = new GalleryAdapter(getApplicationContext(), getSubFolderFile);
            gvGallery.setNumColumns(3);
            gvGallery.setAdapter(mGalleryAdapter);
            gvGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    GalleryActivity.this.startSupportActionMode(new ActionBarCallBack());
                    return true;
                }
            });


            gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent it = new Intent(getApplicationContext(), CropImage.class);
                    it.putExtra(CropImage.IMAGE_PATH, getSubFolderFile.get(i).getAbsolutePath().replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath));
                    it.putExtra(CropImage.SCALE, true);
                    it.putExtra(CropImage.ASPECT_X, 3);
                    it.putExtra(CropImage.ASPECT_Y, 2);
                    startActivityForResult(it, 0x23);
                    strProcessCropImgPath = getSubFolderFile.get(i).getAbsolutePath();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult");
        if (resultCode == RESULT_OK && requestCode != FacebookShare.FB_REQ_CODE) {
            Bitmap bmp;
            bmp = BitmapFactory.decodeFile(strProcessCropImgPath.replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath));
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(strProcessCropImgPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, fos);  //壓縮90%
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //否則不動作
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mGalleryAdapter != null)
//            dropboxShare.Authenticate();
        Log.d("TAG", "onResume()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ActionBarCallBack implements android.support.v7.view.ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_folder, menu);
            mGalleryAdapter.bEnterActionMode = true;
            mGalleryAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            IsShare = false;
            Log.d("TAG", "onPrepareActionMode");
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_select_all:
                    Log.d("TAG", "全選按下");
                    if (mGalleryAdapter.bSelectAll) {
                        mGalleryAdapter.DeSelectAll();
                    } else {
                        mGalleryAdapter.SelectAll();
                    }
                    break;

                case R.id.action_facebook:
                    Log.d("TAG", "facebook分享按下");
                    facebookShare.Share();
                    IsShare = true;
                    actionMode.finish();
                    break;

                case R.id.action_dropbox:
                    Log.d("TAG", "Dropbox分享按下");
                    Toast.makeText(GalleryActivity.this, "正在分享至Dropbox, 進度請下拉狀態欄查看..", Toast.LENGTH_SHORT).show();
                    dropboxShare.Share();
                    IsShare = true;
                    actionMode.finish();
                    break;

                case R.id.action_delete:
                    Log.d("TAG", "刪除按下");
                    FileUtils.deleteFile(mGalleryAdapter);
                    actionMode.finish();
                    break;
            }
            mGalleryAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode actionMode) {
            mGalleryAdapter.bEnterActionMode = false;
            //2015-0616 原本因為離開actionmode會清空 mGalleryAdapter, 所以加了一個IsShare判斷是不是分享功能, 如果是的話就不要清空alSelectedPics
            if(!IsShare) {
                mGalleryAdapter.DeSelectAll();
            }
            mGalleryAdapter.notifyDataSetChanged();
        }
    }
}
