package com.lilun.passionlife.cloudplatform.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Role;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.List;


/**
 * Created by youke on 2016/6/8.
 */
public class ExtRoleAdapter extends BaseAdapter {


    private  List<Role> data;
    private  OnItemDeleteListen listen;
    private final boolean isEnable;


    public ExtRoleAdapter(List<Role> data, boolean isEnable, OnItemDeleteListen listen) {
        this.data=data;
        this.listen =listen;
        this.isEnable = isEnable;
        initData();
    }

    private void initData() {

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
            int layout = R.layout.item_checkbox;
            LayoutInflater inflater = LayoutInflater.from(App.app);
            convertView = inflater.inflate(layout, parent, false);
            holder=new ViewHolder();

            //字体颜色
            TextView tv = (TextView) convertView.findViewById(R.id.tv_title);
            TextView delete = (TextView) convertView.findViewById(R.id.item_check);
            FrameLayout fr_check = (FrameLayout) convertView.findViewById(R.id.fr_check);
            if (!isEnable){
                tv.setTextColor(Color.GRAY);
            }
            delete.setVisibility(isEnable ? View.VISIBLE:View.GONE);

            fr_check.setOnClickListener(v -> {
                listen.onItemDelete(ExtRoleAdapter.this,position);
//                data.remove(position);
//                notifyDataSetChanged();
            });


            holder.item_title= tv;
            holder.item_choise= delete;
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(data.get(position).getTitle());
        return convertView;
    }


   public static class ViewHolder{
      public   TextView  item_title;
       public TextView  item_choise;
    }


    public interface  OnItemDeleteListen{
        void  onItemDelete(ExtRoleAdapter parentOrgisAdapter, int position);
    }

}
