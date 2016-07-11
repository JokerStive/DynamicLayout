package com.lilun.passionlife.cloudplatform.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by youke on 2016/6/8.
 */
public class addStaff_deptListAdapter extends BaseAdapter {


    public   HashMap<Integer, Boolean> mCBFlag;
    private  List<Organization> data;
    private  OnItemClickListen listen;
    private final boolean isShowDelete;


    public addStaff_deptListAdapter(List<Organization> data, boolean isShowDelete, OnItemClickListen listen) {
        this.data=data;
        this.listen =listen;
        this.isShowDelete = isShowDelete;
        mCBFlag = new HashMap<Integer, Boolean>();
        Logger.d("data size = "+data.size());
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            int layout = R.layout.item_authority_list;
            LayoutInflater inflater = LayoutInflater.from(App.app);
            convertView = inflater.inflate(layout, parent, false);
            holder=new ViewHolder();

            //字体颜色
            TextView authro_title = (TextView) convertView.findViewById(R.id.authro_title);
            TextView authro_choise = (TextView) convertView.findViewById(R.id.authro_choise);
            TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            RelativeLayout authro_delete = (RelativeLayout) convertView.findViewById(R.id.authro_delete);


            tv_delete.setVisibility(isShowDelete ?View.VISIBLE:View.GONE);
            authro_choise.setVisibility(!isShowDelete ?View.VISIBLE:View.GONE);





            holder.item_authro_choise= authro_choise;
            holder.item_authro_delete= authro_delete;
            holder.item_authro_title= authro_title;
            holder.item_tv_delete= tv_delete;
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.item_authro_title.setText(data.get(position).getName());
        holder.item_authro_delete.setOnClickListener(v -> {
            if (holder.item_authro_choise.getVisibility()==View.VISIBLE){
                holder.item_authro_choise.setEnabled(!holder.item_authro_choise.isEnabled());
                if (holder.item_authro_choise.isEnabled()){
                    mCBFlag.put(position, true);
                }else{
                    mCBFlag.put(position, false);
                }
//                Logger.d("set enable = "+mCBFlag.get(position));





            }else{
                listen.onItemDelete(addStaff_deptListAdapter.this,position);
            }
        });

        holder.item_authro_choise.setEnabled(mCBFlag.get(position));
        return convertView;
    }


   public static class ViewHolder{
      public   TextView  item_authro_title;
      public   TextView  item_tv_delete;
       public RelativeLayout item_authro_delete;
       public TextView  item_authro_choise;
    }


    public interface  OnItemClickListen{
        void  onItemDelete(addStaff_deptListAdapter authrovityListAdapter, int position);
    }


    public Map<Integer, Boolean> getmCBFlag() {
        return mCBFlag;
    }

    public void setmCBFlag(HashMap<Integer, Boolean> mCBFlag) {
        this.mCBFlag = mCBFlag;
    }

}