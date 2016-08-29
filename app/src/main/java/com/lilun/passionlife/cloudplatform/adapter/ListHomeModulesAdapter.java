package com.lilun.passionlife.cloudplatform.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.bean.OrganizationService;
import com.lilun.passionlife.cloudplatform.common.PicloadManager;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ListHomeModulesAdapter extends Adapter<OrganizationService>{
    protected boolean isShowDelete;
    private boolean isDeleteShow;

    public ListHomeModulesAdapter(@Nullable List<OrganizationService> data, @NonNull int... layoutResIds) {
        super(App.app, data, layoutResIds);
    }




    @Override
    protected void convert(AdapterHelper helper, OrganizationService os) {
        helper.setText(R.id.module_title, StringUtils.filteEmpty(os.getTitle()))
                .setText(R.id.module_desc, StringUtils.filteEmpty(os.getDescription()));

        Picasso.with(App.app).load(PicloadManager.orgaServiceIconUrl(os.getId()))
                .placeholder(R.drawable.default_pic)
                .error(R.drawable.default_pic)
                .into((ImageView) helper.getItemView().findViewById(R.id.module_icon));

    }


}
