package com.foxlinkimage.fit.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.foxlinkimage.fit.ShareUtils.DropboxShare;
import com.foxlinkimage.fit.ShareUtils.FacebookShare;

import java.io.File;
import java.util.ArrayList;


public class FolderActivity extends ActionBarActivity {
    ListView lvFolders;
    Boolean IsShare;

    TextView tvLocation;
    ArrayList<File> alFolders;
    FolderAdapter mFolderAdapter;
    DropboxShare dropboxShare;
    FacebookShare facebookShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        setupComponent();

        if (mFolderAdapter == null) {
            Log.d("TAG", "mFolderAdapter is null");
        } else {
            // 初始化分享功能
            dropboxShare = new DropboxShare(getApplicationContext(), mFolderAdapter.getSelectedFolder(), "FOLDER");
            facebookShare = new FacebookShare(FolderActivity.this, mFolderAdapter.getSelectedFolder(), "FOLDER");
        }
    }

    void setupComponent() {
        alFolders = FileUtils.getFiles(PreferenceHelper.strDefaultSaveFolderThumbnailsPath);  //20150601 改使用thumbnail的位置
        tvLocation = (TextView) findViewById(R.id.location);
        tvLocation.setText(PreferenceHelper.strDefaultSaveFolderThumbnailsPath);
        lvFolders = (ListView) findViewById(R.id.folders);

        //如果資料夾內是空的話, 顯示no file訊息
        if (alFolders == null) {
            lvFolders.setEmptyView(findViewById(R.id.empty));
        } else {
            mFolderAdapter = new FolderAdapter(FolderActivity.this, alFolders);
            lvFolders.setAdapter(mFolderAdapter);
        }

        lvFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File tmp = alFolders.get(position);
                String tmp_path = tmp.getPath();
                if (tmp.isDirectory()) {
                    Intent it = new Intent(FolderActivity.this, GalleryActivity.class);
                    it.putExtra("FolderPath", tmp_path);
                    startActivity(it);
                }
            }
        });

        lvFolders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FolderActivity.this.startSupportActionMode(new ActionBarCallBack());
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFolderAdapter != null)
            dropboxShare.Resume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ActionBarCallBack implements android.support.v7.view.ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_folder, menu);
            mFolderAdapter.bEnterActionMode = true;
            mFolderAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            Log.d("TAG", "onPrepareActionMode");
            IsShare = false;
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_select_all:
                    Log.d("TAG", "全選按下");
                    if (mFolderAdapter.bSelectAll) {
                        mFolderAdapter.DeSelectAll();
                    } else {
                        mFolderAdapter.SelectAll();
                    }
                    mFolderAdapter.notifyDataSetChanged();
                    break;

                case R.id.action_facebook:
                    Log.d("TAG", "facebook分享按下");
                    facebookShare.Share();
                    break;

                case R.id.action_dropbox:
                    Log.d("TAG", "Dropbox分享按下");
                    Toast.makeText(FolderActivity.this, "正在分享至Dropbox, 進度請下拉狀態欄查看..", Toast.LENGTH_SHORT).show();
                    dropboxShare.Share();
                    IsShare = true;
                    actionMode.finish();
                    break;

                case R.id.action_googleplus:

                    break;

                case R.id.action_delete:
                    Log.d("TAG", "刪除按下");
                    FileUtils.deleteFolder(mFolderAdapter);
                    actionMode.finish();
                    break;
            }
            mFolderAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode actionMode) {
            mFolderAdapter.bEnterActionMode = false;
            if (!IsShare) {
                mFolderAdapter.DeSelectAll();
            }
            mFolderAdapter.notifyDataSetChanged();
        }
    }
}
