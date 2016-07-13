package com.lilun.passionlife.cloudplatform.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by youke on 2016/6/8.
 */
public class AuthrovityListAdapter extends BaseAdapter {


    public   HashMap<Integer, Boolean> mCBFlag;
    private  List<Role> data;
    private  OnItemClickListen listen;
    private final boolean isShowDelete;


    public AuthrovityListAdapter(List<Role> data, boolean isShowDelete, OnItemClickListen listen) {
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
    public Role getItem(int position) {
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
        holder.item_authro_title.setText(data.get(position).getTitle());


        //初始化选中的勾选
        if(data.get(position).isHave()){
            mCBFlag.put(position, true);
        }
        holder.item_authro_delete.setOnClickListener(v -> {
            if (holder.item_authro_choise.getVisibility()==View.VISIBLE){


                    mCBFlag.put(position, !holder.item_authro_choise.isEnabled());


                //取消选中的监听
//                if(data.get(position).isHave()){
//                    Logger.d("删除权限");
//                    listen.onChoiseItemCancle(AuthrovityListAdapter.this,position);
//                }
//
//                if (holder.item_authro_choise.isEnabled()){
//                    mCBFlag.put(position, false);
//                }else{
//                    mCBFlag.put(position, true);
//                }
                holder.item_authro_choise.setEnabled(mCBFlag.get(position));

            }
//            else{
//                listen.onChoiseItemCancle(AuthrovityListAdapter.this,position);
//            }
        });

        holder.item_authro_choise.setEnabled(mCBFlag.get(position));
        return convertView;
    }

public void setEnable(int position,boolean enable){

}

   public static class ViewHolder{
      public   TextView  item_authro_title;
      public   TextView  item_tv_delete;
       public RelativeLayout item_authro_delete;
       public TextView  item_authro_choise;
    }



    public interface  OnItemClickListen{
        void onChoiseItemCancle(AuthrovityListAdapter authrovityListAdapter, int position);


    }


    public Map<Integer, Boolean> getmCBFlag() {
        return mCBFlag;
    }

    public void setmCBFlag(HashMap<Integer, Boolean> mCBFlag) {
        this.mCBFlag = mCBFlag;
    }

}
