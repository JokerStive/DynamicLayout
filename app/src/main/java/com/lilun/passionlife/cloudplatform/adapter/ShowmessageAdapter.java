package com.lilun.passionlife.cloudplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationInformation;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import java.util.List;

/**
 * Created by youke on 2016/8/11.
 */
public class ShowmessageAdapter extends RecyclerAdapter<OrganizationInformation> {


    private List<OrganizationInformation> data;
    private OnItemClickListener listener;
    private LinearLayout.LayoutParams params;
    private int[] layoutResIds;
    private boolean isDeleteShow;


    public ShowmessageAdapter(List<OrganizationInformation> data, int... layoutResIds) {
        super(App.app, data, layoutResIds);
        this.layoutResIds = layoutResIds;
        this.data = data;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = UIUtils.dip2px(App.app, 10);
    }

    /**
     * 设置删除图标显示
     */
    public void setIsDeleteShow() {
        if ( data.size()!=0 ) {
            this.isDeleteShow = !isDeleteShow;
            notifyDataSetChanged();
        }
    }


    @Override
    public void onEmptyData() {
        super.onEmptyData();
        if (listener!=null){
            listener.onEmptyData();
        }
    }

    public List<OrganizationInformation> getData() {
        return data;
    }



    @Override
    protected void convert(RecyclerAdapterHelper helper, OrganizationInformation message) {

        if (helper.getAdapterPosition() != 0) {
            helper.getItemView().setLayoutParams(params);
        }
        helper.setVisible(R.id.message_delete, isDeleteShow ? View.VISIBLE : View.INVISIBLE);
        helper.setOnClickListener(R.id.message_delete, v -> {
            if (listener != null) {
                listener.onItemDelete(helper.getAdapterPosition());
            }
        });
        helper.setText(R.id.message_title, message.getTitle())
                .setText(R.id.message_content, message.getContext())
                .setText(R.id.message_time, message.getCreatedAt())
                .setText(R.id.message_writer, message.getUpdatorId())
                .getItemView().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(helper.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @Override
    public int getLayoutResId(int viewType) {
        return layoutResIds[0];
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemDelete(int position);
        void onEmptyData();
    }
}
