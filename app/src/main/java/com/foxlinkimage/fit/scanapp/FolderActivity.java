package com.foxlinkimage.fit.scanapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class FolderActivity extends AppCompatActivity {
    ListView lvFolders;
    Boolean IsShare;

    TextView tvLocation;
    ArrayList<File> alFolders;
    FolderAdapter mFolderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        setupComponent();
    }

    void setupComponent() {
        alFolders = FileUtils.getFiles(PreferenceHelper.strDefaultSaveFolderThumbnailsPath);  //20150601 改使用thumbnail的位置
        tvLocation = (TextView) findViewById(R.id.location);
        tvLocation.setText(PreferenceHelper.strDefaultSaveFolderThumbnailsPath);
        lvFolders = (ListView) findViewById(R.id.folders);
        IsShare = false;

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
                    IsShare = false;
                    Log.d("TAG", "全選按下");
                    if (mFolderAdapter.bSelectAll) {
                        mFolderAdapter.DeSelectAll();
                    } else {
                        mFolderAdapter.SelectAll();
                    }
                    mFolderAdapter.notifyDataSetChanged();
                    break;


                case R.id.action_share:
                    IsShare = true;
                    ArrayList<Uri> filestoshare = new ArrayList<>();
                    ArrayList<String> folders;
                    folders = mFolderAdapter.getSelectedFolder();

                    for (int item = 0; item < folders.size(); item++) {
                        String a = folders.get(item).replace(PreferenceHelper.strDefaultSaveFolderThumbnailsPath, PreferenceHelper.strDefaultSaveFolderPath);
                        folders.set(item,a);
                    }

                    File[] filesinfolder;

                    for (int i = 0; i < folders.size(); i++) {
                        File tmp = new File(folders.get(i));
                        filesinfolder = tmp.listFiles();

                        for (File aFilesinfolder : filesinfolder) {
                            Uri file = Uri.fromFile(aFilesinfolder);
                            filestoshare.add(file);
                        }
                    }
                    Intent it = new Intent();
                    it.setAction(Intent.ACTION_SEND_MULTIPLE);
                    it.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filestoshare);
                    it.setType("image/jpeg");
                    startActivity(Intent.createChooser(it, "Choose the place you want to upload"));
                    actionMode.finish();
                    break;

                case R.id.action_delete:
                    IsShare = false;
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
