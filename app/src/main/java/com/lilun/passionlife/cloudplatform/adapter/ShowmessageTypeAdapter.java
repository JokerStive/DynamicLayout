package com.lilun.passionlife.cloudplatform.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationInformation;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import java.util.List;

/**
 * Created by youke on 2016/8/11.
 */
public class ShowmessageTypeAdapter extends RecyclerAdapter<OrganizationInformation> {


    private List<OrganizationInformation> data;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public ShowmessageTypeAdapter(@Nullable List<OrganizationInformation> data, @NonNull int... layoutResIds) {
        super(App.app, data, layoutResIds);
        this.data = data;
    }


    @Override
    protected void convert(RecyclerAdapterHelper helper, OrganizationInformation messageType) {
        helper.setText(R.id.item_name, messageType.getTitle())
                .getItemView().setOnLongClickListener(v -> {
                    if (longListener!=null){
                        longListener.onItemLongClick(helper.getAdapterPosition());
                    }
                    return true;
                });
        helper.getItemView().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(helper.getAdapterPosition());
            }
        });
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


}
