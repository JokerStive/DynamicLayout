package com.lilun.passionlife.cloudplatform.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.KnownServices;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;

import java.util.List;


/**
 * Created by youke on 2016/6/8.
 */
public class SystemConfigGvAdapter extends BaseAdapter {
    private Context context;
    private List<OrganizationService> servicess;

    public SystemConfigGvAdapter(Context context, List<OrganizationService> servicess) {
        this.context = context;

        this.servicess = servicess;

        for (int i=0;i<servicess.size();i++){
            if (servicess.get(i).getServiceId()!=null && servicess.get(i).getServiceId().equals(KnownServices.SysConfig_Service)){
                servicess.remove(i);
            }

        }
    }

    @Override
    public int getCount() {
        return servicess.size();
    }

    @Override
    public OrganizationService getItem(int position) {
        return servicess.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.item_gv_home,null);
            viewHolder= new ViewHolder();
            viewHolder.module_icon = (ImageView) convertView.findViewById(R.id.module_icon);
            viewHolder.module_title = (TextView) convertView.findViewById(R.id.module_title);
            viewHolder.module_desc = (TextView) convertView.findViewById(R.id.module_desc);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //TODO :数据
        String serviceId = servicess.get(position).getServiceId();
        if (PicloadManager.getServiceIcon(serviceId)!=0){
            viewHolder.module_icon.setBackgroundResource(PicloadManager.getServiceIcon(serviceId));
        }
        viewHolder.module_title.setText(servicess.get(position).getTitle());
        viewHolder.module_desc.setText(servicess.get(position).getDescription());


        return convertView;
    }

    class ViewHolder{
        ImageView module_icon;
        TextView  module_title;
        TextView  module_desc;
    }

}
