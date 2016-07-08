package com.lilun.passionlife.cloudplatform.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by youke on 2016/7/6.
 */
public class BaseModuleListAdapter  extends BaseAdapter{
    protected  onDeleteClickListerer listener;
    protected boolean isShowDelete;

    public BaseModuleListAdapter(onDeleteClickListerer listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public  void setDeleteShow(boolean isShowDelete ){
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    public interface onDeleteClickListerer{
        void onDeleteClick(int position);
    }
}
