package com.lilun.passionlife.cloudplatform.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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


    public ExtRoleAdapter(List<Role> data) {
        this.data=data;
        initData();
    }

    public ExtRoleAdapter(List<Role> data,  OnItemDeleteListen listen) {
        this.data=data;
        this.listen =listen;
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
            ImageView delete = (ImageView) convertView.findViewById(R.id.item_check);
            ImageView edit = (ImageView) convertView.findViewById(R.id.item_edit);
            if (listen==null){
                tv.setTextColor(Color.GRAY);
            }
            delete.setVisibility(listen!=null ? View.VISIBLE:View.INVISIBLE);
            edit.setVisibility(listen!=null ? View.VISIBLE:View.INVISIBLE);

            delete.setOnClickListener(v -> {
                if (listen!=null){
                    listen.onItemDelete(ExtRoleAdapter.this,position);
                }
//                data.remove(position);
//                notifyDataSetChanged();
            });


            edit.setEnabled(!data.get(position).isNew());
            edit.setOnClickListener(v -> {
                if (v.isEnabled()){

                        listen.onItemEdit(ExtRoleAdapter.this,position);

                }
            });

            holder.item_title= tv;
            holder.item_choise= delete;
            holder.item_edit= edit;
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(data.get(position).getTitle());
        return convertView;
    }


   public static class ViewHolder{
      public   TextView  item_title;
       public ImageView  item_choise;
       public ImageView  item_edit;
    }


    public interface  OnItemDeleteListen{
        void  onItemDelete(ExtRoleAdapter parentOrgisAdapter, int position);
        void  onItemEdit(ExtRoleAdapter parentOrgisAdapter, int position);
    }

}
