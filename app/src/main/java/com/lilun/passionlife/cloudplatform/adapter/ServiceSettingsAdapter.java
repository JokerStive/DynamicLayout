package com.lilun.passionlife.cloudplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ServiceSettingsAdapter  extends BaseAdapter{

    private final Map<String,Map<String,Object>> data;

    public ServiceSettingsAdapter(Map<String,Map<String,Object>> data) {
        this.data = data;
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }
}
