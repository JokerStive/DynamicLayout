package com.lilun.passionlife.cloudplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.Organization;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Administrator on 2016/6/8.
 */
public class OrgaListAdapter extends BaseModuleListAdapter {
    private  List<Organization> orginals;
    private String add;

    public OrgaListAdapter(List<Organization> serviceList, onDeleteClickListerer listerer) {
        super(listerer);
        orginals =serviceList;
        add= App.app.getString(R.string.add);

    }



    @Override
    public int getCount() {
        return orginals.size()+1;
    }

    @Override
    public Organization getItem(int position) {
        return orginals.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position-1;
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
            String orgaName = orginals.get(position-1).getName();
            String organid= orginals.get(position-1).getId();
//            viewHolder.module_icon.setBackgroundResource(R.drawable.add);
            viewHolder.module_title.setText(orgaName);

            Picasso.with(App.app).load(PicloadManager.orgaIconUrl(organid))
                    .placeholder(R.drawable.default_pic)
                    .error(R.drawable.default_pic)
                    .into(viewHolder.module_icon);
//          Logger.d(PicloadManager.orgaIconUrl(organid));
            viewHolder.module_delete.setVisibility(isShowDelete ?View.VISIBLE:View.GONE);
            viewHolder.module_delete.setOnClickListener(v -> {
                listener.onDeleteClick(position-1);
            });
        }
    }

    class ViewHolder{
        ImageView module_icon;
        TextView  module_title;
        ImageView  module_delete;
    }

}
