package com.foxlinkimage.fit.scanapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
 * Created by Alex on 2015/5/11.
 */
public class GalleryAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<File> alPictures;
    ArrayList<Boolean> b_arrChecked;//用來儲存check的狀態
    ArrayList<String> alSelectedPics;  //用來儲存被選中的圖片
    Boolean bEnterActionMode;
    Boolean bSelectAll;
    Bitmap bmp;
    final int iSmallImgWidth = GalleryActivity.screenWidth / 6;
    ViewHolder viewHolder;

    GalleryAdapter(Context context, ArrayList<File> arrayList) {
        mContext = context;
        alPictures = arrayList;
        alSelectedPics = new ArrayList<>();
        b_arrChecked = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++)
            b_arrChecked.add(false);
        mInflater = LayoutInflater.from(mContext);
        bEnterActionMode = false;
        bSelectAll = false;
    }

    @Override
    public int getCount() {
        Log.d("GETT", "getCount " + String.valueOf(alPictures.size()));
        return alPictures.size();
    }

    @Override
    public Object getItem(int i) {
        Log.d("GETT", "getItem: " + String.valueOf(i));
        return alPictures.get(i);
    }

    @Override
    public long getItemId(int i) {
        Log.d("GETT", "getItemId: " + String.valueOf(i));
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        viewHolder = new ViewHolder();
        if (convertView == null) {
            Log.d("GETT", "convertview null: " + String.valueOf(position));
            convertView = mInflater.inflate(R.layout.photo_item_layout, viewGroup, false);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.multi = (CheckBox) convertView.findViewById(R.id.multi);
            viewHolder.filename = (TextView)convertView.findViewById(R.id.filename);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            Log.d("GETT", "convertview: " + String.valueOf(position));
        }

        //設定多選模式，checkbox是否顯示
        if (bEnterActionMode) {
            viewHolder.multi.setVisibility(View.VISIBLE);
        } else {
            viewHolder.multi.setVisibility(View.INVISIBLE);
        }

        // 載入圖片前先進行縮圖, 以避免Load進的圖檔太大
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;   //將inJustDecodeBounds為true時, 並未將圖load進記憶體, 僅取得圖片相關資訊
        option.inPreferredConfig = Bitmap.Config.RGB_565;   //ARGB_8888較為耗空間
        BitmapFactory.decodeFile(alPictures.get(position).getPath(), option);
        int iSampleSize;
        //求得縮圖比例

        iSampleSize = calculateInSampleSize(option,iSmallImgWidth);
        option.inSampleSize =iSampleSize;
        option.inJustDecodeBounds = false;  //將inJustDecodeBounds為false時, 之後decodeFile動作就會真正將圖片Load進記憶體
        bmp = BitmapFactory.decodeFile(alPictures.get(position).getPath(), option);
        viewHolder.photo.setImageBitmap(bmp);

        File f = new File(alPictures.get(position).getPath());
        viewHolder.filename.setText(f.getName());

        viewHolder.multi.setChecked(b_arrChecked.get(position));
        viewHolder.multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    b_arrChecked.set(position, true);
                    alSelectedPics.add(getItem(position).toString());
                } else {
                    b_arrChecked.set(position, false);
                    int unselect_index = alSelectedPics.indexOf(getItem(position).toString());
                    alSelectedPics.remove(unselect_index);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView photo;
        CheckBox multi;
        TextView filename;
    }

    //!! 此function為移除存放在arraylist的項目, 並非移除實際的檔案
    public void Delete(String deletefolder) {
        int delete_index = alPictures.indexOf(new File(deletefolder));
        alPictures.remove(delete_index);
        b_arrChecked.remove(delete_index);
    }


    public void SelectAll() {
        for (int i = 0; i < b_arrChecked.size(); i++) {
            b_arrChecked.set(i, true);
            alSelectedPics.add(i, alPictures.get(i).getPath());
        }
        bSelectAll = true;
    }

    public void DeSelectAll() {
        for (int i = 0; i < b_arrChecked.size(); i++) {
            b_arrChecked.set(i, false);
            alSelectedPics.clear();
        }
        bSelectAll = false;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth) {
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // width larger than the requested width.
            while ((halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public ArrayList<String> getSelectedPics()
    {
        return alSelectedPics;
    }

}
