package com.lilun.passionlife.cloudplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.Organization;

import java.util.List;


/**
 * Created by youke on 2016/6/8.
 */
public class ShowOrgiChildrenAdapter extends BaseAdapter {
    private Context context;
    private List<Organization> orgis;

    public ShowOrgiChildrenAdapter(Context context, List<Organization> orgis) {
        this.context = context;

        this.orgis = orgis;
    }

    @Override
    public int getCount() {
        return orgis.size();
    }

    @Override
    public Organization getItem(int position) {
        return orgis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_show_orgi,null);
            viewHolder= new ViewHolder();
            viewHolder.module_title = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //TODO :数据
        viewHolder.module_title.setText(orgis.get(position).getName());


        return convertView;
    }

    class ViewHolder{
        TextView  module_title;

    }

}
