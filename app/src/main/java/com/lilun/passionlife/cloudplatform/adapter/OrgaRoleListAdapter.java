package com.lilun.passionlife.cloudplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseModuleListAdapter;
import com.lilun.passionlife.cloudplatform.bean.OrganizationRole;
import com.lilun.passionlife.cloudplatform.ui.App;

import java.util.List;


/**
 * Created by Administrator on 2016/6/8.
 */
public class OrgaRoleListAdapter extends BaseModuleListAdapter {
    private List<OrganizationRole> data;
    private String add;

    public OrgaRoleListAdapter(List<OrganizationRole> data, onDeleteClickListerer listerer) {
        super(listerer);
        this.data = data;
        add = App.app.getString(R.string.add);

    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public OrganizationRole getItem(int position) {
        return data.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position - 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(App.app, R.layout.item_module_list, null);
            viewHolder = new ViewHolder();
            viewHolder.module_icon = (ImageView) convertView.findViewById(R.id.module_list_icon);
            viewHolder.module_title = (TextView) convertView.findViewById(R.id.module_list_title);
            viewHolder.module_delete = (ImageView) convertView.findViewById(R.id.module_list_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setData(position, viewHolder, convertView);


        return convertView;
    }

    private void setData(int position, ViewHolder viewHolder, View convertView) {
        if (position == 0) {
            viewHolder.module_delete.setVisibility(View.GONE);
            viewHolder.module_icon.setBackgroundResource(R.drawable.add);
            viewHolder.module_title.setText(add);
        } else {
            //TODO 从网络url加载图片
            String RoleName = data.get(position - 1).getRole();
            viewHolder.module_icon.setBackgroundResource(R.drawable.add);
            viewHolder.module_title.setText(RoleName);

            viewHolder.module_delete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
            viewHolder.module_delete.setOnClickListener(v -> {
                listener.onDeleteClick(position - 1);
            });
        }
    }

    class ViewHolder {
        ImageView module_icon;
        TextView module_title;
        ImageView module_delete;
    }

}
