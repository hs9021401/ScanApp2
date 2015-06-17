package com.foxlinkimage.fit.scanapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DevicesAdapter extends BaseAdapter
{
    LayoutInflater mInflater;
    Context mContext;
    ArrayList<String[]> mCandidateDeviceList;

    DevicesAdapter(Context context, ArrayList<String[]> lis)
    {
        mContext = context;
        mCandidateDeviceList = lis;
    }

    @Override
    public int getCount() {
        return mCandidateDeviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCandidateDeviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final DeviceViewHolder viewHolder;
        if(convertView == null)
        {
            mInflater = LayoutInflater.from(mContext);
            viewHolder = new DeviceViewHolder();
            convertView = mInflater.inflate(R.layout.device_item_layout, viewGroup, false);
            viewHolder.device_name = (TextView)convertView.findViewById(R.id.device_name);
            viewHolder.device_ip = (TextView)convertView.findViewById(R.id.device_ip);
            viewHolder.device_port = (TextView)convertView.findViewById(R.id.device_port);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (DeviceViewHolder)convertView.getTag();
        }

        viewHolder.device_name.setText(mCandidateDeviceList.get(position)[0]);
        viewHolder.device_ip.setText(mCandidateDeviceList.get(position)[1]);
        viewHolder.device_port.setText(mCandidateDeviceList.get(position)[2]);

        return convertView;
    }

    class DeviceViewHolder {
        TextView device_name;
        TextView device_ip;
        TextView device_port;
    }
}

