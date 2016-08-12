package com.lilun.passionlife.cloudplatform.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.List;


/**
 * Created by youke on 2016/6/8.
 */
public class ExtDeptAdapter extends BaseAdapter {


    private  List<Organization> data;
    private  OnItemDeleteListen listen;


    public ExtDeptAdapter(List<Organization> data,OnItemDeleteListen listen) {
        this.data=data;
        this.listen =listen;
        initData();
    }

    public ExtDeptAdapter(List<Organization> data) {
        this.data=data;
        initData();
    }

    private void initData() {

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
            int layout = R.layout.item_orga_dept;
            LayoutInflater inflater = LayoutInflater.from(App.app);
            convertView = inflater.inflate(layout, parent, false);
            holder=new ViewHolder();

            //字体颜色
            TextView tv = (TextView) convertView.findViewById(R.id.tv_title);
            ImageView delete = (ImageView) convertView.findViewById(R.id.item_check);
            if (listen==null){
                tv.setTextColor(Color.GRAY);
            }
            delete.setVisibility(listen!=null ? View.VISIBLE:View.INVISIBLE);

            delete.setOnClickListener(v -> {
                if (listen!=null){
                    listen.onItemDelete(ExtDeptAdapter.this,position);
                }
            });


            holder.item_title= tv;
            holder.item_choise= delete;
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(data.get(position).getName());
        return convertView;
    }


   public static class ViewHolder{
      public   TextView  item_title;
       public ImageView  item_choise;
    }


    public interface  OnItemDeleteListen{
        void  onItemDelete(ExtDeptAdapter parentOrgisAdapter, int position);
    }

}
