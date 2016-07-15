package com.lilun.passionlife.cloudplatform.adapter;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by youke on 2016/6/8.
 */
public class addStaff_deptListAdapter extends BaseAdapter {


    public HashMap<Integer, Boolean> mCBFlag;
    private List<Organization> data;


    public addStaff_deptListAdapter(List<Organization> data) {
        this.data = data;
        mCBFlag = new HashMap<Integer, Boolean>();
//        Logger.d("data size = " + data.size());
        initData();
    }

    private void initData() {
        for (int i = 0; i < data.size(); i++) {
            mCBFlag.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Organization getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.app).inflate(R.layout.item_container, null);
            holder = new ViewHolder();

            //名称
            holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.item_ll = (LinearLayout) convertView.findViewById(R.id.item_ll);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_name.setText(data.get(position).getName());
        holder.item_ll.setOnClickListener(v -> {

           mCBFlag.put(position,!holder.item_name.isEnabled());

            holder.item_name.setEnabled(mCBFlag.get(position));
            holder.item_name.setTextColor(mCBFlag.get(position) ? Color.BLACK:Color.GRAY);

        });

        holder.item_name.setEnabled(mCBFlag.get(position));
//        holder.item_name.setTextColor(App.app.getColor(R.color.default_tv_gray));
        return convertView;
    }


    public static class ViewHolder {
        public TextView item_name;
        public LinearLayout item_ll;

    }


    public List<Organization>  getChoiseDepts(){
        List<Organization> choiss = new ArrayList<>();
        for (int i=0;i<mCBFlag.size();i++){
            if (mCBFlag.get(i)){
                choiss.add(data.get(i));
            }
        }

        return choiss;
    }


    public Map<Integer, Boolean> getmCBFlag() {
        return mCBFlag;
    }

    public void setmCBFlag(HashMap<Integer, Boolean> mCBFlag) {
        this.mCBFlag = mCBFlag;
    }

}
