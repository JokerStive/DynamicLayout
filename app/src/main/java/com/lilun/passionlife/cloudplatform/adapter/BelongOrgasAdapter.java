package com.lilun.passionlife.cloudplatform.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/6/8.
 */
public class BelongOrgasAdapter extends BaseAdapter {
    private List<OrganizationAccount> data;

    public BelongOrgasAdapter(List<OrganizationAccount> data) {
        super();
        this.data = data;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public OrganizationAccount getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(App.app, R.layout.item_change_belong_orga, null);
            viewHolder = new ViewHolder();
            viewHolder.module_title = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String name = StringUtils.belongOgaName(data.get(position).getOrganizationId());
        if (TextUtils.isEmpty(name)){
            name="/";
        }

        viewHolder.module_title.setText(name);


        return convertView;
    }



    class ViewHolder {
        TextView module_title;
    }

}
