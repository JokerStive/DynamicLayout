package com.lilun.passionlife.cloudplatform.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by youke on 2016/6/8.
 */
public class addStaff_roleListAdapter extends BaseAdapter {


    ViewHolder holder;
    public HashMap<Integer, Boolean> mCBFlag;
    private List<Role> data;
    private onItemCancleLister listen;


    public addStaff_roleListAdapter(List<Role> data) {
        this.data = data;
        mCBFlag = new HashMap<Integer, Boolean>();
//        Logger.d("data size = " + data.size());
        initData();
    }

    private void initData() {
        for (int i = 0; i < data.size(); i++) {
            mCBFlag.put(i, data.get(i).isBelong());
        }
    }

    public void setOnItemCancleListener(onItemCancleLister listener) {
        this.listen = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Role getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
        holder.item_name.setText(data.get(position).getTitle());
        holder.item_ll.setOnClickListener(v -> {

            if (data.get(position).isBelong() && listen != null) {
                listen.onItemCancle(position);
            }else{
                mCBFlag.put(position, !holder.item_name.isEnabled());
                holder.item_name.setEnabled(mCBFlag.get(position));
            }


        });

        holder.item_name.setEnabled(mCBFlag.get(position));
//        holder.item_name.setTextColor(App.app.getColor(R.color.default_tv_gray));
        return convertView;
    }


    public static class ViewHolder {
        public TextView item_name;
        public LinearLayout item_ll;

    }

    public List<Role> getChoiseRoles() {
        List<Role> choiss = new ArrayList<>();
        for (int i = 0; i < mCBFlag.size(); i++) {
            if (mCBFlag.get(i)) {
                choiss.add(data.get(i));
            }
        }

        return choiss;
    }


    public interface onItemCancleLister {
        void onItemCancle(int position);
    }


    public void setEnable(int position,boolean enable){
        mCBFlag.put(position, !holder.item_name.isEnabled());
        holder.item_name.setEnabled(mCBFlag.get(position));
    }


}
