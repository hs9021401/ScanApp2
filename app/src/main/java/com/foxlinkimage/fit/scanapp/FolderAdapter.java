package com.foxlinkimage.fit.scanapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 2015/4/30.
 */
public class FolderAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<String> alSelectedFolder;  //用來儲存被選中的資料夾
    ArrayList<Boolean> b_arrChecked;//用來儲存check的狀態
    ArrayList<File> alFiles;
    Boolean bEnterActionMode;
    Boolean bSelectAll;


    FolderAdapter(Context context, ArrayList<File> files) {
        alSelectedFolder = new ArrayList<>();
        alFiles = files;
        mContext = context;
        b_arrChecked = new ArrayList<>();
        for (int i = 0; i < files.size(); i++)
            b_arrChecked.add(false);
        mInflater = LayoutInflater.from(mContext);
        bEnterActionMode = false;
        bSelectAll = false;
    }


    @Override
    public int getCount() {
        return alFiles.size();
    }

    @Override
    public Object getItem(int i) {
        return alFiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final File fileJudgement = alFiles.get(position); //用以判斷是資料夾icon

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.folder_item_layout, viewGroup, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.foldername = (TextView) convertView.findViewById(R.id.foldername);
            viewHolder.foldername.setTextColor(Color.parseColor("#acaae4"));
            viewHolder.multiselect = (CheckBox) convertView.findViewById(R.id.multiselect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //設定多選模式，checkbox是否顯示
        if (bEnterActionMode) {
            viewHolder.multiselect.setVisibility(View.VISIBLE);
        } else {
            viewHolder.multiselect.setVisibility(View.INVISIBLE);
        }


        viewHolder.multiselect.setChecked(b_arrChecked.get(position));
        viewHolder.multiselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    b_arrChecked.set(position, true);
                    alSelectedFolder.add(getItem(position).toString());
                } else {
                    b_arrChecked.set(position, false);
                    int unselect_index = alSelectedFolder.indexOf(getItem(position).toString());
                    alSelectedFolder.remove(unselect_index);
                }
            }
        });

        //Folder
        if (fileJudgement.isDirectory()) {
            if (fileJudgement.getPath().equals(PreferenceHelper.strDefaultSaveFolderPath)) {   //如果資料夾是Root資料夾, 就顯示... , 否則顯示該資料夾名稱
                viewHolder.foldername.setText("...");
            } else {
                viewHolder.foldername.setText(fileJudgement.getName());
            }
            viewHolder.icon.setImageResource(R.mipmap.icon_folder);
        } else {
            viewHolder.icon.setImageResource(R.mipmap.icon_image);
        }

        return convertView;
    }

    // 提供外部程式呼叫. 從alFiles刪除要被刪除的檔案名稱
    public void Delete(String deletefolder) {
        int delete_index = alFiles.indexOf(new File(deletefolder));
        alFiles.remove(delete_index);
        b_arrChecked.remove(delete_index);
    }


    public void SelectAll() {
        for (int i = 0; i < b_arrChecked.size(); i++) {
            b_arrChecked.set(i, true);
            alSelectedFolder.add(i, alFiles.get(i).getAbsolutePath());
        }
        bSelectAll = true;
    }

    public void DeSelectAll() {
        for (int i = 0; i < b_arrChecked.size(); i++) {
            b_arrChecked.set(i, false);
            alSelectedFolder.clear();
        }
        bSelectAll = false;
    }

    public ArrayList<String> getSelectedFolder()
    {
        return alSelectedFolder;
    }

    class ViewHolder {
        ImageView icon;
        TextView foldername;
        CheckBox multiselect;
    }

}



