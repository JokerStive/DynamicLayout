package com.lilun.passionlife.cloudplatform.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import java.util.List;

/**
 * Created by youke on 2016/8/11.
 */
public class ShowmessageTypeAdapter extends RecyclerAdapter<String> {


    private  List<String> data;
    private OnItemClickListener listener;

    public ShowmessageTypeAdapter(@Nullable List<String> data, @NonNull int... layoutResIds) {
        super(App.app, data, layoutResIds);
        this.data = data;
    }


    @Override
    protected void convert(RecyclerAdapterHelper helper, String messageType) {
        helper.setText(R.id.item_name,messageType)
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
