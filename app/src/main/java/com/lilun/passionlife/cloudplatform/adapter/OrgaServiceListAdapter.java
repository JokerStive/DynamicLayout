package com.lilun.passionlife.cloudplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.List;


/**
 * Created by Administrator on 2016/6/8.
 */
public class OrgaServiceListAdapter extends BaseModuleListAdapter {
    private final List<OrganizationService> services;
    private String add;

    public OrgaServiceListAdapter(List<OrganizationService> serviceList, onDeleteClickListerer listerer) {
        super(listerer);
        services =serviceList;

//        for (int i=0;i<serviceList.size();i++){
//            if (serviceList.get(i).getServiceId()!=null && serviceList.get(i).getServiceId().equals("SysConfig")){
//                serviceList.remove(i);
//            }
//
//        }
        add= App.app.getString(R.string.add);

    }

    @Override
    public int getCount() {
        return services.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(App.app, R.layout.item_module_list,null);
            viewHolder= new ViewHolder();
            viewHolder.module_icon = (ImageView) convertView.findViewById(R.id.module_list_icon);
            viewHolder.module_title = (TextView) convertView.findViewById(R.id.module_list_title);
            viewHolder.module_delete = (ImageView) convertView.findViewById(R.id.module_list_delete);
//            viewHolder.module_desc = (TextView) convertView.findViewById(R.id.module_desc);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        setData(position,viewHolder,convertView);

        return convertView;
    }

    private void setData(int position, ViewHolder viewHolder, View convertView) {
        if(position==0){
            viewHolder.module_delete.setVisibility(View.GONE);
            viewHolder.module_icon.setBackgroundResource(R.drawable.add);
            viewHolder.module_title.setText(add);
        }else{
            //TODO 从网络url加载图片
            String title = services.get(position-1).getTitle();
            viewHolder.module_icon.setBackgroundResource(R.drawable.add);
            viewHolder.module_title.setText(title==null?"标题":title);

            viewHolder.module_delete.setVisibility(isShowDelete ?View.VISIBLE:View.GONE);
            viewHolder.module_delete.setOnClickListener(v -> listener.onDeleteClick(position-1));
        }
    }

    class ViewHolder{
        ImageView module_icon;
        TextView  module_title;
         ImageView module_delete;
    }

}
