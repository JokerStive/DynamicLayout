package com.lilun.passionlife.cloudplatform.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import java.util.List;

/**
 * Created by youke on 2016/8/11.
 */
public class ShowmessageAdapter extends RecyclerAdapter<String> {


    private  List<String> data;
    private OnItemClickListener listener;
    private final LinearLayout.LayoutParams params;

    public ShowmessageAdapter(@Nullable List<String> data, @NonNull int... layoutResIds) {
        super(App.app, data, layoutResIds);
        this.data = data;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin= UIUtils.dip2px(App.app,10);
    }


    @Override
    protected void convert(RecyclerAdapterHelper helper, String messageType) {
        if (helper.getAdapterPosition()!=0){
            helper.getItemView().setLayoutParams(params);
        }
        helper.setText(R.id.message_title,messageType)
                .getItemView().setOnClickListener(v -> {
                    if (listener!=null){
                        listener.onItemClick(helper.getAdapterPosition());
                    }
                });
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface   OnItemClickListener{
       void onItemClick(int position);
    }
}
