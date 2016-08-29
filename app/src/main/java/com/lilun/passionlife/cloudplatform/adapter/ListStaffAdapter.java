package com.lilun.passionlife.cloudplatform.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationAccount;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ListStaffAdapter extends Adapter<OrganizationAccount>{
    protected OnItemDeleteListener listener;
    protected boolean isShowDelete;
    private boolean isDeleteShow;
    private OnItemDeleteListener listen;

    public ListStaffAdapter(@Nullable List<OrganizationAccount> data, @NonNull int... layoutResIds) {
        super(App.app, data, layoutResIds);
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
    protected void convert(AdapterHelper helper, OrganizationAccount oa) {
        helper.setText(R.id.module_list_title,oa.getAccount().getName()==null?oa.getAccount().getUsername():oa.getAccount().getName())
                .setVisible(R.id.module_list_desc, View.GONE)
                .setVisible(R.id.module_list_delete,isDeleteShow?View.VISIBLE:View.INVISIBLE)
                .setOnClickListener(R.id.module_list_delete, v -> {
                    if (listen!=null){
                        listen.onItemDelete(helper.getPosition());
                    }
                });
        Picasso.with(App.app).load(PicloadManager.accountIconUrl((double) oa.getId()))
                .placeholder(R.drawable.default_pic)
                .error(R.drawable.default_pic)
                .into((ImageView) helper.getItemView().findViewById(R.id.module_list_icon));

    }


    public void setOnItemDeleteListener(OnItemDeleteListener listener){
        this.listen =listener;
    }

    public interface OnItemDeleteListener {
        void onItemDelete(int position);
    }
}
